package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String BASE_NAME = "test";
 private static final String XBUS_ETC = "etc";
 private static final String XBUS_PLUGIN_ETC = "plugin-etc";
 private static final String MESSAGE_FILE_NAME = "test_en.properties";
 private static final String MESSAGE_KEY = "test.key";
 private static final String MESSAGE_VALUE = "This is a test message.";
 private static final List<Object> MESSAGE_PARAMS = new ArrayList<>();

 @BeforeClass
 public static void setup() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = Configuration.class;
 messageHandler = MessageHandler.getInstance(BASE_NAME);
 messageHandler.addMessages(BASE_NAME, XBUS_ETC);
 messageHandler.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);
 try {
 File messagesFile = new File("src/test/resources/" + XBUS_ETC + File.separator + MESSAGE_FILE_NAME);
 Properties properties = new Properties();
 properties.load(new FileInputStream(messagesFile));
 properties.put(MESSAGE_KEY, MESSAGE_VALUE);
 messageHandler.mMessages.putAll((Hashtable<String, Object>) properties);
 } catch (FileNotFoundException e) {
 Trace.error("Cannot find messagefile: " + e.getMessage());
 } catch (IOException e) {
 Trace.error("Cannot find messagefile: " + e.getMessage());
 }
 }

 @Test
 public void testGetMessageOptional() {
 String messageText = messageHandler.getMessageOptional(MESSAGE_KEY, MESSAGE_PARAMS);
 assertNotNull(messageText);
 assertEquals(MESSAGE_VALUE, messageText);
 }

 @Test
 public void testGetMessageOptionalWithParams() {
 MESSAGE_PARAMS.add("test param 1");
 MESSAGE_PARAMS.add("test param 2");
 String messageText = messageHandler.getMessageOptional(MESSAGE_KEY, MESSAGE_PARAMS);
 assertEquals(MESSAGE_VALUE.replace("$1$", "test param 1")
 .replace("$2$", "test param 2"), messageText);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptionalWithNullParams() {
 messageHandler.getMessageOptional(MESSAGE_KEY, null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 messageHandler.getMessage(MESSAGE_KEY, null);
 }

 @Test
 public void testGetMessage() {
 String messageText = messageHandler.getMessage(MESSAGE_KEY, MESSAGE_PARAMS);
 assertNotNull(messageText);
 assertEquals(MESSAGE_VALUE, messageText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageWithInvalidKey() {
 messageHandler.getMessage("invalidKey", MESSAGE_PARAMS);
 }
}