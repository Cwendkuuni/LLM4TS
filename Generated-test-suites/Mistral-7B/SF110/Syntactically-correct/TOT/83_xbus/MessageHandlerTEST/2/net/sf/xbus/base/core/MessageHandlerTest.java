import org.junit.Test;
import static org.junit.Assert.*;

public class MessageHandlerTest {
    // ... (import statements and class setup)
    Numbers numbers = new Numbers(); // Assuming Numbers is a List implementation

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test"); // Assuming there is a valid instance with the key "test"
        String message = messageHandler.getMessage("HelloKey", numbers);
        String expectedMessage = "HelloKey: [1, 2, 3]"; // Replace [1, 2, 3] with actual parameters
        String actualMessage = XStringSupport.replaceAll(message, "\\$(\\d+)\\$", numbers.toString());
        assertEquals(expectedMessage, actualMessage);
    }
}