package net.sf.xbus.base.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MessageHandlerTest {

    @Test
    public void testgetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");

        assertEquals(handler1, handler2);
    }

    @Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "test.message";
        List params = Arrays.asList("param1", "param2");

        String message = handler.getMessage(key, params);
        assertNull(message);

        handler.addMessages("test", "test.properties");
        handler.addMessages("test", "test2.properties");

        Properties testProperties = new Properties();
        testProperties.setProperty("test.message", "Test message with {0} and {1}");

        try (FileInputStream input = new FileInputStream("test.properties")) {
            testProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        handler.mMessages.putAll(testProperties);

        message = handler.getMessage(key, params);
        assertEquals("Test message with param1 and param2", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String key = "test.message";
        List params = Arrays.asList("param1", "param2");

        String message = handler.getMessageOptional(key, params);
        assertNull(message);

        handler.addMessages("test", "test.properties");
        handler.addMessages("test", "test2.properties");

        Properties testProperties = new Properties();
        testProperties.setProperty("test.message", "Test message with {0} and {1}");

        try (FileInputStream input = new FileInputStream("test.properties")) {
            testProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        handler.mMessages.putAll(testProperties);

        message = handler.getMessageOptional(key, params);
        assertEquals("Test message with <null> and <null>", message);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "src/test/resources";

        handler.addMessages(basename, dir);

        List<String> files = new ArrayList<>();
        files.add("test_en_US.properties");
        files.add("test2_en_US.properties");

        File dirFile = new File(dir);
        String[] messagesFiles = dirFile.list(new MessagesFilter(basename, "_%s.properties"));

        assertArrayEquals(files.toArray(), messagesFiles);

        for (String fileName : messagesFiles) {
            Properties newProps = new Properties();
            try (FileInputStream input = new FileInputStream(dir + File.separator + fileName)) {
                newProps.load(input);
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String key = null;
            Enumeration keys = newProps.keys();

            while (keys.hasMoreElements()) {
                key = keys.nextElement().toString();
                assertNotNull(handler.mMessages.get(key));
            }
        }
    }

    @Test
    public void testMessagesFilter() {
        String prefix = "test";
        String postfix = "_en_US.properties";

        MessagesFilter filter = new MessagesFilter(prefix, postfix);

        File dir = new File("src/test/resources");
        String[] files = dir.list(filter);

        assertArrayEquals(new String[] { "test_en_US.properties" }, files);
    }
}