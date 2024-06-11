package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

    @Test
    public void testStopWatch() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long startTime = stopWatch.getTime();

        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        Assert.assertEquals(100, totalTime);

        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertEquals(100, splitTime);

        stopWatch.unsplit();
        totalTime = stopWatch.getTime();
        Assert.assertEquals(100, totalTime);

        stopWatch.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long secondSplitTime = stopWatch.getTime();
        Assert.assertTrue(secondSplitTime > totalTime);

        stopWatch.reset();
        totalTime = stopWatch.getTime();
        Assert.assertEquals(0, totalTime);

        stopWatch.start();
        try {
            throw new RuntimeException();
        } finally {
            stopWatch.stop();
        }
        totalTime = stopWatch.getTime();
        Assert.assertTrue(totalTime >= 0);

        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
        stopWatch.stop();
        Assert.assertThrows(IllegalStateException.class, () -> stopWatch.getSplitTime());
    }

    @Test
    public void testConcurrentAccess() {
        StopWatch stopWatch = new StopWatch();
        Thread thread1 = new Thread(() -> {
            stopWatch.start();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopWatch.stop();
        });
        Thread thread2 = new Thread(() -> {
            stopWatch.start();
            try {
                TimeUnit.MILLISECONDS.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopWatch.split();
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long totalTime = stopWatch.getTime();
        Assert.assertTrue(totalTime >= 150);
    }
}