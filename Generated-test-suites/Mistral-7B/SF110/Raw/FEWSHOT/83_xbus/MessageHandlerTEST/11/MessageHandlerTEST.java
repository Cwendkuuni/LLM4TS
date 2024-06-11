package net.sf.xbus.base.core.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

    @Test
    public void testMessageHandlerInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertEquals("test", messageHandler.getBasename());
        MessageHandler messageHandler2 = MessageHandler.getInstance("test");
        assertEquals(messageHandler, messageHandler2);
    }

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        String key = "message.test.key";
        String expectedMessage = "Test message with param1: $1$";

        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = null;
        String key = "message.test.key";
        String expectedMessage = "message.test.key";

        String actualMessage = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String key = "message.test.key";
        String expectedMessage = "Test message with param1: $1$ and param2: $2$";

        String actualMessage = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageNotFound() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<String> params = null;
        String key = "unknown.key";
        String expectedMessage = "Key: unknown.key not found in message file";

        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "src/test/resources/messages/";

        messageHandler.addMessages(basename, dir);

        String key = "message.test.key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        String expectedMessage = "Test message with param1: $1$";

        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMessagesNullParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "src/test/resources/messages/";

        messageHandler.addMessages(basename, dir);

        String key = "message.test.key";
        List<String> params = null;

        messageHandler.getMessage(key, params);
    }

    @Test
    public void testMessagesFilter() {
        String prefix = "test";
        String postfix = "_en.properties";
        File dir = new File("src/test/resources/messages");
        MessagesFilter filter = new MessagesFilter(prefix, postfix);

        String[] files = dir.list(filter);

        assertNull(files);

        File testFile = new File(dir, "test_en.properties");
        files = new String[] { testFile.getName() };

        assertTrue(files != null && files.length == 1 && filter.accept(dir, files[0]));
    }
}