import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting()); // checks if timer is counting down when it should be
    }
}