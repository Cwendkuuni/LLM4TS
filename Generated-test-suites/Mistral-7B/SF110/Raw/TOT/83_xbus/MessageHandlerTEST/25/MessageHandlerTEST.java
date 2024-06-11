import org.junit.Before;
import org.junit.Test;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private Trace trace;

    @Before
    public void setUp() {
        trace = mock(Trace.class);
        messageHandler = new MessageHandler("test") {
            @Override
            public Trace getTrace() {
                return trace;
            }
        };
    }

    @Test
    public void testgetMessageNullParams() {
        List<Object> params = null;
        String expectedMessage = "Key: <Key> not found in message file";
        String actualMessage = messageHandler.getMessage("Key", params);
        Trace.errorCallCount = 0;
        when(trace.error(String.class)).thenAnswer(invocation -> {
            Trace.errorCallCount++;
            return null;
        });
        String resultMessage = messageHandler.getMessageOptional("Key", params);
        Assert.assertNull(resultMessage);
        Assert.assertEquals(expectedMessage, actualMessage);
        Assert.assertEquals(1, Trace.errorCallCount);
    }

    @Test
    public void testgetMessageEmptyListParams() {
        List<Object> params = new ArrayList<>();
        String expectedMessage = "Key: Key not found in message file";
        String actualMessage = messageHandler.getMessage("Key", params);
        String resultMessage = messageHandler.getMessageOptional("Key", params);
        Assert.assertEquals(expectedMessage, actualMessage);
        Assert.assertEquals(resultMessage, messageHandler.getMessage("Key", params));
    }

    @Test
    public void testgetMessageValidParams() {
        List<Object> params = Arrays.asList("Test Param 1", "Test Param 2");
        String expectedMessage = "Key: {0}: {1}";
        String actualMessage = messageHandler.getMessage("Key", params);
        String resultMessage = messageHandler.getMessageOptional("Key", params);
        Assert.assertEquals(expectedMessage, actualMessage);
        Assert.assertEquals(resultMessage, messageHandler.getMessage("Key", params));
    }

    @Test
    public void testgetMessageOptionalNullParams() {
        List<Object> params = null;
        String expectedMessage = null;
        String resultMessage = messageHandler.getMessageOptional("Key", params);
        Assert.assertNull(resultMessage);
        Assert.assertEquals(expectedMessage, resultMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testgetMessageOptionalNullKey() {
        List<Object> params = new ArrayList<>();
        String key = null;
        messageHandler.getMessageOptional(key, params);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("test");
        MessageHandler instance2 = MessageHandler.getInstance("test");
        Assert.assertSame(instance1, instance2);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", "test_dir");
    }

    static class Trace {
        public static int errorCallCount = 0;
        public static void error(String message) {}
    }
}