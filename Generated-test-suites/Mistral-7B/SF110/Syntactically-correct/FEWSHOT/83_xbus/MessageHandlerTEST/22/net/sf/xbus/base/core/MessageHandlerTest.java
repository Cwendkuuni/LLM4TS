package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MessageHandlerTest {

    private static final String BASE_NAME = "test";
    private static final String XBUS_ETC = "etc";
    private static final String XBUS_PLUGIN_ETC = "plugin-etc";
    private static final String ENGLISH_LOCALE = "en_US";
    private static final String MESSAGES_FILE_NAME_1 = "test_en_US.properties";
    private static final String MESSAGES_FILE_NAME_2 = "test_fr_CA.properties";
    private static final String MESSAGE_KEY_1 = "key1";
    private static final String MESSAGE_VALUE_1 = "value1";
    private static final String MESSAGE_KEY_2 = "key2";
    private static final String MESSAGE_VALUE_2 = "value2 {$1}";

    private MessageHandler messageHandler;
    private Hashtable<String, String> mMockMessages;

    @Before
    public void setUp() {
        MessageHandler.mInstances = new Hashtable<>();
        mMockMessages = new Hashtable<>();
        mMockMessages.put(MESSAGE_KEY_1, MESSAGE_VALUE_1);
        mMockMessages.put(MESSAGE_KEY_2, MESSAGE_VALUE_2);

        doReturn(mMockMessages).when(messageHandler).mMessages;
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance(BASE_NAME);
        assertEquals(messageHandler1, MessageHandler.getInstance(BASE_NAME));
    }

    @Test
    public void testGetInstanceDifferentBasename() {
        MessageHandler messageHandler1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler messageHandler2 = MessageHandler.getInstance("different_basename");
        assertNotEquals(messageHandler1, messageHandler2);
    }

    @Test
    public void testGetMessage() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);

        List<String> params = new ArrayList<>();
        params.add("param1");

        String messageText = messageHandler.getMessage(MESSAGE_KEY_1, params);
        assertEquals(MESSAGE_VALUE_1, messageText);

        messageText = messageHandler.getMessage(MESSAGE_KEY_2, params);
        assertEquals(MESSAGE_VALUE_2, messageText);

        messageText = messageHandler.getMessage("non_existent_key", params);
        assertNull(messageText);

        messageText = messageHandler.getMessage("non_existent_key", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageOptional() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);

        String messageText = messageHandler.getMessageOptional(MESSAGE_KEY_1, null);
        assertNull(messageText);

        messageText = messageHandler.getMessageOptional(MESSAGE_KEY_2, null);
        assertEquals(MESSAGE_VALUE_2, messageText);

        messageText = messageHandler.getMessageOptional("non_existent_key", null);
        assertNull(messageText);

        params = new ArrayList<>();
        params.add("param1");

        messageText = messageHandler.getMessageOptional("non_existent_key", params);
        assertNull(messageText);
    }

    @Test
    public void testAddMessages() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);

        File dir = Mockito.mock(File.class);
        when(dir.isDirectory()).thenReturn(true);
        when(dir.listFiles((FilenameFilter) anyObject())).thenReturn(new File[] { new File(MESSAGE_FILE_NAME_1), new File(MESSAGE_FILE_NAME_2) });

        Properties properties1 = new Properties();
        properties1.setProperty(MESSAGE_KEY_1, MESSAGE_VALUE_1);

        Properties properties2 = new Properties();
        properties2.setProperty(MESSAGE_KEY_1, MESSAGE_VALUE_1);
        properties2.setProperty(MESSAGE_KEY_2, MESSAGE_VALUE_2);

        doReturn(properties1).when(messageHandler).loadProperties(eq(new File(XBUS_ETC)));
        doReturn(properties2).when(messageHandler).loadProperties(eq(new File(XBUS_PLUGIN_ETC)));

        try {
            messageHandler.addMessages(BASE_NAME, XBUS_ETC);
            messageHandler.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(2, messageHandler.mMessages.size());
        assertTrue(messageHandler.mMessages.containsKey(MESSAGE_KEY_1));
        assertTrue(messageHandler.mMessages.containsKey(MESSAGE_KEY_2));
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesFailedToLoadPropertiesFile() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);

        File dir = Mockito.mock(File.class);
        when(dir.isDirectory()).thenReturn(true);
        when(dir.listFiles((FilenameFilter) anyObject())).thenReturn(new File[] {});

        try {
            messageHandler.addMessages(BASE_NAME, XBUS_ETC);
        } finally {
            messageHandler.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);
        }

        fail("Unexpected success");
    }

    @Test
    public void testLoadProperties() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);

        Properties properties = new Properties();
        properties.setProperty(MESSAGE_KEY_1, MESSAGE_VALUE_1);

        InputStream inputStream = Mockito.mock(InputStream.class);
        when(inputStream.available()).thenReturn(properties.size() * 2);
        when(inputStream.readAllBytes()).thenReturn(properties.toString().getBytes());

        try {
            messageHandler.loadProperties(Mockito.mock(File.class));
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(1, messageHandler.mMessages.size());
        assertTrue(messageHandler.mMessages.containsKey(MESSAGE_KEY_1));
    }
}