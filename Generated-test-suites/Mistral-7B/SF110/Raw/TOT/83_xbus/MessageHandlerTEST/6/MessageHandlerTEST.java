import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.MessageHandler.MessagesFilter;
import net.sf.xbus.base.core.Trace;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class MessageHandlerTest {

    private MessageHandler messageHandler1;
    private MessageHandler messageHandler2;
    private String basename1 = "test1";
    private String basename2 = "test2";
    private String key = "testKey";
    private List<Object> params = new ArrayList<>();
    private String expectedMessage = "This is a test message";

    @Before
    public void setUp() {
        messageHandler1 = MessageHandler.getInstance(basename1);
        messageHandler1.addMessages(basename1, "src/test/resources1");
        messageHandler2 = MessageHandler.getInstance(basename2);
        messageHandler2.addMessages(basename2, "src/test/resources2");
        params.add("testParam1");
        params.add(123);
    }

    @Test
    public void testgetMessage() {
        // Test messageHandler1
        String actualMessage1 = messageHandler1.getMessage(key, params);
        String messageText1 = messageHandler1.getMessageOptional(key, params);
        Trace.reset();

        assertEquals(expectedMessage, actualMessage1);
        assertEquals(expectedMessage, messageText1);
        assertTrue(Trace.isEmpty());

        // Test messageHandler2
        String actualMessage2 = messageHandler2.getMessage(key, params);
        String messageText2 = messageHandler2.getMessageOptional(key, params);

        assertNotEquals(expectedMessage, actualMessage1); // Different instances should return different messages
        assertEquals(expectedMessage, actualMessage2); // Same key and params should return the same message
        assertEquals(expectedMessage, messageText2);
        assertTrue(Trace.isEmpty());

        // Test key not found
        String keyNotFound = "nonExistentKey";
        String actualMessageNotFound1 = messageHandler1.getMessage(keyNotFound, null);
        String actualMessageNotFound2 = messageHandler2.getMessage(keyNotFound, null);
        String messageTextNotFound1 = messageHandler1.getMessageOptional(keyNotFound, null);
        String messageTextNotFound2 = messageHandler2.getMessageOptional(keyNotFound, null);

        assertNull(actualMessageNotFound1);
        assertNull(actualMessageNotFound2);
        assertEquals("Key: " + keyNotFound + " not found in message file", Trace.getLastMessage());
        assertEquals("Key: " + keyNotFound + " not found in message file", Trace.getLastMessage());

        // Test null params
        List<Object> emptyParams1 = new ArrayList<>();
        List<Object> emptyParams2 = new ArrayList<>();
        String actualMessageOptionalNullParams1 = messageHandler1.getMessageOptional(key, null);
        String actualMessageOptionalNullParams2 = messageHandler2.getMessageOptional(key, null);

        assertEquals(expectedMessage, actualMessageOptionalNullParams1);
        assertEquals(expectedMessage, actualMessageOptionalNullParams2);

        // Test empty params
        Map<String, Object> emptyParamsMap = new HashMap<>();
        String actualMessageOptionalEmptyParams1 = messageHandler1.getMessageOptional(key, emptyParamsMap.entrySet().iterator().next().getValue());
        String actualMessageOptionalEmptyParams2 = messageHandler2.getMessageOptional(key, emptyParamsMap.entrySet().iterator().next().getValue());

        assertEquals(expectedMessage, actualMessageOptionalEmptyParams1);
        assertEquals(expectedMessage, actualMessageOptionalEmptyParams2);
    }

    @Test
    public void testgetMessageOptional() {
        // Test messageHandler1
        String actualMessageOptional1 = messageHandler1.getMessageOptional(key, params);
        String messageTextOptional1 = messageHandler1.getMessageOptional(key, null);

        assertEquals(expectedMessage, actualMessageOptional1);
        assertNull(messageTextOptional1);

        // Test messageHandler2
        String actualMessageOptional2 = messageHandler2.getMessageOptional(key, params);
        String messageTextOptional2 = messageHandler2.getMessageOptional(key, null);

        assertEquals(expectedMessage, actualMessageOptional2);
        assertNull(messageTextOptional2);

        // Test null params
        List<Object> nullParams = null;
        String actualMessageOptionalNullParams = messageHandler1.getMessageOptional(key, nullParams);

        assertEquals(expectedMessage, actualMessageOptionalNullParams);

        // Test empty params
        Map<String, Object> emptyParams = new HashMap<>();
        String actualMessageOptionalEmptyParams = messageHandler1.getMessageOptional(key, emptyParams.entrySet().iterator().next().getValue());

        assertEquals(expectedMessage, actualMessageOptionalEmptyParams);
    }

    @Test
    public void testgetInstance() {
        String basename3 = "test3";
        MessageHandler messageHandler3 = MessageHandler.getInstance(basename3);

        assertNull(messageHandler3.mMessages);

        messageHandler3 = MessageHandler.getInstance(basename1);
        assertEquals(messageHandler1.mMessages, messageHandler3.mMessages);

        messageHandler3 = MessageHandler.getInstance(basename2);
        assertEquals(messageHandler2.mMessages, messageHandler3.mMessages);
    }

    @Test
    public void testaddMessages() {
        messageHandler1 = new MessageHandler(basename1);
        messageHandler1.addMessages(basename1, "src/test/resources1");
        messageHandler2 = new MessageHandler(basename2);
        messageHandler2.addMessages(basename2, "src/test/resources2");

        String key1 = "testKey1";
        String key2 = "testKey2";
        String expectedMessage1 = "This is a test message 1";
        String expectedMessage2 = "This is a test message 2";
        List<Object> params = new ArrayList<>();
        params.add("testParam1");
        params.add(123);

        String actualMessage1 = messageHandler1.getMessage(key1, params);
        String actualMessage2 = messageHandler1.getMessage(key2, params);
        String actualMessage3 = messageHandler2.getMessage(key1, params);
        String actualMessage4 = messageHandler2.getMessage(key2, params);

        assertEquals(expectedMessage1, actualMessage1);
        assertEquals(expectedMessage1, actualMessage3); // Both instances should have the same messages
        assertNotEquals(expectedMessage1, actualMessage2); // Different keys should return different messages
        assertNotEquals(expectedMessage1, actualMessage4);

        String messageText1 = messageHandler1.getMessageOptional(key1, null);
        String messageText2 = messageHandler1.getMessageOptional(key2, null);
        String messageText3 = messageHandler2.getMessageOptional(key1, null);
        String messageText4 = messageHandler2.getMessageOptional(key2, null);

        assertEquals(expectedMessage1, messageText1);
        assertNull(messageText2);
        assertEquals(expectedMessage1, messageText3);
        assertNull(messageText4);
    }

    @Test
    public void testMessagesFilter() {
        File dir = new File("src/test/resources");
        String[] messagesFiles = dir.list(new MessageHandler.MessagesFilter(basename1, "_EN.properties"));

        assertNotNull(messagesFiles);
        assertEquals(2, messagesFiles.length);
    }
}