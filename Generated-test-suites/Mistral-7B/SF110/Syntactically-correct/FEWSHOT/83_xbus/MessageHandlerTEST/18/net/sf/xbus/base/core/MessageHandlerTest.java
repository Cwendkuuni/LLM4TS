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
import java.util.ArrayList;
import java.util.Hashtable;
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
        assertNotNull(handler2);
        assertSame(handler1, handler2);
    }

    @Test
    public void testGetMessages_emptyMessages() {
        MessageHandler handler = new MessageHandler("test");
        assertNull(handler.mMessages);
    }

    @Test
    public void testGetMessages_simplePropertiesFile() {
        MessageHandler handler = new MessageHandler("test");
        Properties properties = new Properties();
        properties.setProperty("Key1", "Message1");
        properties.setProperty("Key2", "Message2 {0}");

        File testFile = mock(File.class);
        when(testFile.getName()).thenReturn("test_en_US.properties");
        when(testFile.exists()).thenReturn(true);
        when(testFile.isDirectory()).thenReturn(false);
        when(testFile.length()).thenReturn(1024);

        FileInputStream inputStream = mock(FileInputStream.class);
        when(inputStream.available()).thenReturn(1024);
        when(inputStream.readAllBytes()).thenReturn(new byte[1024]);

        when(new File(Constants.XBUS_ETC, "test_en_US.properties").exists()).thenReturn(false);
        when(new File(Constants.XBUS_PLUGIN_ETC, "test_en_US.properties").exists()).thenReturn(false);
        when(new File(Constants.XBUS_ETC, "test_en_US_messages.properties").exists()).thenReturn(true);
        when(new FileInputStream(new File(Constants.XBUS_ETC, "test_en_US_messages.properties"))).thenReturn(inputStream);

        handler.addMessages("test", Constants.XBUS_ETC);
        assertEquals("Message1", handler.getMessage("Key1", null));
        assertEquals("Message2 {0}", handler.getMessage("Key2", null));
        assertNull(handler.getMessage("Key3", null));
    }

    @Test
    public void testGetMessages_multiplePropertiesFiles() {
        MessageHandler handler = new MessageHandler("test");
        Properties properties1 = new Properties();
        properties1.setProperty("Key1_1", "Message1_1");
        properties1.setProperty("Key1_2", "Message1_2 {0}");

        Properties properties2 = new Properties();
        properties2.setProperty("Key2_1", "Message2_1");
        properties2.setProperty("Key2_2", "Message2_2 {0}");

        File testFile1 = mock(File.class);
        when(testFile1.getName()).thenReturn("test_en_US_1.properties");
        when(testFile1.exists()).thenReturn(true);
        when(testFile1.isDirectory()).thenReturn(false);
        when(testFile1.length()).thenReturn(2048);

        File testFile2 = mock(File.class);
        when(testFile2.getName()).thenReturn("test_en_US_2.properties");
        when(testFile2.exists()).thenReturn(true);
        when(testFile2.isDirectory()).thenReturn(false);
        when(testFile2.length()).thenReturn(3072);

        FileInputStream inputStream1 = mock(FileInputStream.class);
        when(inputStream1.available()).thenReturn(2048);
        when(inputStream1.readAllBytes()).thenReturn(new byte[2048]);

        FileInputStream inputStream2 = mock(FileInputStream.class);
        when(inputStream2.available()).thenReturn(3072);
        when(inputStream2.readAllBytes()).thenReturn(new byte[3072]);

        when(new File(Constants.XBUS_ETC, "test_en_US_1.properties").exists()).thenReturn(true);
        when(new File(Constants.XBUS_ETC, "test_en_US_1.properties")).thenReturn(testFile1);
        when(new File(Constants.XBUS_ETC, "test_en_US_1.properties").isDirectory()).thenReturn(false);
        when(new FileInputStream(new File(Constants.XBUS_ETC, "test_en_US_1.properties"))).thenReturn(inputStream1);

        when(new File(Constants.XBUS_ETC, "test_en_US_2.properties").exists()).thenReturn(true);
        when(new File(Constants.XBUS_ETC, "test_en_US_2.properties")).thenReturn(testFile2);
        when(new File(Constants.XBUS_ETC, "test_en_US_2.properties").isDirectory()).thenReturn(false);
        when(new FileInputStream(new File(Constants.XBUS_ETC, "test_en_US_2.properties"))).thenReturn(inputStream2);

        handler.addMessages("test", Constants.XBUS_ETC);
        assertEquals("Message1_1", handler.getMessage("Key1_1", null));
        assertEquals("Message1_2 {0}", handler.getMessage("Key1_2", null));
        assertEquals("Message2_1", handler.getMessage("Key2_1", null));
        assertEquals("Message2_2 {0}", handler.getMessage("Key2_2", null));
        assertNull(handler.getMessage("Key3", null));
    }

    @Test
    public void testGetMessages_nullParams() {
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", Constants.XBUS_ETC);
        assertEquals("Message1", handler.getMessage("Key1", null));
        assertEquals("Message2 {0}", handler.getMessage("Key2", null));
    }

    @Test
    public void testGetMessages_paramsReplacement() {
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", Constants.XBUS_ETC);
        assertEquals("Key1: Message1, Key2: Message2 Parameter1", handler.getMessage("Key3", new ArrayList<Object>() {{
            add("Message1");
            add("Message2");
            add("Parameter1");
        }}));
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetMessages_fileNotFoundException() {
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", Constants.XBUS_ETC);
        handler.getMessage("Key1", null);
    }

    @Test(expected = IOException.class)
    public void testGetMessages_ioException() {
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", Constants.XBUS_ETC);
        handler.getMessage("Key1", null);
    }

    @Test
    public void testTraceErrorMessage() {
        MessageHandler handler = new MessageHandler("test");
        handler.addMessages("test", Constants.XBUS_ETC);
        Trace trace = mock(Trace.class);
        when(trace.error(String.class)).thenReturn(null);

        handler.setTrace(trace);
        String message = "Key: Key1 not found in message file";
        handler.getMessage("Key1", null);
        verify(trace).error(message);
    }

    @Test
    public void testXStringSupportReplaceAll() {
        String message = "{0} {1} {2}";
        List<String> params = new ArrayList<String>() {{
            add("Param1");
            add("Param2");
            add("Param3");
        }};
        String result = XStringSupport.replaceAll(message, "{0}", params.get(0));
        assertEquals("Param1 Param2 Param3", result);
    }
}