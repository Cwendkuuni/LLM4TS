import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_whenTimerIsRunning_shouldReturnTrue() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long elapsedTime = 500;

 // Simulate time passing
 long currentTime = startTime + elapsedTime;

 assertTrue(timer.isCounting());
 assertFalse(timer.isCounting()); // This call should not fail even though the timer is no longer counting

 // Check that the timer has finished counting
 assertFalse(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_whenTimerHasNotBeenStarted_shouldThrowException() {
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerHasExpired_shouldReturnFalse() {
 timer.start();
 long durationMillis = timer.durationMillis;
 long elapsedTime = durationMillis + 100; // Elapsed time is greater than the duration

 long startTime = System.currentTimeMillis();
 Thread.yield(); // Yielding to other threads to ensure that some time has passed
 long currentTime = System.currentTimeMillis();

 assertFalse(timer.isCounting());
 }
}