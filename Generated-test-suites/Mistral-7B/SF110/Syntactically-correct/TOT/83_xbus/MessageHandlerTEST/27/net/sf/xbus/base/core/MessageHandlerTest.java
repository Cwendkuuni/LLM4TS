package net.sf.xbus.base.core.test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private String basename = "testMessageHandler";
 private List params = new ArrayList<>();

 @Before
 public void setUp() {
 messageHandler = MessageHandler.getInstance(basename);
 }

 @Test
 public void testGetMessage() {
 String key = "Key1";
 String expectedMessage = "This is a test message for Key1";
 params.add("Test param 1");
 params.add("Test param 2");
 messageHandler.addMessages(basename, "testMessages");
 messageHandler.addMessages(basename, "testMessages.English.properties");
 messageHandler.addMessages(basename, "testMessages.English_test.properties");
 messageHandler.putMessage(key, "This is a test message for {0} and {1}");

 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, XStringSupport.replaceAll(actualMessage, "{0}", "Test param 1") + " " + XStringSupport.replaceAll(actualMessage, "{1}", "Test param 2"));
 }

 @Test
 public void testGetMessageOptional() {
 String key = "Key2";
 String expectedMessage = null;
 messageHandler.addMessages(basename, "testMessages");
 messageHandler.addMessages(basename, "testMessages.English.properties");
 messageHandler.addMessages(basename, "testMessages.English_test.properties");
 messageHandler.putMessage(key, "This is a test message for {0}");
 messageHandler.putMessage(key, "Key2 not found in message file");

 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertNull(actualMessage);
 }

 @Test
 public void testGetMessageWithNullParams() {
 String key = "Key3";
 String expectedMessage = "This is a test message for Key3";
 messageHandler.addMessages(basename, "testMessages");
 messageHandler.addMessages(basename, "testMessages.English.properties");
 messageHandler.addMessages(basename, "testMessages.English_test.properties");
 messageHandler.putMessage(key, "This is a test message for {0}");

 String actualMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, messageHandler.getMessageOptional(key, null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithEmptyList() {
 String key = "Key4";
 String expectedMessage = null;
 messageHandler.addMessages(basename, "testMessages");
 messageHandler.addMessages(basename, "testMessages.English.properties");
 messageHandler.addMessages(basename, "testMessages.English_test.properties");
 messageHandler.putMessage(key, "This is a test message for {0}");

 List emptyList = new ArrayList<>();
 String actualMessage = messageHandler.getMessage(key, emptyList);
 assertNull(actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullMessageHandler() {
 String key = "Key5";
 String expectedMessage = null;
 String actualMessage = null;

 assertNull(actualMessage = MessageHandler.getMessage(key, params));
 assertNull(actualMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithEmptyKey() {
 String emptyKey = "";
 String expectedMessage = null;
 messageHandler.addMessages(basename, "testMessages");
 messageHandler.addMessages(basename, "testMessages.English.properties");
 messageHandler.addMessages(basename, "testMessages.English_test.properties");
 messageHandler.putMessage("", "This is a test message for {0}");

 assertNull(messageHandler.getMessageOptional("", params));
 assertNull(messageHandler.getMessageOptional("", null));
 }
}