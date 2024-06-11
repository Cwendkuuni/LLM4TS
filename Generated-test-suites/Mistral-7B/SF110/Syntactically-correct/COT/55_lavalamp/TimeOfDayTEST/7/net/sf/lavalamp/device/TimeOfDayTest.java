import org.junit.Test;
import org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithIntHoursMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        int expectedHours = 10;
        int expectedMinutes = 30;
        assertEquals(expectedHours, timeOfDay.getHours());
        assertEquals(expectedMinutes, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithStringTime() {
        TimeOfDay timeOfDay = new TimeOfDay("10:30");
        int expectedHours = 10;
        int expectedMinutes = 30;
        assertEquals(expectedHours, timeOfDay.getHours());
        assertEquals(expectedMinutes, timeOfDay.getMinutes());
    }

    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructorWithInvalidStringFormat() {
        new TimeOfDay("10:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(10, 35);
        int expectedComparisonResult1 = -1;
        int expectedComparisonResult2 = 1;
        int expectedComparisonResult3 = -1;

        int comparisonResult1 = timeOfDay1.compareTo(timeOfDay2);
        int comparisonResult2 = timeOfDay1.compareTo(timeOfDay3);

        assertEquals(expectedComparisonResult1, comparisonResult1);
        assertEquals(expectedComparisonResult2, comparisonResult2);
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        int expectedHours = 10;
        assertEquals(expectedHours, timeOfDay.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        int expectedMinutes = 30;
        assertEquals(expectedMinutes, timeOfDay.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        String expectedString = "10:30";
        assertEquals(expectedString, timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 35);
        int expectedHashCode1 = 1212022598;
        int expectedHashCode2 = 1212101674;

        int hashCode1 = timeOfDay1.hashCode();
        int hashCode2 = timeOfDay2.hashCode();

        assertEquals(expectedHashCode1, hashCode1);
        assertEquals(expectedHashCode2, hashCode2);
    }

    @Test
    public void testEqualsObject() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }

    @Test
    public void testEqualsNull() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertFalse(timeOfDay.equals(null));
    }

    @Test
    public void testEqualsOtherClass() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Object otherObject = new Object();
        assertFalse(timeOfDay.equals(otherObject));
    }
}