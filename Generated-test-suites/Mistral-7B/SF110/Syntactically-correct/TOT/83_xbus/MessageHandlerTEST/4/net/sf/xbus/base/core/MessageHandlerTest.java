package net.sf.xbus.base.core.test;

import org.junit.*; 
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;
    private static final String BASE_NAME = "testMessageHandler";

    @BeforeClass
    public static void setup() {
        MessageHandler.mInstances = new Hashtable<>();
        messageHandler = new MessageHandler(BASE_NAME);
    }

    @Test
    public void testGetInstance() {
        MessageHandler messageHandler2 = MessageHandler.getInstance(BASE_NAME);
        assertEquals(messageHandler, messageHandler2);
    }

    @Test
    public void testGetMessage() {
        List params = new ArrayList<>();
        String message = messageHandler.getMessage("testKey", params);
        assertNotNull(message);
        message = messageHandler.getMessage("testKey2", params);
        assertNull(message);
        message = messageHandler.getMessage("testKey", null);
        assertNull(message);

        try {
            messageHandler = new MessageHandler("test");
            messageHandler.getMessage("testKey", params);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        messageHandler = new MessageHandler("test");
        message = messageHandler.getMessage("testKey", params);
        assertNotNull(message);
        message = messageHandler.getMessage("testKey2", params);
        assertNull(message);
    }

    @Test
    public void testGetMessageOptional() {
        List params = Arrays.asList("testParam1", "testParam2");
        String message = messageHandler.getMessageOptional("testKey", params);
        assertNull(message);

        messageHandler.addMessages(BASE_NAME, "testPath");
        message = messageHandler.getMessageOptional("testKey", params);
        assertNotNull(message);
        String expectedMessage = "Test param1 Test param2";
        message = XStringSupport.replaceAll(message, "$1$", "Actual param 1");
        message = XStringSupport.replaceAll(message, "$2$", "Actual param 2");
        assertEquals(expectedMessage, message);

        messageHandler.addMessages(BASE_NAME, "testPath2");
        message = messageHandler.getMessageOptional("testKey", params);
        assertNotNull(message);
        assertNotEquals(expectedMessage, message);
    }

    @Test
    public void testAddMessages() {
        List params = Arrays.asList("testParam1", "testParam2");
        messageHandler.addMessages(BASE_NAME, "testPath");
        String message = messageHandler.getMessageOptional("testKey", params);
        assertNotNull(message);
        message = messageHandler.getMessageOptional("testKey2", params);
        assertNull(message);

        messageHandler.addMessages(BASE_NAME, "testPath2");
        message = messageHandler.getMessageOptional("testKey", params);
        assertNotNull(message);
        assertNotEquals(message, messageHandler.getMessageOptional("testKey", params));
    }

    @Test
    public void testMessagesFilter() {
        File dir = new File("testPath");
        String[] messagesFiles = new MessagesFilter("test", "_ENGLISH.properties").list(dir);
        assertEquals(2, messagesFiles.length);
        assertEquals("test_ENGLISH.properties", messagesFiles[0]);
        assertEquals("test_ENGLISH2.properties", messagesFiles[1]);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadPropertiesFile() {
        File dir = new File("testPath");
        Properties newProps = new Properties();
        try {
            newProps.load(new FileInputStream(dir + "test_ENGLISH.properties"));
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }
    }

    @AfterClass
    public static void teardown() {
        MessageHandler.mInstances = null;
    }
}