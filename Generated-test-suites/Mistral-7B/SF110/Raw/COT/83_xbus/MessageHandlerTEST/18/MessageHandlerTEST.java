package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;
import java.io.*;
import java.util.*;
import org.junit.Test;

public class MessageHandlerTest {

 private static final String BASENAME = "MessageHandlerTest";
 private static final String TEST_DIR = "testResources/net/sf/xbus/base/core/";

 @Test
 public void testGetInstance() {
     MessageHandler handler1 = MessageHandler.getInstance(BASENAME);
     MessageHandler handler2 = MessageHandler.getInstance(BASENAME);
     assertEquals("Should return the same instance", handler1, handler2);
 }

 @Test
 public void testGetMessages_emptyParams() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     String message = handler.getMessage("TestKey", null);
     assertEquals("Should return a default message if key not found", "Key: TestKey not found in message file", message);
 }

 @Test
 public void testGetMessage_withParams() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     List params = new ArrayList();
     params.add("TestParam1");
     params.add(123);
     String message = handler.getMessage("TestKey{$0}${$1}", params);
     assertEquals("Should replace placeholders with params", "TestKey TestParam1 123", message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessage_nullParams() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     String message = handler.getMessage("TestKey", null);
     fail("Should throw NullPointerException if params is null");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessage_invalidParamsType() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     Object invalidParam = new Object();
     String message = handler.getMessage("TestKey", new Object[] { "TestParam1", 123, invalidParam });
     fail("Should throw IllegalArgumentException if params contains invalid type");
 }

 @Test
 public void testAddMessages() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     handler.addMessages(BASENAME, TEST_DIR);
     String message = handler.getMessage("TestKey", null);
     assertNotNull("Should add messages after calling addMessages method", message);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessages_fileNotFound() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     handler.addMessages(BASENAME, TEST_DIR + "non_existent_folder/");
     fail("Should throw FileNotFoundException if the file or folder doesn't exist");
 }

 @Test(expected = IOException.class)
 public void testAddMessages_ioException() {
     MessageHandler handler = MessageHandler.getInstance(BASENAME);
     handler.addMessages(BASENAME, TEST_DIR + "IOException_test.properties"); // Create a test file with IOException exception
     fail("Should throw IOException if there's an issue reading the file");
 }
}