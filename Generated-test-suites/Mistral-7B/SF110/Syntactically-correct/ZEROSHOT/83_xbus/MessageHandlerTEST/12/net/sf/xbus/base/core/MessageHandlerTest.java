package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private static final String BASENAME = "test";
 private static final String XBUS_ETC = "xbus-etc";
 private static final String XBUS_PLUGIN_ETC = "xbus-plugin-etc";
 private static final String TEST_PROPERTIES_FILE = "test_messages.properties";
 private static final String MESSAGE_KEY = "test.message";
 private static final String MESSAGE_VALUE = "This is a test message.";

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance(BASENAME);
 MessageHandler handler2 = MessageHandler.getInstance(BASENAME);
 assertEquals(handler1, handler2);
 }

 @Test
 public void testAddMessages() throws IOException {
 MessageHandler handler = MessageHandler.getInstance(BASENAME);
 handler.addMessages(BASENAME, XBUS_ETC);
 handler.addMessages(BASENAME, XBUS_PLUGIN_ETC);

 Properties testProperties = new Properties();
 testProperties.setProperty(MESSAGE_KEY, MESSAGE_VALUE);
 File testPropertiesFile = new File(TEST_PROPERTIES_FILE);
 try (FileOutputStream outputStream = new FileOutputStream(testPropertiesFile)) {
 testProperties.store(outputStream, null);
 }
 handler.addMessages(BASENAME, XBUS_ETC);

 Map<Object, Object> expectedMessages = new HashMap<>();
 expectedMessages.put(MESSAGE_KEY, MESSAGE_VALUE);
 List<String> expectedDirs = new ArrayList<>();
 expectedDirs.add(XBUS_ETC);
 expectedDirs.add(XBUS_PLUGIN_ETC);
 expectedDirs.add(TEST_PROPERTIES_FILE.replaceAll(File.separator, File.separator));

 assertEquals(expectedMessages, handler.getMessages());
 assertEquals(expectedDirs, handler.getDirectories());
 }

 @Test
 public void testGetMessage() {
 MessageHandler handler = MessageHandler.getInstance(BASENAME);
 List<String> params = new ArrayList<>();
 String message = handler.getMessage(MESSAGE_KEY, params);
 assertEquals(MESSAGE_VALUE, message);
 }

 @Test
 public void testGetMessageOptional() {
 MessageHandler handler = MessageHandler.getInstance(BASENAME);
 List<String> params = new ArrayList<>();
 params.add("test parameter");
 String message = handler.getMessageOptional(MESSAGE_KEY, params);
 assertEquals(MESSAGE_VALUE, message);
 message = handler.getMessageOptional("nonexistent.key", params);
 assertNull(message);
 }

 @Test
 public void testTraceError() {
 MessageHandler handler = MessageHandler.getInstance(BASENAME);
 handler.getMessage("nonexistent.key", null);
 Trace.assertTraceMessageContains("Key: nonexistent.key not found in message file");
 }

}