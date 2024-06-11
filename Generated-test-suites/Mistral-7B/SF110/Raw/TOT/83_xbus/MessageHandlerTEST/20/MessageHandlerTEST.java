
package net.sf.xbus.base.core;

import org.junit.*;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    private static MessageHandler messageHandler;

    @BeforeClass
    public static void setUpClass() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessageWithValidKeyAndParams() {
        List params = Arrays.asList("Test param 1", "Test param 2");
        String expectedMessage = "This is a test message with two parameters: $1$ and $2$";
        String actualMessage = messageHandler.getMessage("test.message.key", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageWithNullParams() {
        String expectedMessage = "Key: test.message.key not found in message file";
        String actualMessage = messageHandler.getMessage("test.message.key", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageWithInvalidKey() {
        List params = Arrays.asList("Test param 1", "Test param 2");
        String expectedMessage = "Key: invalid.key not found in message file";
        String actualMessage = messageHandler.getMessage("invalid.key", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndParams() {
        List params = Arrays.asList("Test param 1", "Test param 2");
        String expectedMessage = "This is a test message with two parameters: {0} and {1}";
        String actualMessage = messageHandler.getMessageOptional("test.message.key", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithNullParams() {
        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional("test.message.key", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithInvalidKey() {
        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional("invalid.key", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetInstanceWithValidBasename() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test");
        MessageHandler messageHandler2 = MessageHandler.getInstance("test");
        assertTrue(messageHandler1 == messageHandler2);
    }

    @Test
    public void testGetInstanceWithDifferentBasenames() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test");
        MessageHandler messageHandler2 = MessageHandler.getInstance("other");
        assertNotSame(messageHandler1, messageHandler2);
    }

    @Test
    public void testAddMessagesWithValidBasenameAndDir() {
        messageHandler.addMessages("test", "src/test/resources/messages");
        String expectedMessage = "This is a test message";
        String actualMessage = messageHandler.getMessage("test.message.key", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesWithInvalidDir() {
        messageHandler.addMessages("test", "src/test/resources/non_existing_dir");
    }
}