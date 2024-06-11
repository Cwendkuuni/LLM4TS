package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest
{
    private static MessageHandler messageHandler;
    private static MessageHandler instance1;
    private static MessageHandler instance2;

    @BeforeClass
    public static void setUpBeforeClass()
    {
        messageHandler = MessageHandler.getInstance(MessageHandlerTest.class.getName());
        instance1 = MessageHandler.getInstance(MessageHandlerTest.class.getName());
        instance2 = MessageHandler.getInstance(MessageHandlerTest.class.getName());
    }

    @AfterClass
    public static void tearDownAfterClass()
    {
        messageHandler = null;
        instance1 = null;
        instance2 = null;
    }

    @Test
    public void testGetMessage()
    {
        List params = new ArrayList();
        String key = "testKey";
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);

        // Test with valid key and parameters
        String validKey = "testKeyWithParams";
        List validParams = new ArrayList(Arrays.asList(new Object[] {"Test", 1, "value"}));
        String expectedValidMessage = "Key: testKeyWithParams $Test$ $1$ value";
        String actualValidMessage = messageHandler.getMessage(validKey, validParams);
        String replacedValidMessage = XStringSupport.replaceAll(expectedValidMessage, "$Test$", "Test");
        String replacedActualValidMessage = XStringSupport.replaceAll(actualValidMessage, "$Test$", "Test");
        assertEquals(replacedValidMessage, replacedActualValidMessage);
    }

    @Test
    public void testGetMessageOptional()
    {
        String key = "testOptionalKeyWithParams";
        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional(key, null);
        assertEquals(expectedMessage, actualMessage);

        // Test with valid key and parameters
        String validKey = "testOptionalKey";
        String expectedValidMessage = "Valid message for testOptionalKey";
        messageHandler.mMessages.put(validKey, expectedValidMessage);
        String actualValidMessage = messageHandler.getMessageOptional(validKey, null);
        assertEquals(expectedValidMessage, actualValidMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullKey()
    {
        List params = new ArrayList();
        messageHandler.getMessage(null, params);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullParams()
    {
        String key = "testKeyWithNullParams";
        messageHandler.getMessage(key, null);
    }

    @Test
    public void testGetInstance()
    {
        assertEquals(instance1, instance2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidBasename()
    {
        MessageHandler invalidMessageHandler = MessageHandler.getInstance("InvalidBasename");
    }

    @Test
    public void testAddMessages()
    {
        String basename = MessageHandlerTest.class.getName();
        messageHandler = MessageHandler.getInstance(basename);

        String dir = "testDir";
        messageHandler.addMessages(basename, dir);

        Map messages = messageHandler.mMessages;
        assertEquals(2, messages.size());
        assertTrue(messages.containsKey(Constants.XBUS_ETC + "_ENGLISH_properties"));
        assertTrue(messages.containsKey(Constants.XBUS_PLUGIN_ETC + "_ENGLISH_properties"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesWithInvalidDir()
    {
        String basename = MessageHandlerTest.class.getName();
        messageHandler = MessageHandler.getInstance(basename);

        String invalidDir = "invalidDir";
        messageHandler.addMessages(basename, invalidDir);
    }

    @Test(expected = IOException.class)
    public void testAddMessagesWithInvalidFile()
    {
        String basename = MessageHandlerTest.class.getName();
        messageHandler = MessageHandler.getInstance(basename);

        String dir = "testDir";
        messageHandler.addMessages(basename, dir);

        String invalidFileName = "testDir/invalidFileName.properties";
        messageHandler.addMessages(basename, invalidFileName);
    }
}