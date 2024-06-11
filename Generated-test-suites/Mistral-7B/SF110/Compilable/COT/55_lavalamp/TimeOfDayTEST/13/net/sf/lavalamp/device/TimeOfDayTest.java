package net.sf.lavalamp.device;
import org.junit.Assert;
import org.junit.Test;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;

 @Test
 public void testConstructorWithValidHoursAndMinutes() {
 validTime1 = new TimeOfDay(12, 30);
 }

 @Test
 public void testConstructorWithValidStringRepresentation() {
 validTime2 = new TimeOfDay("14:45");
 }

 @Test(expected = NumberFormatException.class)
 public void testConstructorWithInvalidStringRepresentation() {
 invalidTime1 = new TimeOfDay("15:61"); // Invalid minutes value
 }

 @Test
 public void testCompareToWithSameTimeOfDay() {
 TimeOfDay sameTime = new TimeOfDay(14, 30);
 Assert.assertEquals(0, validTime2.compareTo(sameTime));
 }

 @Test
 public void testCompareToWithLaterTimeOfDay() {
 TimeOfDay laterTime = new TimeOfDay(15, 0);
 Assert.assertTrue(validTime2.compareTo(laterTime) < 0);
 }

 @Test
 public void testCompareToWithEarlierTimeOfDay() {
 TimeOfDay earlierTime = new TimeOfDay(13, 30);
 Assert.assertTrue(validTime2.compareTo(earlierTime) > 0);
 }

 @Test
 public void testGetHours() {
 Assert.assertEquals((Integer)14, validTime2.getHours());
 }

 @Test
 public void testGetMinutes() {
 Assert.assertEquals((Integer)45, validTime2.getMinutes());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("14:45", validTime2.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay sameTime2 = new TimeOfDay(14, 45);
 Assert.assertEquals(Integer.hashCode(new Integer(100 * 14 + 45)), validTime2.hashCode());
 Assert.assertEquals(Integer.hashCode(sameTime2.hashCode()), validTime2.hashCode());
 }

 @Test
 public void testEqualsWithSameTimeOfDay() {
 Assert.assertTrue(validTime2.equals(validTime2));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 Assert.assertFalse(validTime2.equals(new Object()));
 }

 @Test
 public void testEqualsWithNullObject() {
 Assert.assertFalse(validTime2.equals(null));
 }

 @Test
 public void testEqualsWithDifferentTimeOfDay() {
 TimeOfDay differentTime = new TimeOfDay(15, 30);
 Assert.assertFalse(validTime2.equals(differentTime));
 }

 @Test
 public void testEqualsWithSameTimeOfDayButDifferentInstance() {
 TimeOfDay sameTime3 = new TimeOfDay(14, 45);
 Assert.assertTrue(validTime2.equals(sameTime3));
 }
}