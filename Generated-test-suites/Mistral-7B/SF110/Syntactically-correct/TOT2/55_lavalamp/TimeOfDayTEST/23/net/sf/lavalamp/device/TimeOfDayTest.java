import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithTimeString() {
        TimeOfDay time = new TimeOfDay("10:30");
        assertEquals(10, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidHours() {
        new TimeOfDay(-5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidMinutes() {
        new TimeOfDay(0, -10);
    }

    @Test(expected = NullPointerException.class)
    public void testTimeOfDayConstructorWithNullInput() {
        new TimeOfDay(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayParserWithInvalidTimeString() {
        new TimeOfDay("25:30");
    }

    @Test(expected = NullPointerException.class)
    public void testTimeOfDayParserWithNullTimeString() {
        new TimeOfDay(null);
    }

    @Test
    public void testCompareToWithSameHoursDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(-5, comparisonResult);
    }

    @Test
    public void testCompareToWithDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 0);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 15);
        int time1HashCode = time1.hashCode();
        int time2HashCode = time2.hashCode();
        assertNotEquals(time1HashCode, time2HashCode);
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(11, 30);
        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}