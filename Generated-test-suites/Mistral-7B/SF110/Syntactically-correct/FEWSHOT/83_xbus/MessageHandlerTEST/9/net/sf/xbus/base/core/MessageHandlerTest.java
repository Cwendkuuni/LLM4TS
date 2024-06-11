package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import static net.sf.xbus.base.core.strings.XStringSupport.replaceAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.config.Constants;
import org.junit.Test;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertTrue(handler1 == handler2);
    }

    @Test
    public void testGetMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        assertEquals(null, handler.getMessage("key1", null));

        handler.addMessages("test", "test.properties");
        Properties properties = new Properties();
        properties.setProperty("key1", "Value 1");
        handler.mMessages.putAll(properties);

        assertEquals("Value 1", handler.getMessage("key1", null));
        assertEquals("Value 1", handler.getMessageOptional("key1", null));
    }

    @Test
    public void testReplaceParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "test.properties");
        Properties properties = new Properties();
        properties.setProperty("param1", "Param 1");
        properties.setProperty("param2", "Param 2");
        handler.mMessages.putAll(properties);

        String message = "Parameter 1 is {0}, Parameter 2 is {1}";
        String expected = replaceAll(message, "$1", "Value 1") + ", Parameter 2 is " + "Value 2";
        assertEquals(expected, handler.getMessage("key", new ArrayList<Object>() {{
            add("Value 1");
            add("Value 2");
        }}));

        assertEquals(expected, handler.getMessageOptional("key", new ArrayList<Object>() {{
            add("Value 1");
            add("Value 2");
        }}));
    }

    @Test
    public void testGetMessagesFromMultipleFiles() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "test1.properties");
        handler.addMessages("test", "test2.properties");

        Properties test1Properties = new Properties();
        test1Properties.setProperty("key1", "Value 1");
        handler.mMessages.putAll(test1Properties);

        Properties test2Properties = new Properties();
        test2Properties.setProperty("key2", "Value 2");
        handler.mMessages.putAll(test2Properties);

        assertEquals("Value 1", handler.getMessage("key1", null));
        assertEquals("Value 2", handler.getMessage("key2", null));
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessagesWithNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        handler.addMessages("test", "test.properties");
        Properties properties = new Properties();
        properties.setProperty("key", "Value {0}");
        handler.mMessages.putAll(properties);

        handler.getMessage("key", null);
    }

    @Test
    public void testMessagesFilter() {
        File testDir = new File("test");
        testDir.mkdir();

        String baseName = "test";
        String dirPath = "test";

        File testFile1 = new File(testDir, baseName + "_en_US.properties");
        testFile1.createNewFile();
        Properties testProperties1 = new Properties();
        testProperties1.setProperty("key1", "Value 1");
        testProperties1.store(new FileOutputStream(testFile1), null);

        File testFile2 = new File(testDir, baseName + "_es_ES.properties");
        testFile2.createNewFile();
        Properties testProperties2 = new Properties();
        testProperties2.setProperty("key2", "Valor 2");
        testProperties2.store(new FileOutputStream(testFile2), null);

        MessageHandler handler = MessageHandler.getInstance(baseName);
        handler.addMessages(baseName, dirPath);

        assertEquals("Value 1", handler.getMessage("key1", null));
        assertEquals("Valor 2", handler.getMessage("key2", null));

        testDir.delete();
    }

    @Test
    public void testErrorMessageOnMissingMessageFile() {
        MessageHandler handler = null;
        try {
            handler = MessageHandler.getInstance("test");
            handler.addMessages("test", "non-existent-directory");
        } catch (Exception e) {
            Trace.error("Expected error: " + e.getMessage());
        }
        assertNull(handler);
    }
}