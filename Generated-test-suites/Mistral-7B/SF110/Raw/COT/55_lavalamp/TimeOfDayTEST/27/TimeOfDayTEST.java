package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(5, 30);
        Assert.assertEquals(5, timeOfDay.getHours());
        Assert.assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testConstructorWithTimeString() {
        TimeOfDay timeOfDay = new TimeOfDay("17:25");
        Assert.assertEquals(17, timeOfDay.getHours());
        Assert.assertEquals(25, timeOfDay.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(15, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(16, 0);
        Assert.isTrue(timeOfDay1.compareTo(timeOfDay2) < 0);

        TimeOfDay timeOfDay3 = new TimeOfDay(16, 30);
        Assert.isTrue(timeOfDay1.compareTo(timeOfDay3) < 0);

        TimeOfDay timeOfDay4 = new TimeOfDay(16, 45);
        Assert.isTrue(timeOfDay3.compareTo(timeOfDay4) < 0);

        TimeOfDay timeOfDay5 = new TimeOfDay(16, 59);
        Assert.isTrue(timeOfDay4.compareTo(timeOfDay5) < 0);

        TimeOfDay timeOfDay6 = new TimeOfDay(17, 0);
        Assert.isTrue(timeOfDay5.compareTo(timeOfDay6) < 0);

        TimeOfDay timeOfDay7 = new TimeOfDay(17, 30);
        Assert.assertEquals(0, timeOfDay5.compareTo(timeOfDay7));

        TimeOfDay timeOfDay8 = new TimeOfDay(17, 45);
        Assert.isTrue(timeOfDay7.compareTo(timeOfDay8) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        Assert.assertEquals(12, timeOfDay.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        Assert.assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        Assert.assertEquals("12:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHoursValue() {
        new TimeOfDay(25, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinutesValue() {
        new TimeOfDay(12, 61);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeStringFormat() {
        new TimeOfDay("12:60");
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        Assert.assertTrue(timeOfDay1.equals(timeOfDay2));

        Object obj = new Object();
        Assert.assertFalse(timeOfDay1.equals(obj));

        TimeOfDay timeOfDay3 = new TimeOfDay(13, 45);
        Assert.assertFalse(timeOfDay1.equals(timeOfDay3));
    }
}