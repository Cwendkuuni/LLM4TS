package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import net.sf.xbus.base.core.config.*;
import net.sf.xbus.base.core.strings.*;
import org.junit.Before;
import org.junit.Test;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private Properties testProperties;

 @Before
 public void setup() {
 messageHandler = new MessageHandler("test");
 testProperties = new Properties();
 }

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance("test");
 MessageHandler instance2 = MessageHandler.getInstance("test");
 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetMessageOptional_EmptyParams() {
 String message = messageHandler.getMessageOptional("test.message.key", null);
 assertNull(message);
 }

 @Test
 public void testGetMessageOptional_ParamsPresent() {
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");

 String message = messageHandler.getMessageOptional("test.message.key", params);
 String expectedMessage = "test.message.key$1$testParam1$2$testParam2";
 String actualMessage = XStringSupport.replaceAll(message, "\\$\\d+\\$", arg -> args.get(Integer.parseInt(arg.substring(1))).toString());
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testAddMessages() throws IOException {
 String baseName = "test";
 String filePath = "testDirectory/";

 File directory = new File(filePath);
 File testFile = new File(filePath, baseName + "_en_US.properties");
 testFile.createNewFile();
 try (FileOutputStream output = new FileOutputStream(testFile)) {
 testProperties.setProperty("test.message.key1", "Test Message Key 1");
 testProperties.store(output, "Test Properties");
 }

 messageHandler = MessageHandler.getInstance(baseName);
 assertEquals("Test Message Key 1", messageHandler.getMessageOptional("test.message.key1", null));

 directory.deleteOnExit();
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullParams() {
 List<Object> nullParams = null;
 messageHandler.getMessageOptional("test.message.key", nullParams);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageOptional_InvalidKey() {
 messageHandler.getMessageOptional("test.invalid.key", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMessage_MissingMessages() {
 messageHandler.mMessages = null;
 assertThrows(IllegalStateException.class, () -> messageHandler.getMessage("test.message.key", null));
 }

 @Test
 public void testGetMessage_ValidKey() {
 String key = "test.message.key";
 String message = messageHandler.getMessage(key, null);
 String expectedMessage = "test.message.key";
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessage_ValidKey_ParamsPresent() {
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");

 String key = "test.message.key";
 String message = messageHandler.getMessage(key, params);
 String expectedMessage = "test.message.key$1$testParam1$2$testParam2";
 String actualMessage = XStringSupport.replaceAll(message, "\\$\\d+\\$", arg -> args.get(Integer.parseInt(arg.substring(1))).toString());
 assertEquals(expectedMessage, actualMessage);
 }
}