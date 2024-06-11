import org.junit.Before;
import org.junit.Test;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("testBasename");
    }

    @Test
    public void testgetMessage_ValidKeyAndParams_ReturnsMessageText() {
        String key = "testKey";
        List params = new ArrayList();
        params.add("testParam1");
        params.add("testParam2");

        String expectedMessage = "testMessage $1$ $2$"; // Replace this with the expected message text
        String actualMessage = messageHandler.getMessage(key, params);

        Trace.reset(); // Reset the trace to avoid cluttering the test output
        String traceMessage = Trace.getLastMessage();
        String errorMessage = "Key: " + key + " not found in message file";

        assertNull(traceMessage);
        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetMessage_InvalidKeyAndParams_ReturnsErrorMessage() {
        String key = "invalidKey";
        List params = null;

        String expectedMessage = "Key: invalidKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);

        Trace trace = new Trace(); // Create a trace object to capture the error message
        Trace.setTrace(trace);

        String actualTraceMessage = trace.getLastMessage();

        assertNull(actualMessage);
        assertEquals(expectedMessage, actualTraceMessage);
    }
}