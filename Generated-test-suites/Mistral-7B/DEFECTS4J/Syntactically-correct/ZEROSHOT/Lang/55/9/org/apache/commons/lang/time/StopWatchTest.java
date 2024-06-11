import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
        Assert.assertEquals(0, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
        Assert.assertNotEquals(-1, splitTime);
        stopWatch.unsplit();
        time = stopWatch.getTime();
        Assert.assertEquals(time, time - splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        long time = stopWatch.getTime();
        Assert.assertEquals(time, pauseTime + time);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testGetTimeAndToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = System.currentTimeMillis();
        stopWatch.stop();
        String strTime = stopWatch.toString();
        String expectedStrTime = String.format("%d:%02d:%02d.%03d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % 60,
                TimeUnit.MILLISECONDS.toSeconds(time) % 60,
                TimeUnit.MILLISECONDS.toMillis(time) % 1000);
        Assert.assertEquals(expectedStrTime, strTime);
    }

    @Test
    public void testGetSplitTimeAndtoSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        String strSplitTime = stopWatch.toSplitString();
        String expectedStrSplitTime = String.format("%d:%02d:%02d.%03d",
                TimeUnit.MILLISECONDS.toHours(splitTime),
                TimeUnit.MILLISECONDS.toMinutes(splitTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(splitTime) % 60,
                TimeUnit.MILLISECONDS.toMillis(splitTime) % 1000);
        Assert.assertEquals(expectedStrSplitTime, strSplitTime);
    }
}