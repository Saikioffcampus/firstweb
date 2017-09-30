package com.cs5600testclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by saikikwok on 28/09/2017.
 */
public class ClientService {

    final static int ARGV_LENTH = 4;
    final static int THREAD_NUM = 0;
    final static int ITERATION_NUM = 1;
    final static int IP = 2;
    final static int PORT = 3;

    public static void main(String[] argv) {
        // Please, do not remove this line from file template, here invocation of web service will be inserted
        int threadNum = 100;
        int iterationNum = 100;
        String ip = "34.213.182.195";
        String port = "8080";
        if (argv.length == ARGV_LENTH) {
            threadNum = Integer.parseInt(argv[THREAD_NUM]);
            iterationNum = Integer.parseInt(argv[ITERATION_NUM]);
            ip = argv[IP];
            port = argv[PORT];
        }
        String url = "http://" + ip + ":" + port + "/firstweb_war/rest/myresource";

        ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
        CyclicBarrier barrier = new CyclicBarrier(threadNum + 1);
        List<Long> latencies = Collections.synchronizedList(new ArrayList<>());
        Counter requests = new Counter();
        Counter successRequest = new Counter();

        System.out.println(threadNum + " threads are requesting...");
        Util.redirectOutputToLog();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            threadPool.execute(new ClientRunnable(url, barrier, iterationNum,
                                    latencies, requests, successRequest));
        }

        try {
            barrier.await();
            Util.resetOutputToStdout();
            long wallTime = System.currentTimeMillis() - startTime;
            System.out.println("——————————————————————————————");
            System.out.println("Wall time: " + wallTime + " ms");
            System.out.println("——————————————————————————————");
            System.out.println("Total number of requests: " + requests.getCount());
            System.out.println("Total number of successful requests: " + requests.getCount());
            double[] stats = Util.parseLatency(latencies);
            System.out.println("Mean latency for all requests: " + stats[Util.MEAN] + " ms");
            System.out.println("Median latency for all requests: " + stats[Util.MEDIAN] + " ms");
            System.out.println("P95 latency for all requests: " + stats[Util.P95] + " ms");
            System.out.println("P99 latency for all requests: " + stats[Util.P99] + " ms");
            System.out.println("——————————————————————————————");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }


}
