import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 /**
 * Test isCounting() method when timer is counting down.
 */
 @Test
 public void testIsCountingWhileCounting() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long elapsedTime;

 do {
 elapsedTime = System.currentTimeMillis() - startTime;
 } while (elapsedTime < timer.durationMillis());

 assertTrue(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer has finished counting.
 */
 @Test
 public void testIsCountingAfterCountingFinished() {
 timer.start();
 long startTime = System.currentTimeMillis();
 long elapsedTime;

 do {
 elapsedTime = System.currentTimeMillis() - startTime;
 } while (elapsedTime <= timer.durationMillis());

 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer has not been started.
 */
 @Test
 public void testIsCountingBeforeStarting() {
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method with invalid duration value.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsCountingWithNegativeDuration() {
 timer = new Timer(-1000);
 timer.start();
 }

}