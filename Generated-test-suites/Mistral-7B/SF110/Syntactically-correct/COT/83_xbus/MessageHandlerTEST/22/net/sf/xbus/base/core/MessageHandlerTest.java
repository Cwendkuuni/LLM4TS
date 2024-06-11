package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.XStringSupport;
import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {

 private static MessageHandler instance;
 private static final String basename = "MessageHandlerTest";

 @Test
 public void testGetInstance() {
     instance = MessageHandler.getInstance(basename);
     assertNotNull(instance);
 }

 @Test
 public void testGetMessageNullKey() {
     instance = MessageHandler.getInstance(basename);
     List params = null;
     String result = instance.getMessage("nullKey", params);
     assertEquals("Key: nullKey not found in message file", result);
 }

 @Test
 public void testGetMessageNullParams() {
     instance = MessageHandler.getInstance(basename);
     String key = "testKey";
     String result = instance.getMessage(key, null);
     assertNotNull(result);
 }

 @Test
 public void testGetMessageSimple() {
     instance = MessageHandler.getInstance(basename);
     List params = Arrays.asList("testParam");
     String key = "testKey";
     String result = instance.getMessage(key, params);
     String expected = "testKey: $1$";
     String replacedResult = XStringSupport.replaceAll(result, "$1$", "testParam");
     assertEquals(expected, replacedResult);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageNullParamsObject() {
     instance = MessageHandler.getInstance(basename);
     List params = null;
     String key = "testKey";
     String result = instance.getMessage(key, params);
     assertNull(result);
     params.add(null);
 }

 @Test
 public void testGetMessageOptionalNullKey() {
     instance = MessageHandler.getInstance(basename);
     List params = Arrays.asList("testParam1", "testParam2");
     String result = instance.getMessageOptional("nullKey", params);
     assertNull(result);
 }

 @Test
 public void testGetMessageOptionalNullParams() {
     instance = MessageHandler.getInstance(basename);
     String key = "testKeyOptional";
     String result = instance.getMessageOptional(key, null);
     assertNotNull(result);
 }

 @Test
 public void testGetMessageOptionalEmptyParams() {
     instance = MessageHandler.getInstance(basename);
     String key = "testKeyOptional";
     String result = instance.getMessageOptional(key, Collections.emptyList());
     assertNotNull(result);
 }

 @Test
 public void testGetMessageOptionalParamsReplaced() {
     instance = MessageHandler.getInstance(basename);
     String key = "testKeyOptional";
     List params = Arrays.asList("testParam1", "testParam2");
     String result = instance.getMessageOptional(key, params);
     assertNotNull(result);
     String expected = "testKeyOptional: $1$: $2$";
     String replacedResult = XStringSupport.replaceAll(result, "$1$", "testParam1")
             + XStringSupport.replaceAll(result, "$2$", "testParam2");
     assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptionalNullParamsIterator() {
     instance = MessageHandler.getInstance(basename);
     String key = "testKeyOptional";
     List params = new ArrayList();
     params.add(null);
     String result = instance.getMessageOptional(key, params);
     assertNull(result);
 }

 @Test
 public void testAddMessages() {
     instance = MessageHandler.getInstance(basename);
     instance.addMessages(basename, "testDir");
     String key = "testKeyAddMessage";
     List params = Arrays.asList("testParamAddMessage1", "testParamAddMessage2");
     String expected = "testKeyAddMessage: $1$: $2$";
     instance.mMessages.put(key, expected);
     String result = instance.getMessage(key, params);
     assertEquals(expected, result);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessagesFileNotFound() {
     instance = MessageHandler.getInstance(basename);
     instance.addMessages(basename, "testDirNotFound");
 }

 @Test(expected = IOException.class)
 public void testAddMessagesIOException() {
     instance = MessageHandler.getInstance(basename);
     instance.addMessages(basename, "testDirIOException");
 }

}