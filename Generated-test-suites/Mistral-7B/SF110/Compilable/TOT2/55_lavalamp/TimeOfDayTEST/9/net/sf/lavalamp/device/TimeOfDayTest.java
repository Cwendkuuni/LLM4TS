package net.sf.lavalamp.device;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithIntArguments() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals("12:30", timeOfDay.toString());
    }
}