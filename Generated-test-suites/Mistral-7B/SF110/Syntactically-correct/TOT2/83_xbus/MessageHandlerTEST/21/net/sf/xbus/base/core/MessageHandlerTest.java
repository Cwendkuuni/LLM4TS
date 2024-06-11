import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        String key = "validKey";
        List params = new ArrayList();
        MessageHandler messageHandler = MessageHandler.getInstance("testInstance");
        String expectedMessage = "Valid message for key 'validKey'";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
    }
}