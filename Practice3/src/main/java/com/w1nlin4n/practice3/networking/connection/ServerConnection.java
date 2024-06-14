package com.w1nlin4n.practice3.networking.connection;

import com.w1nlin4n.practice3.networking.message.Message;
import com.w1nlin4n.practice3.networking.message.MessageCommand;
import com.w1nlin4n.practice3.networking.packet.Packet;
import com.w1nlin4n.practice3.networking.pipeline.Handler;
import com.w1nlin4n.practice3.networking.pipeline.Receiver;
import com.w1nlin4n.practice3.networking.pipeline.Sender;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerConnection implements Runnable {
    private final SocketChannel channel;
    private final Selector selector;
    private final Receiver receiver;
    private final Handler handler;
    private final Sender sender;
    private final AtomicInteger packetId;
    private final boolean[] clientIdAvailable;

    public ServerConnection(SocketChannel channel, Receiver receiver, Handler handler, Sender sender, AtomicInteger packetId, boolean[] clientIdAvailable) throws IOException {
        this.channel = channel;
        this.receiver = receiver;
        this.handler = handler;
        this.sender = sender;
        this.packetId = packetId;
        this.clientIdAvailable = clientIdAvailable;

        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        System.out.println("Server connected to client: " + channel);
    }

    private int getAvailableClientId() {
        synchronized (clientIdAvailable) {
            for (int i = 0; i < clientIdAvailable.length; i++) {
                if (clientIdAvailable[i]) {
                    clientIdAvailable[i] = false;
                    return i;
                }
            }
        }
        return -1;
    }

    private void addAvailableClientId(int clientId) {
        synchronized (clientIdAvailable) {
            clientIdAvailable[clientId] = true;
        }
    }

    @Override
    public void run() {
        Packet incomingPacket;
        Packet outgoingPacket = null;
        boolean done = false;
        boolean read = false;

        try {
            while (!done) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (!done && key.isReadable() && !read) {
                        incomingPacket = receiver.receivePacket(channel);
                        read = true;

                        Message response;
                        switch (incomingPacket.getMessage().getCommand()) {
                            case ESTABLISH_CONNECTION:
                                int id = getAvailableClientId();
                                if (id == -1)
                                    response = new Message(MessageCommand.ERROR, incomingPacket.getMessage().getUserId(), "");
                                else
                                    response = new Message(MessageCommand.ESTABLISH_CONNECTION, incomingPacket.getMessage().getUserId(), Integer.toString(id));
                                break;
                            case CLOSE_CONNECTION:
                                response = new Message(MessageCommand.CLOSE_CONNECTION, incomingPacket.getMessage().getUserId(), "");
                                addAvailableClientId(Integer.parseInt(incomingPacket.getMessage().getBody()));
                                break;
                            default:
                                try {
                                    response = handler.handleMessage(incomingPacket.getMessage());
                                } catch (Exception e) {
                                    response = new Message(MessageCommand.ERROR, incomingPacket.getMessage().getUserId(), "");
                                }
                        }

                        outgoingPacket = new Packet((byte) 0xFF, packetId.getAndIncrement(), response);
                    }

                    if (!done && key.isWritable() && read) {
                        sender.sendPacket(outgoingPacket, channel);
                        read = false;
                        if (outgoingPacket.getMessage().getCommand() == MessageCommand.CLOSE_CONNECTION)
                            done = true;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                System.out.println("Server disconnected from client: " + channel);
                channel.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
