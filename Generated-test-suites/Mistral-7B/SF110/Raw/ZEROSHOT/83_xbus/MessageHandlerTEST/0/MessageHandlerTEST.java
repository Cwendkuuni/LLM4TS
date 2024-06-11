package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 static {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = new Object();
 }

 @BeforeClass
 public static void setup() {
 messageHandler = MessageHandler.getInstance("test");
 messageHandler.addMessages("test", "src/test/resources");
 }

 @Test
 public void testGetMessage() {
 String key = "testKey";
 List<Object> params = Arrays.asList("param1", "param2");
 String expectedMessage = "Test key: $1$ test param1: $2$ test param2: $3$";
 String actualMessage = messageHandler.getMessage(key, params);
 assertTrue(expectedMessage.equals(actualMessage));
 }

 @Test
 public void testGetMessageOptional() {
 String key = "testKey";
 List<Object> params = null;
 String expectedMessage = "testKey";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertTrue(expectedMessage.equals(actualMessage));
 }

 @Test
 public void testGetMessageOptionalWithParams() {
 String key = "testKeyWithParams";
 List<Object> params = Arrays.asList("param1", "param2");
 String expectedMessage = "Test key with params: $1$ test param1: $2$ test param2: $3$";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertTrue(expectedMessage.equals(actualMessage));
 }

 @Test
 public void testGetMessageNotFound() {
 String key = "nonexistentKey";
 List<Object> params = null;
 String expectedMessage = "Key: nonexistentKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals("Key: nonexistentKey not found in message file", actualMessage);
 }

 @Test
 public void testGetMessageWithNullParams() {
 String key = "testKey";
 List<Object> params = null;
 String expectedMessage = "testKey";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertTrue(expectedMessage.equals(actualMessage));
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 List<Object> params = Arrays.asList("param1", "param2");
 String actualMessage = messageHandler.getMessage(null, params);
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParamsList() {
 String key = "testKey";
 String actualMessage = messageHandler.getMessage(key, null);
 fail("Should throw NullPointerException");
 }
}