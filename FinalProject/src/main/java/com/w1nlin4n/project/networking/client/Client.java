package com.w1nlin4n.project.networking.client;

import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private final AtomicBoolean isRunning;
    private final HttpClient client;

    public Client(String clientAddress, int clientPort, String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.isRunning = new AtomicBoolean(false);

        client = HttpClient
                .newBuilder()
                .localAddress(new InetSocketAddress(clientAddress, clientPort).getAddress())
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build()
        ;
    }

    public void start() {
        isRunning.set(true);
    }

    public Response send(Request request) throws IOException, InterruptedException {
        if (!isRunning.get())
            throw new IllegalStateException("Client is not running");

        HttpRequest.Builder httpRequestBuilder = HttpRequest
                .newBuilder(URI.create("http://" + serverAddress + ":" + serverPort + request.getPath()))
                .version(HttpClient.Version.HTTP_1_1);
        if (request.getAccessToken() != null)
            httpRequestBuilder.header("Authorization", request.getAccessToken());
        if (request.getBody() == null) {
            httpRequestBuilder.method(request.getMethod().name(), HttpRequest.BodyPublishers.noBody());
        } else {
            httpRequestBuilder
                    .header("Content-Type", "application/json")
                    .method(request.getMethod().name(), HttpRequest.BodyPublishers.ofString(request.getBody()));
        }
        HttpRequest httpRequest = httpRequestBuilder.build();
        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return new Response(
                HttpCode.fromCode(httpResponse.statusCode()),
                httpResponse.body()
        );
    }

    public void stop() {
        isRunning.set(false);
        client.close();
    }
}
