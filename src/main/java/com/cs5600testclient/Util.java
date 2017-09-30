package com.cs5600testclient;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by saikikwok on 29/09/2017.
 */
class Util {

    final static int MEAN = 0;
    final static int MEDIAN = 1;
    final static int P99 = 2;
    final static int P95 = 3;

    static double[] parseLatency(List<Long> requests) {
        Collections.sort(requests);
        double[] ans = new double[4];
        if (requests.size() == 0) {
            return ans;
        }
        int indexMedian = requests.size() / 2;
        int index99 = (int) Math.ceil(((99 / (double) 100) * (double) requests.size()));
        int index95 = (int) Math.ceil(((95 / (double) 100) * (double) requests.size()));
        ans[MEAN] = requests.stream().map(Object::toString)
                            .mapToDouble(Double::parseDouble)
                            .average().getAsDouble();
        if (requests.size() % 2 == 1) {
            ans[MEDIAN] = requests.get(indexMedian);
        } else {
            ans[MEDIAN] = (requests.get(indexMedian) + requests.get(indexMedian - 1)) / 2;
        }
        ans[P99] = requests.get(index99 - 1);
        ans[P95] = requests.get(index95 - 1);
        return ans;
    }

    static void redirectOutputToLog() {
        try {
            System.setOut(new PrintStream("log.txt"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void resetOutputToStdout() {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
