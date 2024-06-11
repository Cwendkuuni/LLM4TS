package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String BASENAME = "test";

 @ org.junit.BeforeClass
 static
 public void setupClass() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = new Object();
 }

 @ org.junit.AfterClass
 static
 public void tearDownClass() {
 MessageHandler.mInstances.clear();
 }

 @ org.junit.Before
 public void setup() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, "src/test/resources/");
 }

 @ org.junit.Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance(BASENAME);
 MessageHandler handler2 = MessageHandler.getInstance(BASENAME);
 assertTrue(handler1 == handler2);
 }

 @ org.junit.Test
 public void testGetMessage_validKeyAndParams() {
 String key = "testKey";
 List<Object> params = new ArrayList<>();
 params.add("Test Param 1");
 params.add("Test Param 2");
 String expectedMessage = "Key: testKey {$1$}: Test Param 1 {$2$}: Test Param 2";
 String actualMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @ org.junit.Test
 public void testGetMessage_validKeyNoParams() {
 String key = "testKeyNoParams";
 String expectedMessage = "Key: testKeyNoParams";
 String actualMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @ org.junit.Test
 public void testGetMessage_invalidKey() {
 String key = "invalidKey";
 String expectedMessage = "Key: invalidKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @ org.junit.Test
 public void testGetMessageOptional_validKeyAndParams() {
 String key = "testKeyOptionalParams";
 List<Object> params = new ArrayList<>();
 params.add("Test Param 1");
 params.add("Test Param 2");
 String expectedMessage = "Key: testKeyOptionalParams {$1$}: Test Param 1 {$2$}: Test Param 2";
 String actualMessage = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessage, actualMessage);
 }

 @ org.junit.Test
 public void testGetMessageOptional_validKeyNoParams() {
 String key = "testKeyOptionalNoParams";
 String expectedMessage = "Key: testKeyOptionalNoParams";
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @ org.junit.Test
 public void testGetMessageOptional_invalidKey() {
 String key = "invalidKey";
 String expectedMessage = null;
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

 @ org.junit.Test(expected = NullPointerException.class)
 public void testGetMessage_nullParams() {
 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, null);
 assertNull(actualMessage);
 assertNotNull(expectedMessage);
 }

 @ org.junit.Test(expected = NullPointerException.class)
 public void testGetMessageOptional_nullKey() {
 String expectedMessage = null;
 String actualMessage = messageHandler.getMessageOptional(null, null);
 assertNull(actualMessage);
 assertNotNull(expectedMessage);
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetMessage_emptyListParams() {
 String key = "testKey";
 List<Object> emptyList = new ArrayList<>();
 String expectedMessage = "Key: testKey not found in message file";
 String actualMessage = messageHandler.getMessage(key, emptyList);
 assertNull(actualMessage);
 assertNotNull(expectedMessage);
 }

 @ org.junit.Test
 public void testAddMessages() {
 String testFile = "testProperties.properties";
 String expectedMessage = "Test Key: Test Message";
 messageHandler.addMessages(BASENAME, "src/test/resources/");
 Properties properties = new Properties();
 try {
 FileInputStream inputStream = new FileInputStream(new File("src/test/resources/" + testFile));
 properties.load(inputStream);
 inputStream.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 String key = "TestKey";
 String actualMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, actualMessage);
 }

}