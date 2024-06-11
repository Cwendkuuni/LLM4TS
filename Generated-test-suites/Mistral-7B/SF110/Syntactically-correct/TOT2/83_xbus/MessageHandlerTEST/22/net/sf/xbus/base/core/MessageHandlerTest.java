import org.junit.Test;import static org.junit.Assert.*;import java.util.Arrays;import java.util.List;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = Arrays.asList("testKey");
        String expectedMessage = "This is a test message";
        String actualMessage = handler.getMessage("testKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = Arrays.asList("testKey1", "testKey2");
        String expectedMessage = "This is a message with {0} and {1}";
        String actualMessage = handler.getMessageOptional("testMessageKey", params);
        String messageWithParams = XStringSupport.replaceAll(expectedMessage, "{0}", "testValue1") + " " + XStringSupport.replaceAll(expectedMessage, "{1}", "testValue2");
        assertEquals(messageWithParams, actualMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testgetMessageWithNullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = Arrays.asList("testKey");
        handler.getMessage(null, params);
    }

    @Test(expected = NullPointerException.class)
    public void testgetMessageWithNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        handler.getMessage(key, null);
    }

    @Test
    public void testgetMessageWithNonExistentKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = Arrays.asList("nonExistentKey");
        String expectedMessage = "Key: nonExistentKey not found in message file";
        String actualMessage = handler.getMessage("nonExistentKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetMessageWithNullHashtable() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.mMessages = null;
        String expectedMessage = "Key: testKey not found in message file";
        String key = "testKey";
        List params = Arrays.asList("testKey");
        String actualMessage = handler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testgetMessageWithNullParamsObject() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        List params = Arrays.asList(null);
        handler.getMessage(key, params);
    }

    @Test(expected = IOException.class)
    public void testAddMessagesWithInvalidDir() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "testInvalidDir");
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesWithNonExistentMessageFile() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "testDir");
    }
}