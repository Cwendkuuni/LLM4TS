package net.sf.xbus.base.core.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

    private static final String BASE_NAME = "test";
    private static final String DIR = "src/test/resources/net/sf/xbus/base/core/messages/";

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance(BASE_NAME);
        MessageHandler handler2 = MessageHandler.getInstance(BASE_NAME);

        assert (handler1 == handler2);
    }

    @Test
    public void testGetMessage() {
        MessageHandler handler = MessageHandler.getInstance(BASE_NAME);

        List params = new ArrayList();
        params.add("foo");

        String key1 = "message1";
        String expected1 = "This is message 1: $1$";
        String result1 = handler.getMessage(key1, params);
        assertEquals(expected1, result1);

        String key2 = "message2";
        String expected2 = "This is message 2: $1$";
        String result2 = handler.getMessage(key2, params);
        assertEquals(expected2, result2);

        String key3 = "message3";
        String expected3 = "Key: " + key3 + " not found in message file";
        String result3 = handler.getMessage(key3, params);
        assertEquals(expected3, result3);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance(BASE_NAME);

        String key1 = "message1";
        String expected1 = "This is message 1: $1$";
        String result1 = handler.getMessageOptional(key1, null);
        assertEquals(expected1, result1);

        List params1 = new ArrayList();
        params1.add("foo");
        String result1_params = handler.getMessageOptional(key1, params1);
        assertEquals(expected1, result1_params);

        String key2 = "message2";
        String expected2 = null;
        String result2 = handler.getMessageOptional(key2, null);
        assertEquals(expected2, result2);

        String key3 = "message3";
        String expected3 = "Key: " + key3 + " not found in message file";
        String result3 = handler.getMessageOptional(key3, null);
        assertEquals(expected3, result3);
    }

    @Test
    public void testAddMessages() {
        MessageHandler handler = MessageHandler.getInstance(BASE_NAME);

        Properties properties1 = new Properties();
        properties1.setProperty("message1.1", "This is message 1.1: $1$");
        properties1.setProperty("message1.2", "This is message 1.2: $1$");

        handler.addMessages(BASE_NAME, DIR);

        String key1_1 = "message1.1";
        String expected1_1 = "This is message 1.1: $1$";
        String result1_1 = handler.getMessageOptional(key1_1, null);
        assertEquals(expected1_1, result1_1);

        String key1_2 = "message1.2";
        String expected1_2 = "This is message 1.2: $1$";
        String result1_2 = handler.getMessageOptional(key1_2, null);
        assertEquals(expected1_2, result1_2);

        Properties properties2 = new Properties();
        properties2.setProperty("message2.1", "This is message 2.1: $1$");
        properties2.setProperty("message2.2", "This is message 2.2: $1$");

        handler.addMessages(BASE_NAME, DIR);

        String key2_1 = "message2.1";
        String expected2_1 = "This is message 2.1: $1$";
        String result2_1 = handler.getMessageOptional(key2_1, null);
        assertEquals(expected2_1, result2_1);

        String key2_2 = "message2.2";
        String expected2_2 = "This is message 2.2: $1$";
        String result2_2 = handler.getMessageOptional(key2_2, null);
        assertEquals(expected2_2, result2_2);
    }

    @Test
    public void testMessagesFilter() {
        File dir = new File(DIR);
        String[] messagesFiles = dir.list(new MessageHandler.MessagesFilter(BASE_NAME, "_en_US.properties"));

        assert (messagesFiles != null);
        assert (messagesFiles.length > 0);
    }
}