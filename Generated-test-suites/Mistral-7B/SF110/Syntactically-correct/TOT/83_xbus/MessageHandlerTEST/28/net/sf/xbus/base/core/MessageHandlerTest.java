import static org.junit.Assert.assertEquals;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;

public class MessageHandlerTest {
    
    @org.junit.Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        List<Object> params = new ArrayList<>();
        params.add("testValue");

        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = handler.getMessage(key, params);
        Trace.error("test error message"); // Simulate an error message for testing
        assertEquals(expectedMessage, actualMessage);
    }
    
}