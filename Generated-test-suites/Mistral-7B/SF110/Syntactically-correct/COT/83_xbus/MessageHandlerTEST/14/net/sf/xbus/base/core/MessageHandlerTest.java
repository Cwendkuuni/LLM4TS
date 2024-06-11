package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.Test;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertTrue(handler1 == handler2);
    }

    @Test
    public void testGetMessage_NullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String result = handler.getMessage("testKey", null);
        assertEquals("Key: testKey not found in message file", result);
    }

    @Test
    public void testGetMessage_EmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        String result = handler.getMessage(key, new ArrayList<>());
        assertEquals("Key: testKey not found in message file", result);
    }

    @Test
    public void testGetMessage_ValidParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey{$0}";
        List<String> params = new ArrayList<>();
        params.add("Test Value");
        String result = handler.getMessage(key, params);
        assertEquals("Test Value", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessage_NullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        handler.getMessage(null, params);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessage_NullParamsList() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        handler.getMessage(key, null);
    }

    @Test
    public void testGetMessageOptional_NullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        String result = handler.getMessageOptional(key, null);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_ValidParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey{$0}";
        List<String> params = new ArrayList<>();
        params.add("Test Value");
        String result = handler.getMessageOptional(key, params);
        assertEquals("Test Value", result);
    }

    @Test
    public void testGetMessageOptional_EmptyParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        String result = handler.getMessageOptional(key, new ArrayList<>());
        assertNull(result);
    }

    @Test
    public void testGetMessageOptional_NullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<String> params = new ArrayList<>();
        String result = handler.getMessageOptional(null, params);
        assertNull(result);
    }

    @Test
    public void testAddMessages_NullBasename() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String dir = "testDir";
        handler.addMessages(null, dir);
        assertTrue(handler.mMessages == null);
    }

    @Test
    public void testAddMessages_NullDir() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        handler.addMessages(basename, null);
        assertTrue(handler.mMessages == null);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "testDir";
        handler.addMessages(basename, dir);
        assertTrue(handler.mMessages != null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_FileNotFound() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "testDir";
        handler.addMessages(basename, dir);
        File file = new File("nonExistingFile.properties");
        handler.addMessages(basename, file.getAbsolutePath());
    }

    @Test(expected = IOException.class)
    public void testAddMessages_IOException() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "testDir";
        handler.addMessages(basename, dir);
        Properties props = new Properties();
        FileInputStream stream = new FileInputStream("testFile.properties");
        try {
            props.load(stream);
            stream.close();
        } catch (IOException e) {
            throw e;
        }
        handler.addMessages(basename, props);
    }

    @Test
    public void testGetMessage_EmptyMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "testKey";
        List<String> params = new ArrayList<>();
        String result = handler.getMessage(key, params);
        assertEquals("", result);
    }

}