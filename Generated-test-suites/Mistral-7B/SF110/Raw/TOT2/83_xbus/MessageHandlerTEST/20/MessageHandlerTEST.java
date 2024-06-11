import org.junit.Test;
import static org.junit.Assert.assertEquals;
import net.sf.xbus.base.core.MessageHandler;
import java.util.ArrayList;

public class MessageHandlerTest {
    @Test
    public void testgetMessage() {
        String expectedMessage = "Hello World";
        List<Object> params = new ArrayList<>();
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String actualMessage = messageHandler.getMessage("KEY_HelloWorld", params);
        assertEquals(expectedMessage, actualMessage);
    }
}