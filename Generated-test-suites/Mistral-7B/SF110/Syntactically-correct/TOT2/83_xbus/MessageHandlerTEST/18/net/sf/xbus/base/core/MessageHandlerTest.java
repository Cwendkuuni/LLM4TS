import org.junit.Test;
import static org.junit.Assert.assertEquals;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.util.ArrayList;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List params = new ArrayList<>();
        String key = "testKey";
        String expectedMessage = "Test message for key testKey";
        String actualMessage = messageHandler.getMessage(key, params);
        Trace.reset(); // Reset the trace for testing
        assertEquals(expectedMessage, actualMessage);
        assertEquals("", Trace.getLast()); // Check there is no error message
    }
}