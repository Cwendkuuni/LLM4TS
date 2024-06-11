package net.sf.xbus.base.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Test;

public class MessageHandlerTest {

    @Test
    public void testGetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("testBasename");
        MessageHandler handler2 = MessageHandler.getInstance("testBasename");
        assertEquals(handler1, handler2);
    }

    @Test
    public void testGetMessages() {
        MessageHandler handler = MessageHandler.getInstance("testBasename");
        List params = new ArrayList();
        String message = handler.getMessage("testKey", params);
        assertNull(message);

        handler = new MessageHandler("testBasename");
        Properties properties = new Properties();
        properties.put("testKey", "Test message");
        handler.addMessages("testBasename", "." + File.separator + "test" + File.separator + "messages");
        handler.mMessages = properties;

        message = handler.getMessage("testKey", params);
        assertEquals("Test message", message);
    }

    @Test
    public void testReplaceParams() {
        String message = "Hello ${param1}, your order ${param2} will be processed soon.";
        List params = new ArrayList();
        params.add("John");
        params.add("12345");

        MessageHandler handler = new MessageHandler("testBasename");
        handler.addMessages("testBasename", "." + File.separator + "test" + File.separator + "messages");
        handler.mMessages = new Hashtable<>();
        handler.mMessages.put("message.hello", message);

        String result = handler.getMessageOptional("message.hello", params);
        assertEquals("Hello John, your order 12345 will be processed soon.", result);
    }

    @Test
    public void testLoadMessages() throws IOException {
        String basename = "testBasename";
        MessageHandler handler = MessageHandler.getInstance(basename);

        File dir = new File("." + File.separator + "test" + File.separator + "messages");
        String prefix = basename + File.separator;
        String postfix = "_EN.properties";
        String[] messagesFiles = dir.list(new MessagesFilter(basename, postfix));

        handler = new MessageHandler(basename);
        handler.addMessages(basename, "." + File.separator + "test" + File.separator + "messages");

        for (String messagesFile : messagesFiles) {
            Properties newProps = new Properties();
            FileInputStream instream = new FileInputStream(dir + File.separator + messagesFile);
            newProps.load(instream);
            instream.close();

            String key = null;
            Enumeration keys = newProps.keys();
            while (keys.hasMoreElements()) {
                key = keys.nextElement().toString();
                handler.mMessages.put(key, ((Hashtable<String, String>) newProps).get(key));
            }
        }

        List params = new ArrayList();
        String message = handler.getMessage("testKey", params);
        assertNotNull(message);
        assertEquals("Test message", message);
    }

    private static class MessagesFilter implements FilenameFilter {
        private String mPrefix;
        private String mPostfix;

        public MessagesFilter(final String prefix, final String postfix) {
            this.mPrefix = prefix;
            this.mPostfix = postfix;
        }

        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(this.mPrefix) && filename.endsWith(this.mPostfix);
        }
    }
}