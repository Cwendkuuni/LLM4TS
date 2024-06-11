import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test case for normal usage
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        Assert.assertTrue(timer.isCounting());
        Assert.assertTrue(currentTime < (timer.getStartTime() + timer.getDurationMillis()));

        // Test case for timer expired
        currentTime = System.currentTimeMillis();
        Assert.assertFalse(timer.isCounting());
        Assert.assertTrue(currentTime > (timer.getStartTime() + timer.getDurationMillis()));
    }

    @Test
    public void testIsCountingWithNegativeDuration() {
        // Test case for error scenario with negative duration
        Timer timer = new Timer(-1000);
        Assert.assertFalse(timer.isCounting());
    }

    // Getters for accessing private fields
    private long getStartTime(Timer timer) {
        return timer.startTime;
    }

    private long getDurationMillis(Timer timer) {
        return timer.durationMillis;
    }

    

    
}