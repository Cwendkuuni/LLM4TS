package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;
import java.util.*;

public class MessageHandlerTest {

 private static final String TEST_BASENAME = "testMessageHandler";
 private static MessageHandler messageHandler = null;

 @BeforeClass
 public static void setupClass() {
 messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 }

 @AfterClass
 public static void tearDownClass() {
 MessageHandler.mInstances.remove(TEST_BASENAME);
 }

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance(TEST_BASENAME);
 MessageHandler instance2 = MessageHandler.getInstance(TEST_BASENAME);
 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetMessageNullParams() {
 String message = messageHandler.getMessage("testKey", null);
 assertEquals("Key: testKey not found in message file", message);
 }

 @Test
 public void testGetMessageEmptyParams() {
 List params = new ArrayList();
 String message = messageHandler.getMessage("testKey", params);
 assertEquals("Key: testKey not found in message file", message);
 }

 @Test
 public void testGetMessageValidParams() {
 List params = Arrays.asList("param1", "param2");
 String message = messageHandler.getMessage("testKey", params);
 String expectedMessage = "Key: testKey $1$ $2$";
 String replacedMessage = XStringSupport.replaceAll(expectedMessage, "$1", "param1");
 replacedMessage = XStringSupport.replaceAll(replacedMessage, "$2", "param2");
 assertEquals(replacedMessage, message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullKey() {
 messageHandler.getMessage(null, new ArrayList());
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParamsList() {
 messageHandler.getMessage("testKey", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageEmptyMessageFile() {
 messageHandler = new MessageHandler(TEST_BASENAME);
 String message = messageHandler.getMessage("testKey", new ArrayList());
 assertNull(message);
 }

 @Test
 public void testGetMessageMissingMessage() {
 messageHandler = new MessageHandler(TEST_BASENAME);
 String message = messageHandler.getMessage("testMissingKey", new ArrayList());
 assertEquals("Key: testMissingKey not found in message file", message);
 }
}