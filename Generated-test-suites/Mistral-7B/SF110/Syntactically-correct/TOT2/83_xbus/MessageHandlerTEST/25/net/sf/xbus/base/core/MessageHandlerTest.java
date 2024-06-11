import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import java.util.ArrayList;

public class MessageHandlerTest {

    @Test
    public void testMessage() {
        MessageHandler handler = MessageHandler.getInstance("test");
        List<Object> params = new ArrayList<Object>();
        String key = " greeting";
        String expected = "Hello, {0}!";
        String result = handler.getMessage(key, params);
        String actual = "Hello, World!"; // Replace with the expected value for "World"
        assertEquals(expected, actual);
    }
}