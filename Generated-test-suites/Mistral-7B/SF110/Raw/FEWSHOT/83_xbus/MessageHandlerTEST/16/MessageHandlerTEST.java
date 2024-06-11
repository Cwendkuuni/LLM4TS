package net.sf.xbus.base.core.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test");
        MessageHandler handler2 = MessageHandler.getInstance("test");

        assertEquals(handler1, handler2);
    }

    @Test
    public void testGetMessages() {
        MessageHandler handler = MessageHandler.getInstance("test");
        HashMap<String, String> expectedMessages = new HashMap<>();
        expectedMessages.put("message1", "This is message 1");
        expectedMessages.put("message2", "This is message 2");

        handler.addMessages("test", "src/test/resources/messages");

        assertEquals(expectedMessages, handler.getMessages());
    }

    @Test
    public void testGetMessagesWithParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0}", "This is message {0}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");

        assertEquals("This is message param1", handler.getMessage("message{0}", params));
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");
        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0}", "This is message {0}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");

        assertEquals("This is message {0}", handler.getMessageOptional("message{0}", params));
        assertEquals(null, handler.getMessageOptional("non-existent-key", null));
    }

    @Test
    public void testGetMessageOptionalWithNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0}", "This is message {0}");

        handler.addMessages("test", "src/test/resources/messages");

        assertEquals(null, handler.getMessageOptional("message{0}", null));
    }

    @Test
    public void testAddMessages() throws IOException {
        MessageHandler handler = MessageHandler.getInstance("test");

        File dir = new File("src/test/resources/messages");
        String[] files = dir.list();

        handler.addMessages("test", "src/test/resources/messages");

        Set<String> keys = new HashSet<>();
        for (String file : files) {
            Properties props = new Properties();
            try (FileInputStream stream = new FileInputStream(new File("src/test/resources/messages/" + file))) {
                props.load(stream);
            }

            Enumeration<Object> keysEnum = props.keys();
            while (keysEnum.hasMoreElements()) {
                String key = keysEnum.nextElement().toString();
                keys.add(key);
            }
        }

        HashMap<String, String> expectedMessages = new HashMap<>();
        for (String key : keys) {
            expectedMessages.put(key, props.getProperty(key));
        }

        assertEquals(expectedMessages, handler.getMessages());
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetMessagesWithInvalidDirectory() {
        MessageHandler handler = MessageHandler.getInstance("test");

        handler.addMessages("test", "src/test/resources/invalid");
    }

    @Test(expected = IOException.class)
    public void testGetMessagesWithInvalidFile() throws IOException {
        MessageHandler handler = MessageHandler.getInstance("test");

        handler.addMessages("test", "src/test/resources/messages/invalid.properties");
    }

    @Test
    public void testTraceErrorMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        String errorMessage = handler.getMessage("errorKey", null);

        Trace.error(errorMessage);

        assertEquals("Key: errorKey not found in message file", Trace.getLastErrorMessage());
    }

    @Test
    public void testReplaceParamsInMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0} and message{1}", "Message {0} and message {1}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");

        assertEquals("Message param1 and message param2", handler.getMessage("message{0} and message{1}", params));
    }

    @Test
    public void testReplaceNullParamInMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0} and message{1}", "Message {0} and message {1}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add(null);

        assertEquals("Message param1 and <null>", handler.getMessage("message{0} and message{1}", params));
    }

    @Test
    public void testReplaceEmptyParamInMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0} and message{1}", "Message {0} and message {1}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("");

        assertEquals("Message param1 and ", handler.getMessage("message{0} and message{1}", params));
    }

    @Test
    public void testReplaceInvalidParamInMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0} and message{1}", "Message {0} and message {1}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        params.add("param3");

        assertEquals("Message param1 and message param2", handler.getMessage("message{0} and message{1}", params));
    }

    @Test
    public void testReplaceInvalidParamIndexInMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0} and message{1}", "Message {0} and message {1}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        params.add("param3");
        params.add("param4");

        assertEquals("Message param1 and message param2", handler.getMessage("message{0} and message{1}", params));
    }

    @Test
    public void testReplaceMultipleParamsInMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");

        HashMap<String, String> messages = new HashMap<>();
        messages.put("message{0} {1} {2}", "Message {0} {1} {2}");

        handler.addMessages("test", "src/test/resources/messages");

        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        params.add("param3");

        assertEquals("Message param1 param2 param3", handler.getMessage("message{0} {1} {2}", params));
    }

    @Test
    public void testReplaceEmptyMessageKey() {
        MessageHandler handler = MessageHandler.getInstance("test");

        assertEquals("", handler.getMessage(null, null));
    }
}