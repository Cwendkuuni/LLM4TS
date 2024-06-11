package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.config.Constants;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static List<Object> params;

 @BeforeClass
 public static void setUpBeforeClass() {
 messageHandler = MessageHandler.getInstance("test");
 params = new ArrayList<>();
 }

 @Test
 public void testgetMessage() {
 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testgetMessageOptionalWithParams() {
 String key = "testKey";
 String param1 = "testParam1";
 String param2 = "testParam2";
 String expectedMessage = "${1} ${2}";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 params.add(param1);
 params.add(param2);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testgetMessageOptionalWithNullParams() {
 String key = "testKey";
 String expectedMessage = null;
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testgetMessageWithNullParams() {
 String key = "testKey";
 List<Object> nullParams = null;
 String expectedMessage = "Key: testKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, nullParams);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetMessageWithEmptyParams() {
 String key = "testKey";
 List<Object> emptyParams = new ArrayList<>();
 String expectedMessage = "Key: testKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, emptyParams);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testgetMessageInstance() {
 String expectedBasename = "test";
 String actualBasename = MessageHandler.getInstance(expectedBasename).getBasename();
 assertEquals(expectedBasename, actualBasename);
 }

 @Test
 public void testaddMessages() {
 String basename = "test";
 String dir = "testDir";
 messageHandler = new MessageHandler(basename);
 MessageHandler.addMessages(basename, dir);
 String key = "testKey";
 String expectedMessage = "TestMessage";
 messageHandler.mMessages.put(key, expectedMessage);
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = FileNotFoundException.class)
 public void testaddMessagesWithInvalidFile() {
 String basename = "test";
 String dir = "testDir";
 messageHandler = new MessageHandler(basename);
 MessageHandler.addMessages(basename, dir);
 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";
 messageHandler.mMessages.put(key, expectedMessage);
 FileInputStream inputStream = new FileInputStream("testFile");
 try {
 Properties properties = new Properties();
 properties.load(inputStream);
 inputStream.close();
 }
 catch (IOException e) {
 throw new FileNotFoundException("testFile not found");
 }
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = IOException.class)
 public void testaddMessagesWithIOException() {
 String basename = "test";
 String dir = "testDir";
 messageHandler = new MessageHandler(basename);
 MessageHandler.addMessages(basename, dir);
 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";
 messageHandler.mMessages.put(key, expectedMessage);
 FileInputStream inputStream = new FileInputStream("testFile") {
 @Override
 public int read() throws IOException {
 throw new IOException("testIOException");
 }
 };
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }
}