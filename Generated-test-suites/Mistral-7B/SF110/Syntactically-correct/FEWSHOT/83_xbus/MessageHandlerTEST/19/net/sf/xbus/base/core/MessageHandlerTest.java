package net.sf.xbus.base.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

    private MessageHandler messageHandler;
    private List params;

    @org.junit.Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance("test");
        params = new LinkedList<>();
    }

    @org.junit.Test
    public void testGetMessage() {
        assertEquals("Key: not found in message file not found", messageHandler.getMessage("notFoundKey", params));

        Properties properties = new Properties();
        properties.put("notFoundKey", "Key: not found in message file {0}");

        messageHandler = new MessageHandler("test") {
            @Override
            private void addMessages(String basename, String dir) {
                Hashtable messages = new Hashtable();
                messages.put("notFoundKey", properties);
                this.mMessages = messages;
            }
        };

        params.add("test");
        assertEquals("Key: not found in message file test", messageHandler.getMessage("notFoundKey", params));

        messageHandler = new MessageHandler("test") {
            @Override
            private void addMessages(String basename, String dir) {
                Properties properties = new Properties();
                properties.put("foundKey", "Key: found {0}");
                this.mMessages = new Hashtable() {
                    {
                        put("foundKey", properties);
                    }
                };
            }
        };

        params.add("value");
        assertEquals("Key: found value", messageHandler.getMessage("foundKey", params));
    }

    @org.junit.Test
    public void testGetMessageOptional() {
        assertNull(messageHandler.getMessageOptional("notFoundKey", params));

        Properties properties = new Properties();
        properties.put("notFoundKey", null);

        messageHandler = new MessageHandler("test") {
            @Override
            private void addMessages(String basename, String dir) {
                Hashtable messages = new Hashtable();
                messages.put("notFoundKey", properties);
                this.mMessages = messages;
            }
        };

        assertNull(messageHandler.getMessageOptional("notFoundKey", params));

        properties.put("foundKey", "Key: {0}");

        messageHandler = new MessageHandler("test") {
            @Override
            private void addMessages(String basename, String dir) {
                Hashtable messages = new Hashtable();
                messages.put("foundKey", properties);
                this.mMessages = messages;
            }
        };

        params.add("value");
        assertEquals("Key: value", messageHandler.getMessageOptional("foundKey", params));
    }

    @org.junit.Test
    public void testGetInstance() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test");
        MessageHandler messageHandler2 = MessageHandler.getInstance("test");

        assertEquals(messageHandler1, messageHandler2);
    }

    @org.junit.Test
    public void testAddMessages() throws IOException {
        File testDir = new File("test");
        testDir.mkdir();

        File propertiesFile1 = new File(testDir, "test_en_US_messages.properties");
        Properties properties1 = new Properties();
        properties1.put("key1", "Message: {0}");
        try (FileOutputStream outputStream1 = new FileOutputStream(propertiesFile1)) {
            properties1.store(outputStream1, null);
        }

        File propertiesFile2 = new File(testDir, "test_en_US_messages_2.properties");
        Properties properties2 = new Properties();
        properties2.put("key2", "Message: {0} {1}");
        try (FileOutputStream outputStream2 = new FileOutputStream(propertiesFile2)) {
            properties2.store(outputStream2, null);
        }

        messageHandler = new MessageHandler("test") {
            @Override
            private void addMessages(String basename, String dir) {
                this.mMessages = new Hashtable();
                addMessagesHelper(basename, dir);
            }

            private void addMessagesHelper(String basename, String dir) {
                Locale locale = Locale.ENGLISH;
                String postfix = "_" + locale.toString() + ".properties";
                File dirFile = new File(dir);
                String[] messagesFiles = dirFile.list(new MessagesFilter(basename, postfix));
                for (String messagesFile : messagesFiles) {
                    Properties newProps = new Properties();
                    FileInputStream instream = new FileInputStream(new File(dir, messagesFile));
                    newProps.load(instream);
                    instream.close();
                    String key = null;
                    Enumeration<Object> keys = newProps.keys();
                    while (keys.hasMoreElements()) {
                        key = keys.nextElement().toString();
                        this.mMessages.put(key, ((Hashtable<K, Object>) newProps).get(key));
                    }
                }
            }
        };

        assertEquals("Message: test", messageHandler.getMessage("key1", params));
        assertEquals("Message: test value", messageHandler.getMessage("key2", params));

        testDir.delete();
    }

    @org.junit.Test
    public void testGetInstanceWithException() {
        when(Trace.error((String) anyString())).thenReturn(mock(Trace.class));

        try {
            MessageHandler.getInstance("test_with_exception");
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testMessagesFilterAccept() {
        MessagesFilter filter = new MessagesFilter("test", "_en_US.properties");
        assertTrue(filter.accept(new File("test"), "test_en_US_messages.properties"));
        assertFalse(filter.accept(new File("test"), "test_de_DE_messages.properties"));
    }

    private static boolean isMockitoAvailable() {
        try {
            Class.forName("org.mockito.Mockito");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean isJUnit4Available() {
        Class<?> clazz;
        try {
            clazz = Class.forName("org.junit.Test");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    private static boolean isJUnit5Available() {
        Class<?> clazz;
        try {
            clazz = Class.forName("org.junit.jupiter.api.Test");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    @org.junit.Test
    public void testIsAvailable() {
        assertTrue(isJUnit4Available());
        assertTrue(isMockitoAvailable());
        assertFalse(isJUnit5Available());
    }
}

class MessagesFilter implements FilenameFilter {
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