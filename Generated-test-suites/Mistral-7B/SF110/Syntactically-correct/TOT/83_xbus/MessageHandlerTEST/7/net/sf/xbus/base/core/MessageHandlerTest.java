import org.junit.BeforeClass;
import org.junit.Test;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;
    private static final String BASE_NAME = "TestMessageHandler";
    private static final String MESSAGE_FILE_NAME = "TestMessageFile.properties";

    @BeforeClass
    public static void setup() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);
        messageHandler.addMessages(BASE_NAME, "./src/test/resources");
    }

    @Test
    public void testgetMessage() {
        // Happy path: Key exists and params are provided
        List<Object> params = new ArrayList<>(Arrays.asList("Test", 123));
        String expectedMessage = "Key: Test $1$ has value 123";
        String actualMessage = messageHandler.getMessage("Test.$1$", params);
        assertEquals(expectedMessage, actualMessage);

        // Edge case: Key exists but no params
        actualMessage = messageHandler.getMessage("TestKey", null);
        expectedMessage = "Key: TestKey";
        assertEquals(expectedMessage, actualMessage);

        // Error case: Key does not exist
        actualMessage = messageHandler.getMessage("InvalidKey", null);
        assertNull(actualMessage);
        String errorMessage = "Key: InvalidKey not found in message file";
        assertEquals(errorMessage, Trace.getErrorMessage());
        Trace.clearError();
    }

    @Test
    public void testgetMessageOptional() {
        // Happy path: Key exists and params are provided
        List<Object> params = new ArrayList<>(Arrays.asList("Test", 123));
        String expectedMessage = "Key: Test $1$ has value 123";
        String actualMessage = messageHandler.getMessageOptional("Test.$1$", params);
        assertEquals(expectedMessage, actualMessage);

        // Edge case: Key exists but no params
        actualMessage = messageHandler.getMessageOptional("TestKey", null);
        assertEquals("Key: TestKey", actualMessage);

        // Error case: Key does not exist
        actualMessage = messageHandler.getMessageOptional("InvalidKey", null);
        assertNull(actualMessage);
    }

    @Test
    public void testgetInstance() {
        // Happy path: Singleton instance is created and returned
        MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testAddMessages() {
        // Happy path: Messages are loaded from the specified directory
        String key = "TestKey";
        String expectedMessage = "TestKey: TestMessage";
        messageHandler.addMessages(BASE_NAME, "./src/test/resources");
        String actualMessage = messageHandler.getMessage(key, null);
        assertEquals(expectedMessage, actualMessage);

        // Edge case: Messages file does not exist
        messageHandler.addMessages(BASE_NAME, "./src/test/non-existent");
        actualMessage = messageHandler.getMessage(key, null);
        assertNull(actualMessage);
    }

    
    // Add more test cases here if needed
    
}