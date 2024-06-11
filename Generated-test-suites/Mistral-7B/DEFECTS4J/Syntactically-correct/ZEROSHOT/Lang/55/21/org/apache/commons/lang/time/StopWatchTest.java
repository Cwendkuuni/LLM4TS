import org.apache.commons.lang3.TimeBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StopWatchTest {

 private StopWatch stopWatch;

 @BeforeEach
 public void setUp() {
 stopWatch = new StopWatch();
 }

 @Test
 public void testStartAndStop() {
 stopWatch.start();
 stopWatch.stop();
 long actual = stopWatch.getTime();
 Assertions.assertGreaterThan(actual, 0);
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch.start();
 stopWatch.split();
 long actualSplit = stopWatch.getSplitTime();
 Assertions.assertGreaterThan(actualSplit, 0);
 stopWatch.unsplit();
 long actualTotal = stopWatch.getTime();
 Assertions.assertEquals(actualTotal, actualSplit);
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch.start();
 stopWatch.suspend();
 long beforeSuspend = System.currentTimeMillis();
 stopWatch.resume();
 long afterResume = System.currentTimeMillis();
 long actualTimeElapsed = stopWatch.getTime();
 long timeElapsed = afterResume - beforeSuspend;
 Assertions.assertEquals(actualTimeElapsed, actualTimeElapsed - stopWatch.getTime());
 }

 @Test
 public void testMultipleSplitAndUnsplit() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 long actualFirstSplit = stopWatch.getSplitTime();
 stopWatch.unsplit();
 long actualTotal = stopWatch.getTime();
 Assertions.assertEquals(actualTotal, actualFirstSplit * 2);
 }

 @Test
 public void testStartTwiceWithoutReset() {
 Assertions.assertThrows(IllegalStateException.class, () -> {
 stopWatch.start();
 stopWatch.start();
 });
 }

 @Test
 public void testSplitTwice() {
 stopWatch.start();
 Assertions.assertThrows(IllegalStateException.class, () -> {
 stopWatch.split();
 stopWatch.split();
 });
 }

 @Test
 public void testSuspendTwice() {
 stopWatch.start();
 Assertions.assertThrows(IllegalStateException.class, () -> {
 stopWatch.suspend();
 stopWatch.suspend();
 });
 }

 @Test
 public void testResumeTwiceWithoutSuspend() {
 stopWatch.start();
 Assertions.assertThrows(IllegalStateException.class, () -> {
 stopWatch.resume();
 stopWatch.resume();
 });
 }

}