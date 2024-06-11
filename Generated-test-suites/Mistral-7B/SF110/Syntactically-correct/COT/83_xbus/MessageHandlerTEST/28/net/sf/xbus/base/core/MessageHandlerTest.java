package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.config.Constants;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertEquals(handler1, handler2);
    }

    @Test
    public void testGetMessageNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String result = handler.getMessage("key", null);
        assertNull(result);
    }

    @Test
    public void testGetMessageEmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String result = handler.getMessage("key", new ArrayList<>());
        assertNotNull(result);
    }

    @Test
    public void testGetMessageWithParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String result = handler.getMessage("key.$1$.$2$", params);
        String expected = "param1 param2";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageNullMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.mMessages = null;
        String result = handler.getMessage("key", null);
        fail("Expected NullPointerException, but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageInvalidKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String result = handler.getMessage(null, null);
        fail("Expected IllegalArgumentException, but got: " + result);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        assertNull(handler.mMessages);
        handler.addMessages("test", Constants.XBUS_ETC);
        Hashtable messages = handler.mMessages;
        assertTrue(messages.size() > 0);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        int size = messages.size();
        assertEquals(size + 1, messages.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesFileNotFound() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "/nonexistentdirectory");
        fail("Expected FileNotFoundException, but no exception thrown");
    }

    @Test(expected = IOException.class)
    public void testAddMessagesIOException() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", ".");
        File tempFile = new File("tempfile.properties");
        try {
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            outputStream.close();
            throw new IOException();
        } catch (FileNotFoundException e) {
            // Expected FileNotFoundException when creating tempFile, ignore it
        }
        handler.addMessages("test", tempFile.getAbsolutePath());
        fail("Expected IOException, but no exception thrown");
    }

    @Test
    public void testGetMessageOptionalNullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String result = handler.getMessageOptional(null, null);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptionalEmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String result = handler.getMessageOptional("key", null);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String result = handler.getMessageOptional("key.$1$.$2$", params);
        String expected = "key param1 param2";
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptionalNullMessage() {
        MessageHandler handler = new MessageHandler("test");
        String result = handler.getMessageOptional("key", null);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageOptionalInvalidKey() {
        MessageHandler handler = new MessageHandler("test");
        String result = handler.getMessageOptional(null, null);
        fail("Expected IllegalArgumentException, but got: " + result);
    }

    @Test
    public void testGetMessageOptionalWithInvalidKey() {
        MessageHandler handler = new MessageHandler("test");
        String result = handler.getMessageOptional("$invalidkey$", null);
        assertNull(result);
    }
}