package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import static org.junit.Assert.*;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;
    private static final String BASENAME = "testMessageHandler";

    @BeforeClass
    public static void setup() {
        messageHandler = MessageHandler.getInstance(BASENAME);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler2 = MessageHandler.getInstance(BASENAME);
        assertSame(messageHandler, messageHandler2);
    }

    @Test
    public void testGetMessageNullParams() {
        String result = messageHandler.getMessage("testKey", null);
        assertEquals("Key: testKey not found in message file", result);
    }

    @Test
    public void testGetMessageEmptyParams() {
        List params = new ArrayList();
        String result = messageHandler.getMessage("testKey", params);
        assertEquals("Key: testKey not found in message file", result);
    }

    @Test
    public void testGetMessageSimpleParams() {
        List params = new ArrayList();
        params.add("TestParam1");
        String result = messageHandler.getMessage("testKey.$1", params);
        assertEquals("TestParam1", result);
    }

    @Test
    public void testGetMessageMultipleParams() {
        List params = new ArrayList();
        params.add("TestParam1");
        params.add("TestParam2");
        String result = messageHandler.getMessage("testKey.$1.$2", params);
        assertEquals("TestParam1 TestParam2", result);
    }

    @Test
    public void testGetMessageOptionalNullParams() {
        String result = messageHandler.getMessageOptional("testKey", null);
        assertNull(result);
    }

    @Test
    public void testGetMessageOptionalEmptyParams() {
        String result = messageHandler.getMessageOptional("testKey", new ArrayList());
        assertNull(result);
    }

    @Test
    public void testGetMessageOptionalSimpleParams() {
        List params = new ArrayList();
        params.add("TestParam1");
        String result = messageHandler.getMessageOptional("testKey.$1", params);
        assertEquals("TestParam1", result);
    }

    @Test
    public void testGetMessageOptionalMultipleParams() {
        List params = new ArrayList();
        params.add("TestParam1");
        params.add("TestParam2");
        String result = messageHandler.getMessageOptional("testKey.$1.$2", params);
        assertEquals("TestParam1 TestParam2", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageNullParamsInOptional() {
        List params = new ArrayList();
        params.add(null);
        messageHandler.getMessageOptional("testKey.", params);
    }

    @Test
    public void testAddMessages() {
        messageHandler = new MessageHandler(BASENAME);
        Properties properties = new Properties();
        properties.setProperty("testKey", "TestMessage{0}");
        File file = new File("testMessageFile.properties");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageHandler.addMessages(BASENAME, ".");
        String result = messageHandler.getMessage("testKey.", null);
        assertEquals("TestMessage{}", result);
    }
}