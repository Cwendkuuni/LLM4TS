import org.junit.Test;
import static org.junit.Assert.*;

public class AccessEventTest {
    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long expectedTimeStamp = 123L;
        accessEvent.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = accessEvent.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }
}