package net.sf.xbus.base.core.test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler instance;
 private static final String BASE_NAME = "MyTestMessageHandler";
 private static final String XBUS_ETC = "src/test/resources/net/sf/xbus/base/core/Messages/XBUS_ETC";
 private static final String XBUS_PLUGIN_ETC = "src/test/resources/net/sf/xbus/base/core/Messages/XBUS_PLUGIN_ETC";

 @BeforeClass
 public static void setup() {
 instance = MessageHandler.getInstance(BASE_NAME);
 instance.addMessages(BASE_NAME, XBUS_ETC);
 instance.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);
 }

 @Test
 public void testGetMessage() {
 String key = "key1";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 String expectedMessage = "Message key1 with param1";
 String actualMessage = instance.getMessage(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "key2";
 List<Object> params = null;
 String expectedMessage = null;
 String actualMessage = instance.getMessageOptional(key, params);
 Assert.assertNull(actualMessage);
 }

 @Test
 public void testGetMessageOptionalWithParams() {
 String key = "key3";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");
 String expectedMessage = "Message key3 with param1 and param2";
 String actualMessage = instance.getMessageOptional(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = MissingResourceException.class)
 public void testGetMessageMissingKey() {
 String key = "nonExistentKey";
 List<Object> params = null;
 String actualMessage = instance.getMessage(key, params);
 Assert.fail("Expected MissingResourceException but got: " + actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParams() {
 String key = "key4";
 List<Object> params = null;
 String actualMessage = instance.getMessage(key, params);
 Assert.fail("Expected NullPointerException but got: " + actualMessage);
 }

 @Test
 public void testGetMessageError() {
 Trace.setEnabled(true);
 String key = "errorKey";
 List<Object> params = null;
 String expectedMessage = "Error: Key: errorKey not found in message file";
 String actualMessage = instance.getMessage(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 Trace.setEnabled(false);
 }
}