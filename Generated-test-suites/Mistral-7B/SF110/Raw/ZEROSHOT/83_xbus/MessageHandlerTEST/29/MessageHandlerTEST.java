package net.sf.xbus.base.core.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;
    private static List<String> params;

    @BeforeClass
    public static void init() {
        messageHandler = MessageHandler.getInstance("test");
    }

    @Test
    public void testGetMessageOptionalWithEmptyParams() {
        String messageText = messageHandler.getMessageOptional("testKey", null);
        assertEquals("testKey", messageText);
    }

    @Test
    public void testGetMessageOptionalWithParams() {
        params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String messageText = messageHandler.getMessageOptional("testKey", params);
        String expectedMessageText = "testKey: $1$: $2$";
        assertEquals(expectedMessageText, messageText);
    }

    @Test
    public void testGetMessageOptionalWithNullParams() {
        String messageText = messageHandler.getMessageOptional("testKey", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageOptionalWithEmptyHashTable() {
        MessageHandler messageHandlerEmpty = new MessageHandler("testEmpty");
        String messageText = messageHandlerEmpty.getMessageOptional("testKey", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageOptionalWithInvalidKey() {
        String messageText = messageHandler.getMessageOptional("testKeyInvalid", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageOptionalWithInvalidKeyAndParams() {
        String messageText = messageHandler.getMessageOptional("testKeyInvalid", params);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndEmptyParams() {
        String messageText = messageHandler.getMessageOptional("testKeyValid", null);
        assertEquals("testKeyValid", messageText);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndParams() {
        params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String messageText = messageHandler.getMessageOptional("testKeyValidWithParams", params);
        String expectedMessageText = "testKeyValidWithParams: $1$: $2$";
        assertEquals(expectedMessageText, messageText);
    }

    @Test
    public void testGetMessageOptionalWithInvalidKeyAndInvalidFile() {
        MessageHandler messageHandlerInvalidFile = new MessageHandler("testInvalidFile");
        String messageText = messageHandlerInvalidFile.getMessageOptional("testKeyInvalid", null);
        assertNull(messageText);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndValidFile() {
        MessageHandler messageHandlerValidFile = new MessageHandler("testValidFile");
        String messageText = messageHandlerValidFile.getMessageOptional("testKeyValidWithFile", null);
        String expectedMessageText = "testKeyValidWithFile: testMessageWithFile";
        assertEquals(expectedMessageText, messageText);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndValidFileWithParams() {
        MessageHandler messageHandlerValidFile = new MessageHandler("testValidFile");
        params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        String messageText = messageHandlerValidFile.getMessageOptional("testKeyValidWithFileAndParams", params);
        String expectedMessageText = "testKeyValidWithFileAndParams: testMessageWithFile: $1$: $2$";
        assertEquals(expectedMessageText, messageText);
    }

    @Test
    public void testgetInstanceWithValidBasename() {
        MessageHandler messageHandlerInstance = MessageHandler.getInstance("test");
        MessageHandler messageHandlerInstance2 = MessageHandler.getInstance("test");
        assertEquals(messageHandlerInstance, messageHandlerInstance2);
    }

    @Test(expected = IllegalStateException.class)
    public void testgetInstanceWithDifferentBasename() {
        MessageHandler messageHandlerInstance = MessageHandler.getInstance("test");
        MessageHandler messageHandlerInstance2 = MessageHandler.getInstance("testDifferent");
    }

    private static class MessagesFilterMock extends MessageHandler.MessagesFilter {
        @Override
        public boolean accept(File dir, String filename) {
            return filename.equals("testMessageFile.properties");
        }
    }

    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = new MessageHandler("testAddMessages");
        Properties properties = new Properties();
        properties.setProperty("testKey", "testMessage");
        messageHandler.addMessages("testAddMessages", "testDirectory", new Properties[]{properties});
        String messageText = messageHandler.getMessageOptional("testKey", null);
        String expectedMessageText = "testMessage";
        assertEquals(expectedMessageText, messageText);
    }
}