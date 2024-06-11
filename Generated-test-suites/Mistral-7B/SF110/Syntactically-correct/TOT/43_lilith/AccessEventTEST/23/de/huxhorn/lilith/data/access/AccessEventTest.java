import org.junit.Assert;
import org.junit.Test;

public class AccessEventTest {
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 1L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        Assert.assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    @Test
    public void testGetTimeStamp_NullValue() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(null);
        Assert.assertNull(event.getTimeStamp());
    }
}