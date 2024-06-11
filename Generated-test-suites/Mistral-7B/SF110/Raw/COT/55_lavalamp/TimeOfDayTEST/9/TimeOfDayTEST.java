import org.junit.Assert;
import org.junit.Test;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 private TimeOfDay regularTime;
 private TimeOfDay timeWithHoursOver23;
 private TimeOfDay timeWithMinutesOver59;
 private TimeOfDay timeWithInvalidInput;

 @Test
 public void testConstructorWithHoursAndMinutes() {
 regularTime = new TimeOfDay(12, 30);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidHours() {
 timeWithInvalidInput = new TimeOfDay(-1, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidMinutes() {
 timeWithInvalidInput = new TimeOfDay(0, 60);
 }

 @Test
 public void testGetHours() {
 Assert.assertEquals(12, regularTime.getHours());
 }

 @Test
 public void testGetMinutes() {
 Assert.assertEquals(30, regularTime.getMinutes());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 Assert.assertEquals(1, time1.compareTo(time2));

 TimeOfDay time3 = new TimeOfDay(12, 30);
 TimeOfDay time4 = new TimeOfDay(12, 30);
 Assert.assertEquals(0, time3.compareTo(time4));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("12:30", regularTime.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 Assert.assertEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 Assert.assertTrue(time1.equals(time2));

 TimeOfDay time3 = new TimeOfDay(13, 30);
 Assert.assertFalse(time1.equals(time3));
 }

 @Test
 public void testConstructorWithString() {
 regularTime = new TimeOfDay("12:30");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidStringFormatHours() {
 timeWithInvalidInput = new TimeOfDay("13:60");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidStringFormatMinutes() {
 timeWithInvalidInput = new TimeOfDay("12:70");
 }

 @Test
 public void testTimeWithHoursOver23() {
 timeWithHoursOver23 = new TimeOfDay("24:45");
 }

 @Test
 public void testTimeWithMinutesOver59() {
 timeWithMinutesOver59 = new TimeOfDay("12:60");
 }

 @Test
 public void testCompareToWithTimeOver24Hours() {
 TimeOfDay time1 = new TimeOfDay("12:30");
 TimeOfDay time2 = timeWithHoursOver23;
 Assert.assertEquals(-1, time1.compareTo(time2));
 }

 @Test
 public void testCompareToWithTimeMinutesOver59() {
 TimeOfDay time1 = new TimeOfDay("12:30");
 TimeOfDay time2 = timeWithMinutesOver59;
 Assert.assertEquals(-1, time1.compareTo(time2));
 }
}