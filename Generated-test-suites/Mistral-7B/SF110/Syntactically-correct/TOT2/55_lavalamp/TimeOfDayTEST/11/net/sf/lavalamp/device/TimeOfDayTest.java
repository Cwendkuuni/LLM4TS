import static org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;
import org.junit.Test;
import org.junit.Assert.*;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayWithValidHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals("12:30", timeOfDay.toString());
    }

    @Test
    public void testTimeOfDayWithValidTimeString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:25");
        assertEquals("14:25", timeOfDay.toString());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
        int compareResult = timeOfDay1.compareTo(timeOfDay2);
        assertTrue(compareResult < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidHours() {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(25, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinutes() {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(12, 60));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidTimeStringFormat() {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("15:61"));
    }

    @Test
    public void testToStringWithHoursLessThan10() {
        TimeOfDay timeOfDay = new TimeOfDay(9, 30);
        assertEquals("09:30", timeOfDay.toString());
    }

    // Add test cases for remaining methods and edge cases here

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        assertTrue(timeOfDay1.equals(timeOfDay2));
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }
}