package net.sf.xbus.base.core;

import static org.junit.Assert.*;

import java.util.*;
import net.sf.xbus.base.core.strings.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;
 private static final String BASENAME = "testMessageHandler";
 private static final String XBUS_ETC = "xbus_etc";
 private static final String XBUS_PLUGIN_ETC = "xbus_plugin_etc";
 private static final String KEY = "testKey";
 private static final List params = new ArrayList();
 private static final String MESSAGE_TEXT = "Test message text $1$ and $2$";

 @BeforeClass
 public static void setup() {
 messageHandler = MessageHandler.getInstance(BASENAME);
 messageHandler.addMessages(BASENAME, XBUS_ETC);
 messageHandler.addMessages(BASENAME, XBUS_PLUGIN_ETC);
 }

 @Test
 public void testgetMessage() {
 String expectedMessageText = MESSAGE_TEXT;
 String actualMessageText = messageHandler.getMessage(KEY, params);
 assertEquals("getMessage should return expected message text", expectedMessageText, actualMessageText);
 }

 @Test
 public void testgetMessageOptionalNullParams() {
 String actualMessageText = messageHandler.getMessageOptional(KEY, null);
 assertNull("getMessageOptional should return null when params is null", actualMessageText);
 }

 @Test
 public void testgetMessageOptionalEmptyParams() {
 String actualMessageText = messageHandler.getMessageOptional(KEY, new ArrayList());
 assertEquals("getMessageOptional should return message text without any params", MESSAGE_TEXT, actualMessageText);
 }

 @Test
 public void testgetMessageOptionalOneParam() {
 params.add("Test param");
 String actualMessageText = messageHandler.getMessageOptional(KEY, params);
 String expectedMessageText = MESSAGE_Text.replace("$1$", "Test param");
 assertEquals("getMessageOptional should replace $1$ with Test param", expectedMessageText, actualMessageText);
 }

 @Test
 public void testgetMessageOptionalTwoParams() {
 params.add("Test param1");
 params.add("Test param2");
 String actualMessageText = messageHandler.getMessageOptional(KEY, params);
 String expectedMessageText = MESSAGE_Text.replace("$1$", "Test param1").replace("$2$", "Test param2");
 assertEquals("getMessageOptional should replace $1$ with Test param1 and $2$ with Test param2", expectedMessageText, actualMessageText);
 }

 @Test(expected = NullPointerException.class)
 public void testgetMessageNullParams() {
 messageHandler.getMessage(KEY, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetMessageEmptyKey() {
 messageHandler.getMessage("", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetMessageNullMessage() {
 messageHandler.mMessages = null;
 messageHandler.getMessage(KEY, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testgetMessageKeyNotFound() {
 messageHandler.getMessage("NotFoundKey", params);
 }
}