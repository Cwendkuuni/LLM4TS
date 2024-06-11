package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MessageHandlerTest {

    private static final String BASE_NAME = "testBaseName";
    private static final String DIRECTORY = "testDirectory";
    private static final String MESSAGE_FILE = "testMessageFile.properties";
    private static final String KEY = "testKey";
    private static final String VALUE = "testValue";
    private static final String PARAM_KEY = "paramKey";
    private static final String PARAM_VALUE = "paramValue";

    private MessageHandler messageHandler;
    private Hashtable messages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        messageHandler = MessageHandler.getInstance(BASE_NAME);
        messages = messageHandler.mMessages;
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);

        assertEquals(instance1, instance2);
    }

    @Test
    public void testAddMessages() throws IOException {
        when(new File(DIRECTORY).listFiles((FilenameFilter) anyObject()))
                .thenReturn(new File[] { new File(MESSAGE_FILE) });

        Properties properties = mock(Properties.class);
        when(properties.load(any(FileInputStream.class)))
                .thenReturn();

        doReturn(properties).when(messageHandler).loadPropertiesFromFile(eq(DIRECTORY), eq(MESSAGE_FILE));

        messageHandler.addMessages(BASE_NAME, DIRECTORY);

        assertEquals(1, messages.size());
        assertEquals(VALUE, messages.get(KEY));
    }

    @Test
    public void testGetMessage() {
        when(messages.get(KEY)).thenReturn(VALUE);

        String result = messageHandler.getMessage(KEY, null);

        assertEquals(VALUE, result);
    }

    @Test
    public void testGetMessageOptional() {
        when(messages.get(KEY)).thenReturn(null);

        String result = messageHandler.getMessageOptional(KEY, null);

        assertNull(result);
    }

    @Test
    public void testGetMessageWithParams() {
        when(messages.get(KEY)).thenReturn("{$0} {$1}");

        List params = new ArrayList();
        params.add(PARAM_VALUE);

        String result = messageHandler.getMessage(KEY, params);

        assertEquals("testValue testParamValue", result);
    }

    @Test
    public void testGetMessageWithEmptyParams() {
        when(messages.get(KEY)).thenReturn("{$0} {$1}");

        List params = Collections.emptyList();

        String result = messageHandler.getMessage(KEY, params);

        assertEquals("", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageWithNullParams() {
        when(messages.get(KEY)).thenReturn("{$0} {$1}");

        String result = messageHandler.getMessage(KEY, null);

        fail("Expected NullPointerException but got: " + result);
    }
}