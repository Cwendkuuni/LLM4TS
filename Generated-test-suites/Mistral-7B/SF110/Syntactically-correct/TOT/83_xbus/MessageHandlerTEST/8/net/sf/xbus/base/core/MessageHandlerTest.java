package net.sf.xbus.base.core;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MessageHandlerTest {
    @Test
    public void testGetMessage() {
        String key = "validKey";
        List<Object> params = new ArrayList<>();

        MessageHandler messageHandler = MessageHandler.getInstance("testBasename");

        String expectedMessage = "This is a valid message";
        String message = messageHandler.getMessage(key, params);

        Trace.reset(); // Reset the trace to avoid cluttering the test output
        String actualMessage = messageHandler.getMessage(key, params);

        assertEquals(expectedMessage, message);
        assertEquals(expectedMessage, actualMessage);
        assertTrue(Trace.isEmpty()); // Check that no error messages were printed
    }

    @Test
    public void testGetMessageOptional() {
        String key = "validKey";
        List<Object> params = Arrays.asList("param1", "param2");

        MessageHandler messageHandler = MessageHandler.getInstance("testBasename");

        String expectedMessage = "This is a valid message with parameters: $1$ and $2$";
        String message = messageHandler.getMessageOptional(key, params);

        String actualMessage = XStringSupport.replaceAll(expectedMessage, "$1$", "value1");
        actualMessage = XStringSupport.replaceAll(actualMessage, "$2$", "value2");

        assertEquals(expectedMessage, message);
        assertEquals(actualMessage, actualMessage);
    }

    @Test
    public void testGetInstance() {
        String basename1 = "testBasename1";
        String basename2 = "testBasename2";

        MessageHandler messageHandler1 = MessageHandler.getInstance(basename1);
        MessageHandler messageHandler2 = MessageHandler.getInstance(basename2);

        assertNotSame(messageHandler1, messageHandler2);

        messageHandler1 = MessageHandler.getInstance(basename1);
        messageHandler2 = MessageHandler.getInstance(basename1);

        assertEquals(messageHandler1, messageHandler2);
    }
}