package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.config.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static MessageHandler instance;

 @BeforeClass
 public static void setup() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = Configuration.class;
 messageHandler = MessageHandler.getInstance("test");
 instance = MessageHandler.getInstance("test");
 }

 @Test
 public void testGetMessageOptional_EmptyParams() {
 String message = messageHandler.getMessageOptional("testKey", null);
 assertEquals("testKey", message);
 }

 @Test
 public void testGetMessageOptional_NullKey() {
 String message = messageHandler.getMessageOptional(null, null);
 assertNull(message);
 }

 @Test
 public void testGetMessageOptional_NullValue() {
 String message = messageHandler.getMessageOptional("testKey", null);
 assertEquals("Key: testKey not found in message file", message);

 messageHandler.addMessages("test", Constants.XBUS_ETC);
 Properties properties = new Properties();
 try {
 properties.load(new FileInputStream("src/test/resources/MessageHandlerTest/XBUS_ETC_en_US.properties"));
 messageHandler.mMessages.putAll(properties);
 } catch (IOException e) {
 e.printStackTrace();
 }
 message = messageHandler.getMessageOptional("testKey", null);
 assertEquals("testKey", message);
 }

 @Test
 public void testGetMessageOptional_EmptyList() {
 List<Object> params = new ArrayList<>();
 String message = messageHandler.getMessageOptional("testKeyWithParams", params);
 assertEquals("Key: testKeyWithParams not found in message file", message);

 messageHandler.addMessages("test", Constants.XBUS_ETC);
 message = messageHandler.getMessageOptional("testKeyWithParams{$0}", params);
 assertEquals("testKeyWithParams[0]", message);
 }

 @Test
 public void testGetMessageOptional_NonEmptyList() {
 List<Object> params = new ArrayList<>();
 params.add("testParam");
 String message = messageHandler.getMessageOptional("testKeyWithParams{$0}", params);
 assertEquals("testKeyWithParams[0]: testParam", message);

 messageHandler.addMessages("test", Constants.XBUS_ETC);
 messageHandler.addMessages("test", Constants.XBUS_ETC);
 message = messageHandler.getMessageOptional("testKeyWithParams{$0}${1}", params);
 assertEquals("testKeyWithParams[0]: testParam, testKeyWithParams[1]: null", message);
 }

 @Test
 public void testGetMessage_EmptyParams() {
 String message = instance.getMessage("testKey", null);
 assertEquals("testKey", message);
 }

 @Test
 public void testGetMessage_NullKey() {
 String message = instance.getMessage(null, null);
 assertNull(message);
 }

 @Test
 public void testGetMessage_KeyFound() {
 messageHandler.addMessages("test", Constants.XBUS_ETC);
 messageHandler.addMessages("test", Constants.XBUS_ETC);
 message = messageHandler.getMessage("testKey", null);
 assertEquals("testKey", message);
 }

 @Test
 public void testGetMessage_KeyNotFound() {
 message = messageHandler.getMessage("testKeyNotFound", null);
 assertEquals("Key: testKeyNotFound not found in message file", message);
 }

 @Test
 public void testGetMessage_KeyWithParamsFound() {
 messageHandler.addMessages("test", Constants.XBUS_ETC);
 Properties properties = new Properties();
 try {
 properties.load(new FileInputStream("src/test/resources/MessageHandlerTest/XBUS_ETC_en_US.properties"));
 messageHandler.mMessages.putAll(properties);
 } catch (IOException e) {
 e.printStackTrace();
 }
 message = messageHandler.getMessage("testKeyWithParams{$0}", params);
 assertEquals("testKeyWithParams[0]", message);
 }

 @Test
 public void testGetMessage_KeyWithParamsNotFound() {
 message = messageHandler.getMessage("testKeyWithParams{$0}", params);
 assertEquals("Key: testKeyWithParams not found in message file", message);
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance("test");
 MessageHandler handler2 = MessageHandler.getInstance("test");
 assertEquals(handler1, handler2);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInstance_DifferentInstances() {
 MessageHandler handler1 = MessageHandler.getInstance("test1");
 MessageHandler handler2 = MessageHandler.getInstance("test");
 }
}