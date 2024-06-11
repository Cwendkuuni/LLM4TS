package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.MessagesFilter;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static MessageHandler instance;
 private static final String BASENAME = "test";

 @BeforeClass
 public static void setup() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = MessageHandler.class;
 }

 @Test
 public void testGetInstance() {
 instance = MessageHandler.getInstance(BASENAME);
 assertSame(instance, MessageHandler.getInstance(BASENAME));
 }

 @Test
 public void testGetInstanceDifferentBasename() {
 MessageHandler otherInstance = MessageHandler.getInstance("otherBasename");
 assertSame(instance, MessageHandler.getInstance(BASENAME));
 assertNull(otherInstance);
 }

 @Test
 public void testGetMessages() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 testProperties.setProperty("testKey1", "testMessage1%$1%$");
 testProperties.setProperty("testKey2", "testMessage2%$2%$");
 messageHandler.addMessages(BASENAME, "testDir", testProperties);

 List<String> keys = new ArrayList<>(messageHandler.mMessages.keySet());
 assertEquals(2, keys.size());
 assertEquals("testMessage1%$1%$", messageHandler.getMessage("testKey1", null));
 assertEquals("testMessage2%$2%$", messageHandler.getMessage("testKey2", null));
 }

 @Test
 public void testGetOptionalMessage() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 testProperties.setProperty("testKey1", "testMessage1%$1%$");
 testProperties.setProperty("testKey2", "testMessage2%$2%$");
 messageHandler.addMessages(BASENAME, "testDir", testProperties);

 List<String> params = new ArrayList<>();
 params.add("testParam1");
 params.add(Locale.GERMAN);
 assertEquals("testMessage1%$1%$testParam1%$2%$", messageHandler.getMessage("testKey1", params));
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 messageHandler.addMessages(BASENAME, "testDir", testProperties);
 assertNull(messageHandler.getMessage(null, null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 testProperties.setProperty("testKey1", "testMessage1%$1%$");
 testProperties.setProperty("testKey2", "testMessage2%$2%$");
 messageHandler.addMessages(BASENAME, "testDir", testProperties);
 assertNull(messageHandler.getMessage("testKey1", null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithEmptyKey() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 testProperties.setProperty("testKey1", "testMessage1%$1%$");
 testProperties.setProperty("testKey2", "testMessage2%$2%$");
 messageHandler.addMessages(BASENAME, "testDir", testProperties);
 assertNull(messageHandler.getMessage("", null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithEmptyParams() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 testProperties.setProperty("testKey1", "testMessage1%$1%$");
 testProperties.setProperty("testKey2", "testMessage2%$2%$");
 messageHandler.addMessages(BASENAME, "testDir", testProperties);
 messageHandler.getMessage("testKey1", new ArrayList<>());
 }

 @Test
 public void testTraceMessage() {
 messageHandler = new MessageHandler(BASENAME);
 messageHandler.addMessages(BASENAME, "testDir");
 Properties testProperties = new Properties();
 testProperties.setProperty("testKey1", "testMessage1%$1%$");
 testProperties.setProperty("testKey2", "testMessage2%$2%$");
 messageHandler.addMessages(BASENAME, "testDir", testProperties);
 String testMessage = "testKey3 not found in message file";
 Trace.error(testMessage);
 assertEquals(testMessage, messageHandler.getMessage("testKey3", null));
 }
}