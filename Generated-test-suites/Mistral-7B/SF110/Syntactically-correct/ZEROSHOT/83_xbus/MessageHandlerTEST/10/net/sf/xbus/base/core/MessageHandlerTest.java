package net.sf.xbus.base.core;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String basename = "MessageHandlerTest";

 @BeforeClass
 public static void setupClass() {
 MessageHandler.mInstances = new Hashtable<>();
 messageHandler = MessageHandler.getInstance(basename);
 }

 @Test
 public void testGetInstance() {
 MessageHandler msgHandler1 = MessageHandler.getInstance(basename);
 MessageHandler msgHandler2 = MessageHandler.getInstance(basename);

 assertEquals(msgHandler1, msgHandler2);
 }

 @Test
 public void testGetMessageOptional_NoParams() {
 String key = "testKey";
 String expectedMessage = "Key: " + key + " not found in message file";

 String message = messageHandler.getMessageOptional(key, null);
 assertNull(message);

 message = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageOptional_WithParams() {
 String key = "testKey";
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");

 String expectedMessage = "Key: " + key + " not found in message file";
 String param1 = "testParam1";
 String param2 = "testParam2";

 String message = messageHandler.getMessageOptional(key, params);
 assertNull(message);

 message = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, message);

 message = messageHandler.getMessageOptional("testMessageKey", params);
 String param1Text = "$1$";
 String param2Text = "$2$";
 String messageText = "This is a test message with {0} and {1}.";

 expectedMessage = messageText.replaceAll(param1Text, param1);
 expectedMessage = expectedMessage.replaceAll(param2Text, param2);

 assertEquals(expectedMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullParams() {
 String key = "testKey";
 List<Object> params = null;

 String expectedMessage = "Key: " + key + " not found in message file";

 String message = messageHandler.getMessageOptional(key, params);
 assertNull(message);

 message = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testAddMessages() {
 String key1 = "testKey1";
 String key2 = "testKey2";
 String message1 = "This is test message 1.";
 String message2 = "This is test message 2.";

 messageHandler.addMessages(basename, "testDir");

 Properties expectedProps1 = new Properties();
 expectedProps1.setProperty(key1, message1);
 File file1 = new File("testDir/MessageHandlerTest_ENGLISH_testDir_testMessage1.properties");
 try {
 FileInputStream inputStream1 = new FileInputStream(file1);
 Properties actualProps1 = new Properties();
 actualProps1.load(inputStream1);
 inputStream1.close();

 assertEquals(expectedProps1, actualProps1);
 } catch (IOException e) {
 // File does not exist yet, it will be created in the next test
 }

 messageHandler.addMessages(basename, "testDir");

 Properties expectedProps2 = new Properties();
 expectedProps2.setProperty(key1, message1);
 expectedProps2.setProperty(key2, message2);
 File file2 = new File("testDir/MessageHandlerTest_ENGLISH_testDir_testMessage2.properties");
 try {
 FileInputStream inputStream2 = new FileInputStream(file2);
 Properties actualProps2 = new Properties();
 actualProps2.load(inputStream2);
 inputStream2.close();

 assertEquals(expectedProps2, actualProps2);
 } catch (IOException e) {
 // File does not exist yet, it will be created in the next test
 }
 }
}