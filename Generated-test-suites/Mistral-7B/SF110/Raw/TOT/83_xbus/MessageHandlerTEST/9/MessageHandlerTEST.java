import org.junit.Before;
import org.junit.Test;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    private static final String BASE_NAME = "test";

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);
        messageHandler.addMessages(BASE_NAME, "src/test/resources");
    }

    @Test
    public void testGetMessageWithValidKeyAndParams() {
        List params = new ArrayList(Arrays.asList("param1", "param2"));
        String expected = "Key: test$1$ not found in message file param1: param1 param2: param2";
        String actual = messageHandler.getMessage("test$1$", params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageWithValidKey() {
        String expected = "This is a test message";
        String actual = messageHandler.getMessage("testMessageKey", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndParams() {
        List params = new ArrayList(Arrays.asList("param1", "<null>"));
        String expected = "Key: test$1$ not found in message file param1: param1 param2: null";
        String actual = messageHandler.getMessageOptional("test$1$", params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptionalWithValidKey() {
        String expected = "This is a test message with {0} and {1}";
        String actual = messageHandler.getMessageOptional("testMessageWithParamsKey", null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageWithInvalidKey() {
        List params = new ArrayList<>();
        String expected = "Key: testInvalidKey not found in message file";
        String actual = messageHandler.getMessage("testInvalidKey", params);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageWithNullParams() {
        String expected = "Key: testMessageKey not found in message file";
        String actual = messageHandler.getMessage("testMessageKey", null);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullMessageHandler() {
        String key = "testKey";
        List params = new ArrayList<>();
        MessageHandler messageHandler = null;
        String actual = messageHandler.getMessage(key, params);
        fail("NullPointerException expected but not thrown");
    }
}