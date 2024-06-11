package net.sf.xbus.base.core.test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import junit.framework.TestCase;

public class MessageHandlerTest extends TestCase {

 private MessageHandler messageHandler;

 @Before
 public void setUp() {
 messageHandler = MessageHandler.getInstance("test");
 }

 // Test for getInstance method
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance("test");
 MessageHandler instance2 = MessageHandler.getInstance("test");
 assertEquals(instance1, instance2);
 }

 // Test for getMessage method with valid key and params
 public void testGetMessageValid() {
 List params = new ArrayList();
 params.add("TestParam1");
 params.add("TestParam2");
 String expectedMessage = "This is a message with two placeholders: $1$ and $2$";
 String actualMessage = messageHandler.getMessage("testKey", params);
 assertEquals(expectedMessage, actualMessage);
 }

 // Test for getMessage method with valid key and no params
 public void testGetMessageNoParams() {
 String expectedMessage = "This is a message without placeholders";
 String actualMessage = messageHandler.getMessage("testKeyNoParams", null);
 assertEquals(expectedMessage, actualMessage);
 }

 // Test for getMessage method with invalid key
 public void testGetMessageInvalidKey() {
 List params = new ArrayList();
 params.add("TestParam1");
 params.add("TestParam2");
 String actualMessage = messageHandler.getMessage("invalidKey", params);
 assertNull(actualMessage);
 }

 // Test for getMessageOptional method with valid key and params
 public void testGetMessageOptionalValid() {
 List params = new ArrayList();
 params.add("TestParam1");
 params.add("TestParam2");
 String expectedMessage = "This is a message with two placeholders: TestParam1 and TestParam2";
 String actualMessage = messageHandler.getMessageOptional("testKeyOptional", params);
 assertEquals(expectedMessage, actualMessage);
 }

 // Test for getMessageOptional method with valid key and no params
 public void testGetMessageOptionalNoParams() {
 String expectedMessage = "This is a message without placeholders";
 String actualMessage = messageHandler.getMessageOptional("testKeyOptionalNoParams", null);
 assertEquals(expectedMessage, actualMessage);
 }

 // Test for getMessageOptional method with invalid key
 public void testGetMessageOptionalInvalidKey() {
 String actualMessage = messageHandler.getMessageOptional("invalidKey", null);
 assertNull(actualMessage);
 }

 // Test for getMessageOptional method with null params
 public void testGetMessageOptionalNullParams() {
 String actualMessage = messageHandler.getMessageOptional("testKeyOptionalNullParams", null);
 assertNull(actualMessage);
 }

 // Test for addMessages method with valid basename and dir
 public void testAddMessagesValid() {
 File dir = new File("testDir");
 MessageHandler messageHandler2 = new MessageHandler("test2");
 messageHandler2.addMessages("test2", dir.getAbsolutePath());
 String expectedMessage1 = "Message1_ENGLISH";
 String expectedMessage2 = "Message2_ENGLISH";
 String actualMessage1 = messageHandler2.getMessage("MessageKey1", null);
 String actualMessage2 = messageHandler2.getMessage("MessageKey2", null);
 assertEquals(expectedMessage1, actualMessage1);
 assertEquals(expectedMessage2, actualMessage2);
 }

 // Test for addMessages method with invalid dir
 public void testAddMessagesInvalidDir() {
 MessageHandler messageHandler2 = new MessageHandler("test2");
 try {
 messageHandler2.addMessages("test2", null);
 fail("Expected FileNotFoundException");
 } catch (Exception e) {
 assertTrue(e instanceof FileNotFoundException);
 }
 }

 // Test for addMessages method with empty messages file
 public void testAddMessagesEmptyFile() {
 MessageHandler messageHandler2 = new MessageHandler("test2");
 File dir = new File("testDirEmptyFile");
 try {
 messageHandler2.addMessages("test2", dir.getAbsolutePath());
 fail("Expected IOException");
 } catch (Exception e) {
 assertTrue(e instanceof IOException);
 }
 }

 // Test for addMessages method with invalid messages file format
 public void testAddMessagesInvalidFormat() {
 MessageHandler messageHandler2 = new MessageHandler("test2");
 File dir = new File("testDirInvalidFormat");
 try {
 messageHandler2.addMessages("test2", dir.getAbsolutePath());
 fail("Expected IOException");
 } catch (Exception e) {
 assertTrue(e instanceof IOException);
 }
 }
}