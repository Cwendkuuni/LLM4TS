import org.junit.Test;
import static org.junit.Assert.*;;

public class TimeOfDayTest {

    // Constructor with int hours and minutes test

    @Test
    public void testTimeOfDayConstructorWithInt() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    // Constructor with String time test

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay time = new TimeOfDay("14:45");
        assertEquals(14, time.getHours());
        assertEquals(45, time.getMinutes());
    }

    // compareTo test

    @Test
    public void testCompareToSameTime() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testCompareToDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testCompareToDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(13, 31);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }

    // getHours test

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals(14, time.getHours());
    }

    // getMinutes test

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals(30, time.getMinutes());
    }

    // compareTo test (edge case)

    @Test
    public void testCompareToMidnightAndSameTime() {
        TimeOfDay time1 = new TimeOfDay(0, 0);
        TimeOfDay time2 = new TimeOfDay(23, 59);
        TimeOfDay time3 = new TimeOfDay(23, 59);
        assertEquals(0, time1.compareTo(time3));
        assertEquals(-1, time3.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
    }

    // compareTo test (error case)

    @Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        TimeOfDay time = new TimeOfDay(12, 30);
        time.compareTo(null);
    }

    // toString test

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals("14:30", time.toString());
    }

    // hashCode test

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    // equals test

    @Test
    public void testEqualsSameObject() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        assertEquals(time1, time1);
    }

    @Test
    public void testEqualsSameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(time1, time2);
    }

    @Test
    public void testEqualsDifferentObjects() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(13, 31);
        assertNotEquals(time1, time2);
    }

    @Test
    public void testEqualsNull() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotEquals(time, null);
    }

    // equals test (error case)

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotEquals(time, null);
    }

    // equals test (edge case)

    @Test
    public void testEqualsSameTimeAsString() {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals(time, new TimeOfDay("14:30"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsInvalidTimeString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotEquals(time, new TimeOfDay("14:61"));
    }

    // constructor with int hours and minutes test (edge case)

    @Test
    public void testTimeOfDayConstructorWithMaxHours() {
        TimeOfDay time = new TimeOfDay(23, 59);
        assertEquals(23, time.getHours());
        assertEquals(59, time.getMinutes());
    }

    // constructor with int hours and minutes test (error case)

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidHours() {
        new TimeOfDay(25, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidMinutes() {
        new TimeOfDay(0, 60);
    }

    // toString test (error case)

    @Test(expected = NullPointerException.class)
    public void testToStringNull() {
        new TimeOfDay(null, null);
    }

    // equals test (error case)

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotEquals(time, null);
    }
}