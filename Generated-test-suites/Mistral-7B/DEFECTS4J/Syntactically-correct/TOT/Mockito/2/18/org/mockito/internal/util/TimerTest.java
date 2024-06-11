// TimerTest.java (Expert 1)

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {// Expert 1

    private Timer timer;

    @Before
    public void initializeTimer() {
        timer = new Timer(100);
    }

    @Test
    public void testIsCounting_NormalCase() {
        timer.start();
        long expectedEndTime = System.currentTimeMillis() - 100;

        Assert.assertTrue("Timer should be counting down", timer.isCounting());
        Assert.assertTrue("Timer's end time matches expected end time", expectedEndTime >= System.currentTimeMillis());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_TimerNotStarted() {
        Assert.assertFalse("Timer should not be counting down before being started", timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_TimerExpired() {
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime();
        timer.isCounting(); // This line will cause an AssertionError

        Assert.assertFalse("Timer should not be counting down after expiration", timer.isCounting());
    }

}