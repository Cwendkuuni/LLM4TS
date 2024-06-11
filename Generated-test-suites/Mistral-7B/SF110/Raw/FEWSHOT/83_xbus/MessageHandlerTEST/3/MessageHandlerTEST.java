package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import static net.sf.xbus.base.core.strings.XStringSupport.*;
import org.junit.Test;
import net.sf.xbus.base.core.*;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertSame(handler1, handler2);
    }

    @Test
    public void testGetMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        assertNull(handler.getMessage("key1", null));

        handler.addMessages("test", "test.properties");
        Properties props = new Properties();
        props.setProperty("key1", "Value1");
        handler.mMessages.putAll(props);

        assertEquals("Value1", handler.getMessage("key1", null));
        assertEquals("Key: key1 not found in message file", handler.getMessage("key2", null));
    }

    @Test
    public void testGetMessagesWithParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "test.properties");
        Properties props = new Properties();
        props.setProperty("key1.param1", "Param1");
        props.setProperty("key1.param2", "Param2");
        handler.mMessages.putAll(props);

        String message = handler.getMessage("key1.$1.$2", Arrays.asList("Param1", "Param2"));
        assertEquals("Param1 Param2", message);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");

        File testDir = new File("src/test/resources/messages");
        String[] messagesFiles = testDir.list(new MessagesFilter("test", "_EN.properties"));

        for (String messagesFile : messagesFiles) {
            Properties newProps = new Properties();
            try (FileInputStream inputStream = new FileInputStream(testDir + File.separator + messagesFile)) {
                newProps.load(inputStream);
            } catch (IOException e) {
                fail("Failed to load messages file: " + messagesFile);
            }

            handler.addMessages("test", "src/test/resources/messages");
            String key = null;
            Enumeration<Object> keys = newProps.keys();
            while (keys.hasMoreElements()) {
                key = keys.nextElement().toString();
                String message = handler.getMessage(key, null);
                assertEquals(newProps.getProperty(key), message);
            }
        }
    }

    @Test
    public void testMultipleMessageFiles() {
        MessageHandler handler = MessageHandler.getInstance("test");

        File testDir = new File("src/test/resources/messages");
        String[] messagesFiles = testDir.list(new MessagesFilter("test", "_EN.properties"));

        for (String messagesFile1 : messagesFiles) {
            Properties newProps1 = new Properties();
            try (FileInputStream inputStream1 = new FileInputStream(testDir + File.separator + messagesFile1)) {
                newProps1.load(inputStream1);
            } catch (IOException e) {
                fail("Failed to load messages file: " + messagesFile1);
            }

            String key1 = null;
            Enumeration<Object> keys1 = newProps1.keys();
            while (keys1.hasMoreElements()) {
                key1 = keys1.nextElement().toString();
            }

            handler.addMessages("test", "src/test/resources/messages");

            messagesFiles = testDir.list(new MessagesFilter("test", "_EN.properties"));
            String messagesFile2 = messagesFiles[0];
            Properties newProps2 = new Properties();
            try (FileInputStream inputStream2 = new FileInputStream(testDir + File.separator + messagesFile2)) {
                newProps2.load(inputStream2);
            } catch (IOException e) {
                fail("Failed to load messages file: " + messagesFile2);
            }

            String key2 = null;
            Enumeration<Object> keys2 = newProps2.keys();
            while (keys2.hasMoreElements()) {
                key2 = keys2.nextElement().toString();
                if (!key1.equals(key2)) {
                    String message1 = handler.getMessage(key1, null);
                    String message2 = handler.getMessage(key2, null);
                    assertNotEquals(message1, message2);
                }
            }
        }
    }

    @Test
    public void testGetMessagesWithNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "test.properties");
        Properties props = new Properties();
        props.setProperty("key1.param1", "Param1");
        props.setProperty("key1.param2", "Param2");
        handler.mMessages.putAll(props);

        String message = handler.getMessage("key1.$1.$2", null);
        assertNull(message);
    }

    @Test
    public void testGetMessagesWithEmptyParamsList() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "test.properties");
        Properties props = new Properties();
        props.setProperty("key1.param1", "Param1");
        props.setProperty("key1.param2", "Param2");
        handler.mMessages.putAll(props);

        String message = handler.getMessage("key1.$1.$2", Collections.emptyList());
        assertNull(message);
    }

    private static class MessagesFilter implements FilenameFilter {
        private String mPrefix;
        private String mPostfix;

        public MessagesFilter(final String prefix, final String postfix) {
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }

        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
}