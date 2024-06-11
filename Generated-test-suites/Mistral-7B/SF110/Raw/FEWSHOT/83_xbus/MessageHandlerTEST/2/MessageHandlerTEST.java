package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import static net.sf.xbus.base.core.strings.XStringSupport.replaceAll;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance("test");
        MessageHandler instance2 = MessageHandler.getInstance("test");

        assertEquals(instance1, instance2);
    }

    @Test
    public void testGetMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");

        String messageText = handler.getMessage("test.message.key", null);
        assertNull(messageText);

        handler.addMessages("test", "src/test/resources/messages");

        messageText = handler.getMessage("test.message.key", null);
        assertEquals("test message key", messageText);

        messageText = handler.getMessage("test.message.key.with.params", Arrays.asList("param1", "param2"));
        assertEquals("test message key $1$ and $2$", messageText);

        messageText = handler.getMessage("test.message.key.with.empty.param", Arrays.asList("", "param2"));
        assertEquals("test message key <null> and param2", messageText);

        messageText = handler.getMessage("test.message.key.with.null.param", Collections.singletonList(null));
        assertEquals("test message key <null> and null", messageText);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");

        handler.addMessages("test", "src/test/resources/messages");

        Properties messages = handler.getMessages("test");
        assertEquals(3, messages.size());
        assertEquals("message1", messages.getProperty("message1"));
        assertEquals("message2 with {$0}", messages.getProperty("message2"));
        assertEquals("message3 with {$0} and {$1}", messages.getProperty("message3"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.params", null);
        fail("Expected NullPointerException, but got: " + messageText);
    }

    @Test
    public void testGetMessageWithEmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.empty.params", Collections.emptyList());
        assertEquals("test message key <null>", messageText);
    }

    @Test
    public void testGetMessageWithNullMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        String messageText = handler.getMessage("test.nonexistent.key", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageWithMissingMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.nonexistent.key", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.nonexistent.key.with.params", Arrays.asList("param1", "param2"));
        assertNull(messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndEmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.nonexistent.key.with.empty.params", Collections.emptyList());
        assertEquals("test message key <null>", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndNullParam() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.nonexistent.key.with.null.param", Collections.singletonList(null));
        assertEquals("test message key <null>", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParamsWithReplace() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.replace", Arrays.asList("param1", "param2"));
        assertEquals("test message key param1 and param2", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParamsWithReplaceEmpty() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.replace.empty", Collections.emptyList());
        assertEquals("test message key <null>", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParamsWithReplaceNull() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.replace.null", Collections.singletonList(null));
        assertEquals("test message key <null>", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParamsWithReplaceAll() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.replace.all", Arrays.asList("param1", "param2"));
        assertEquals("test message key param1 and param2 and param1 and param2", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParamsWithReplaceAllEmpty() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.replace.all.empty", Collections.emptyList());
        assertEquals("test message key <null> and <null>", messageText);
    }

    @Test
    public void testGetMessageWithMissingMessageAndParamsWithReplaceAllNull() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "src/test/resources/messages");

        String messageText = handler.getMessage("test.message.key.with.replace.all.null", Collections.singletonList(null));
        assertEquals("test message key <null> and <null>", messageText);
    }

    @Test
    public void testTraceErrorMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        String messageText = handler.getMessage("test.message.key.trace.error", null);
        assertNull(messageText);

        try {
            throw new Exception("Test Exception");
        } catch (Exception e) {
            handler.getMessage("test.message.key.trace.error", Collections.singletonList(e.getMessage()));
        }

        Trace.error("Test Trace Error Message");
    }
}