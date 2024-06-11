import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 123L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }
}