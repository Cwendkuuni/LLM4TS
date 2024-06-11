package net.sf.xbus.base.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.util.*;

public class MessageHandlerTest {
    @Test
    public void testGetMessage_ValidKey_EmptyParams() {
        String key = "TestKey";
        List params = new ArrayList<>();
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = handler.getMessage(key, params);
        String actualMessage = "Key: TestKey not found in message file";
        Trace.error = (message) -> {}; // replace with proper implementation or disable it
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_ValidKey_NonEmptyParams() {
        String key = "TestKey";
        List<Object> params = Arrays.asList("Param1", "Param2");
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = "TestMessage ${1} ${2}";
        String actualMessage = handler.getMessageOptional(key, params);
        String paramText1 = "Param1";
        String paramText2 = "Param2";
        String paramCounter1 = "$1$";
        String paramCounter2 = "$2$";
        String actualMessageWithParams = XStringSupport.replaceAll(actualMessage, paramCounter1, paramText1)
                + " " + XStringSupport.replaceAll(actualMessage, paramCounter2, paramText2);
        assertEquals(expectedMessage, actualMessageWithParams);
    }
    
    @Test
    public void testGetInstance_SameBasename() {
        String basename = "Test";
        MessageHandler handler1 = MessageHandler.getInstance(basename);
        MessageHandler handler2 = MessageHandler.getInstance(basename);
        assertEquals(handler1, handler2);
    }
    
    @Test
    public void testGetMessage_InvalidKey_EmptyParams() {
        String key = "InvalidKey";
        List params = new ArrayList<>();
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = null;
        String actualMessage = handler.getMessage(key, params);
        assertNull(actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_ValidKey_EmptyParams() {
        String key = "TestKey";
        List params = new ArrayList<>();
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = null;
        String actualMessage = handler.getMessageOptional(key, params);
        assertNull(actualMessage);
    }
    
    @Test
    public void testGetMessage_ValidKey_NonEmptyParams_NullParam() {
        String key = "TestKey";
        List<Object> params = Arrays.asList("Param1", null);
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = "Key: TestKey not found in message file";
        String actualMessage = handler.getMessage(key, params);
        Trace.error = (message) -> {}; // replace with proper implementation or disable it
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_ValidKey_NonEmptyParams() {
        String key = "TestKey";
        List<Object> params = Arrays.asList("Param1", "Param2");
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = "TestMessage ${1} ${2}";
        String actualMessage = handler.getMessageOptional(key, params);
        String paramText1 = "Param1";
        String paramText2 = "Param2";
        String paramCounter1 = "$1$";
        String paramCounter2 = "$2$";
        String actualMessageWithParams = XStringSupport.replaceAll(actualMessage, paramCounter1, paramText1)
                + " " + XStringSupport.replaceAll(actualMessage, paramCounter2, paramText2);
        assertEquals(expectedMessage, actualMessageWithParams);
    }
    
    @Test
    public void testGetMessage_NullKey_EmptyParams() {
        String key = null;
        List params = new ArrayList<>();
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = "Key: null not found in message file";
        String actualMessage = handler.getMessage(key, params);
        Trace.error = (message) -> {}; // replace with proper implementation or disable it
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testGetMessageOptional_NullKey_EmptyParams() {
        String key = null;
        List params = new ArrayList<>();
        MessageHandler handler = MessageHandler.getInstance("Test");
        String expectedMessage = null;
        String actualMessage = handler.getMessageOptional(key, params);
        assertNull(actualMessage);
    }
    
    @Test
    public void testGetInstance_DifferentBasename() {
        String basename1 = "Test1";
        String basename2 = "Test2";
        MessageHandler handler1 = MessageHandler.getInstance(basename1);
        MessageHandler handler2 = MessageHandler.getInstance(basename2);
        assertNotEquals(handler1, handler2);
    }
}