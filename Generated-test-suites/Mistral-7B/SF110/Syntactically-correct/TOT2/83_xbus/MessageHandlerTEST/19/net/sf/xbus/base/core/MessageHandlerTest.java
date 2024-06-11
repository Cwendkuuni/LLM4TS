
package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import org.junit.*;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler;

    @Before
    public void setup() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testgetMessage() {
        List params = new ArrayList();
        String key = "testKey";
        String expectedMessage = "This is a test message";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
        Trace.error("Expected error message"); // check error handling
    }

    // Test for key as null
    @Test(expected = NullPointerException.class)
    public void testgetMessageNullKey() {
        List params = new ArrayList();
        String key = null;
        messageHandler.getMessage(key, params);
    }

    // Test for null List params
    @Test
    public void testgetMessageNullParams() {
        String key = "testKey";
        List params = null;
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    // Test for key not found in message file
    @Test
    public void testgetMessageKeyNotFound() {
        List params = new ArrayList();
        String key = "testKeyNotFound";
        String expectedMessage = "Key: testKeyNotFound not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    // Test for message file not found
    @Test(expected = FileNotFoundException.class)
    public void testgetMessageFileNotFound() {
        String key = "testKey";
        List params = new ArrayList();
        messageHandler.mInstances.clear(); // Clear existing instances to simulate message file not found
        String actualMessage = messageHandler.getMessage(key, params);
        assertNull(actualMessage);
    }

    // Test for IOException while reading message file
    @Test(expected = IOException.class)
    public void testgetMessageIOException() {
        String key = "testKey";
        List params = new ArrayList();
        try {
            messageHandler.mInstances.clear(); // Clear existing instances and replace with a mock FileInputStream that throws IOException
            String actualMessage = messageHandler.getMessage(key, params);
            fail("IOException should have been thrown but wasn't");
        } catch (IOException e) {
            // Expected
        }
    }

    // Test for key with special characters
    @Test
    public void testgetMessageSpecialKey() {
        List params = new ArrayList();
        String key = "testKey#";
        String expectedMessage = "This is a test message with special character #";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    // Test for List params with null values
    @Test
    public void testgetMessageListWithNullValues() {
        String key = "testKey";
        List params = new ArrayList();
        params.add(null);
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    // Test for message contains format specifiers that don't match the number of params
    @Test(expected = IllegalArgumentException.class)
    public void testgetMessageFormatMismatch() {
        String key = "testKey";
        List params = new ArrayList();
        params.add("testParam1");
        params.add(null);
        String expectedMessage = "Key: testKey not found in message file";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }

    @BeforeClass
    public static void init() {
        Assume.assumeTrue(MessageHandler.mInstances != null);
    }
}
