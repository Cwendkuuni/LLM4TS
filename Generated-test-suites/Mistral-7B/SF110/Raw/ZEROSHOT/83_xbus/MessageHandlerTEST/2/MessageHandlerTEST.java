package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import net.sf.xbus.base.core.config.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 static {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = Configuration.class;
 }

 @BeforeClass
 public static void setup() {
 messageHandler = MessageHandler.getInstance("test");
 messageHandler.addMessages("test", Constants.XBUS_ETC);
 messageHandler.addMessages("test", Constants.XBUS_PLUGIN_ETC);
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance("test");
 MessageHandler handler2 = MessageHandler.getInstance("test");
 assertEquals(handler1, handler2);
 }

 @Test
 public void testGetMessage() {
 String key = "message.key";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");

 String expectedMessage = "Message key: ${1}$ with param: ${2}$";
 String message = messageHandler.getMessage(key, params);
 String actualMessage = XStringSupport.replaceAll(expectedMessage, "${1}", "messageKey");
 String actualMessageWithParams = XStringSupport.replaceAll(actualMessage, "${2}", "param1, param2");

 assertEquals(actualMessageWithParams, message);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "message.key.optional";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");

 String expectedMessage = "Message key: ${1}$ with param: ${2}$";
 String message = messageHandler.getMessageOptional(key, params);
 assertNull(message);

 key = "message.key";
 message = messageHandler.getMessageOptional(key, params);
 String actualMessage = XStringSupport.replaceAll(expectedMessage, "${1}", "messageKey");
 String actualMessageWithParams = XStringSupport.replaceAll(actualMessage, "${2}", "param1, param2");

 assertEquals(actualMessageWithParams, message);
 }

 @Test
 public void testAddMessages() {
 File dir = new File("src/test/resources/messages");
 String[] messagesFiles = dir.list(new MessagesFilter("test", "_en_US.properties"));
 assertEquals(2, messagesFiles.length);

 Properties props1 = new Properties();
 try {
 FileInputStream instream1 = new FileInputStream(new File(Constants.XBUS_ETC, "test_en_US.properties").getAbsolutePath());
 props1.load(instream1);
 instream1.close();
 }
 catch (IOException e) {
 e.printStackTrace();
 }

 Properties props2 = new Properties();
 try {
 FileInputStream instream2 = new FileInputStream(new File(Constants.XBUS_PLUGIN_ETC, "test_en_US.properties").getAbsolutePath());
 props2.load(instream2);
 instream2.close();
 }
 catch (IOException e) {
 e.printStackTrace();
 }

 String key1 = "messageKey1";
 String value1 = "Message key 1 with param: ${0}$";
 String key2 = "messageKey2";
 String value2 = "Message key 2";

 assertTrue(props1.containsKey(key1));
 assertTrue(props1.containsKey(key2));
 assertTrue(props2.containsKey(key1));
 assertTrue(props2.containsKey(key2));

 String message1 = messageHandler.getMessage(key1, null);
 String message2 = messageHandler.getMessage(key2, null);

 assertEquals(XStringSupport.replaceAll(value1, "${0}", "param1"), message1);
 assertEquals(value2, message2);
 }

 class MessagesFilter implements FilenameFilter {
 private String mPrefix;
 private String mPostfix;

 public MessagesFilter(final String prefix, final String postfix) {
 this.mPrefix = null;
 this.mPostfix = null;
 this.mPrefix = prefix;
 this.mPostfix = postfix;
 }

 @Override
 public boolean accept(final File dir, final String filename) {
 return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
 }
}
}