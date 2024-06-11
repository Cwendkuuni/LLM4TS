package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;
    private static final String BASE_NAME = "TestMessageHandler";

    @BeforeClass
    public static void setup() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);
    }

    @Test
    public void testGetInstance() {
        MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetMessage_KeyFound() {
        String message = messageHandler.getMessage("test.message.key", null);
        assertNotNull(message);
        assertEquals("Expected message for key 'test.message.key'", message);
    }

    @Test
    public void testGetMessage_KeyNotFound() {
        String message = messageHandler.getMessage("nonexistent.key", null);
        assertNotNull(message);
        assertTrue(message.startsWith("Key: nonexistent.key not found in message file"));
    }

    @Test
    public void testGetMessageOptional_NoParams() {
        String message = messageHandler.getMessageOptional("test.message.key.no_params", null);
        assertNotNull(message);
        assertEquals("Expected message for key 'test.message.key.no_params' without params", message);
    }

    @Test
    public void testGetMessageOptional_WithParams() {
        List<Object> params = new ArrayList<>();
        params.add("Test");
        params.add(123);
        String message = messageHandler.getMessageOptional("test.message.key.params", params);
        assertNotNull(message);

        String expectedMessage = "Test is a test value, and the number is 123.";
        String actualMessage = XStringSupport.replaceAll(message, "$1$", "Test");
        String actualMessageWithoutReplace = XStringSupport.replaceAll(message, "$2$", "123");
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedMessage, actualMessageWithoutReplace);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageOptional_NullParams() {
        List<Object> params = null;
        messageHandler.getMessageOptional("test.message.key.params", params);
    }
}