package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.Test;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String TEST_BASE_NAME = "testMessageHandler";

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance(TEST_BASE_NAME);
 MessageHandler instance2 = MessageHandler.getInstance(TEST_BASE_NAME);

 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetMessage_NullParams() {
 messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

 String key = "testKey";
 String expectedMessage = "Key: " + key + " not found in message file";

 String actualMessage = messageHandler.getMessage(key, null);

 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessage_EmptyParams() {
 messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

 String key = "testKey";
 List<Object> emptyParams = new ArrayList<>();

 String expectedMessage = "Key: " + key + " not found in message file";
 String actualMessage = messageHandler.getMessage(key, emptyParams);

 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessage_NonEmptyParams() {
 messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

 String key = "testKey";
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add(123);

 String expectedMessage = "Key: " + key + " not found in message file";
 String actualMessage = messageHandler.getMessage(key, params);

 fail("Expected an exception or a replaced message text.");
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullMessageTable() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);

 String key = "testKey";
 List<Object> params = null;

 String actualMessage = messageHandler.getMessageOptional(key, params);

 assertNull(actualMessage);
 }

 @Test
 public void testGetMessageOptional_EmptyParams() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);
 messageHandler.addMessages(TEST_BASE_NAME, "/path/to/messages/directory");

 String key = "testKey";
 List<Object> emptyParams = new ArrayList<>();

 String expectedMessage = null;
 String actualMessage = messageHandler.getMessageOptional(key, emptyParams);

 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional_NonEmptyParams() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);
 messageHandler.addMessages(TEST_BASE_NAME, "/path/to/messages/directory");

 String key = "testKey";
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add(123);

 String expectedMessage = "Key: " + key + " not found in message file";
 String actualMessage = messageHandler.getMessageOptional(key, params);

 fail("Expected an exception or a replaced message text.");
 }

 @Test
 public void testGetMessageOptional_ValidMessage() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);
 messageHandler.addMessages(TEST_BASE_NAME, "/path/to/messages/directory");

 String key = "testKey";
 String paramText = "testParam1";
 List<Object> params = new ArrayList<>();
 params.add(paramText);

 String expectedMessage = "Key: " + key + " Param1: " + paramText;
 String actualMessage = messageHandler.getMessageOptional(key, params);

 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMessages_NullDirPath() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);

 String expectedMessage = "Dir path cannot be null.";

 messageHandler.addMessages(TEST_BASE_NAME, null);

 fail(expectedMessage);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessages_NotFoundFile() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);

 String expectedMessage = "Cannot find messagefile";

 messageHandler.addMessages(TEST_BASE_NAME, "/path/to/non_existent_directory");

 fail(expectedMessage);
 }

 @Test(expected = IOException.class)
 public void testAddMessages_IOException() {
 messageHandler = new MessageHandler(TEST_BASE_NAME);

 String expectedMessage = "Cannot find messagefile";

 messageHandler.addMessages(TEST_BASE_NAME, "/path/to/messages/directory");

 fail(expectedMessage);
 }

}