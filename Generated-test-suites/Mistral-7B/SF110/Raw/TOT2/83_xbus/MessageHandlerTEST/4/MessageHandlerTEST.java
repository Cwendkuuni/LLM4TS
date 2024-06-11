import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.util.ArrayList;

public class MessageHandlerTest {
  @Test
  public void testgetMessage() {
    MessageHandler messageHandler = MessageHandler.getInstance("test");
    String key = "testKey";
    List params = new ArrayList<>();
    String expected = "Test key message";
    String actual = messageHandler.getMessage(key, params);
    Trace.reset(); // reset trace for testing
    assertEquals(expected, actual);
    assertTrue(Trace.isEmpty());
  }
}