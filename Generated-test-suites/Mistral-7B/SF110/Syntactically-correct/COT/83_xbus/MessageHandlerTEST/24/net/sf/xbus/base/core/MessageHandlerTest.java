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
 private static final String TEST_BASENAME = "TestMessageHandler";
 private static final String TEST_DIR = "test/resources/messages/";

 @BeforeClass
 public static void setup() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = Configuration.class;
 instance = MessageHandler.getInstance(TEST_BASENAME);
 instance.addMessages(TEST_BASENAME, TEST_DIR);
 }

 // Test getMessage method
 @Test
 public void testGetMessage() {
 List params = new ArrayList<>();
 String key = "test.key";
 String expected = "This is a test message";
 String actual = instance.getMessage(key, params);
 assertEquals(expected, actual);
 }

 // Test getMessage method with null params
 @Test
 public void testGetMessageNullParams() {
 String key = "test.key";
 String actual = instance.getMessage(key, null);
 assertNotNull(actual);
 // Verify that the error message was logged
 assertTrue(Trace.hasError());
 }

 // Test getMessage method with empty params
 @Test
 public void testGetMessageEmptyParams() {
 List params = new ArrayList<>();
 String key = "test.key";
 String actual = instance.getMessage(key, params);
 assertNotNull(actual);
 // Verify that no error message was logged
 assertFalse(Trace.hasError());
 }

 // Test getMessageOptional method with valid params
 @Test
 public void testGetMessageOptionalValidParams() {
 List params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");
 String key = "test.key.$1.$2";
 String expected = "This is a test message with testParam1 and testParam2";
 String actual = instance.getMessageOptional(key, params);
 assertEquals(expected, actual);
 }

 // Test getMessageOptional method with null params
 @Test(expected = NullPointerException.class)
 public void testGetMessageOptionalNullParams() {
 List params = null;
 String key = "test.key.$1.$2";
 instance.getMessageOptional(key, params);
 }

 // Test getMessageOptional method with empty params
 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageOptionalEmptyParams() {
 List params = new ArrayList<>();
 String key = "test.key";
 instance.getMessageOptional(key, params);
 }

 // Test getInstance method
 @Test
 public void testGetInstance() {
 MessageHandler instance2 = MessageHandler.getInstance(TEST_BASENAME);
 assertSame(instance, instance2);
 }

 // Test addMessages method with valid dir
 @Test
 public void testAddMessagesValidDir() {
 instance.addMessages(TEST_BASENAME, TEST_DIR);
 String key = "testAddMessages.key";
 String expected = "This is a test message for addMessages";
 String actual = instance.getMessage(key, null);
 assertEquals(expected, actual);
 }

 // Test addMessages method with invalid dir
 @Test(expected = FileNotFoundException.class)
 public void testAddMessagesInvalidDir() {
 instance.addMessages(TEST_BASENAME, "invalid/test/resources/messages/");
 }

}