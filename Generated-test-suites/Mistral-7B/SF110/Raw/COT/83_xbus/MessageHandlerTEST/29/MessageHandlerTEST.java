package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.config.*;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {

 private static MessageHandler instance;
 private static final String basename = "testMessageHandler";

 @BeforeClass
 public static void setup() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = Configuration.class;
 instance = MessageHandler.getInstance(basename);
 instance.addMessages(basename, Constants.XBUS_ETC);
 instance.addMessages(basename, Constants.XBUS_PLUGIN_ETC);
 }

 // Test getMessage method
 @Test
 public void testgetMessage() {
 String key = "testKey";
 List<Object> params = Arrays.asList("testParam1", "testParam2");
 String expectedMessage = "Test key: testParam1 and testParam2";
 String message = instance.getMessage(key, params);
 assertEquals(expectedMessage, message);
 }

 // Test getMessageOptional method with null params
 @Test
 public void testgetMessageOptionalNullParams() {
 String key = "testKey";
 List<Object> params = null;
 String message = instance.getMessageOptional(key, params);
 assertNull(message);
 }

 // Test getMessageOptional method with empty params
 @Test
 public void testgetMessageOptionalEmptyParams() {
 String key = "testKey";
 List<Object> params = new ArrayList<>();
 String expectedMessage = "Test key: ";
 String message = instance.getMessageOptional(key, params);
 assertEquals(expectedMessage, message);
 }

 // Test getMessageOptional method with valid params
 @Test
 public void testgetMessageOptionalValidParams() {
 String key = "testKey";
 List<Object> params = Arrays.asList("testParam1", "testParam2");
 String expectedMessage = "Test key: testParam1 and testParam2";
 String message = instance.getMessageOptional(key, params);
 assertEquals(expectedMessage, message);
 }

 // Test getInstance method
 @Test
 public void testgetMessageHandlerInstance() {
 MessageHandler expectedInstance = instance;
 MessageHandler testInstance = MessageHandler.getInstance(basename);
 assertEquals(expectedInstance, testInstance);
 }

 // Test addMessages method with valid basename and dir
 @Test
 public void testaddMessagesValidBasenameAndDir() {
 String expectedBasename = basename;
 String expectedDir = Constants.XBUS_ETC;
 String key = "testKey";
 String expectedMessage = "Test key: ";
 String message = instance.getMessageOptional(key, null);
 assertNotNull(message);
 assertEquals(expectedMessage, message);
 }

 // Test addMessages method with null basename
 @Test(expected = IllegalArgumentException.class)
 public void testaddMessagesNullBasename() {
 String nullBasename = null;
 instance.addMessages(nullBasename, Constants.XBUS_ETC);
 }

 // Test addMessages method with null dir
 @Test(expected = IllegalArgumentException.class)
 public void testaddMessagesNullDir() {
 String dir = null;
 instance.addMessages(basename, dir);
 }

 // Test addMessages method with non-existent dir
 @Test(expected = FileNotFoundException.class)
 public void testaddMessagesNonExistentDir() {
 String dir = "nonExistentDir";
 instance.addMessages(basename, dir);
 }

}