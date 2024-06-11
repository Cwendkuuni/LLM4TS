import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import java.util.ArrayList;

public class MessageHandlerTest {
   @Test
   public void testgetMessage() {
      MessageHandler messageHandler = MessageHandler.getInstance("test");
      List params = new ArrayList();
      String key = "testKey";
      String expectedMessage = "Test message for key testKey";
      String actualMessage = messageHandler.getMessage(key, params);
      assertEquals(expectedMessage, actualMessage);
   }
}