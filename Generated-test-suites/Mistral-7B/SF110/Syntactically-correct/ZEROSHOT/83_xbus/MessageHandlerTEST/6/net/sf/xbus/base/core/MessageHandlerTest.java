package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String BASE_NAME = "TestMessageHandler";

 @BeforeClass
 public static void setup() {
 messageHandler = MessageHandler.getInstance(BASE_NAME);
 }

 @Test
 public void testGetMessage() {
 String key = "testKey";
 List params = Arrays.asList(new Object[]{"param1", "param2"});
 String expectedMessage = "Test Key: $1$ is $2$";
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "testKeyOptional";
 String expectedMessage = "Test Key Optional: {0} and {1}";
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptionalWithParams() {
 String key = "testKeyOptionalWithParams";
 List params = Arrays.asList(new Object[]{"param1", "param2"});
 String expectedMessage = "Test Key Optional: {0} is {1}";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 String paramText1 = params.get(0).toString();
 String paramText2 = params.get(1).toString();
 String actualMessageWithParams = XStringSupport.replaceAll(actualMessage, "$0", paramText1)
 + " " + XStringSupport.replaceAll(actualMessage, "$1", paramText2);
 assertEquals(expectedMessage, actualMessageWithParams);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 List params = Arrays.asList(new Object[]{"param1", "param2"});
 messageHandler.getMessage(null, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 String key = "testKey";
 messageHandler.getMessage(key, null);
 }

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
 MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);
 assertEquals(instance1, instance2);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetInstanceWithDifferentBasename() {
 MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
 MessageHandler instance2 = MessageHandler.getInstance("DifferentBaseName");
 assertNotEquals(instance1, instance2);
 }

 @Test
 public void testAddMessages() {
 String key = "testAddMessages";
 String value = "Test Add Messages";
 List params = null;
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_ETC);
 messageHandler.addMessages(BASE_NAME, Constants.XBUS_PLUGIN_ETC);
 messageHandler.getMessageOptional(key, params); // Triggers adding messages in constructor
 String actualMessage = messageHandler.getMessageOptional(key, params);
 String expectedMessage = value;
 assertEquals(expectedMessage, actualMessage);
 }
}