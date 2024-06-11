package de.huxhorn.lilith.data.access;
import org.junit.Assert;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expected = 1L;
        event.setTimeStamp(expected);
        Long actual = event.getTimeStamp();
        Assert.assertEquals(expected, actual);
    }

    // Edge case testing for null timeStamp
    @Test(expected = NullPointerException.class)
    public void testGetTimeStampNullTimeStamp() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(null);
        event.getTimeStamp();
    }
}