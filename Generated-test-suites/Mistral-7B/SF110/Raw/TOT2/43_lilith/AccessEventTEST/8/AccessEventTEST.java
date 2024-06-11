import org.junit.Assert;
import org.junit.Test;

public class AccessEventTest {
    // ... other imports and test setup code
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 123L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        Assert.assertEquals(expectedTimeStamp, actualTimeStamp);
    }
    
    // Edge case: testing null value
    @Test(expected = NullPointerException.class)
    public void testGetTimeStampWithNullValue() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(null);
        event.getTimeStamp();
    }
}