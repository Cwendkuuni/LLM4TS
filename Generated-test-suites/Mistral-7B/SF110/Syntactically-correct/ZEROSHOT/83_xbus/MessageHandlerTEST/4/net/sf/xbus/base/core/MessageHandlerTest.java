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

import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String BASENAME = "test";

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
 public void testGetMessageOptional_NoParams() {
 String message = messageHandler.getMessageOptional("testKey", null);
 assertNotNull(message);
 }

 @Test
 public void testGetMessageOptional_WithParams() {
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");
 String message = messageHandler.getMessageOptional("testKey$0$ testKey$1$", params);
 assertNotNull(message);
 String expectedMessage = "testParam1 testParam2";
 assertEquals(expectedMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullParams() {
 List<Object> params = null;
 String message = messageHandler.getMessageOptional("testKey$0$ testKey$1$", params);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGetMessage_KeyNotFound() {
 List<Object> params = null;
 String message = messageHandler.getMessage("testKeyNotFound", params);
 assertNotNull(message);
 assertTrue(message.contains("Key: testKeyNotFound not found in message file"));
 }

 @Test
 public void testGetMessage_KeyFound() {
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 String message = messageHandler.getMessage("testKey$0$", params);
 assertNotNull(message);
 String expectedMessage = "testParam1";
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testAddMessages() {
 Locale locale = Locale.ENGLISH;
 Properties expectedProps = new Properties();
 expectedProps.setProperty("testKey$0$", "Test message with {0}");
 expectedProps.setProperty("testKey$1$", "Test message with {1}");
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Hashtable<String, String> messages = messageHandler.mMessages;
 Properties actualProps = (Properties) messages;
 assertEquals(expectedProps, actualProps);
 }

 @Test
 public void testMessagesFilter() {
 MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "_EN.properties");
 File file = new File("testDir/testPrefix_EN.properties");
 assertTrue(filter.accept(null, file.getName()));
 }

 @Test
 public void testMessagesFilter_NotAccepted() {
 MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("testPrefix", "_FR.properties");
 File file = new File("testDir/testPrefix_EN.properties");
 assertFalse(filter.accept(null, file.getName()));
 }
}