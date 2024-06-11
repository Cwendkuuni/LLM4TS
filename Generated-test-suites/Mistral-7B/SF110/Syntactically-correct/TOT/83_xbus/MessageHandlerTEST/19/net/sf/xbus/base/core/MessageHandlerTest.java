import org.junit.Before;
import org.junit.Test;
import net.sf.xbus.base.core.*;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;
    private List params;

    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance("test");
        params = new ArrayList();
    }

    @Test
    public void testGetMessage_ValidKeyAndParams() {
        String expectedMessage = "This is a test message with a param: $1$";
        String actualMessage = messageHandler.getMessage("testKey", params);
        params.add("testParam");
        String replacedMessage = XStringSupport.replaceAll(expectedMessage, "$\\d+$", params.get(0).toString());
        assertEquals(replacedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_ValidKeyNoParams() {
        String expectedMessage = "This is a test message with no params";
        String actualMessage = messageHandler.getMessage("testKey", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessage_NullKey() {
        messageHandler.getMessage(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessage_NullParams() {
        messageHandler.getMessage("testKey", null);
    }

    @Test
    public void testGetMessageOptional_ValidKeyAndParams() {
        String expectedMessage = "This is a test message with a param: $1$";
        String actualMessage = messageHandler.getMessageOptional("testKey", params);
        params.add("testParam");
        String replacedMessage = XStringSupport.replaceAll(expectedMessage, "$\\d+$", params.get(0).toString());
        assertNotNull(actualMessage);
        assertEquals(replacedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_ValidKeyNoParams() {
        String expectedMessage = "This is a test message with no params";
        String actualMessage = messageHandler.getMessageOptional("testKey", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_InvalidKey() {
        assertNull(messageHandler.getMessageOptional("testInvalidKey", null));
    }

    @Test
    public void testGetInstance_ValidBasename() {
        MessageHandler instance1 = MessageHandler.getInstance("test");
        MessageHandler instance2 = MessageHandler.getInstance("test");
        assertEquals(instance1, instance2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstance_InvalidBasename() {
        messageHandler.getInstance("invalidBasename");
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("test");
        Properties testProperties = new Properties();
        testProperties.setProperty("testKey1", "This is test message 1");
        testProperties.setProperty("testKey2", "This is test message 2");
        messageHandler.addMessages("test", ".");
        messageHandler.addMessages("test", "./");

        String expectedMessage1 = "This is test message 1";
        String expectedMessage2 = "This is test message 2";
        String actualMessage1 = messageHandler.getMessage("testKey1", null);
        String actualMessage2 = messageHandler.getMessage("testKey2", null);

        assertEquals(expectedMessage1, actualMessage1);
        assertEquals(expectedMessage2, actualMessage2);
    }
}
