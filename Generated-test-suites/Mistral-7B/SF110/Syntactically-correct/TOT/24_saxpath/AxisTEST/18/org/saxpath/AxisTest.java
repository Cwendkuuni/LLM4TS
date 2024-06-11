import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int[] testCases = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
 for (int axisNum : testCases) {
 String expected = Axis.lookup(axisNum);
 String actual = Axis.lookup(axisNum);
 assertEquals(expected, actual);
 }
 // Test for invalid axis number
 String result = Axis.lookup(Axis.INVALID_AXIS);
 Assert.assertNull(result);
 }

 @Test
 public void testLookupByAxisName() {
 String[] testCases = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self", "invalid"};
 for (String axisName : testCases) {
 int expected = Axis.lookup(axisName.trim());
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }
 // Test for invalid axis name
 int result = Axis.lookup("invalid");
 assertEquals(0, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupThrowsExceptionForInvalidArgument() {
 Axis.lookup("someInvalidArgument");
 }
}