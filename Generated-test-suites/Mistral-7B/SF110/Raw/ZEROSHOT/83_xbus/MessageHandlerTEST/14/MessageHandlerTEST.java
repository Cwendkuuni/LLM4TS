package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.MessageHandler.MessagesFilter;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 static {
 System.setProperty("net.sf.xbus.base.core.MessageHandler.trace.Trace.level", "ERROR");
 }

 @BeforeClass
 public static void setup() {
 messageHandler = MessageHandler.getInstance("test");
 }

 @Test
 public void testGetMessage() {
 String key = "testKey";
 List params = new ArrayList();
 params.add("testParam1");
 params.add(null);
 String expectedMessage = "Key: testKey not found in message file testParam1 <null>";
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "testKeyWithParams";
 List params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 String expectedMessage = "$1 has value testParam1, $2 has value testParam2";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 List params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 messageHandler.getMessage(null, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 String key = "testKey";
 messageHandler.getMessage(key, null);
 }

 @Test
 public void testGetMessageWithEmptyParams() {
 String key = "testKey";
 messageHandler.getMessage(key, new ArrayList());
 }

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler1 = MessageHandler.getInstance("test");
 MessageHandler messageHandler2 = MessageHandler.getInstance("test");
 assertEquals(messageHandler1, messageHandler2);
 }

 @Test
 public void testAddMessages() {
 String key = "testKey";
 String expectedMessage = "This is a test message";
 messageHandler.addMessages("test", "src/test/resources");
 messageHandler.mMessages.put(key, expectedMessage);
 String actualMessage = messageHandler.getMessage(key, new ArrayList());
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddMessagesWithDuplicateKey() {
 messageHandler.addMessages("test", "src/test/resources");
 messageHandler.addMessages("test", "src/test/resources");
 }

 @Test
 public void testMessagesFilter() {
 File dir = new File("src/test/resources");
 MessagesFilter filter = new MessagesFilter("test", "_en.properties");
 String[] messagesFiles = filter.filter(dir.listFiles());
 String expectedFileName = "test_en.properties";
 assertEquals(1, messagesFiles.length);
 assertEquals(expectedFileName, messagesFiles[0].getName());
 }
}