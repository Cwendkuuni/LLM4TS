import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expected = 1L;
        event.setTimeStamp(expected);
        Long actual = event.getTimeStamp();
        assertEquals(expected, actual);
    }
}