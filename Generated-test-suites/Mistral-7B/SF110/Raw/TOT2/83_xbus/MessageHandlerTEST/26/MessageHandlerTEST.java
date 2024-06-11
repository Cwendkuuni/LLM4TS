import org.junit.Test;
import static org.junit.Assert.assertEquals;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String key = "testKey";
        List params = new ArrayList();
        String expectedMessage = "Test message for key testKey";
        String actualMessage = messageHandler.getMessage(key, params);
        Trace.setTraceLevel(Trace.ERROR); // Set trace level to error for testing
        assertEquals(expectedMessage, actualMessage);
    }
}