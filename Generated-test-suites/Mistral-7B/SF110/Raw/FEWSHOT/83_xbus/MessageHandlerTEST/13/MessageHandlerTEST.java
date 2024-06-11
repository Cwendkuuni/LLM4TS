package net.sf.xbus.base.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.Test;

public class MessageHandlerTest {

 private static final String BASENAME = "test";
 private static final String DIR = "src/test/resources/net/sf/xbus/base/core/messages/";

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler1 = MessageHandler.getInstance(BASENAME);
 MessageHandler messageHandler2 = MessageHandler.getInstance(BASENAME);
 assertEquals(messageHandler1, messageHandler2);
 }

 @Test
 public void testGetMessages() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 Properties expectedProperties = new Properties();
 expectedProperties.setProperty("test.message.1", "Test message 1 {0}");
 expectedProperties.setProperty("test.message.2", "Test message 2 {1} {2}");

 Properties actualProperties = (Properties) messageHandler.mMessages.clone();
 assertEquals(expectedProperties, actualProperties);
 }

 @Test
 public void testGetMessage() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 List<String> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");
 params.add(null);

 String expectedMessage = "Test message 1 param1 param2 <null>";
 String message = messageHandler.getMessage("test.message.1", params);
 assertEquals(expectedMessage, message);

 String messageOptional = messageHandler.getMessageOptional("test.message.3", params);
 assertNull(messageOptional);
 }

 @Test
 public void testGetMessageOptional() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 List<String> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");

 String expectedMessage = "Test message 1 {0} {1}";
 String message = messageHandler.getMessageOptional("test.message.2", params);
 assertEquals(expectedMessage, message);

 String messageEmpty = messageHandler.getMessageOptional("test.message.empty", params);
 assertNull(messageEmpty);
 }

 @Test
 public void testAddMessages() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR);

 Properties expectedProperties1 = new Properties();
 expectedProperties1.setProperty("test.message.1", "Test message 1 {0}");
 expectedProperties1.setProperty("test.message.2", "Test message 2 {1} {2}");

 Properties expectedProperties2 = new Properties();
 expectedProperties2.setProperty("test2.message.1", "Test message 3 {0}");
 expectedProperties2.setProperty("test2.message.2", "Test message 4 {1} {2}");

 Properties actualProperties1 = (Properties) messageHandler.mMessages.get("test");
 assertEquals(expectedProperties1, actualProperties1);

 Properties actualProperties2 = (Properties) messageHandler.mMessages.get("test2");
 assertNull(actualProperties2);
 }

 @Test(expected = RuntimeException.class)
 public void testAddMessagesInvalidDirectory() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, "invalid_directory");
 }
}