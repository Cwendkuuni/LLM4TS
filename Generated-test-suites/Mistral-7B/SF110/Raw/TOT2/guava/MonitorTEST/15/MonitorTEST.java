import static org.junit.Assert.*;
import com.google.common.util.concurrent.Monitor;
import org.junit.Test;

public class MonitorTest {
  @Test
  public void testEnter() {
    Monitor monitor = new Monitor();
    assertFalse(monitor.isOccupied());
    monitor.enter();
    assertTrue(monitor.isOccupiedByCurrentThread());
    monitor.leave();
    assertFalse(monitor.isOccupiedByCurrentThread());
    assertFalse(monitor.isOccupied());
  }
}