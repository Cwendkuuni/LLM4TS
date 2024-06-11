package net.sf.xbus.base.core.test;

import org.junit.Test;
import org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
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

 private MessageHandler messageHandler;

 @Before
 public void setUp() {
 messageHandler = MessageHandler.getInstance("test");
 }

 @Test
 public void testGetMessage() {
 String key = "testKey";
 List params = new ArrayList();
 String expectedMessage = "Test key message";
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "testKey";
 List params = null;
 String expectedMessage = null;
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptionalWithParams() {
 String key = "testKey";
 List params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 String expectedMessage = "Test key message ${1} ${2}";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 String replacedMessage = XStringSupport.replaceAll(expectedMessage, "${1}", "testParam1");
 replacedMessage = XStringSupport.replaceAll(replacedMessage, "${2}", "testParam2");
 assertEquals(replacedMessage, actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullKey() {
 List params = new ArrayList();
 messageHandler.getMessage(null, params);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageWithNullParams() {
 String key = "testKey";
 messageHandler.getMessage(key, null);
 }

 @Test
 public void testGetMessageInstance() {
 String basename = "test";
 MessageHandler instance1 = MessageHandler.getInstance(basename);
 MessageHandler instance2 = MessageHandler.getInstance(basename);
 assertEquals(instance1, instance2);
 }

 @Test
 public void testAddMessages() {
 String basename = "test";
 messageHandler = new MessageHandler(basename);
 File dir = new File("src/test/resources/messages");
 String[] messagesFiles = dir.list(new MessagesFilter("test", "_ENGLISH.properties"));
 for (int i = 0; i < messagesFiles.length; ++i) {
 Properties newProps = new Properties();
 try {
 FileInputStream instream = new FileInputStream(dir + messagesFiles[i]);
 newProps.load(instream);
 instream.close();
 }
 catch (FileNotFoundException e) {
 fail("Cannot find messagefile " + messagesFiles[i]);
 }
 catch (IOException e2) {
 fail("Cannot read messagefile " + messagesFiles[i]);
 }
 String key = null;
 Enumeration keys = newProps.keys();
 while (keys.hasMoreElements()) {
 key = keys.nextElement().toString();
 messageHandler.mMessages.put(key, newProps.get(key));
 }
 }
 String testMessage = (String) messageHandler.mMessages.get("testMessage");
 assertNotNull(testMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMessagesWithNullBasename() {
 messageHandler.addMessages(null, "messages");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMessagesWithNullDir() {
 messageHandler.addMessages("test", null);
 }

 @Test
 public void testAddMessagesWithEmptyDir() {
 messageHandler = new MessageHandler("test");
 File dir = new File("src/test/resources/messages");
 String[] messagesFiles = dir.listFiles();
 messageHandler.addMessages("test", dir.getAbsolutePath());
 assertEquals(0, messageHandler.mMessages.size());
 }

 @Test
 public void testAddMessagesWithInvalidFilter() {
 messageHandler = new MessageHandler("test");
 File dir = new File("src/test/resources/messages");
 String[] messagesFiles = dir.list(new MessagesFilter("test", "_INVALID.properties"));
 messageHandler.addMessages("test", dir.getAbsolutePath());
 assertEquals(0, messageHandler.mMessages.size());
 }

 @Test
 public void testAddMessagesWithValidFiles() {
 messageHandler = new MessageHandler("test");
 File dir = new File("src/test/resources/messages");
 String[] messagesFiles = dir.list(new MessagesFilter("test", "_ENGLISH.properties"));
 messageHandler.addMessages("test", dir.getAbsolutePath());
 String key = "testMessage";
 String expectedMessage = "Test message";
 String actualMessage = (String) messageHandler.mMessages.get(key);
 assertEquals(expectedMessage, actualMessage);
 }
}