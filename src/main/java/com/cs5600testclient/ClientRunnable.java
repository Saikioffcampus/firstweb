package com.cs5600testclient;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by saikikwok on 28/09/2017.
 */
public class ClientRunnable implements Runnable {

    private String url;
    private int iterationNum;
    private CyclicBarrier barrier;
    private List<Long> latencies;
    private javax.ws.rs.client.Client client = ClientBuilder.newClient();
    private Counter request;
    private Counter success;

    public ClientRunnable(final String url,
                          final CyclicBarrier barrier, final int size,
                          final List<Long> latencies, Counter request, Counter success) {
        this.url = url;
        this.barrier = barrier;
        this.iterationNum = size;
        this.latencies = latencies;
        this.request = request;
        this.success = success;
    }

    public void run() {

        try {
            long start, latency;
            for (int i = 0; i < this.iterationNum; i++) {
                request.incrementCount();
                start = System.currentTimeMillis();
                getClient();
                latency = System.currentTimeMillis() - start;
                latencies.add(latency);
                success.incrementCount();
                System.out.println("GET " + latency);
                request.incrementCount();
                start = System.currentTimeMillis();
                postClient("HELLO");
                latency = System.currentTimeMillis() - start;
                latencies.add(latency);
                success.incrementCount();
                System.out.println("Post " + latency);
            }
            this.barrier.await();
            client.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private String getClient() {
        return this.client.target(this.url)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
    }

    private String postClient(String data) {
        return this.client.target(this.url)
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(data), String.class);
    }
}
