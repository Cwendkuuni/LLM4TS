package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static MessageHandler instance;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 messageHandler = MessageHandler.getInstance("test");
 }

 @Test
 public void testGetMessage() {
 List params = new ArrayList();
 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";
 String message = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, message);

 key = "validKey";
 expectedMessage = "This is a valid message with no params";
 message = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, message);

 key = "validKeyWithParams";
 String param1 = "param1";
 String param2 = "param2";
 List paramsList = new ArrayList();
 paramsList.add(param1);
 paramsList.add(param2);
 expectedMessage = "This is a valid message with params $1$ and $2$";
 message = messageHandler.getMessage(key, paramsList);
 assertEquals(expectedMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParams() {
 List params = null;
 String key = "validKeyWithParams";
 messageHandler.getMessage(key, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullKey() {
 List params = new ArrayList();
 String key = null;
 messageHandler.getMessage(key, params);
 }

 @Test
 public void testGetMessageOptional() {
 List params = new ArrayList();
 String key = "testKeyOptional";
 String expectedMessage = null;
 String message = messageHandler.getMessageOptional(key, params);
 assertNull(message);

 key = "validKeyOptionalWithParams";
 String param1 = "param1";
 String param2 = "param2";
 List paramsList = new ArrayList();
 paramsList.add(param1);
 paramsList.add(param2);
 expectedMessage = "This is a valid message with optional params $1$ and $2$";
 message = messageHandler.getMessageOptional(key, paramsList);
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetInstance() {
 String basename = "testInstance";
 instance = MessageHandler.getInstance(basename);
 MessageHandler expectedInstance = messageHandler;
 assertEquals(expectedInstance, instance);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInstanceDuplicate() {
 String basename = "testInstance";
 MessageHandler instance1 = MessageHandler.getInstance(basename);
 MessageHandler instance2 = MessageHandler.getInstance(basename);
 }

 @Test
 public void testAddMessages() {
 String basename = "testAddMessages";
 messageHandler = null;
 messageHandler = MessageHandler.getInstance(basename);
 String key1 = "message1";
 String value1 = "This is message 1";
 String key2 = "message2";
 String value2 = "This is message 2";
 messageHandler.addMessages(basename, "testDir");
 Properties properties = (Properties)messageHandler.mMessages;
 String expectedMessage1 = key1 + "=" + value1;
 String expectedMessage2 = key2 + "=" + value2;
 assertTrue(properties.containsKey(key1));
 assertEquals(value1, properties.get(key1));
 assertTrue(properties.containsKey(key2));
 assertEquals(value2, properties.get(key2));
 }

 @Test
 public void testAddMessagesNullDir() {
 String basename = "testAddMessagesNullDir";
 messageHandler = null;
 messageHandler = MessageHandler.getInstance(basename);
 messageHandler.addMessages(basename, null);
 assertNull(messageHandler.mMessages);
 }

 @Test(expected = NullPointerException.class)
 public void testAddMessagesNullProperties() {
 String basename = "testAddMessagesNullProperties";
 messageHandler = null;
 messageHandler = MessageHandler.getInstance(basename);
 Properties properties = null;
 messageHandler.addMessages(basename, "testDir");
 }

 @Test
 public void testReplaceMessageText() {
 String message = "This is a message with params $1$ and $2$";
 List params = new ArrayList();
 params.add("param1");
 params.add("param2");
 String expectedMessage = "This is a message with params param1 and param2";
 String replacedMessage = XStringSupport.replaceAll(message, "$1$", params.get(0).toString());
 assertEquals(expectedMessage, replacedMessage);
 }
}