package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    // Test methods as shown above

    // Add test case for hashCode()

    @Test
    public void testHashCodeWithDifferentTimeOfDayObjects() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(14, 0);
        TimeOfDay time3 = new TimeOfDay(13, 30);

        Assert.assertNotEquals(time1.hashCode(), time2.hashCode());
        Assert.assertEquals(time1.hashCode(), time3.hashCode());
    }

    // Add test case for equals()

    @Test
    public void testEqualsWithSameTimeOfDayObject() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        Assert.assertTrue(time1.equals(time2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        String string = "test";
        Assert.assertFalse(time1.equals(string));
    }

    // Add test cases for edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testEqualsWithNullObject() {
        TimeOfDay time = new TimeOfDay(13, 30);
        Assert.assertFalse(time.equals(null));
    }

    @Test(expected = ClassCastException.class)
    public void testEqualsWithDifferentClassObject() {
        TimeOfDay time = new TimeOfDay(13, 30);
        String string = "test";
        Assert.assertFalse(time.equals(string));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareToWithNullObject() {
        TimeOfDay time = new TimeOfDay(13, 30);
        Assert.assertNull(time.compareTo(null));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToWithNullObjectThat() {
        TimeOfDay time = new TimeOfDay(13, 30);
        Assert.assertNull(time.compareTo(null));
    }

    // Add more edge case test cases if necessary

}