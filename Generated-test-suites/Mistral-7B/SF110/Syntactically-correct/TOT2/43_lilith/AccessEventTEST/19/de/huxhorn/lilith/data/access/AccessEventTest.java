import org.junit.Test;
import static org.junit.Assert.*;

public class AccessEventTest {
    // ... other imports and setup

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 123L;
        event.setTimeStamp(timeStamp);
        Long retrievedTimeStamp = event.getTimeStamp();
        assertEquals(timeStamp, retrievedTimeStamp);
    }
}