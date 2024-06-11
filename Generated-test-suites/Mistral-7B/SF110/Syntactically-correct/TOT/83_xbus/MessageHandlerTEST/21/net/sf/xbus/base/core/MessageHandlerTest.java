package net.sf.xbus.base.core;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.strings.XStringSupport;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {
    private MessageHandler messageHandler;

    @Test
    public void testGetInstanceWithValidBasename() {
        MessageHandler instance1 = MessageHandler.getInstance("test");
        MessageHandler instance2 = MessageHandler.getInstance("test");

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetMessageWithValidKeyAndParams() {
        List params = new ArrayList<>();
        params.add("TestParam1");
        params.add(1);

        String expectedMessage = "Key: test$1$ not found in message file TestParam1: 1";
        String actualMessage = messageHandler.getMessage("test$1$", params);

        String actualReplacedMessage = XStringSupport.replaceAll(actualMessage, "test$1$", expectedMessage);

        assertEquals(expectedMessage, actualReplacedMessage);
    }

    @Test
    public void testGetMessageOptionalWithValidKey() {
        String key = "testKey";
        String expectedMessage = "This is a test message";
        String actualMessage = messageHandler.getMessageOptional(key);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithNullParams() {
        String key = "testKey";
        String expectedMessage = "This is a test message";
        String actualMessage = messageHandler.getMessageOptional(key);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndParams() {
        List params = new ArrayList<>();
        params.add("TestParam1");
        params.add(1);

        String key = "test$1$";
        String expectedMessage = "TestParam1: 1";
        String actualMessage = messageHandler.getMessageOptional(key, params);

        String actualReplacedMessage = XStringSupport.replaceAll(actualMessage, key, expectedMessage);

        assertEquals(expectedMessage, actualReplacedMessage);
    }

    @Test
    public void testAddMessages() {
        messageHandler = new MessageHandler("test");
        String key1 = "testKey1";
        String value1 = "This is a test message 1";
        messageHandler.mMessages.put(key1, value1);

        String key2 = "testKey2";
        String value2 = "This is a test message 2";
        messageHandler.addMessages("test", "testDir");
        messageHandler.mMessages.put(key2, value2);

        String expectedMessage1 = messageHandler.getMessageOptional(key1);
        String expectedMessage2 = messageHandler.getMessageOptional(key2);

        assertEquals(value1, expectedMessage1);
        assertEquals(value2, expectedMessage2);
    }

    @Before
    public void setUp() {
        messageHandler = new MessageHandler("test");
    }
}