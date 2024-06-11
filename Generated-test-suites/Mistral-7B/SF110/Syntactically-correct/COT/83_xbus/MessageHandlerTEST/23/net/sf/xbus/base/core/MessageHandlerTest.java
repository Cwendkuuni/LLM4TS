import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import java.util.*;

public class MessageHandlerTest {

 private static final String BASENAME = "MessageHandlerTest";
 private static final String DIR = "src/test/resources/"; // Assuming the test resources are in this location

 @Test
 public void testgetMessage() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 List params = new ArrayList();
 String key = "TEST_KEY";
 String expectedMessage = "This is a test message with a placeholder {$0}";
 String actualMessage = messageHandler.getMessage(key, params);
 params.add("Test Parameter");
 String expectedMessageWithParam = "This is a test message with a placeholder Test Parameter";
 String actualMessageWithParam = messageHandler.getMessage(key, params);

 // Test with null params
 params = null;
 String actualMessageWithNullParams = messageHandler.getMessage(key, params);

 // Test with missing key
 key = "MissingKey";
 String actualMessageWithMissingKey = messageHandler.getMessage(key, params);

 Assert.assertEquals(expectedMessage, actualMessage);
 Assert.assertEquals(expectedMessageWithParam, actualMessageWithParam);
 Assert.assertEquals("Key: MISSING_KEY not found in message file", actualMessageWithMissingKey);
 }

 @Test(expected = NullPointerException.class)
 public void testgetMessageWithNullKey() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 List params = new ArrayList();
 String key = null;
 messageHandler.getMessage(key, params);
 }

 @Test(expected = NullPointerException.class)
 public void testgetMessageWithNullParams() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "TEST_KEY";
 messageHandler.getMessage(key, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetMessageWithEmptyParams() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "TEST_KEY";
 messageHandler.getMessage(key, new ArrayList());
 }

 @Test
 public void testgetMessageOptional() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 List params = new ArrayList();
 String key = "TEST_KEY_OPTIONAL";
 String expectedMessage = "This is a test optional message";
 String actualMessage = messageHandler.getMessageOptional(key, params);

 // Test with null params
 params = null;
 String actualMessageWithNullParams = messageHandler.getMessageOptional(key, params);

 // Test with missing key
 key = "MissingKeyOptional";
 String actualMessageWithMissingKey = messageHandler.getMessageOptional(key, params);

 Assert.assertEquals(expectedMessage, actualMessage);
 Assert.assertNull(actualMessageWithNullParams);
 Assert.assertNull(actualMessageWithMissingKey);
 }

 @Test
 public void testAddMessages() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR);
 String key = "TEST_KEY_ADD_MESSAGES";
 String expectedMessage = "This is a test message added through addMessages();";
 String actualMessage = messageHandler.getMessage(key, null);

 // Test with missing file
 String missingFile = "MissingFile_" + Locale.ENGLISH.toString() + ".properties";
 Assert.assertNull(messageHandler.getMessage(key, null));
}

 @Test(expected = FileNotFoundException.class)
 public void testAddMessagesWithMissingFile() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR);
 String key = "TEST_KEY_ADD_MISSING_FILE";
 messageHandler.getMessage(key, null);
}

 @Test(expected = IOException.class)
 public void testAddMessagesWithIOException() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR);
 String key = "TEST_KEY_ADD_IO_EXCEPTION";
 // Simulate IOException in the file input stream
 Throwable throwable = new IOException("Simulated IOException");
 messageHandler.addMessages(BASENAME, DIR);
 messageHandler.getMessage(key, null);
}

}