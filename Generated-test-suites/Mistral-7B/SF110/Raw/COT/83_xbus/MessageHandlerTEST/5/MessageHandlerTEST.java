package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

 private static final String BASENAME = "testMessageHandler"; // for getInstance test
 private static final String MESSAGE_KEY = "test.message.key";
 private static final String MESSAGE_VALUE = "Test message value";
 private static final String PARAMETER_VALUE_1 = "Parameter value 1";
 private static final String PARAMETER_VALUE_2 = "Parameter value 2";

 @BeforeClass
 public static void setupClass() {
 MessageHandler.getInstance(BASENAME).addMessages(BASENAME, Constants.XBUS_ETC);
 MessageHandler.getInstance(BASENAME).addMessages(BASENAME, Constants.XBUS_PLUGIN_ETC);
 }

 @AfterClass
 public static void tearDownClass() {
 MessageHandler.mInstances.clear();
 }

 @Before
 public void setup() {
 List<Object> params = Arrays.asList(PARAMETER_VALUE_1, PARAMETER_VALUE_2);
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance(BASENAME);
 MessageHandler handler2 = MessageHandler.getInstance(BASENAME);
 assertEquals(handler1, handler2);
 }

 @Test
 public void testGetMessageNullKey() {
 String result = MessageHandler.getInstance(BASENAME).getMessage(null, null);
 assertNull(result);
 }

 @Test
 public void testGetMessageNullParams() {
 String result = MessageHandler.getInstance(BASENAME).getMessage(MESSAGE_KEY, null);
 assertEquals(MESSAGE_VALUE, result);
 }

 @Test
 public void testGetMessageWithParams() {
 String result = MessageHandler.getInstance(BASENAME).getMessage(MESSAGE_KEY, params);
 String expected = XStringSupport.replaceAll(MESSAGE_VALUE, "$1$", PARAMETER_VALUE_1);
 expected = XStringSupport.replaceAll(expected, "$2$", PARAMETER_VALUE_2);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParamsList() {
 MessageHandler.getInstance(BASENAME).getMessage(MESSAGE_KEY, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageEmptyParamsList() {
 MessageHandler.getInstance(BASENAME).getMessage(MESSAGE_KEY, Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMessageNullMessageMap() {
 MessageHandler handler = new MessageHandler(BASENAME) {
 @Override
 protected void addMessages(String basename, String dir) {
 // empty implementation
 }
 };
 String result = handler.getMessage(MESSAGE_KEY, params);
 assertNull(result);
 }

 @Test
 public void testGetMessageOptionalNullKey() {
 String result = MessageHandler.getInstance(BASENAME).getMessageOptional(null, null);
 assertNull(result);
 }

 @Test
 public void testGetMessageOptionalNullParams() {
 String result = MessageHandler.getInstance(BASENAME).getMessageOptional(MESSAGE_KEY, null);
 assertEquals(MESSAGE_VALUE, result);
 }

 @Test
 public void testGetMessageOptionalNullMessageMap() {
 MessageHandler handler = new MessageHandler(BASENAME) {
 @Override
 protected void addMessages(String basename, String dir) {
 // empty implementation
 }
 };
 String result = handler.getMessageOptional(MESSAGE_KEY, params);
 assertNull(result);
 }

 @Test
 public void testGetMessageOptionalEmptyMessageMap() {
 MessageHandler handler = new MessageHandler(BASENAME);
 String result = handler.getMessageOptional(MESSAGE_KEY, params);
 assertNull(result);
 }

 @Test
 public void testGetMessageOptionalMissingKey() {
 MessageHandler handler = MessageHandler.getInstance(BASENAME);
 String result = handler.getMessageOptional("missing.key", params);
 assertNull(result);
 }
}