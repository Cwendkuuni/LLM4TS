package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private static final String BASENAME = "testMessageHandler";
 private static final String DIR_ETC = "src/test/resources/net/sf/xbus/base/core/";
 private static final String DIR_PLUGIN_ETC = "src/test/resources/net/sf/xbus/base/core/plugin/";

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 MessageHandler messageHandler2 = MessageHandler.getInstance(BASENAME);
 assertEquals(messageHandler, messageHandler2);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullBasename() {
 MessageHandler.getInstance(null);
 }

 @Test
 public void testGetMessages() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 String key = "testKey";
 List<String> params = new ArrayList<>();
 params.add("testParam1");
 params.add(123);

 String messageOptional = messageHandler.getMessageOptional(key, params);
 assertNull(messageOptional);

 String message = messageHandler.getMessage(key, params);
 assertEquals("Key: " + key + " not found in message file", message);

 key = "testKey_ENGLISH_properties";
 messageOptional = messageHandler.getMessageOptional(key, null);
 assertNotNull(messageOptional);
 assertEquals("testParam1 is testParam1, testParam2 is 123", messageOptional);

 message = messageHandler.getMessage(key, params);
 assertEquals("testParam1 is testParam1, testParam2 is 123", message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessagesWithNullKey() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 messageHandler.getMessage(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessagesWithNullParams() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 messageHandler.getMessage("testKey", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessagesWithEmptyParams() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 messageHandler.getMessage("testKey", new ArrayList<>());
 }

 @Test
 public void testGetMessagesWithEmptyParamsAndKeyFound() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 String key = "testKey_ENGLISH_properties";
 List<String> params = new ArrayList<>();

 String messageOptional = messageHandler.getMessageOptional(key, params);
 assertNotNull(messageOptional);
 assertEquals("testParam1 is testParam1, testParam2 is 123", messageOptional);

 String message = messageHandler.getMessage(key, params);
 assertEquals("testParam1 is testParam1, testParam2 is 123", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessagesWithWrongParamsNumber() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 messageHandler.getMessage("testKey", Arrays.asList("testParam1", "testParam1", "testParam3"));
 }

 @Test
 public void testGetMessagesWithWrongParamsNumberButKeyFound() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 String key = "testKey_ENGLISH_properties";
 List<String> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");
 params.add("testParam3");

 String messageOptional = messageHandler.getMessageOptional(key, params);
 assertNotNull(messageOptional);
 assertEquals("testParam1 is testParam1, testParam2 is testParam2, testParam3 is <null>", messageOptional);

 String message = messageHandler.getMessage(key, params);
 assertEquals("testParam1 is testParam1, testParam2 is testParam2, testParam3 is <null>", message);
 }

 @Test
 public void testTraceError() {
 MessageHandler messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, DIR_ETC);
 messageHandler.addMessages(BASENAME, DIR_PLUGIN_ETC);

 String key = "testKey";
 List<String> params = new ArrayList<>();
 params.add("testParam1");
 params.add(123);

 messageHandler.getMessage(key, params);
 Trace.error("Test error message");
 Message message = MessageHandler.getInstance(BASENAME).getMessage("errorMessageKey", null);
 assertNotNull(message);
 assertEquals("Test error message", message.getMessageText());
 }

}