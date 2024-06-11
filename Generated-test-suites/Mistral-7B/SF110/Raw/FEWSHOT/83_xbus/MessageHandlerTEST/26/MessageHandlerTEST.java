package net.sf.xbus.base.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

    private MessageHandler handler;
    private Hashtable messages;

    @Before
    public void setUp() {
        handler = MessageHandler.getInstance("test");
        messages = handler.mMessages;
    }

    @Test
    public void testGetMessageWithNullParams() {
        String message = handler.getMessage("test.key", null);
        assertEquals("Key: test.key not found in message file", message);
    }

    @Test
    public void testGetMessageWithEmptyParams() {
        String message = handler.getMessage("test.key", new ArrayList<>());
        assertEquals("Key: test.key not found in message file", message);
    }

    @Test
    public void testGetMessageWithParams() {
        List params = new ArrayList();
        params.add("testParam1");
        params.add("testParam2");
        String message = handler.getMessage("test.key", params);
        String expectedMessage = "Key: test.key $1$ $2$";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testGetMessageOptionalWithNullParams() {
        String message = handler.getMessageOptional("test.key", null);
        assertNull(message);
    }

    @Test
    public void testGetMessageOptionalWithEmptyParams() {
        String message = handler.getMessageOptional("test.key", new ArrayList<>());
        assertNull(message);
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        List params = new ArrayList();
        params.add("testParam1");
        params.add("testParam2");
        String message = handler.getMessageOptional("test.key", params);
        String expectedMessage = "test.key";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testGetInstanceWithNullBasename() {
        assertNull(MessageHandler.getInstance(null));
    }

    @Test
    public void testGetInstanceWithEmptyBasename() {
        assertNull(MessageHandler.getInstance(""));
    }

    @Test
    public void testGetInstanceWithBasename() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertEquals(handler1, handler2);
    }

    @Test
    public void testAddMessagesWithNullBasename() {
        MessageHandler handler = new MessageHandler("test");
        assertNull(handler.mMessages);
        handler.addMessages("test", null);
        assertNull(handler.mMessages);
    }

    @Test
    public void testAddMessagesWithNullDir() {
        MessageHandler handler = new MessageHandler("test");
        assertNull(handler.mMessages);
        File dir = mock(File.class);
        when(dir.list(mock(FilenameFilter.class))).thenReturn(new String[]{"message.properties"});
        handler.addMessages("test", dir);
        assertNull(handler.mMessages);
    }

    @Test
    public void testAddMessagesWithValidFile() throws IOException {
        MessageHandler handler = new MessageHandler("test");
        File dir = new File("test_dir");
        String[] messagesFiles = {"message1.properties", "message2.properties"};
        when(dir.list(mock(FilenameFilter.class))).thenReturn(messagesFiles);
        for (String messagesFile : messagesFiles) {
            Properties newProps = new Properties();
            when(new File(dir, messagesFile).exists()).thenReturn(true);
            when(new File(dir, messagesFile).isFile()).thenReturn(true);
            when(new FileInputStream(new File(dir, messagesFile))).thenReturn(mock(FileInputStream.class));
            when(new FileInputStream(new File(dir, messagesFile)).readAllBytes()).thenReturn("key1=Value1\nkey2=Value2".getBytes());
            handler.addMessages("test", dir);
            String key = null;
            final Enumeration keys = newProps.keys();
            while (keys.hasMoreElements()) {
                key = keys.nextElement().toString();
                assertEquals("Value1", messages.get(key));
                assertEquals("Value2", messages.get("key2"));
            }
        }
    }
}