package net.sf.xbus.base.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");

        assertSame(handler1, handler2);
    }

    @Test
    public void testGetInstanceDifferentBasename() {
        MessageHandler handler1 = MessageHandler.getInstance("test1");
        MessageHandler handler2 = MessageHandler.getInstance("test2");

        assertNotSame(handler1, handler2);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");

        Hashtable messages = handler.mMessages;
        assertNull(messages);

        handler.addMessages("test", "testDir");

        messages = handler.mMessages;
        assertNotNull(messages);
    }

    @Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        String key = "message1";
        List params = new ArrayList();
        params.add("param1");
        params.add("param2");

        String message = handler.getMessage(key, params);
        assertEquals("Key: message1 not found in message file", message);

        handler.addMessages("test", "testDir");

        // Hardcoded test data
        Properties messagesProperties = new Properties();
        messagesProperties.setProperty("message1", "${$1} ${$2}");

        File messagesFile = new File("testDir/test_ENGLISH.properties");
        try (FileInputStream inputStream = new FileInputStream(messagesFile)) {
            messagesProperties.load(inputStream);
        } catch (IOException e) {
            Trace.error("Cannot load message file");
            throw new RuntimeException(e);
        }

        handler.mMessages = messagesProperties;

        message = handler.getMessage(key, params);
        assertEquals("param1 param2", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");

        String key = "message1";
        List params = new ArrayList();
        params.add("param1");
        params.add("param2");

        String message = handler.getMessageOptional(key, params);
        assertNull(message);

        handler.addMessages("test", "testDir");

        // Hardcoded test data
        Properties messagesProperties = new Properties();
        messagesProperties.setProperty("message1", "${$1} ${$2}");

        File messagesFile = new File("testDir/test_ENGLISH.properties");
        try (FileInputStream inputStream = new FileInputStream(messagesFile)) {
            messagesProperties.load(inputStream);
        } catch (IOException e) {
            Trace.error("Cannot load message file");
            throw new RuntimeException(e);
        }

        handler.mMessages = messagesProperties;

        message = handler.getMessageOptional(key, params);
        assertEquals("param1 param2", message);

        message = handler.getMessageOptional("notExistingKey", params);
        assertNull(message);
    }

    @Test
    public void testMessagesFilter() {
        String prefix = "test";
        String postfix = "_ENGLISH.properties";

        MessagesFilter filter = new MessagesFilter(prefix, postfix);

        String[] files = new String[]{"test_ENGLISH.properties", "test_ES.properties", "test_FR.properties"};

        for (String fileName : files) {
            boolean result = filter.accept(new File(""), fileName);
            if (fileName.startsWith(prefix) && fileName.endsWith(postfix)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    @Test
    public void testGetInstanceWithEmptyDir() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "testDir");

        File testDir = new File("testDir");
        testDir.mkdir();

        assertNotNull(handler.mMessages);

        testDir.delete();

        handler = MessageHandler.getInstance("test");
        assertNull(handler.mMessages);
    }
}