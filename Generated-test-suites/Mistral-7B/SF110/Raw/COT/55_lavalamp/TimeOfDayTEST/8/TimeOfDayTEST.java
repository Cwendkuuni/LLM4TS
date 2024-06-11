package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals(10, timeOfDay.getHours());
        Assert.assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:45");
        Assert.assertEquals(14, timeOfDay.getHours());
        Assert.assertEquals(45, timeOfDay.getMinutes());
    }

    @Test
    public void testCompareToWhenHoursAreEqual() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        Assert.assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWhenHoursAreDifferentAndMinutesAreEqual() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        Assert.assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWhenHoursAreDifferentAndMinutesAreDifferent() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 45);
        Assert.assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayStringConstructorWithInvalidFormat() {
        new TimeOfDay("14:60");
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(14, 45);
        Assert.assertEquals("14:45", timeOfDay.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        Assert.assertEquals(timeOfDay1, timeOfDay1);
        Assert.assertEquals(timeOfDay1, timeOfDay1);

        Assert.assertNotEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        Assert.assertNotEquals(timeOfDay1, timeOfDay2);
        Assert.assertNotEquals(timeOfDay1, timeOfDay3);

        TimeOfDay timeOfDay4 = new TimeOfDay(10, 30);
        Assert.assertEquals(timeOfDay1, timeOfDay4);
        Assert.assertEquals(timeOfDay4.hashCode(), timeOfDay1.hashCode());
    }

    @Test
    public void testCompareToWithNull() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertNull(timeOfDay.compareTo(null));
    }

    @Test
    public void testCompareToWithDifferentTypes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Object obj = new Object();
        Assert.assertNull(timeOfDay.compareTo(obj));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        String obj = "time";
        timeOfDay.compareTo(obj);
    }

    @Test
    public void testCompareToWithSameInstance() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals(0, timeOfDay.compareTo(timeOfDay));
    }

    @Test
    public void testCompareToWithFutureTime() {
        TimeOfDay currentTime = new TimeOfDay(LocalTime.now().getHour(), LocalTime.now().getMinute());
        TimeOfDay futureTime = new TimeOfDay(currentTime.getHours() + 1, currentTime.getMinutes());
        Assert.assertEquals(-1, currentTime.compareTo(futureTime));
    }
}