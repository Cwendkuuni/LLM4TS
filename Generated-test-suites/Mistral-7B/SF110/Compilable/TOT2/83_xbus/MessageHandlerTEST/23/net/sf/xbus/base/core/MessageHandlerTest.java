package net.sf.xbus.base.core;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.xbus.base.core.MessageHandler;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        List params = new ArrayList();
        String expectedMessage = "Test Key: testKey";
        String actualMessage = messageHandler.getMessage("testKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_KeyNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        List params = new ArrayList();
        String expectedMessage = "Key: null not found in message file";
        String actualMessage = messageHandler.getMessage(null, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_ParamsNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage("testKey", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage_ParamsWithNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        List params = new ArrayList();
        params.add(null);
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage("testKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        List params = new ArrayList();
        params.add("Test Param 1");
        String expectedMessage = "Test Key: {0} Test Param 1";
        String actualMessage = messageHandler.getMessageOptional("testKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_KeyNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        List params = new ArrayList();
        String expectedMessage = null;
        String actualMessage = messageHandler.getMessageOptional(null, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptional_ParamsNull() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessageOptional("testKey", null);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageOptional_InvalidNumberOfParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("testMessageHandler");
        List params = new ArrayList();
        params.add("Test Param 1");
        params.add("Test Param 2");
        params.add("Test Param 3");
        String expectedMessage = "Test Key: {0} Test Param 1 {1} Test Param 2 {2}";
        String actualMessage = messageHandler.getMessageOptional("testKey", params);
        assertNotEquals(expectedMessage, actualMessage);
    }
}