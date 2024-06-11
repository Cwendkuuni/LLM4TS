import static org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 // Test constructor with valid int arguments
 @Test
 public void testTimeOfDayConstructorValidIntArguments() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 // Test constructor with valid String argument
 @Test
 public void testTimeOfDayConstructorValidStringArgument() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 // Test compareTo method with same hours and different minutes
 @Test
 public void testCompareToSameHoursDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(11, 30);
 TimeOfDay time2 = new TimeOfDay(11, 45);
 int comparisonResult = time1.compareTo(time2);
 assertTrue(comparisonResult < 0);
 }

 // Test compareTo method with same hours and same minutes
 @Test
 public void testCompareToSameHoursSameMinutes() {
 TimeOfDay time1 = new TimeOfDay(11, 30);
 TimeOfDay time2 = new TimeOfDay(11, 30);
 int comparisonResult = time1.compareTo(time2);
 assertEquals(0, comparisonResult);
 }

 // Test compareTo method with different hours
 @Test
 public void testCompareToDifferentHours() {
 TimeOfDay time1 = new TimeOfDay(11, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 int comparisonResult = time1.compareTo(time2);
 assertTrue(comparisonResult < 0);
 }

 // Test getHours method
 @Test
 public void testGetHours() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals(15, time.getHours());
 }

 // Test getMinutes method
 @Test
 public void testGetMinutes() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals(45, time.getMinutes());
 }

 // Test toString method
 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals("15:45", time.toString());
 }

 // Test equals method with same object
 @Test
 public void testEqualsSameObject() {
 TimeOfDay time1 = new TimeOfDay(15, 45);
 TimeOfDay time2 = time1;
 assertTrue(time1.equals(time2));
 }

 // Test equals method with null object
 @Test(expected = IllegalArgumentException.class)
 public void testEqualsNullObject() {
 TimeOfDay time1 = new TimeOfDay(15, 45);
 assertFalse(time1.equals(null));
 }

 // Test equals method with different classes
 @Test
 public void testEqualsDifferentClasses() {
 TimeOfDay time1 = new TimeOfDay(15, 45);
 String str = "15:45";
 assertFalse(time1.equals(str));
 }

 // Test equals method with different TimeOfDay objects
 @Test
 public void testEqualsDifferentTimeOfDayObjects() {
 TimeOfDay time1 = new TimeOfDay(15, 45);
 TimeOfDay time2 = new TimeOfDay(16, 30);
 assertFalse(time1.equals(time2));
 }

 // Test equals method with same hours and same minutes
 @Test
 public void testEqualsSameTimeOfDay() {
 TimeOfDay time1 = new TimeOfDay(15, 45);
 TimeOfDay time2 = new TimeOfDay(15, 45);
 assertTrue(time1.equals(time2));
 }

 // Test hashCode and equals methods consistency
 @Test
 public void testHashCodeAndEqualsConsistency() {
 TimeOfDay time1 = new TimeOfDay(15, 45);
 TimeOfDay time2 = new TimeOfDay(15, 45);
 assertEquals(time1.hashCode(), time2.hashCode());
 }
}