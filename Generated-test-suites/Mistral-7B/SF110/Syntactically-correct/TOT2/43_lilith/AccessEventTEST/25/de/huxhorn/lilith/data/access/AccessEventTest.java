// GetTimeStampTest.java
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 1L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetTimeStampWithNull() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(null);
    }
}