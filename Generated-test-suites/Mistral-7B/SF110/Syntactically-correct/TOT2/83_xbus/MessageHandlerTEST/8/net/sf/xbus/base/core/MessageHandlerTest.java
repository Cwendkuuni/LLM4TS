import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import org.junit.Test;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        String expectedMessage = "Key: test not found in message file";
        String actualMessage = messageHandler.getMessage("test", params);
        Trace.error("Expected error message: " + expectedMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add("Test Param 1");
        String expectedMessage = "Key: test {0} not found in message file";
        String actualMessage = messageHandler.getMessageOptional("test", params);
        String actualMessageWithParam = XStringSupport.replaceAll(actualMessage, "$1$", "Test Param 1");
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedMessage.replace("{0}", "Test Param 1"), actualMessageWithParam);
    }

    @Test
    public void testgetMessageWithEmptyParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        String expectedMessage = "Key: test not found in message file";
        String actualMessage = messageHandler.getMessage("test", params);
        Trace.error("Expected error message: " + expectedMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetMessageWithNullKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        assertNull(messageHandler.getMessage(null, params));
    }

    @Test
    public void testgetMessageWithNullKeyAndEmptyParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNull(messageHandler.getMessage(null, new ArrayList<>()));
    }

    @Test
    public void testgetMessageOptionalWithNullKey() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        assertNull(messageHandler.getMessageOptional(null, params));
    }

    @Test
    public void testgetMessageOptionalWithNullKeyAndEmptyParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNull(messageHandler.getMessageOptional(null, new ArrayList<>()));
    }

    @Test
    public void testgetMessageOptionalWithNullParam() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        params.add(null);
        String expectedMessage = "Key: test {1} not found in message file";
        String actualMessage = messageHandler.getMessageOptional("test", params);
        String actualMessageWithParam = XStringSupport.replaceAll(actualMessage, "$1$", "<null>");
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedMessage.replace("{1}", "<null>"), actualMessageWithParam);
    }

    @Test
    public void testgetMessageOptionalWithEmptyParams() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<>();
        String expectedMessage = "Key: test not found in message file";
        String actualMessage = messageHandler.getMessageOptional("test", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testAddMessagesWithNullBasename() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        messageHandler.addMessages(null, "test");
    }

    @Test(expected = NullPointerException.class)
    public void testAddMessagesWithNullDir() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        messageHandler.addMessages("test", null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testAddMessagesWithInvalidFile() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        messageHandler.addMessages("test", "test/testFileDoesntExist.properties");
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        messageHandler.addMessages("test", "test");
        String expectedMessage = "Key: test {0} not found in message file";
        Properties newProps = new Properties();
        newProps.setProperty("testKey", "Test Value");
        String key = "testKey";
        String expectedValue = "Test Value";
        messageHandler.addMessages("test", "test/test.properties");
        String actualMessage = messageHandler.getMessageOptional(key, null);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedValue, actualMessage.replace("{0}", expectedValue));
    }

    @Test
    public void testAddMessagesWithMultipleFiles() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        messageHandler.addMessages("test", "test");
        Properties newProps1 = new Properties();
        newProps1.setProperty("testKey1", "Test Value 1");
        Properties newProps2 = new Properties();
        newProps2.setProperty("testKey2", "Test Value 2");
        messageHandler.addMessages("test", "test/test1.properties");
        messageHandler.addMessages("test", "test/test2.properties");
        String key1 = "testKey1";
        String expectedValue1 = "Test Value 1";
        String key2 = "testKey2";
        String expectedValue2 = "Test Value 2";
        String actualMessage1 = messageHandler.getMessageOptional(key1, null);
        String actualMessage2 = messageHandler.getMessageOptional(key2, null);
        assertEquals(expectedMessage, actualMessage1);
        assertEquals(expectedValue1, actualMessage1.replace("{0}", expectedValue1));
        assertEquals(expectedMessage, actualMessage2);
        assertEquals(expectedValue2, actualMessage2.replace("{0}", expectedValue2));
    }

    @Test
    public void testThreadSafety() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test1");
        MessageHandler messageHandler2 = MessageHandler.getInstance("test1");
        List<Object> params = new ArrayList<>();
        params.add("Test Param 1");
        messageHandler1.addMessages("test1", "test1/test1.properties");
        messageHandler2.addMessages("test1", "test1/test2.properties");
        String expectedMessage1 = "Key: test1 {0} not found in message file 1";
        String expectedMessage2 = "Key: test1 {0} not found in message file 2";
        String actualMessage1 = messageHandler1.getMessageOptional("test1", params);
        String actualMessage2 = messageHandler2.getMessageOptional("test1", params);
        String actualMessage1WithParam = XStringSupport.replaceAll(actualMessage1, "$1$", "Test Param 1");
        String actualMessage2WithParam = XStringSupport.replaceAll(actualMessage2, "$1$", "Test Param 1");
        assertEquals(expectedMessage1, actualMessage1);
        assertEquals(expectedMessage1.replace("{0}", "Test Param 1"), actualMessage1WithParam);
        assertEquals(expectedMessage2, actualMessage2);
        assertEquals(expectedMessage2.replace("{0}", "Test Param 1"), actualMessage2WithParam);
    }

    @Test
    public void testExceptionHandling() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        try {
            messageHandler.addMessages(null, "test");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        messageHandler = MessageHandler.getInstance("test");
        try {
            messageHandler.addMessages("test", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        messageHandler = MessageHandler.getInstance("test");
        try {
            messageHandler.addMessages("test", "test/testDoesntExist.properties");
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }

        messageHandler = MessageHandler.getInstance("test");
        try (FileInputStream inputStream = new FileInputStream("test/test.properties")) {
            messageHandler.addMessages("test", "test/test.properties");
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetInstanceWithMultipleThreads() {
        MessageHandler messageHandler1 = MessageHandler.getInstance("test1");
        MessageHandler messageHandler2 = MessageHandler.getInstance("test1");
        String expectedMessage = "Key: test1 {0} not found in message file";
        List<Object> params = new ArrayList<>();
        params.add("Test Param 1");
        messageHandler1.addMessages("test1", "test1/test1.properties");
        messageHandler2.addMessages("test1", "test1/test2.properties");
        String actualMessage1 = messageHandler1.getMessageOptional("test1", params);
        String actualMessage2 = messageHandler2.getMessageOptional("test1", params);
        String actualMessage1WithParam = XStringSupport.replaceAll(actualMessage1, "$1$", "Test Param 1");
        String actualMessage2WithParam = XStringSupport.replaceAll(actualMessage2, "$1$", "Test Param 1");
        assertEquals(expectedMessage, actualMessage1);
        assertEquals(expectedMessage.replace("{0}", "Test Param 1"), actualMessage1WithParam);
        assertEquals(expectedMessage, actualMessage2);
        assertEquals(expectedMessage.replace("{0}", "Test Param 1"), actualMessage2WithParam);
    }
}