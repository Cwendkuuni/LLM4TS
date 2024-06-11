import org.junit.Test;
import org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Integer expectedHours = 10;
        Integer expectedMinutes = 30;
        assertEquals(expectedHours, timeOfDay.getHours());
        assertEquals(expectedMinutes, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithTimeString() {
        TimeOfDay timeOfDay = new TimeOfDay("10:30");
        Integer expectedHours = 10;
        Integer expectedMinutes = 30;
        assertEquals(expectedHours, timeOfDay.getHours());
        assertEquals(expectedMinutes, timeOfDay.getMinutes());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidTimeStringFormat() {
        String invalidTime = "10:60";
        try {
            new TimeOfDay(invalidTime);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 31);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);
        int expectedComparisonResult1 = 1;
        int expectedComparisonResult2 = -1;
        int comparisonResult1 = timeOfDay1.compareTo(timeOfDay2);
        int comparisonResult2 = timeOfDay1.compareTo(timeOfDay3);
        assertEquals(expectedComparisonResult1, comparisonResult1);
        assertEquals(expectedComparisonResult2, comparisonResult2);
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(15, 45);
        Integer expectedHours = 15;
        assertEquals(expectedHours, timeOfDay.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(23, 59);
        Integer expectedMinutes = 59;
        assertEquals(expectedMinutes, timeOfDay.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(2, 3);
        String expectedString = "02:03";
        String actualString = timeOfDay.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 2);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 3);
        Integer expectedHashCode1 = 101 * 1 + 2;
        Integer expectedHashCode2 = 101 * 3 + 3;
        int actualHashCode1 = timeOfDay1.hashCode();
        int actualHashCode2 = timeOfDay2.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 2);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 2);
        Object nullObject = null;
        Object anotherObject = new Object();
        TimeOfDay anotherTimeOfDay = new TimeOfDay(3, 3);
        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(nullObject));
        assertFalse(timeOfDay1.equals(anotherObject));
        assertFalse(timeOfDay1.equals(anotherTimeOfDay));
    }
}