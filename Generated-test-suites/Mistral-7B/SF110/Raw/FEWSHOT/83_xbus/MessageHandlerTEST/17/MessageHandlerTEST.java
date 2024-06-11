package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private static final String BASE_NAME = "testMessageHandler";
    private static final String XBUS_ETC = "xbus_etc";
    private static final String XBUS_PLUGIN_ETC = "xbus_plugin_etc";
    private static final String ENGLISH_LOCALE = "en_US";
    private static final String MESSAGE_FILE_PREFIX = "testMessageHandler_";
    private static final String MESSAGE_FILE_SUFFIX = "_en_US.properties";
    private static final String KEY_1 = "key1";
    private static final String VALUE_1 = "value1";
    private static final String KEY_2 = "key2";
    private static final String VALUE_2 = "value2 {0}";
    private static final String PARAM_1 = "param1";

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);
    }

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler handler2 = MessageHandler.getInstance(BASE_NAME);
        assertEquals(handler1, handler2);
    }

    @Test
    public void testGetMessageOptionalNullParams() {
        String message = messageHandler.getMessageOptional(KEY_1, null);
        assertNull(message);
    }

    @Test
    public void testGetMessageOptionalEmptyParams() {
        List emptyParams = new ArrayList();
        String message = messageHandler.getMessageOptional(KEY_2, emptyParams);
        assertEquals(VALUE_2, message);
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        List params = new ArrayList();
        params.add(PARAM_1);
        String message = messageHandler.getMessageOptional(KEY_2, params);
        assertEquals(VALUE_2.replace("{0}", PARAM_1), message);
    }

    @Test
    public void testGetMessageOptionalMissingKey() {
        String message = messageHandler.getMessageOptional("missingKey", null);
        assertNull(message);
    }

    @Test
    public void testAddMessages() {
        // Given
        Properties props = new Properties();
        props.setProperty(KEY_1, VALUE_1);
        props.setProperty(KEY_2, VALUE_2);

        // When
        File dir = mock(File.class);
        when(dir.list(any(FilenameFilter.class))).thenReturn(new String[]{MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX});
        when(dir.getAbsolutePath()).thenReturn("/test/directory");
        when(new File("/test/directory", MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX).exists()).thenReturn(true);
        when(new File("/test/directory", MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX).isFile()).thenReturn(true);
        when(new FileInputStream("/test/directory" + File.separator + MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX)).thenReturn(props.inputStream());

        messageHandler.addMessages(BASE_NAME, "/test/directory");

        // Then
        Map<String, String> messages = messageHandler.mMessages;
        assertEquals(2, messages.size());
        assertTrue(messages.containsKey(KEY_1));
        assertTrue(messages.containsKey(KEY_2));
        assertEquals(VALUE_1, messages.get(KEY_1));
        assertEquals(VALUE_2, messages.get(KEY_2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddMessagesMissingMessageFile() {
        // Given
        Properties props = new Properties();
        props.setProperty(KEY_1, VALUE_1);
        props.setProperty(KEY_2, VALUE_2);

        // When
        File dir = mock(File.class);
        when(dir.list(any(FilenameFilter.class))).thenReturn(new String[]{MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX});
        when(dir.getAbsolutePath()).thenReturn("/test/directory");
        when(new File("/test/directory", MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX).exists()).thenReturn(false);

        // Then
        messageHandler.addMessages(BASE_NAME, "/test/directory");

        // Assertion error due to uncaught exception
    }

    @Test(expected = RuntimeException.class)
    public void testAddMessagesInvalidMessageFile() {
        // Given
        Properties props = new Properties();
        props.setProperty(KEY_1, VALUE_1);
        props.setProperty(KEY_2, VALUE_2);

        // When
        File dir = mock(File.class);
        when(dir.list(any(FilenameFilter.class))).thenReturn(new String[]{MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX});
        when(dir.getAbsolutePath()).thenReturn("/test/directory");
        when(new File("/test/directory", MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX).exists()).thenReturn(true);
        when(new File("/test/directory", MESSAGE_FILE_PREFIX + ENGLISH_LOCALE + MESSAGE_FILE_SUFFIX).isFile()).thenReturn(false);

        // Then
        messageHandler.addMessages(BASE_NAME, "/test/directory");

        // Assertion error due to uncaught exception
    }
}