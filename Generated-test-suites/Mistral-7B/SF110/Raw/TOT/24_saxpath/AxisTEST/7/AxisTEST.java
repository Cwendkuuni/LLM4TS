import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int expectedAxis = 1;
 String result = Axis.lookup(expectedAxis);
 String expectedResult = "child";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testLookupByInvalidAxisNumber() {
 int expectedAxis = 100;
 String result = Axis.lookup(expectedAxis);
 assertNull(result);
 }

 @Test
 public void testLookupByAxisName() {
 String expectedAxis = "parent";
 int result = Axis.lookup(expectedAxis);
 int expectedResult = 3;
 assertEquals(expectedResult, result);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String expectedAxis = "invalid";
 int result = Axis.lookup(expectedAxis);
 assertEquals(Axis.INVALID_AXIS, result);
 }

 @Test
 public void testLookupByEmptyString() {
 String expectedAxis = "";
 int result = Axis.lookup(expectedAxis);
 assertEquals(Axis.INVALID_AXIS, result);
 }

 @Test
 public void testLookupByNullString() {
 String expectedAxis = null;
 int result = Axis.lookup(expectedAxis);
 assertEquals(Axis.INVALID_AXIS, result);
 }
}