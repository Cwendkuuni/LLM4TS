package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");
        assertNotNull(handler1);
        assertSame(handler1, handler2);
    }

    @Test
    public void testGetMessages() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";
        String expectedMessage = "This is a test message";
        Properties properties = new Properties();
        properties.put(key, expectedMessage);

        when(Trace.error(String.class)).thenReturn(new FileOutputStream(mock(PrintStream.class)));

        handler.addMessages("test", Constants.XBUS_ETC);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        handler.addMessages("test", ".");

        when(new File(".").list(Mockito.isA(FilenameFilter.class))).thenReturn(new String[]{"test_en_US.properties"});

        FileInputStream inputStream = mock(FileInputStream.class);
        when(inputStream.readAllBytes()).thenReturn(("key=" + expectedMessage + "\n").getBytes());

        when(new FileInputStream("." + File.separator + "test_en_US.properties")).thenReturn(inputStream);

        String actualMessage = handler.getMessage(key, null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessagesWithParams() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";
        String param1 = "testParam1";
        String param2 = "testParam2";
        String expectedMessage = "This is a test message with params: $1$ and $2$";
        Properties properties = new Properties();
        properties.put(key, expectedMessage);

        when(Trace.error(String.class)).thenReturn(new FileOutputStream(mock(PrintStream.class)));

        handler.addMessages("test", Constants.XBUS_ETC);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        handler.addMessages("test", ".");

        when(new File(".").list(Mockito.isA(FilenameFilter.class))).thenReturn(new String[]{"test_en_US.properties"});

        FileInputStream inputStream = mock(FileInputStream.class);
        when(inputStream.readAllBytes()).thenReturn(("key=" + expectedMessage + "\n").getBytes());

        when(new FileInputStream("." + File.separator + "test_en_US.properties")).thenReturn(inputStream);

        List<Object> params = new LinkedList<>();
        params.add(param1);
        params.add(param2);

        String actualMessage = handler.getMessage(key, params);
        String replaceAllMessage = XStringSupport.replaceAll(expectedMessage, "$1$", param1);
        String replaceAllMessage2 = XStringSupport.replaceAll(replaceAllMessage, "$2$", param2);
        assertEquals(replaceAllMessage2, actualMessage);
    }

    @Test
    public void testGetMessagesNullParams() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";
        String expectedMessage = "This is a test message";
        Properties properties = new Properties();
        properties.put(key, expectedMessage);

        when(Trace.error(String.class)).thenReturn(new FileOutputStream(mock(PrintStream.class)));

        handler.addMessages("test", Constants.XBUS_ETC);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        handler.addMessages("test", ".");

        when(new File(".").list(Mockito.isA(FilenameFilter.class))).thenReturn(new String[]{"test_en_US.properties"});

        FileInputStream inputStream = mock(FileInputStream.class);
        when(inputStream.readAllBytes()).thenReturn(("key=" + expectedMessage + "\n").getBytes());

        when(new FileInputStream("." + File.separator + "test_en_US.properties")).thenReturn(inputStream);

        String actualMessage = handler.getMessage(key, null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessagesOptional() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";
        String expectedMessage = null;
        Properties properties = new Properties();

        handler.addMessages("test", Constants.XBUS_ETC);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        handler.addMessages("test", ".");

        String actualMessage = handler.getMessageOptional(key, null);
        assertNull(actualMessage);
    }

    @Test
    public void testGetMessagesOptionalWithParams() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";
        String expectedMessage = "This is a test message with params: $1$ and $2$";
        Properties properties = new Properties();
        properties.put(key, expectedMessage);

        handler.addMessages("test", Constants.XBUS_ETC);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        handler.addMessages("test", ".");

        List<Object> params = new LinkedList<>();
        params.add("testParam1");
        params.add("testParam2");

        String actualMessage = handler.getMessageOptional(key, params);
        String replaceAllMessage = XStringSupport.replaceAll(expectedMessage, "$1$", "testParam1");
        String replaceAllMessage2 = XStringSupport.replaceAll(replaceAllMessage, "$2$", "testParam2");
        assertEquals(replaceAllMessage2, actualMessage);
    }

    @Test
    public void testGetMessagesEmptyParams() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";
        String expectedMessage = "This is a test message";
        Properties properties = new Properties();
        properties.put(key, expectedMessage);

        handler.addMessages("test", Constants.XBUS_ETC);
        handler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
        handler.addMessages("test", ".");

        List<Object> params = new LinkedList<>();

        String actualMessage = handler.getMessageOptional(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessagesNullKey() {
        MessageHandler handler = new MessageHandler("test");
        List<Object> params = new LinkedList<>();

        handler.getMessage(null, params);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessagesNullParams() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";

        handler.getMessage(key, null);
    }

    @Test
    public void testGetMessagesEmptyHashTable() {
        MessageHandler handler = new MessageHandler("test");
        String key = "testKey";

        String actualMessage = handler.getMessage(key, null);
        assertNull(actualMessage);
    }
}