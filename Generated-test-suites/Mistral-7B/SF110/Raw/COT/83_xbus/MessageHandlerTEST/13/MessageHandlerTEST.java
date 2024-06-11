package net.sf.xbus.base.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String BASENAME = "testMessageHandler";

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance(BASENAME);
 MessageHandler instance2 = MessageHandler.getInstance(BASENAME);
 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetMessage_KeyFound() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 List params = new ArrayList();
 String key = "testKey";
 String expectedMessage = "This is a test message";
 params.add("testParam1");
 params.add("testParam2");
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessage_KeyNotFound() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "invalidKey";
 String expectedMessage = "Key: invalidKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional_NoParams() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "testKey";
 String expectedMessage = "This is a test message";
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testGetMessageOptional_ParamsPresent() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "testKeyParam";
 List params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 String expectedMessage = "This is a test message with testParam1 and testParam2";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullParams() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "testKeyParam";
 String actualMessage = messageHandler.getMessageOptional(key, null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageOptional_EmptyParams() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String key = "testKeyParam";
 List params = new ArrayList();
 params.clear();
 String actualMessage = messageHandler.getMessageOptional(key, params);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testAddMessages() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 String basename = BASENAME;
 String dir = "testMessageFiles";
 File dirFile = new File(dir);
 String[] messagesFiles = {"testMessageFile1.properties", "testMessageFile2.properties"};
 for (String fileName : messagesFiles) {
 File messageFile = new File(dir, fileName);
 Properties newProps = new Properties();
 try {
 FileInputStream instream = new FileInputStream(messageFile);
 newProps.load(instream);
 instream.close();
 } catch (FileNotFoundException e) {
 System.out.println("Cannot find messagefile");
 System.exit(1);
 } catch (IOException e2) {
 System.out.println("Cannot find messagefile");
 System.exit(1);
 }
 String key = null;
 Enumeration keys = newProps.keys();
 while (keys.hasMoreElements()) {
 key = keys.nextElement().toString();
 messageHandler.mMessages.put(key, ((Hashtable<K, Object>)newProps).get(key));
 }
 }
 String testKey = "testKey";
 String testMessage = "This is a test message";
 messageHandler.mMessages.put(testKey, testMessage);
 String actualMessage = messageHandler.getMessageOptional(testKey, null);
 assertEquals(testMessage, actualMessage);
 }

}