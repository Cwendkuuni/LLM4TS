package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

    private static final String TEST_BASENAME = "TestMessageHandler";
    private static final String TEST_DIR = "test-messages";
    private static final String TEST_MESSAGE_FILE_EN = "TestMessageHandler_en_US.properties";
    private static final String TEST_MESSAGE_FILE_DE = "TestMessageHandler_de_DE.properties";
    private static final String TEST_MESSAGE_KEY_1 = "key1";
    private static final String TEST_MESSAGE_VALUE_1_EN = "Message 1 in English";
    private static final String TEST_MESSAGE_VALUE_1_DE = "Message 1 in German";
    private static final String TEST_MESSAGE_KEY_2 = "key2";
    private static final String TEST_MESSAGE_VALUE_2 = "Message 2";

    private MessageHandler messageHandler;
    private Properties testPropertiesEN;
    private Properties testPropertiesDE;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance(TEST_BASENAME);

        testPropertiesEN = new Properties();
        testPropertiesEN.put(TEST_MESSAGE_KEY_1, TEST_MESSAGE_VALUE_1_EN);
        testPropertiesEN.put(TEST_MESSAGE_KEY_2, TEST_MESSAGE_VALUE_2);

        testPropertiesDE = new Properties();
        testPropertiesDE.put(TEST_MESSAGE_KEY_1, TEST_MESSAGE_VALUE_1_DE);

        when(testPropertiesEN.getProperty(TEST_MESSAGE_KEY_1)).thenReturn(TEST_MESSAGE_VALUE_1_EN);
        when(testPropertiesEN.getProperty(TEST_MESSAGE_KEY_2)).thenReturn(TEST_MESSAGE_VALUE_2);
        when(testPropertiesDE.getProperty(TEST_MESSAGE_KEY_1)).thenReturn(TEST_MESSAGE_VALUE_1_DE);

        File dirFile = new File(TEST_DIR);
        File testFileEN = new File(TEST_DIR, TEST_MESSAGE_FILE_EN);
        File testFileDE = new File(TEST_DIR, TEST_MESSAGE_FILE_DE);

        when(testFileEN.exists()).thenReturn(true);
        when(testFileEN.isFile()).thenReturn(true);
        when(testFileDE.exists()).thenReturn(true);
        when(testFileDE.isFile()).thenReturn(true);

        when(testFileEN.list(Mockito.isInstance(MessagesFilter.class))).thenReturn(new String[] { TEST_MESSAGE_FILE_EN });
        when(testFileDE.list(Mockito.isInstance(MessagesFilter.class))).thenReturn(new String[] { TEST_MESSAGE_FILE_DE });
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance(TEST_BASENAME);
        MessageHandler instance2 = MessageHandler.getInstance(TEST_BASENAME);

        assertTrue(instance1 == instance2);
    }

    @Test
    public void testAddMessages() {
        messageHandler.addMessages(TEST_BASENAME, TEST_DIR);

        Hashtable messages = messageHandler.mMessages;

        assertEquals(2, messages.size());
        assertEquals(TEST_MESSAGE_VALUE_1_EN, messages.get(TEST_MESSAGE_KEY_1));
        assertEquals(TEST_MESSAGE_VALUE_2, messages.get(TEST_MESSAGE_KEY_2));
    }

    @Test
    public void testGetMessage() {
        messageHandler.addMessages(TEST_BASENAME, TEST_DIR);

        String message = messageHandler.getMessage(TEST_MESSAGE_KEY_1, null);

        assertEquals(TEST_MESSAGE_VALUE_1_EN, message);
    }

    @Test
    public void testGetMessageWithParams() {
        List params = new ArrayList();
        params.add("Param 1");

        String message = messageHandler.getMessage(TEST_MESSAGE_KEY_1, params);

        assertEquals(XStringSupport.replaceAll(TEST_MESSAGE_VALUE_1_EN, "$1$", "Param 1"), message);
    }

    @Test
    public void testGetMessageOptional() {
        String message = messageHandler.getMessageOptional("NotExistingKey", null);

        assertNull(message);
    }
}