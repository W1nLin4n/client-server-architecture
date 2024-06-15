package com.w1nlin4n.practice4.networking.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.practice4.networking.message.Message;
import com.w1nlin4n.practice4.networking.message.MessageCommand;
import com.w1nlin4n.practice4.networking.packet.Packet;
import com.w1nlin4n.practice4.networking.pipeline.Receiver;
import com.w1nlin4n.practice4.networking.pipeline.Sender;
import com.w1nlin4n.practice4.serialization.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private byte id;
    private final String clientAddress;
    private final int clientPort;
    private final String serverAddress;
    private final int serverPort;
    private SocketChannel channel;
    private Selector selector;
    private final AtomicInteger packetId;
    private final Serializer<Packet> serializer;
    private final Receiver receiver;
    private final Sender sender;

    public Client(String clientAddress, int clientPort, String serverAddress, int serverPort, Serializer<Packet> serializer, Receiver receiver, Sender sender) {
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        packetId = new AtomicInteger(0);
        this.serializer = serializer;
        this.receiver = receiver;
        this.sender = sender;
    }

    public synchronized void start() throws IOException, InterruptedException {
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(clientAddress, clientPort));

        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        channel.connect(new InetSocketAddress(serverAddress, serverPort));

        boolean channelConnected = false;
        int delay = 10;

        while (!channelConnected) {
            wait(delay);

            channelConnected = channel.finishConnect();

            delay = Math.min(delay * 2, 1000);
        }

        System.out.println("Client connected to server: " + channel);

        boolean connected = false;
        delay = 10;

        while (!connected) {
            wait(delay);

            Message response = sendMessage(new Message(MessageCommand.ESTABLISH_CONNECTION, Integer.MAX_VALUE, ""));
            if (response.getCommand() == MessageCommand.ESTABLISH_CONNECTION) {
                id = Byte.parseByte(response.getBody());
                connected = true;
            }

            delay = Math.min(delay * 2, 1000);
        }
    }

    public synchronized Message sendMessage(Message message) throws IOException {
        boolean done = false;
        boolean written = false;

        Packet request = new Packet(id, packetId.getAndIncrement(), message);
        Packet response = null;

        while (!done) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isReadable() && written) {
                    response = receiver.receivePacket(channel);
                    ObjectMapper om = new ObjectMapper();
                    System.out.println("Response: " + om.writeValueAsString(response));
                    done = true;
                }

                if (key.isWritable() && !written) {
                    ObjectMapper om = new ObjectMapper();
                    System.out.println("Request: " + om.writeValueAsString(request));
                    sender.sendPacket(request, channel);
                    written = true;
                }
            }
        }
        return response.getMessage();
    }

    public synchronized void close() throws IOException, InterruptedException {
        boolean closed = false;
        int delay = 10;

        while (!closed) {
            wait(delay);

            Message response = sendMessage(new Message(MessageCommand.CLOSE_CONNECTION, Integer.MAX_VALUE, Integer.toString(id)));
            if (response.getCommand() == MessageCommand.CLOSE_CONNECTION) {
                closed = true;
            }

            delay = Math.min(delay * 2, 1000);
        }

        System.out.println("Client disconnected from server: " + channel);
        channel.close();
    }
}
