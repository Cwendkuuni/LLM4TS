package net.sf.xbus.base.core.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.MessageHandler.MessagesFilter;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String BASENAME = "test";

 @Test
 public void testGetInstance() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 MessageHandler instance = MessageHandler.getInstance(BASENAME);
 Assert.assertSame(messageHandler, instance);
 }

 @Test
 public void testGetMessage() {
 String key = "message.key";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");
 String expectedMessage = "Message key: message.key, param1: param1, param2: param2";
 String actualMessage = messageHandler.getMessage(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "message.key";
 List<Object> params = null;
 String expectedMessage = "Key: message.key not found in message file";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptionalWithParams() {
 String key = "message.key";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");
 String expectedMessage = "Message key: message.key, param1: param1, param2: param2";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");
 messageHandler.getMessage(null, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 String key = "message.key";
 messageHandler.getMessage(key, null);
 }

 @Test
 public void testAddMessages() {
 messageHandler = new MessageHandler(BASENAME);
 File dir = new File("src/test/resources/messages");
 String[] messagesFiles = dir.list(new MessagesFilter(BASENAME, "_EN.properties"));
 for (String fileName : messagesFiles) {
 Properties newProps = new Properties();
 try {
 FileInputStream instream = new FileInputStream(dir + File.separator + fileName);
 newProps.load(instream);
 instream.close();
 } catch (FileNotFoundException e) {
 Assert.fail("Cannot find messagefile: " + fileName);
 } catch (IOException e2) {
 Assert.fail("Cannot find messagefile: " + fileName);
 }
 String key = null;
 Enumeration keys = newProps.keys();
 while (keys.hasMoreElements()) {
 key = keys.nextElement().toString();
 messageHandler.mMessages.put(key, newProps.getProperty(key));
 }
 }
 String key = "message.key";
 List<Object> params = new ArrayList<>();
 params.add("param1");
 params.add("param2");
 String expectedMessage = "Message key: message.key, param1: param1, param2: param2";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 Assert.assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageWithEmptyHashtable() {
 messageHandler = new MessageHandler(BASENAME);
 String key = "message.key";
 List<Object> params = null;
 String expectedMessage = null;
 String actualMessage = messageHandler.getMessageOptional(key, params);
 Assert.assertNull(actualMessage);
 }

}