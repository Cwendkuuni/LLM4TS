package net.sf.xbus.base.core.test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {
    private static final String testBasename = "testMessageHandler";
    private static final String testDir = "testData";
    private MessageHandler messageHandler;

    @Before
    public void setUp() {
        messageHandler = MessageHandler.getInstance(testBasename);
    }

    @Test
    public void testgetMessage() {
        List params = new ArrayList();
        params.add("expectedParam1");
        params.add("expectedParam2");
        String message = messageHandler.getMessage("testKey", params);
        assertEquals("Expected message text", message);
    }

    @Test
    public void testgetMessageOptional() {
        List params = new ArrayList();
        params.add("expectedParam1");
        String message = messageHandler.getMessageOptional("testKey", params);
        assertEquals("Expected message text", message);
    }

    @Test(expected = NullPointerException.class)
    public void testgetMessage_MessageNotFound() {
        List params = new ArrayList();
        MessageHandler handler = new MessageHandler("testBasename");
        String key = "testKeyNotFound";
        handler.getMessage(key, params);
    }

    @Test(expected = NullPointerException.class)
    public void testgetMessage_NullParameters() {
        List params = null;
        MessageHandler handler = new MessageHandler("testBasename");
        String key = "testKey";
        handler.getMessage(key, params);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_InvalidFileExtension() {
        MessageHandler handler = new MessageHandler("testBasename");
        handler.addMessages("testBasename", testDir, "invalidFile.txt");
    }

    @Test(expected = IOException.class)
    public void testAddMessages_MalformedPropertiesFile() {
        MessageHandler handler = new MessageHandler("testBasename");
        handler.addMessages("testBasename", testDir, "malformedFile.properties");
    }
}