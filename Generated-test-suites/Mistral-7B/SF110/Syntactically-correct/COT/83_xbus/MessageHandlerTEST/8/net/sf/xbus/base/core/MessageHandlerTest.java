package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;
import org.junit.*;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private static final String TEST_BASENAME = "testMessageHandler";

 @BeforeClass
 public static void setupClass() {
 MessageHandler.mInstances = new Hashtable<>();
 MessageHandler.classLock = Configuration.class;
 }

 @AfterClass
 public static void tearDownClass() {
 MessageHandler.mInstances.clear();
 }

 @Before
 public void setUp() {
 messageHandler = MessageHandler.getInstance(TEST_BASENAME);
 messageHandler.addMessages(TEST_BASENAME, Constants.XBUS_ETC);
 messageHandler.addMessages(TEST_BASENAME, Constants.XBUS_PLUGIN_ETC);
 }

 @After
 public void tearDown() {
 messageHandler = null;
 }

 @Test
 public void testGetMessage_KeyFoundAndParamsNull() {
 String key = "testKey";
 String expectedMessage = "Key: testKey not found in message file";
 String resultMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testGetMessage_KeyFoundAndParamsPresent() {
 String key = "testKey";
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");
 String expectedMessage = "Test param1 Test param2";
 String resultMessage = messageHandler.getMessage(key, params);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testGetMessage_KeyNotFound() {
 String key = "testKeyNotFound";
 String expectedMessage = null;
 String resultMessage = messageHandler.getMessage(key, null);
 assertNull(resultMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessage_NullKey() {
 String key = null;
 List<Object> params = new ArrayList<>();
 messageHandler.getMessage(key, params);
 }

 @Test
 public void testGetMessageOptional_KeyFoundAndParamsNull() {
 String key = "testKey";
 String expectedMessage = null;
 String resultMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testGetMessageOptional_KeyFoundAndParamsPresent() {
 String key = "testKey";
 List<Object> params = new ArrayList<>();
 params.add("testParam1");
 params.add("testParam2");
 String expectedMessage = "Test param1 Test param2";
 String resultMessage = messageHandler.getMessageOptional(key, params);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test
 public void testGetMessageOptional_KeyNotFound() {
 String key = "testKeyNotFound";
 String expectedMessage = null;
 String resultMessage = messageHandler.getMessageOptional(key, null);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptional_NullKey() {
 String key = null;
 List<Object> params = new ArrayList<>();
 messageHandler.getMessageOptional(key, params);
 }

 @Test
 public void testGetInstance_SameBasename() {
 MessageHandler instance1 = MessageHandler.getInstance(TEST_BASENAME);
 MessageHandler instance2 = MessageHandler.getInstance(TEST_BASENAME);
 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetInstance_DifferentBasenames() {
 MessageHandler instance1 = MessageHandler.getInstance(TEST_BASENAME);
 MessageHandler instance2 = MessageHandler.getInstance("testDifferentBasename");
 assertNotEquals(instance1, instance2);
 }

 @Test
 public void testAddMessages_ValidFile() {
 String key = "testAddMessages";
 String expectedMessage = "Test message";
 messageHandler.addMessages(TEST_BASENAME, Constants.XBUS_ETC);
 messageHandler.addMessages(TEST_BASENAME, Constants.XBUS_PLUGIN_ETC);
 messageHandler.mMessages.put(key, expectedMessage);
 String resultMessage = messageHandler.getMessage(key, null);
 assertEquals(expectedMessage, resultMessage);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessages_InvalidFile() {
 String key = "testAddMessagesInvalidFile";
 messageHandler.addMessages(TEST_BASENAME, "nonExistentDirectory");
 }

 @Test(expected = IOException.class)
 public void testAddMessages_IOError() {
 String key = "testAddMessagesIOError";
 messageHandler.addMessages(TEST_BASENAME, Constants.XBUS_PLUGIN_ETC);
 final DataInputStream dis = new DataInputStream(new ByteArrayInputStream("Error".getBytes()));
 try {
 messageHandler.addMessages(TEST_BASENAME, Constants.XBUS_PLUGIN_ETC);
 } catch (FileNotFoundException e) {
 // expected
 } finally {
 try {
 dis.close();
 } catch (IOException e2) {
 // expected
 }
 }
 }

}