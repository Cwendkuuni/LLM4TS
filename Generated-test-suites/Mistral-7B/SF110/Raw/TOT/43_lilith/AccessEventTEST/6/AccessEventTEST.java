import org.junit.Assert;
import org.junit.Test;

public class AccessEventTest {
    @Test
    public void testSetAndGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 123456L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        Assert.assertEquals(expectedTimeStamp, actualTimeStamp);
    }
}