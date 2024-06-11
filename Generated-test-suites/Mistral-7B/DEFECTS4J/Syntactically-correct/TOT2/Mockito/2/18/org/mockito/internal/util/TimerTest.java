import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    Timer timer;

    @Test
    public void testStart() {
        timer = new Timer(-1);
        try {
            timer.start();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testIsCounting_TimerNotStarted() {
        timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerElapsed() {
        timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime() + 1001;
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerCounting() {
        timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerReset() {
        timer = new Timer(1000);
        timer.start();
        timer.reset();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart_NegativeDuration() {
        timer = new Timer(-1000);
        try {
            timer.start();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    @Test
    public void testStart_NullDuration() {
        try {
            new Timer(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testIsCounting_TimerCountingLongerThanExpected() {
        timer = new Timer(1000);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.getStartTime() + 2000;
        assertTrue(timer.isCounting());
    }

}