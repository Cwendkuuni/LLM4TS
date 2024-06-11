// IsCountingTest.java
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Timer;

public class IsCountingTest {

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsNotRunning_thenReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

}