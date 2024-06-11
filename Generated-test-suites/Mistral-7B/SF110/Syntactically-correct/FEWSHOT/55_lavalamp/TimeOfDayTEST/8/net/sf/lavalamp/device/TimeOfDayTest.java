package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(5, 30);
        assertEquals(5, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("11:30");
        assertEquals(11, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidStringFormat() {
        new TimeOfDay("11:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay t1 = new TimeOfDay(11, 30);
        TimeOfDay t2 = new TimeOfDay(12, 30);
        TimeOfDay t3 = new TimeOfDay(11, 45);
        TimeOfDay t4 = new TimeOfDay(11, 30);

        assertEquals(1, t1.compareTo(t2));
        assertEquals(-1, t2.compareTo(t1));
        assertEquals(0, t1.compareTo(t4));
        assertEquals(0, t4.compareTo(t1));
        assertEquals(-1, t3.compareTo(t1));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(11, 30);
        assertEquals("11:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);

        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test
    public void testEqualsObjectNull() {
        assertNull(new TimeOfDay(11, 30).equals(null));
    }

    @Test
    public void testEqualsObjectNotInstance() {
        assertFalse(new TimeOfDay(11, 30).equals(new Object()));
    }

    @Test
    public void testEqualsObjectSameInstance() {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay2 = timeOfDay1;

        assertTrue(timeOfDay1.equals(timeOfDay2));
    }

    @Test
    public void testEqualsObjectDifferentInstances() {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);

        assertFalse(timeOfDay1.equals(timeOfDay2));
    }

    @Test
    public void testEqualsObjectDifferentValues() {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 45);

        assertFalse(timeOfDay1.equals(timeOfDay2));
    }
}