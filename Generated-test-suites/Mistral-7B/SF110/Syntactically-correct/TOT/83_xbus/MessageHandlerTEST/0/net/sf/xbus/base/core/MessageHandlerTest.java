package net.sf.xbus.base.core;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MessageHandlerTest {

    @Test
    public void testGetInstanceWithValidBasename() {
        MessageHandler handler = MessageHandler.getInstance("test");
        assertNotNull(handler);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullBasename() {
        MessageHandler handler = MessageHandler.getInstance(null);
    }

    @Test
    public void testGetMessagesWithValidBasenameAndDir() {
        MessageHandler handler = MessageHandler.getInstance("test");
        assertEquals(2, handler.mMessages.size()); // Assuming two properties files are present
    }

    @Test
    public void testGetMessageWithValidKeyAndValidParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String expectedMessage = "This is a test message with param: Test param";
        String resultMessage = handler.getMessage("test.message.key", Arrays.asList("Test param"));
        assertEquals(expectedMessage, resultMessage);
    }

    @Test
    public void testGetMessageWithValidKeyAndEmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String expectedMessage = "This is a test message";
        String resultMessage = handler.getMessage("test.message.key.no_params", null);
        assertEquals(expectedMessage, resultMessage);
    }

    @Test
    public void testGetMessageWithInvalidKeyAndParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String resultMessage = handler.getMessage("invalid.key", Arrays.asList("Test param"));
        assertNull(resultMessage);
    }

    @Test
    public void testGetMessageWithoutParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String expectedMessage = "This is a test message without params";
        String resultMessage = handler.getMessage("test.message.key.no_params", null);
        assertEquals(expectedMessage, resultMessage);
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetInstanceWithBasenameNotFindingMessageFiles() {
        MessageHandler handler = MessageHandler.getInstance("test_no_files");
    }

    @Test(expected = IOException.class)
    public void testAddMessagesWithIOExceptionWhileReadingPropertiesFile() {
        MessageHandler handler = new MessageHandler("test_io_exception");
    }

    @Test
    public void testMessageFilterAcceptance() {
        String [] files = {"test_messages_en_US.properties", "test_messages_fr_FR.properties"};
        MessagesFilter filter = new MessagesFilter("test_messages", "_%s.properties");
        for (String file : files) {
            assertTrue(filter.accept(null, file));
        }
    }

    @Test
    public void testMessageFilterRejection() {
        String [] files = {"test_messages_en_US.properties", "test_messages_fr_FR_messages.txt"};
        MessagesFilter filter = new MessagesFilter("test_messages", "_%s.properties");
        for (String file : files) {
            assertFalse(filter.accept(null, file));
        }
    }

    @Test
    public void testSingletonInstanceRetrieval() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertEquals(handler1, handler2);
    }

    @Test
    public void testSingletonInstanceAddition() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = new MessageHandler("test");
        assertEquals(handler1, handler2);
    }

    @Test
    public void testMessageHandlingWithValidParameters() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessage("test.message.key.with_params", Arrays.asList("Test", 1));
        String expectedMessage = "This is a test message with param: Test and number: 1";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testMessageHandlingWithEmptyParameters() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessage("test.message.key.with_empty_params", null);
        String expectedMessage = "This is a test message with empty params";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testMessageHandlingWithNullParameters() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessage("test.message.key.with_null_params", null);
        String expectedMessage = "This is a test message with null params";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testMessageHandlingWithMissingKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessage("test.message.missing_key", null);
        assertNull(message);
    }

    @Test
    public void testMessageHandlingWithMissingFile() {
        MessageHandler handler = MessageHandler.getInstance("test_missing_file");
        String message = handler.getMessage("test.message.key", null);
        assertNull(message);
    }

    @Test
    public void testMessageHandlingWithIOException() {
        MessageHandler handler = new MessageHandler("test_io_exception");
        String message = handler.getMessage("test.message.key", null);
        assertNull(message);
    }

    @Test
    public void testMessageHandlingWithInvalidPostfix() {
        MessageHandler handler = new MessageHandler("test_invalid_postfix");
        String message = handler.getMessage("test.message.key", null);
        assertNull(message);
    }
}