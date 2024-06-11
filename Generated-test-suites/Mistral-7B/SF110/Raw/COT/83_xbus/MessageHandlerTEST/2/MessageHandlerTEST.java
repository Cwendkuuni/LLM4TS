package net.sf.xbus.base.core.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String BASENAME = "MessageHandlerTest";

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance(BASENAME);
 MessageHandler instance2 = MessageHandler.getInstance(BASENAME);
 Assert.assertEquals(instance1, instance2);
 }

 @Test
 public void testGetMessageNullKey() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 List<Object> emptyList = new ArrayList<>();
 String result = messageHandler.getMessage(null, emptyList);
 Assert.assertNull(result);
 }

 @Test
 public void testGetMessageEmptyList() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String result = messageHandler.getMessage("testKey", emptyList);
 Assert.assertNull(result);
 }

 @Test
 public void testGetMessageWithParams() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 List<Object> paramList = new ArrayList<>();
 paramList.add("testParam1");
 paramList.add("testParam2");
 String key = "testKey{$1}${$2}";
 String result = messageHandler.getMessage(key, paramList);
 String expected = "testKeytestParam1testParam2";
 Assert.assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParams() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String result = messageHandler.getMessage("testKey", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageInvalidKey() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 List<Object> emptyList = new ArrayList<>();
 String result = messageHandler.getMessage(null, emptyList);
 }

 @Test
 public void testGetMessageOptionalNullKey() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 List<Object> emptyList = new ArrayList<>();
 String result = messageHandler.getMessageOptional("testKey", emptyList);
 Assert.assertNull(result);
 }

 @Test
 public void testGetMessageOptionalEmptyMessages() {
 messageHandler = new MessageHandler(BASENAME);
 List<Object> emptyList = new ArrayList<>();
 String result = messageHandler.getMessageOptional("testKey", emptyList);
 Assert.assertNull(result);
 }

 @Test
 public void testGetMessageOptionalValidKey() {
 messageHandler = new MessageHandler(BASENAME);
 List<Object> emptyList = new ArrayList<>();
 String key = "testKey";
 String result = messageHandler.getMessageOptional(key, emptyList);
 String expected = "testKey";
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageOptionalInvalidKey() {
 messageHandler = new MessageHandler(BASENAME);
 List<Object> emptyList = new ArrayList<>();
 String result = messageHandler.getMessageOptional("testKey{$1}", emptyList);
 }

 @Test
 public void testAddMessagesNullBasename() {
 messageHandler = new MessageHandler(null);
 Assert.assertNull(messageHandler.mMessages);
 }

 @Test
 public void testAddMessagesEmptyDir() {
 messageHandler = new MessageHandler(BASENAME);
 String emptyDir = "";
 try {
 messageHandler.addMessages(BASENAME, emptyDir);
 Failure expectedFailure = new Failure(new RuntimeException("Cannot find messagefile"));
 throw expectedFailure;
 } catch (AssertionError e) {
 // assertion passed
 }
 }

 @Test
 public void testAddMessagesValidDir() {
 messageHandler = new MessageHandler(BASENAME);
 String validDir = "validDir";
 messageHandler.addMessages(BASENAME, validDir);
 Assert.assertNotNull(messageHandler.mMessages);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessagesInvalidFile() {
 messageHandler = new MessageHandler(BASENAME);
 String invalidFile = "invalidFile";
 try {
 messageHandler.addMessages(BASENAME, invalidFile);
 Failure expectedFailure = new Failure(new FileNotFoundException("Cannot find messagefile"));
 throw expectedFailure;
 } catch (AssertionError e) {
 // assertion passed
 }
 }

 @Test(expected = IOException.class)
 public void testAddMessagesInvalidFileFormat() {
 messageHandler = new MessageHandler(BASENAME);
 String invalidFileFormat = "invalidFileFormat.txt";
 try {
 messageHandler.addMessages(BASENAME, invalidFileFormat);
 Failure expectedFailure = new Failure(new IOException("Cannot read messagefile"));
 throw expectedFailure;
 } catch (AssertionError e) {
 // assertion passed
 }
}

}