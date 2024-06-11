package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.trace.*;
import net.sf.xbus.base.core.strings.*;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String BASENAME = "testMessageHandler";

 @BeforeClass
 public static void setUpBeforeClass() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 }

 @Test
 public void testGetInstance() {
 MessageHandler messageHandler2 = MessageHandler.getInstance(BASENAME);
 assertEquals("MessageHandler instances should be the same", messageHandler, messageHandler2);
 }

 @Test
 public void testGetMessage() {
 String key = "testKey";
 List params = new ArrayList();
 params.add("testParam");
 String expectedMessage = "Key: testKey not found in message file TestParam";
 String resultMessage = messageHandler.getMessage(key, params);
 assertEquals("getMessage should return expected message", expectedMessage, resultMessage);

 key = "existingKey";
 params = null;
 String expectedMessage2 = "existingKey";
 String resultMessage2 = messageHandler.getMessage(key, params);
 assertEquals("getMessage should return expected message for existing key", expectedMessage2, resultMessage2);
 }

 @Test
 public void testGetMessageOptional() {
 String key = "testKeyWithParams";
 List params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 String expectedMessage = "${1} ${2}";
 String resultMessage = messageHandler.getMessageOptional(key, params);
 assertEquals("getMessageOptional should replace params in message", expectedMessage, resultMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptionalNullParams() {
 String key = "testKeyWithParams";
 String expectedMessage = "${1} ${2}";
 String resultMessage = messageHandler.getMessageOptional(key, null);
 fail("getMessageOptional should throw NullPointerException for null params");
 }

 @Test
 public void testGetMessageOptionalNoParams() {
 String key = "testKeyNoParams";
 String expectedMessage = "testKeyNoParams";
 String resultMessage = messageHandler.getMessageOptional(key, null);
 assertEquals("getMessageOptional should return message without params for no params", expectedMessage, resultMessage);
 }

 @Test
 public void testAddMessages() throws IOException {
 messageHandler = new MessageHandler(BASENAME);
 String dir = System.getProperty("user.dir");
 File dirFile = new File(dir);
 String testPropertiesFileName = "MessageHandlerTestMessages.properties";
 File testPropertiesFile = new File(dirFile, testPropertiesFileName);
 Properties testProps = new Properties();
 testProps.setProperty("testKey", "testMessage");
 try (FileOutputStream testOutputStream = new FileOutputStream(testPropertiesFile)) {
 testProps.store(testOutputStream, null);
 }
 messageHandler.addMessages(BASENAME, ".");
 String resultMessage = messageHandler.getMessageOptional("testKey", null);
 assertEquals("getMessageOptional should return message from added properties file", "testMessage", resultMessage);
 }

}