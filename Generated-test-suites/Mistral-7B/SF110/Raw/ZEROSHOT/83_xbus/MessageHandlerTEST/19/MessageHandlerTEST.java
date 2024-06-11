package net.sf.xbus.base.core.test;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class MessageHandlerTest {

 private static MessageHandler instance;
 static String basename = "testMessageHandler";
 static String dirPath = "src/test/resources/";

 @BeforeClass
 public static void setup() {
 instance = MessageHandler.getInstance(basename);
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance(basename);
 MessageHandler handler2 = MessageHandler.getInstance(basename);
 assertEquals(handler1, handler2);
 }

 @Test
 public void testGetMessages() {
 HashMap<String, String> expectedMap = new HashMap<>();
 expectedMap.put("KEY1", "This is message 1");
 expectedMap.put("KEY2", "This is message 2 with {$0} and {$1}");

 HashMap<String, String> actualMap = (HashMap<String, String>) instance.mMessages;
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetMessage() {
 List<Object> params = new ArrayList<>();
 params.add("test");
 String key = "KEY2";
 String expectedMessage = "This is message 2 with test and 1";
 String message = instance.getMessage(key, params);
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageOptional() {
 List<Object> params = new ArrayList<>();
 params.add("test");
 String key = "KEY3";
 String expectedMessage = "Key: " + key + " not found in message file";
 String message = instance.getMessageOptional(key, params);
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageWithEmptyList() {
 List<Object> params = new ArrayList<>();
 String key = "KEY2";
 String expectedMessage = "This is message 2";
 String message = instance.getMessage(key, params);
 assertEquals(expectedMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullList() {
 String key = "KEY2";
 String message = instance.getMessage(key, null);
 fail("Expected NullPointerException, but got: " + message);
 }

 @Test
 public void testAddMessages() throws IOException {
 instance = new MessageHandler(basename);
 File dirFile = new File(dirPath + basename + "/");
 String[] messagesFiles = dirFile.list(new MessagesFilter(basename, "_en_US.properties"));
 instance.addMessages(basename, dirPath);
 HashMap<String, String> expectedMap = new HashMap<>();
 expectedMap.put("KEY1", "This is test message 1");
 expectedMap.put("KEY2", "This is test message 2 with {$0} and {$1}");
 HashMap<String, String> actualMap = (HashMap<String, String>) instance.mMessages;
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testMessagesFilter() {
 File filter = new File(dirPath + basename + "/");
 String[] files = filter.list(new MessagesFilter(basename, "_en_US.properties"));
 assertTrue(files.length > 0);
 }

 @Test
 public void testGetInstanceWithEmptyHashtable() {
 instance = new MessageHandler(basename);
 assertNull(instance.mMessages);
 }

}