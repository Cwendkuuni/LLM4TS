package net.sf.xbus.base.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        assertEquals("Expected message", handler.getMessage("key", null));
    }
}