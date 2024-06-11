package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler handler;
 private static Properties messages;

 @BeforeClass
 public static void init() {
 handler = MessageHandler.getInstance("test");
 messages = new Properties();
 messages.put("testKey1", "Test Message 1: {0}");
 messages.put("testKey2", "Test Message 2: {1}");
 handler.addMessages("test", ".");
 handler.setMessages(messages);
 }

 @Test
 public void testGetMessageOptional() {
 String message = handler.getMessageOptional("testKey1", null);
 assertEquals("Test Message 1: null", message);

 message = handler.getMessageOptional("testKey1", Arrays.asList("Test Value 1"));
 assertEquals("Test Message 1: Test Value 1", message);

 message = handler.getMessageOptional("testKey2", Arrays.asList("Test Value 1", "Test Value 2"));
 assertEquals("Test Message 2: Test Value 1, Test Value 2", message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptionalNullParams() {
 handler.getMessageOptional("testKey1", null);
 }

 @Test
 public void testGetMessage() {
 String message = handler.getMessage("testKey1", null);
 assertEquals("Test Message 1: null", message);

 message = handler.getMessage("testKey1", Arrays.asList("Test Value 1"));
 assertEquals("Test Message 1: Test Value 1", message);

 message = handler.getMessage("testKey2", Arrays.asList("Test Value 1", "Test Value 2"));
 assertEquals("Test Message 2: Test Value 1, Test Value 2", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageNullKey() {
 handler.getMessage(null, null);
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler2 = MessageHandler.getInstance("test");
 assertEquals(handler, handler2);
 }

 @Test
 public void testAddMessages() {
 handler.setMessages(null);
 handler.addMessages("test", ".");

 List<String> files = Arrays.asList("test_ENGLISH_messages.properties");
 Properties expectedMessages = new Properties();
 expectedMessages.put("testKey1", "Test Message 1: {0}");
 expectedMessages.put("testKey2", "Test Message 2: {1}");
 expectedMessages.load(MessageHandlerTest.class.getResourceAsStream("/net/sf/xbus/base/core/test/test_ENGLISH_messages.properties"));

 assertEquals(expectedMessages, handler.getMessages());
 }
}