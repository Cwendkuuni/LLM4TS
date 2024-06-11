package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String BASE_NAME = "test";

 @Before
 public void setUp() {
 messageHandler = MessageHandler.getInstance(BASE_NAME);
 }

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
 MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);

 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetMessageOptional_NoParams() {
 String key = "testMessage";
 String expectedMessage = "This is a test message.";
 String message = messageHandler.getMessageOptional(key, null);

 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageOptional_SingleParam() {
 String key = "testMessageWithParam";
 String param = "testParam";
 String expectedMessage = "This is a test message with a param: ${1}$";
 String message = messageHandler.getMessageOptional(key, new ArrayList<Object>() {{
 add(param);
 }});

 String actualMessage = XStringSupport.replaceAll(expectedMessage, "${1}", param);
 assertEquals(actualMessage, message);
 }

 @Test
 public void testGetMessageOptional_MultipleParams() {
 String key = "testMessageWithMultipleParams";
 String param1 = "testParam1";
 String param2 = "testParam2";
 String expectedMessage = "This is a test message with multiple params: ${1}$ {2}$";
 String message = messageHandler.getMessageOptional(key, new ArrayList<Object>() {{
 add(param1);
 add(param2);
 }});

 String actualMessage = XStringSupport.replaceAll(expectedMessage, "${1}", param1);
 actualMessage = XStringSupport.replaceAll(actualMessage, "${2}", param2);
 assertEquals(actualMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessage_NoParams() {
 String key = "testMessage";
 List<Object> params = null;
 String message = messageHandler.getMessage(key, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessage_EmptyParams() {
 String key = "testMessage";
 List<Object> params = new ArrayList<>();
 String message = messageHandler.getMessage(key, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessage_InvalidKey() {
 String key = null;
 List<Object> params = new ArrayList<>();
 try {
 messageHandler.getMessage(key, params);
 } catch (IllegalArgumentException e) {
 fail("Expected NullPointerException, but got IllegalArgumentException: " + e.getMessage());
 }
 }

 @Test
 public void testGetMessage_ValidKey() {
 String key = "testMessage";
 List<Object> params = null;
 String expectedMessage = "This is a test message.";
 String message = messageHandler.getMessage(key, params);

 assertEquals(expectedMessage, message);
 }

 @Test
 public void testAddMessages() {
 messageHandler = new MessageHandler(BASE_NAME);
 String key1 = "testMessage1";
 String value1 = "This is a test message 1.";
 String key2 = "testMessage2";
 String value2 = "This is a test message 2.";

 messageHandler.addMessages(BASE_NAME, getClass().getResource("/").getPath());
 messageHandler.mMessages.put(key1, value1);
 messageHandler.mMessages.put(key2, value2);

 assertEquals(value1, messageHandler.getMessageOptional(key1, null));
 assertEquals(value2, messageHandler.getMessageOptional(key2, null));
 }
}