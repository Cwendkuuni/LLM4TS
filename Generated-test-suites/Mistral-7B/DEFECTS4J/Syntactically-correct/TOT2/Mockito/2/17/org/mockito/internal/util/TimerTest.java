// TimerIsCountingTest.java
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

public class TimerIsCountingTest {

    @Test
    public void testIsCountingWhenTimerIsRunning() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        long elapsedTime = currentTime - timer.getStartTime();
        assertTrue(elapsedTime <= timer.getDurationMillis());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCountingWhenTimerIsNotRunning() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.start();
        assertFalse(timer.isCounting()); // should throw IllegalStateException
    }
}