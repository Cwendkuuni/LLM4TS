import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long expectedTimeStamp = 1L;
        accessEvent.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = accessEvent.getTimeStamp();
        assertEquals("Expected timeStamp to be set correctly", expectedTimeStamp, actualTimeStamp);
    }

    // Edge case testing for null timeStamp
    @Test(expected = NullPointerException.class)
    public void testGetTimeStampNullAccessEvent() {
        AccessEvent accessEvent = null;
        accessEvent.getTimeStamp();
    }
}