package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace; // Assuming Trace is a static class, if not, modify this line accordingly
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String BASE_NAME = "testMessageHandler";

 @BeforeClass
 public static void setupClass() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = new Object();
 }

 @Before
 public void setup() {
 messageHandler = MessageHandler.getInstance(BASE_NAME);
 messageHandler.addMessages(BASE_NAME, "/path/to/your/messages/directory"); // Replace with your messages directory
 }

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler1 = MessageHandler.getInstance(BASE_NAME);
 MessageHandler messageHandler2 = MessageHandler.getInstance(BASE_NAME);
 assertTrue(messageHandler1 == messageHandler2);
 }

 @Test
 public void testGetMessageNullParams() {
 String message = messageHandler.getMessage("key1", null);
 assertEquals("Key: key1 not found in message file", message);
 }

 @Test
 public void testGetMessageEmptyParams() {
 String message = messageHandler.getMessage("key2", new ArrayList<>());
 String expectedMessage = "Key: key2 not found in message file";
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageParamsReplaced() {
 String key = "hello.$1.$2";
 List<Object> params = new ArrayList<>();
 params.add("world");
 params.add(123);
 String message = messageHandler.getMessage(key, params);
 String expectedMessage = "hello.world.123";
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageOptionalNullParams() {
 String message = messageHandler.getMessageOptional("key3", null);
 assertNull(message);
 }

 @Test
 public void testGetMessageOptionalEmptyParams() {
 String message = messageHandler.getMessageOptional("key4", new ArrayList<>());
 assertNull(message);
 }

 @Test
 public void testGetMessageOptionalParamsReplaced() {
 String key = "hello.$1.$2";
 List<Object> params = new ArrayList<>();
 params.add("world");
 params.add(123);
 String message = messageHandler.getMessageOptional(key, params);
 String expectedMessage = "hello.world.123";
 assertEquals(expectedMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 messageHandler.getMessage(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 messageHandler.getMessage("key5", null);
 }

 @AfterClass
 public static void teardownClass() {
 MessageHandler.mInstances = null;
 }
}