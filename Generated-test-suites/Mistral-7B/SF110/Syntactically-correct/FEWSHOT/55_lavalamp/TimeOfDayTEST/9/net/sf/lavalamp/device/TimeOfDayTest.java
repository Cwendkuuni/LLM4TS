package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithInts() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(23, 59);

        Assert.assertEquals(new TimeOfDay(1, 30), timeOfDay1);
        Assert.assertEquals(new TimeOfDay(2, 45), timeOfDay2);
        Assert.assertEquals(new TimeOfDay(23, 59), timeOfDay3);
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay1 = new TimeOfDay("01:30");
        TimeOfDay timeOfDay2 = new TimeOfDay("02:45");
        TimeOfDay timeOfDay3 = new TimeOfDay("23:59");

        Assert.assertEquals(new TimeOfDay(1, 30), timeOfDay1);
        Assert.assertEquals(new TimeOfDay(2, 45), timeOfDay2);
        Assert.assertEquals(new TimeOfDay(23, 59), timeOfDay3);

        Assert.assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("25:00"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("01:60"));
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 30);
        TimeOfDay timeOfDay4 = new TimeOfDay(1, 30);

        Assert.isTrue(timeOfDay1.compareTo(timeOfDay4) < 0);
        Assert.isTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        Assert.isTrue(timeOfDay2.compareTo(timeOfDay3) < 0);
    }

    @Test
    public void testGetters() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);

        Assert.assertEquals(1, timeOfDay.getHours().intValue());
        Assert.assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(23, 59);

        Assert.assertEquals("01:30", timeOfDay1.toString());
        Assert.assertEquals("02:45", timeOfDay2.toString());
        Assert.assertEquals("23:59", timeOfDay3.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 45);
        TimeOfDay timeOfDay4 = new TimeOfDay(1, 31);

        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        Assert.assertTrue(timeOfDay1.equals(timeOfDay2));
        Assert.assertFalse(timeOfDay1.equals(timeOfDay3));
        Assert.assertFalse(timeOfDay1.equals(timeOfDay4));

        TimeOfDay[] times = new TimeOfDay[32];
        for (int i = 0; i < times.length; i++) {
            times[i] = new TimeOfDay(i / 60, i % 60);
        }

        // Check for unique hash codes and no duplicates in the equals method
        Set<TimeOfDay> set = new HashSet<>();
        for (TimeOfDay time : times) {
            set.add(time);
        }

        Assert.assertEquals(times.length, set.size());
    }
}