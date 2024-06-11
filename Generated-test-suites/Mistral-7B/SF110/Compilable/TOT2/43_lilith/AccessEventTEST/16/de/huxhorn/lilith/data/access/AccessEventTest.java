package de.huxhorn.lilith.data.access;
import org.junit.Assert;
import org.junit.Test;

public class AccessEventTest {
    // ... other imports and class setup code

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expectedTimeStamp = 123L;
        event.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = event.getTimeStamp();
        Assert.assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    // Edge cases and exception handling scenarios can be added here or in separate tests
}