package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.Test;

public class MessageHandlerTest {

 private static final String TEST_BASENAME = "test";
 private static final String TEST_DIR = "test_dir/";
 private static final String TEST_PROPERTIES_FILE = "test_properties.properties";
 private static final String TEST_MESSAGE_KEY = "test.key";
 private static final String TEST_MESSAGE_VALUE = "Test Value";

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 assertNotNull(messageHandler);
 }

 @Test
 public void testAddMessages() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 Properties testProperties = new Properties();
 testProperties.put(TEST_MESSAGE_KEY, TEST_MESSAGE_VALUE);
 try {
 FileOutputStream out = new FileOutputStream(TEST_DIR + TEST_PROPERTIES_FILE);
 testProperties.store(out, "Test Properties");
 out.close();
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to write test properties file.");
 }
 MessageHandler newMessageHandler = MessageHandler.getInstance(TEST_BASENAME);
 assertNotNull(newMessageHandler);
 assertEquals(TEST_MESSAGE_VALUE, newMessageHandler.getMessage(TEST_MESSAGE_KEY, null));
 }

 @Test
 public void testGetMessage() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 assertEquals(TEST_MESSAGE_VALUE, messageHandler.getMessage(TEST_MESSAGE_KEY, null));
 }

 @Test
 public void testGetMessageOptional() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 List<Object> params = new ArrayList<Object>();
 params.add("Test Param");
 assertEquals(XStringSupport.replaceAll(TEST_MESSAGE_VALUE, "$1$", "Test Param"), messageHandler.getMessageOptional(TEST_MESSAGE_KEY, params));
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParams() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 assertEquals(null, messageHandler.getMessageOptional(TEST_MESSAGE_KEY, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageEmptyParams() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 List<Object> emptyParams = new ArrayList<Object>();
 assertEquals(null, messageHandler.getMessageOptional(TEST_MESSAGE_KEY, emptyParams));
 }

 @Test
 public void testGetMessageWithMissingKey() {
 MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 assertEquals("Key: missingKey not found in message file", messageHandler.getMessage("missingKey", null));
 }
}