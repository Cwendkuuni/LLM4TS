import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerCreation() {
        Timer timer = new Timer(1000L);
        Assert.assertNotEquals(-1, timer.startTime);
        Assert.assertEquals(1000L, timer.durationMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerNegativeDuration() {
        Timer timer = new Timer(-1000L);
        Assert.fail("Expected IllegalArgumentException but was not thrown");
    }

    @Test
    public void testTimerIsCountingInitially() {
        Timer timer = new Timer(1000L);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerStartsCounting() {
        Timer timer = new Timer(1000L);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime;
        Assert.assertTrue(elapsedTime <= timer.durationMillis);
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerCountDown() {
        Timer timer = new Timer(1000L);
        timer.start();
        long delay = 500L;
        long currentTime = System.currentTimeMillis();
        long expectedElapsedTime = currentTime - timer.startTime;

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long elapsedTime = currentTime - timer.startTime;
        Assert.assertTrue(elapsedTime >= delay);
        Assert.assertTrue(elapsedTime <= timer.durationMillis);
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testTimerCountDownToZero() {
        Timer timer = new Timer(1000L);
        timer.start();
        long delay = timer.durationMillis;
        long currentTime = System.currentTimeMillis();

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingAfterReset() {
        Timer timer = new Timer(1000L);
        timer.start();
        long delay = timer.durationMillis;
        long currentTime = System.currentTimeMillis();

        try {
            Thread.sleep(delay * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.start();
        Assert.assertFalse(timer.isCounting());
    }
}