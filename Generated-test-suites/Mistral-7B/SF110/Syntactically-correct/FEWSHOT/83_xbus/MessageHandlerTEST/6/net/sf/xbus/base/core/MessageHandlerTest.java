package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import net.sf.xbus.base.core.config.*;
import net.sf.xbus.base.core.strings.*;
import net.sf.xbus.base.core.trace.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MessageHandlerTest {

 private MessageHandler messageHandler;
 private List params;
 private String key;
 private Properties properties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 messageHandler = new MessageHandler("test");
 params = new ArrayList();
 params.add("testParam1");
 params.add("testParam2");
 key = "testKey";
 properties = new Properties();
 }

 @Test
 public void testGetInstance() {
 MessageHandler handler1 = MessageHandler.getInstance("test");
 MessageHandler handler2 = MessageHandler.getInstance("test");
 assertEquals(handler1, handler2);
 }

 @Test
 public void testGetMessage() {
 String message = messageHandler.getMessage(key, params);
 assertNull(message);
 messageHandler.addMessages("test", "test.properties");
 message = messageHandler.getMessage(key, params);
 assertNotNull(message);
 assertEquals("Key: " + key + " not found in message file", message);
 }

 @Test
 public void testGetMessageOptional() {
 String message = messageHandler.getMessageOptional(key, params);
 assertNull(message);
 properties.put(key, "Test Message: {$1} and {$2}");
 messageHandler.addMessages("test", "test.properties", properties);
 message = messageHandler.getMessageOptional(key, params);
 assertEquals("Test Message: testParam1 and testParam2", message);
 }

 @Test
 public void testGetMessageOptionalNullParams() {
 String message = messageHandler.getMessageOptional(key, null);
 assertNull(message);
 message = messageHandler.getMessage("key", null);
 assertNull(message);
 }

 @Test
 public void testAddMessages() throws FileNotFoundException, IOException {
 File dir = mock(File.class);
 when(dir.list(any(FilenameFilter.class))).thenReturn(new String[]{"test.properties"});
 MessageHandler handler = new MessageHandler("test");
 handler.addMessages("test", dir);
 verify(dir, times(1)).list(any(FilenameFilter.class));
 }

}