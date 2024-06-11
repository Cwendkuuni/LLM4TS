import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageHandlerTest {
    // ... other imports and setup code omitted for brevity
    
    @Test
    public void testgetMessage() {
        MessageHandler messageHandler = MessageHandler.getInstance("test");
        String key = "testKey";
        List params = Arrays.asList("testParam1", "testParam2");
        String expectedMessage = "This is a test message with two params: testParam1 and testParam2";
        String actualMessage = messageHandler.getMessage(key, params);
        assertEquals(expectedMessage, actualMessage);
        
        key = "nonExistentKey";
        assertNull(messageHandler.getMessage(key, params));
    }
}