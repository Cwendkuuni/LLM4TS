package net.sf.xbus.base.core.test;

import org.junit.Assert;
import org.junit.Test;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MessageHandlerTest {

 private static final String TEST_BASENAME = "testMessageHandler";
 private static final String TEST_DIR = "src/test/resources/net/sf/xbus/base/core/";

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance(TEST_BASENAME);
 MessageHandler handler2 = MessageHandler.getInstance(TEST_BASENAME);
 Assert.assertSame(handler1, handler2);
 }

 @Test
 public void testAddMessages() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 }

 @Test
 public void testGetMessage() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = new ArrayList();
 String key = "test.key";
 String expectedMessage = "This is a test message with a parameter: {$1}";
 String message = handler.getMessage(key, params);
 String actualMessage = XStringSupport.replaceAll(expectedMessage, "$1", "value");
 Assert.assertEquals(actualMessage, message);
 }

 @Test
 public void testGetMessageOptional() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = null;
 String key = "test.key";
 String message = handler.getMessageOptional(key, params);
 Assert.assertNull(message);

 params = new ArrayList();
 params.add("value");
 key = "test.key";
 message = handler.getMessageOptional(key, params);
 String expectedMessage = "This is a test message with a parameter: value";
 String actualMessage = XStringSupport.replaceAll(expectedMessage, "value", "value");
 Assert.assertEquals(actualMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = null;
 String key = null;
 String message = handler.getMessage(key, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = null;
 String key = "test.key";
 String message = handler.getMessage(key, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithEmptyParams() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = new ArrayList();
 String key = "test.key";
 String message = handler.getMessage(key, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithInvalidParamsType() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = new ArrayList();
 params.add(123);
 String key = "test.key";
 String message = handler.getMessage(key, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithMissingKey() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = new ArrayList();
 String key = "test.missingKey";
 String message = handler.getMessage(key, params);
 }

 @Test
 public void testGetMessageWithMissingMessageFile() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR + "missingFolder");
 List params = new ArrayList();
 String key = "test.key";
 String message = handler.getMessage(key, params);
 Assert.assertEquals("Key: test.key not found in message file", message);
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetMessageWithMissingMessageFileInputStream() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = new ArrayList();
 String key = "test.key";
 FileInputStream inputStream = null;
 Properties newProps = new Properties();
 String message = handler.getMessageOptional(key, params);
 }

 @Test(expected = IOException.class)
 public void testGetMessageWithIOException() {
 MessageHandler handler = MessageHandler.getInstance(TEST_BASENAME);
 handler.addMessages(TEST_BASENAME, TEST_DIR);
 List params = new ArrayList();
 String key = "test.key";
 Properties newProps = new Properties();
 FileInputStream inputStream = new FileInputStream(TEST_DIR + "invalidFile.txt");
 String message = handler.getMessageOptional(key, params);
 }
}