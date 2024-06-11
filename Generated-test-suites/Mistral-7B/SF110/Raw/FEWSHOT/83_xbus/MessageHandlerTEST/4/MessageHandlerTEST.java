package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

    private static final String BASE_NAME = "test";
    private static final String XBUS_ETC = "etc";
    private static final String XBUS_PLUGIN_ETC = "plugin_etc";
    private static final String ENGLISH = "en";
    private static final String MESSAGE_FILE_PREFIX = "MessageHandler.";
    private static final String MESSAGE_FILE_SUFFIX = "_en.properties";

    private MessageHandler messageHandler;
    private Properties testProperties;

    @Before
    public void setup() throws IOException {
        testProperties = new Properties();
        testProperties.put("key1", "Message for key1 {0} {1}");
        testProperties.put("key2", "Message for key2 {2}");

        messageHandler = new MessageHandler(BASE_NAME);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetMessageOptional() {
        List params = new ArrayList<>();
        params.add("param1");
        params.add(123);

        assertEquals("Message for key1 param1 123", messageHandler.getMessageOptional("key1", params));
        assertEquals("Message for key2 {2}", messageHandler.getMessageOptional("key2", null));
        assertEquals("Key: key3 not found in message file", messageHandler.getMessageOptional("key3", params));
        assertEquals("", messageHandler.getMessageOptional(null, params));
    }

    @Test
    public void testGetMessage() {
        List params = new ArrayList<>();
        params.add("param1");
        params.add(123);

        assertEquals("Message for key1 param1 123", messageHandler.getMessage("key1", params));
        assertEquals("Message for key2 {2}", messageHandler.getMessage("key2", null));
        assertEquals("Key: key3 not found in message file", messageHandler.getMessage("key3", params));
        assertEquals("", messageHandler.getMessage(null, params));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullParams() {
        messageHandler.getMessage("key1", null);
    }

    @Test
    public void testGetMessageWithEmptyParams() {
        List emptyParams = new ArrayList<>();

        assertEquals("Message for key1 {} ", messageHandler.getMessage("key1", emptyParams));
        assertEquals("Message for key2 {2}", messageHandler.getMessage("key2", null));
    }

    @Test
    public void testAddMessages() throws IOException {
        String testDir = System.getProperty("user.dir");
        MessageHandler messageHandlerSpy = mock(MessageHandler.class);

        when(messageHandlerSpy.addMessages(BASE_NAME, XBUS_ETC)).thenCallRealMethod();
        when(messageHandlerSpy.addMessages(BASE_NAME, XBUS_PLUGIN_ETC)).thenCallRealMethod();

        File testMessagesDir = new File(testDir + "/testMessages");
        testMessagesDir.mkdir();

        Properties testProperties1 = new Properties();
        testProperties1.put("key11", "Message for key11 {0}");
        testProperties1.put("key12", "Message for key12 {1} {2}");

        Properties testProperties2 = new Properties();
        testProperties2.put("key21", "Message for key21 {3} {4}");
        testProperties2.put("key22", "Message for key22 {5}");

        File testMessageFile1 = new File(testMessagesDir.getPath() + "/" + MESSAGE_FILE_PREFIX + BASE_NAME + "_en.properties");
        try (FileOutputStream testOutputStream1 = new FileOutputStream(testMessageFile1)) {
            testProperties1.store(testOutputStream1, null);
        }

        File testMessageFile2 = new File(testMessagesDir.getPath() + "/" + MESSAGE_FILE_PREFIX + BASE_NAME + "_en.properties");
        try (FileOutputStream testOutputStream2 = new FileOutputStream(testMessageFile2)) {
            testProperties2.store(testOutputStream2, null);
        }

        messageHandlerSpy.addMessages(BASE_NAME, XBUS_ETC);
        messageHandlerSpy.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);

        assertEquals(2, messageHandlerSpy.mMessages.size());

        String key11 = (String) messageHandlerSpy.mMessages.get("key11");
        String key12 = (String) messageHandlerSpy.mMessages.get("key12");
        String key21 = (String) messageHandlerSpy.mMessages.get("key21");
        String key22 = (String) messageHandlerSpy.mMessages.get("key22");

        assertEquals("Message for key11 {0}", key11);
        assertEquals("Message for key12 {1} {2}", key12);
        assertEquals("Message for key21 {3} {4}", key21);
        assertEquals("Message for key22 {5}", key22);

        testMessagesDir.delete();
    }
}