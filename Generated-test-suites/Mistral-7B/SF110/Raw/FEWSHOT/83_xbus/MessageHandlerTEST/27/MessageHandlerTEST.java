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
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MessageHandlerTest {

    private static final String BASENAME = "test";
    private static final String DIR = "testDir";
    private static final String MESSAGES_FILE_1 = "messages1.properties";
    private static final String MESSAGES_FILE_2 = "messages2.properties";
    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String PARAM_1 = "param1";
    private static final String PARAM_2 = "param2";
    private static final String MESSAGE_1 = "Message 1: $1$ and $2$";
    private static final String MESSAGE_2 = "Message 2: $1$";

    private MessageHandler messageHandler;
    private Properties properties1;
    private Properties properties2;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance(BASENAME);
        properties1 = new Properties();
        properties2 = new Properties();
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance(BASENAME);
        MessageHandler messageHandler2 = MessageHandler.getInstance(BASENAME);
        assertNotNull(messageHandler1);
        assertNotNull(messageHandler2);
        assertSame(messageHandler1, messageHandler2);
    }

    @Test
    public void testGetMessage() {
        properties1.setProperty(KEY_1, MESSAGE_1);
        messageHandler.addMessages(BASENAME, DIR);
        List params = Arrays.asList(PARAM_1, PARAM_2);
        String result = messageHandler.getMessage(KEY_1, params);
        assertEquals(MESSAGE_1, result);
    }

    @Test
    public void testGetMessageOptional() {
        messageHandler.addMessages(BASENAME, DIR);
        String result = messageHandler.getMessageOptional(KEY_1, null);
        assertNull(result);
        result = messageHandler.getMessageOptional(KEY_1, Collections.emptyList());
        assertNull(result);
        result = messageHandler.getMessageOptional(KEY_1, Arrays.asList(PARAM_1));
        assertNull(result);
        properties1.setProperty(KEY_1, MESSAGE_1);
        result = messageHandler.getMessageOptional(KEY_1, Arrays.asList(PARAM_1, PARAM_2));
        assertEquals(MESSAGE_1, result);
    }

    @Test
    public void testGetMessageWithError() {
        messageHandler.addMessages(BASENAME, DIR);
        String result = messageHandler.getMessage(KEY_2, null);
        assertNull(result);
    }

    @Test
    public void testAddMessages() {
        messageHandler = new MessageHandler(BASENAME);
        when(properties1.getProperty(KEY_1)).thenReturn(MESSAGE_1);
        when(properties2.getProperty(KEY_2)).thenReturn(MESSAGE_2);
        File dirFile = mock(File.class);
        when(dirFile.list(mock(FilenameFilter.class))).thenReturn(new String[] { MESSAGES_FILE_1, MESSAGES_FILE_2 });
        FileInputStream inputStream1 = mock(FileInputStream.class);
        when(inputStream1.readAllBytes()).thenReturn(("key1=" + MESSAGE_1 + "\nkey2=" + MESSAGE_2).getBytes());
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        when(new File(DIR).listFiles()).thenReturn(new File[] { new File(DIR + "/" + MESSAGES_FILE_1), new File(DIR + "/" + MESSAGES_FILE_2) });
        when(new File(DIR + "/" + MESSAGES_FILE_1).exists()).thenReturn(true);
        when(new File(DIR + "/" + MESSAGES_FILE_1).isFile()).thenReturn(true);
        when(new FileInputStream(fileCaptor.capture(File.class))).thenReturn(inputStream1);
        messageHandler.addMessages(BASENAME, DIR);
        Properties messages = messageHandler.getMessages();
        assertEquals(2, messages.size());
        assertEquals(MESSAGE_1, messages.getProperty(KEY_1));
        assertEquals(MESSAGE_2, messages.getProperty(KEY_2));
    }
}