package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import net.sf.xbus.base.core.strings.*;

import org.junit.Test;

public class MessageHandlerTest {

 private static final String BASE_NAME = "testMessageHandler";
 private static MessageHandler messageHandler;

 @Test
 public void testGetInstance() {
 messageHandler = MessageHandler.getInstance(BASE_NAME);
 MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);
 assertEquals(messageHandler, instance2);
 }

 @Test
 public void testAddMessages() {
 messageHandler = new MessageHandler(BASE_NAME);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_ETC);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_PLUGIN_ETC);

 HashTable messages = messageHandler.mMessages;
 assertNotNull(messages);
 assertTrue(messages.size() > 0);
 }

 @Test
 public void testGetMessage() {
 messageHandler = new MessageHandler(BASE_NAME);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_ETC);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_PLUGIN_ETC);

 String key = "testKey";
 List params = Arrays.asList("param1", "param2");
 String expectedMessage = "Key: testKey not found in message file";

 String message = messageHandler.getMessage(key, params);
 assertNull(message);

 String messageOptional = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessage, messageOptional);

 key = "testKeyWithParams";
 params = Arrays.asList("value1", "value2");
 String expectedMessageWithParams = "Key: testKeyWithParams {0}: value1, {1}: value2";

 messageOptional = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessageWithParams, messageOptional);

 message = messageHandler.getMessage(key, params);
 assertNotNull(message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 messageHandler = new MessageHandler(BASE_NAME);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_ETC);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_PLUGIN_ETC);

 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";

 String message = messageHandler.getMessage(key, null);
 assertNull(message);

 String messageOptional = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, messageOptional);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithEmptyParams() {
 messageHandler = new MessageHandler(BASE_NAME);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_ETC);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_PLUGIN_ETC);

 String key = "testKey";
 List<Object> params = new ArrayList<>();

 String message = messageHandler.getMessage(key, params);
 assertNull(message);

 String messageOptional = messageHandler.getMessageOptional(key, params);
 assertEquals("Key: testKey not found in message file", messageOptional);
 }
}