import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.Trace;
import java.io.*;
import java.util.*;
import java.util.Properties.PropertyDescriptor;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;

    @org.junit.BeforeClass
    public static void setUpBeforeClass() {
        messageHandler = MessageHandler.getInstance("MessageHandlerTest");
    }

    @org.junit.Test
    public void testGetMessage() {
        String key = "testKey";
        List params = new ArrayList<>();
        String expectedMessage = "Expected message when key is found";
        String actualMessage = messageHandler.getMessage(key, params);
        Trace.error = new StubErrorLogger();
        assertEquals(expectedMessage, actualMessage);
    }

    @org.junit.Test
    public void testGetMessage_EmptyMessageFile() {
        String key = "testKey";
        List params = new ArrayList<>();
        File dir = new File("path/to/empty/message/file");
        messageHandler.addMessages("test", dir.getAbsolutePath());
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @org.junit.Test
    public void testGetMessage_NonExistentMessageFile() {
        String key = "testKey";
        List params = new ArrayList<>();
        File dir = new File("path/to/nonExistent/message/file");
        messageHandler.addMessages("test", dir.getAbsolutePath());
        assertNull(messageHandler.getMessage(key, params));
    }

    @org.junit.Test
    public void testGetMessage_InvalidMessageFileFormat() {
        String key = "testKey";
        List params = new ArrayList<>();
        File dir = new File("path/to/invalid/message/file");
        messageHandler.addMessages("test", dir.getAbsolutePath());
        assertNull(messageHandler.getMessage(key, params));
    }

    @org.junit.Test
    public void testGetMessageOptional_KeyNotFound() {
        String key = "testKey";
        List params = new ArrayList<>();
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @org.junit.Test
    public void testGetMessageOptional_KeyFoundWithParams() {
        String key = "testKey";
        String paramValue = "paramValue";
        List params = new ArrayList<>();
        params.add(paramValue);
        String expectedMessage = "Expected message with paramValue";
        String actualMessage = messageHandler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetMessageOptional_NullParams() {
        String key = "testKey";
        List params = null;
        messageHandler.getMessageOptional(key, params);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testGetMessageOptional_NullParam() {
        String key = "testKey";
        List params = new ArrayList<>();
        params.add(null);
        messageHandler.getMessageOptional(key, params);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetMessageOptional_InvalidParamType() {
        String key = "testKey";
        List params = new ArrayList<>();
        params.add(new Object());
        messageHandler.getMessageOptional(key, params);
    }

    @org.junit.Test
    public void testGetInstance() {
        String basename = "test";
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        assertEquals(instance1, instance2);
    }

    @org.junit.Test
    public void testAddMessages_EmptyMessageFile() {
        String basename = "test";
        String dir = "path/to/empty/message/file";
        messageHandler.addMessages(basename, dir);
        assertEquals(0, messageHandler.mMessages.size());
    }

    @org.junit.Test
    public void testAddMessages_NonExistentMessageFile() {
        String basename = "test";
        String dir = "path/to/nonExistent/message/file";
        messageHandler.addMessages(basename, dir);
        assertEquals(0, messageHandler.mMessages.size());
    }

    @org.junit.Test
    public void testAddMessages_InvalidMessageFileFormat() {
        String basename = "test";
        String dir = "path/to/invalid/message/file";
        messageHandler.addMessages(basename, dir);
        assertEquals(0, messageHandler.mMessages.size());
    }

    @org.junit.Test
    public void testAddMessages_ValidMessageFile() {
        String basename = "test";
        String dir = "path/to/valid/message/file";
        messageHandler.addMessages(basename, dir);
        int expectedSize = 2; // Number of messages in the valid message file
        assertEquals(expectedSize, messageHandler.mMessages.size());
    }

    @org.junit.Test
    public void testAddMessages_MultipleMessageFiles() {
        String basename = "test";
        String dir = "path/to/message/files";
        messageHandler.addMessages(basename, dir);
        int expectedSize = 4; // Number of messages in all message files
        assertEquals(expectedSize, messageHandler.mMessages.size());
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() {
        messageHandler = null;
    }
}

class StubErrorLogger implements Trace.ErrorLogger {
    @Override
    public void logError(String message) {
        // Do nothing
    }
}

class MessagesFilter implements FileNameFilter {
    // Implementation similar to the existing one
}