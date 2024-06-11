import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    // ... (import statements and test setup)

    @Test
    public void testGetMessageHappyPath() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = new ArrayList();
        String expectedMessage = "This is a test message";
        String actualMessage = handler.getMessage("KEY_TEST_MESSAGE", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String actualMessage = handler.getMessage("KEY_TEST_MESSAGE", null);
        assertNull(actualMessage);
    }

    @Test
    public void testGetMessageEmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = new ArrayList();
        String expectedMessage = "Key: KEY_TEST_MESSAGE not found in message file";
        String actualMessage = handler.getMessage("KEY_NONE_EXISTS", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageNullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = new ArrayList();
        handler.getMessage(null, params);
    }

    // ... (test cases for other methods)
}