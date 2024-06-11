package net.sf.xbus.base.core;

import static org.junit.Assert.*;
import static net.sf.xbus.base.core.strings.XStringSupport.replaceAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

import org.junit.Test;

public class MessageHandlerTest {
    @Test
    public void testMessageHandlerInstance() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        assertNotNull(messageHandler);
        MessageHandler messageHandler2 = MessageHandler.getInstance("test");
        assertEquals(messageHandler, messageHandler2);
    }

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        List params = new ArrayList();
        params.add("param1");
        params.add(123);
        String message = messageHandler.getMessage("test.key", params);
        assertEquals("test.key", message.substring(0, message.indexOf(":")).trim());
        assertEquals("param1", message.substring(message.indexOf("$1$")).substring(message.indexOf("$1$") + 3, message.length() - 1));
        assertEquals(123, Integer.parseInt(message.substring(message.indexOf("$2$")).substring(message.indexOf("$2$") + 3, message.length() - 1)));

        messageHandler = MessageHandler.getInstance("test");
        params = new ArrayList();
        message = messageHandler.getMessage("test.missing.key", params);
        assertEquals("Key: test.missing.key not found in message file", message);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String message = messageHandler.getMessageOptional("test.key", null);
        assertNull(message);

        messageHandler = MessageHandler.getInstance("test");
        message = messageHandler.getMessageOptional("test.key", new ArrayList());
        assertNotNull(message);
        assertEquals("test.key", message);
    }

    @Test
    public void testAddMessages() throws IOException {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String basename = "test";
        String dir = "src/test/resources";

        messageHandler.addMessages(basename, dir);

        MessageHandler messageHandler2 = MessageHandler.getInstance(basename);
        String message = messageHandler2.getMessage("test.key1", null);
        assertEquals("test.key1", message);

        message = messageHandler2.getMessage("test.key2", null);
        assertEquals("test.key2", message);

        message = messageHandler2.getMessage("test.key3", null);
        assertNull(message);
    }

    @Test
    public void testMessagesFilter() {
        String prefix = "test";
        String postfix = "_en_US.properties";

        MessagesFilter messagesFilter = new MessagesFilter(prefix, postfix);

        File dir = new File("src/test/resources");
        String[] messagesFiles = dir.list(messagesFilter);

        assertEquals(3, messagesFiles.length);
    }
}