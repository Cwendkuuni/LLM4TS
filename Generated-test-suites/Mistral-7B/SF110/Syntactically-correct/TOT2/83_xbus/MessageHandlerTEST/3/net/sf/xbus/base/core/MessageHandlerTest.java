import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List params = new ArrayList();
        String key = "testKey";
        String expectedMessage = "This is a test message";
        String actualMessage = handler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }
}