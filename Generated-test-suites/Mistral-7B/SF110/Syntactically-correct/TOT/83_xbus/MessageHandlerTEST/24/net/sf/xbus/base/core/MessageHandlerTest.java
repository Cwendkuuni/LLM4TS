import org.junit.Test;
import static org.junit.Assert.assertEquals;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;

public class MessageHandlerTest {

    @Test
    public void testgetMessage() {
        // Create a MessageHandler instance
        MessageHandler handler = MessageHandler.getInstance("test");
        // Test with valid key and empty params
        String key = "TestKey";
        List params = new ArrayList();
        String expectedMessage = "Key: TestKey not found in message file";
        String actualMessage = handler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
        // Test with valid key and non-empty params
        String param1 = "test param 1";
        params.add(param1);
        expectedMessage = "Key: TestKey $1$ not found in message file";
        actualMessage = handler.getMessage(key, params);
        expectedMessage = XStringSupport.replaceAll(expectedMessage, "$1$", param1);
        assertEquals(expectedMessage, actualMessage);
        // Test with invalid key
        key = "InvalidKey";
        expectedMessage = "Key: InvalidKey not found in message file";
        actualMessage = handler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
        // Test error scenario: null params
        params = null;
        Trace.clear();
        actualMessage = handler.getMessage(key, params);
        assertEquals("Key: TestKey not found in message file", actualMessage);
        assertEquals(1, Trace.getErrorCount());
    }
}