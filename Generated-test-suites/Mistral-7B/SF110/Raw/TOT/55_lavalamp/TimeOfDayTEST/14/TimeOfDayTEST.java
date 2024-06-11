import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }

    
    @Test
    public void testTimeOfDayConstructionWithInvalidHours() {
        try {
            new TimeOfDay(-1, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new TimeOfDay(24, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new TimeOfDay(12, 61);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new TimeOfDay(12, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testTimeOfDayConstructionWithInvalidMinutes() {
        try {
            new TimeOfDay(12, 61);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new TimeOfDay(12, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testTimeOfDayCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        TimeOfDay time3 = new TimeOfDay(12, 30);

        assertEquals(-1, time1.compareTo(time3));
        assertEquals(1, time3.compareTo(time1));
        assertEquals(0, time3.compareTo(time3));

        assertEquals(1, time1.compareTo(time2));

        time3.setHours(13);
        assertEquals(0, time3.compareTo(time2));
        assertEquals(1, time2.compareTo(time3));
    }

    @Test
    public void testTimeOfDayToString() {
        TimeOfDay time = new TimeOfDay(15, 45);
        assertEquals("15:45", time.toString());
    }

    @Test
    public void testTimeOfDayEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 30);

        assertEquals(time1, time1);
        assertEquals(time1, time1);

        assertEquals(time1, time2);
        assertEquals(time2, time1);

        assertNotEquals(time1, time3);
        assertNotEquals(time3, time1);
    }

    @Test
    public void testTimeOfDayHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 30);

        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time1.hashCode());

        assertEquals(time1.hashCode(), time2.hashCode());
        assertEquals(time2.hashCode(), time1.hashCode());

        assertNotEquals(time1.hashCode(), time3.hashCode());
        assertNotEquals(time3.hashCode(), time1.hashCode());
    }

    
}