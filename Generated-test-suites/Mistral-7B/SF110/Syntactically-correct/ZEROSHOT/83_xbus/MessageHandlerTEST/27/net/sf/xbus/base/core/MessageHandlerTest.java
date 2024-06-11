package net.sf.xbus.base.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MessageHandlerTest {

 private static final String BASENAME = "testMessageHandler";
 private static final String XBUS_ETC = "xbus-etc";
 private static final String XBUS_PLUGIN_ETC = "xbus-plugin-etc";
 private MessageHandler messageHandler;

 @Before
 public void setUp() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 }

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler1 = MessageHandler.getInstance(BASENAME);
 MessageHandler messageHandler2 = MessageHandler.getInstance(BASENAME);
 assertEquals(messageHandler1, messageHandler2);
 }

 @Test
 public void testGetMessage_NullParams() {
 String key = "testKey";
 String expectedMessage = "Key: " + key + " not found in message file";
 String resultMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testGetMessage_ValidParams() {
 String key = "testKey";
 String param1 = "testParam1";
 String param2 = "testParam2";
 String expectedMessage = "Key: {0} Param1: {1} Param2: {2}";
 List params = new ArrayList();
 params.add(param1);
 params.add(param2);
 String resultMessage = messageHandler.getMessage(key, params);
 String formattedMessage = String.format(expectedMessage, key, param1, param2);
 assertEquals(formattedMessage, resultMessage);
 }

 @Test
 public void testGetMessageOptional_NullParams() {
 String key = "testKey";
 List params = null;
 String resultMessage = messageHandler.getMessageOptional(key, params);
 assertNull(resultMessage);
 }

 @Test
 public void testGetMessageOptional_ValidParams() {
 String key = "testKey";
 String param1 = "testParam1";
 String param2 = "testParam2";
 String expectedMessage = "Key: {0} Param1: {1} Param2: {2}";
 List params = new ArrayList();
 params.add(param1);
 params.add(param2);
 String resultMessage = messageHandler.getMessageOptional(key, params);
 String formattedMessage = XStringSupport.replaceAll(expectedMessage, "{0}", key)
 .replaceAll("{1}", param1)
 .replaceAll("{2}", param2);
 assertEquals(formattedMessage, resultMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullKey() {
 List params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 messageHandler.getMessageOptional(null, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullParams() {
 String key = "testKey";
 messageHandler.getMessageOptional(key, null);
 }

 @Test
 public void testAddMessages() {
 String key1 = "testKey1";
 String message1 = "Test message 1";
 String key2 = "testKey2";
 String message2 = "Test message 2";
 messageHandler.addMessages(BASENAME, XBUS_ETC);
 messageHandler.addMessages(BASENAME, XBUS_PLUGIN_ETC);

 Properties messagesETC = new Properties();
 messagesETC.setProperty(key1, message1);
 messageHandler.mMessages.put(key1, message1);
 Properties messagesPluginETC = new Properties();
 messagesPluginETC.setProperty(key2, message2);
 messageHandler.mMessages.put(key2, message2);

 assertTrue(messageHandler.mMessages.containsKey(key1));
 assertTrue(messageHandler.mMessages.containsKey(key2));
 assertEquals(message1, messageHandler.getMessage(key1, null));
 assertEquals(message2, messageHandler.getMessage(key2, null));
 }
}