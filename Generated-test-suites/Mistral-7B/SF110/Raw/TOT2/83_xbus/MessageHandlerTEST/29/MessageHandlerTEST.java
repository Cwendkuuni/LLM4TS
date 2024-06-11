import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MessageHandlerTest {

    @Test
    public void testgetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessage("greeting", params);
        assertEquals("Hello, World!", message);
    }

    @Test
    public void testgetMessageOptional() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessageOptional("greeting", params);
        assertEquals("Hello, World!", message);
    }

    @Test
    public void testgetInstance() {
        MessageHandler handler1 = MessageHandler.getInstance("test1");
        MessageHandler handler2 = MessageHandler.getInstance("test1");
        assertEquals(handler1, handler2);
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageNullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessage(null, params);
        assertEquals("Key: null not found in message file", "Key: null not found in message file", message);
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageEmptyKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessage("", params);
        assertEquals("Key:  not found in message file", "Key:  not found in message file", message);
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessage("greeting", null);
        assertEquals("Key: greeting not found in message file", "Key: greeting not found in message file", message);
    }

    @Test(expected = IOException.class)
    public void testgetMessageIOException() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        handler.getMessage("greeting", params); // IOException is expected to be thrown during test execution
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageOptionalNullKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessageOptional(null, params);
        assertEquals("Key: null not found in message file", "Key: null not found in message file", message);
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageOptionalEmptyKey() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessageOptional("", params);
        assertEquals("", "", message);
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageOptionalNullParams() {
        MessageHandler handler = MessageHandler.getInstance("test");
        String message = handler.getMessageOptional("greeting", null);
        assertEquals("Key: greeting not found in message file", "Key: greeting not found in message file", message);
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetMessageOptionalNullMessages() {
        MessageHandler handler = new MessageHandler("test");
        String message = handler.getMessageOptional("greeting", new ArrayList<Object>());
        assertEquals("Key: greeting not found in message file", "Key: greeting not found in message file", message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetMessageOptionalUnsupportedPlaceholder() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String message = handler.getMessageOptional("greeting$invalid", params);
        assertEquals("Key: greeting$invalid not found in message file", "Key: greeting$invalid not found in message file", message);
    }

    @Test(expected = IOException.class)
    public void testgetMessageOptionalIOException() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        handler.getMessageOptional("greeting", params); // IOException is expected to be thrown during test execution
    }

    @Test(expected = FileNotFoundException.class)
    public void testgetInstanceNullBasename() {
        MessageHandler handler = MessageHandler.getInstance(null);
        assertEquals(null, handler);
    }
}