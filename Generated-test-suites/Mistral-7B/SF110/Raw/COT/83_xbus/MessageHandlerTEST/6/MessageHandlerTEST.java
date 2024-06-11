package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;

import org.junit.*;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String TEST_BASENAME = "TestMessageHandler";
 private static final String TEST_DIR = "testMessages";

 @BeforeClass
 public static void setupClass() {
 messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance(TEST_BASENAME);
 MessageHandler handler2 = MessageHandler.getInstance(TEST_BASENAME);
 assertEquals(handler1, handler2);
 }

 @Test
 public void testGetMessageNullKey() {
 String result = messageHandler.getMessage(null, null);
 assertNull(result);
 }

 @Test
 public void testGetMessageEmptyList() {
 String result = messageHandler.getMessage("testKey", Collections.emptyList());
 assertNotNull(result);
 }

 @Test
 public void testGetMessageValidParams() {
 List params = new ArrayList();
 params.add("testParam1");
 params.add(123);
 String result = messageHandler.getMessage("testKey$0$testParam1$1$${1}", params);
 String expected = "testKey testParam1 123";
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParams() {
 messageHandler.getMessage("testKey", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageInvalidKey() {
 messageHandler.getMessage(null, Collections.emptyList());
 }

 @Test
 public void testGetMessageOptionalNullKey() {
 String result = messageHandler.getMessageOptional("testKey", null);
 assertNull(result);
 }

 @Test
 public void testGetMessageOptionalEmptyTable() {
 messageHandler.mMessages = null;
 String result = messageHandler.getMessageOptional("testKey", null);
 assertNull(result);
 }

 @Test
 public void testGetMessageOptionalValidKey() {
 String result = messageHandler.getMessageOptional("testKey", null);
 String expected = "testKey";
 assertEquals(expected, result);
 }

 @Test
 public void testAddMessagesNullBasename() {
 messageHandler = new MessageHandler(null);
 assertNull(messageHandler.mMessages);
 }

 @Test
 public void testAddMessagesNullDir() {
 messageHandler = new MessageHandler(TEST_BASENAME);
 messageHandler.addMessages(null, TEST_DIR);
 assertEquals(0, messageHandler.mMessages.size());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessagesNonExistentFile() {
 messageHandler = new MessageHandler(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, "testNonExistentDir");
 }

 @Test(expected = IOException.class)
 public void testAddMessagesIOException() {
 messageHandler = new MessageHandler(TEST_BASENAME);
 File testFile = new File("testIOException.properties");
 testFile.createNewFile();
 try {
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
} catch (FileNotFoundException e) {
 throw new RuntimeException(e);
}
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 testFile.delete();
 }

 @Test
 public void testAddMessagesMultipleFiles() {
 messageHandler = new MessageHandler(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 int sizeBefore = messageHandler.mMessages.size();
 messageHandler.addMessages(TEST_BASENAME, TEST_DIR);
 int sizeAfter = messageHandler.mMessages.size();
 assertEquals(sizeBefore + 1, sizeAfter);
 }

}