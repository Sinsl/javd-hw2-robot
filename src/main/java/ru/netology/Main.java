package ru.netology;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final int COUNT_ROUTES = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            String route = generateRoute("RLRFR", 100);
            int countR = 0;
            for (char r : route.toCharArray()) {
                if (r == 'R') {
                    countR++;
                }
            }
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(countR)) {
                    sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                } else {
                    sizeToFreq.put(countR, 1);
                }
            }
        };

        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < COUNT_ROUTES; i++) {
            pool.submit(runnable);
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        int maxRepeatKey = 0;
        int maxRepeatValue = 0;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() > maxRepeatValue) {
                maxRepeatValue = entry.getValue();
                maxRepeatKey = entry.getKey();
            }
        }
        System.out.println("Самое частое количество повторений " + maxRepeatKey + " (встретилось " + maxRepeatValue + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getKey() != maxRepeatKey) {
                System.out.println(entry.getKey() + " повторений встретилось " + entry.getValue() + " раз");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}