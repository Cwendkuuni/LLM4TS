package net.sf.xbus.base.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;
import java.io.*;
import java.util.*;
import net.sf.xbus.base.core.config.*;

public class MessageHandlerTest
{
    @Test
    public void testGetInstance()
    {
        MessageHandler messageHandler1 = MessageHandler.getInstance("testBasename");
        MessageHandler messageHandler2 = MessageHandler.getInstance("testBasename");
        assertTrue(messageHandler1 == messageHandler2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageNullKey()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String result = messageHandler.getMessage(null, null);
        fail("Expected NullPointerException, but got: " + result);
    }

    @Test
    public void testGetMessageEmptyList()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String key = "testKey";
        String result = messageHandler.getMessage(key, new ArrayList<>());
        assertEquals("Key: " + key + " not found in message file", result);
    }

    @Test
    public void testGetMessageBasic()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String key = "testKey";
        List params = new ArrayList();
        String expected = "This is a test message";
        params.add("testParam");
        String result = messageHandler.getMessage(key, params);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessageOptionalEmpty()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String key = "testKey";
        List params = null;
        String result = messageHandler.getMessageOptional(key, params);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptionalBasic()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String key = "{$0} testKey";
        List params = new ArrayList();
        params.add("testParam");
        String expected = "testParam testKey";
        String result = messageHandler.getMessageOptional(key, params);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageOptionalNullParams()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String key = "testKey";
        List params = null;
        String result = messageHandler.getMessageOptional(key, params);
        fail("Expected NullPointerException, but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageOptionalInvalidParam()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        String key = "{$0} testKey";
        List params = new ArrayList();
        params.add(null);
        String result = messageHandler.getMessageOptional(key, params);
        fail("Expected IllegalArgumentException, but got: " + result);
    }

    @Test
    public void testAddMessagesNullBasename()
    {
        MessageHandler messageHandler = new MessageHandler(null);
        messageHandler.addMessages("testBasename", Constants.XBUS_ETC);
        assertTrue(messageHandler.mMessages == null);
    }

    @Test
    public void testAddMessagesNullDir()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        messageHandler.addMessages(null, Constants.XBUS_ETC);
        assertTrue(messageHandler.mMessages == null);
    }

    @Test
    public void testAddMessagesEmptyDir()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        messageHandler.addMessages("testBasename", "");
        assertTrue(messageHandler.mMessages == null);
    }

    @Test
    public void testAddMessagesMultipleFiles()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        messageHandler.addMessages("testBasename", Constants.XBUS_ETC);
        assertTrue(messageHandler.mMessages != null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesInvalidFile()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        messageHandler.addMessages("testBasename", "/invalid/file/path");
        fail("Expected FileNotFoundException, but no exception was thrown");
    }

    @Test(expected = IOException.class)
    public void testAddMessagesInvalidFileStream()
    {
        MessageHandler messageHandler = new MessageHandler("testBasename");
        messageHandler.addMessages("testBasename", Constants.XBUS_ETC);
        Properties newProps = new Properties();
        try {
            final FileInputStream instream = new FileInputStream("testFile.properties");
            newProps.load(instream);
            instream.close();
        }
        catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException: " + e.getMessage());
        }
        messageHandler.addMessages("testBasename", Constants.XBUS_ETC, newProps);
        fail("Expected IOException, but no exception was thrown");
    }
}