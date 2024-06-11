package net.sf.lavalamp.device;
import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayConstructionWithIntHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals("Expected hours: 12, minutes: 30", "12:30", time.toString());
    }
}