import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int[] validAxes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int axis : validAxes) {
 String expected = Axis.lookup(axis);
 String actual = Axis.lookup(axis);
 assertEquals(expected, actual);
 }

 // Test for invalid axis number
 String result = Axis.lookup(Axis.INVALID_AXIS);
 assertNull(result);
 }

 @Test
 public void testLookupByAxisName() {
 String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (String axisName : validAxisNames) {
 int expected = Axis.lookup(axisName);
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }

 // Test for invalid axis name
 String invalidAxisName = "invalid";
 int result = Axis.lookup(invalidAxisName);
 assertEquals(Axis.INVALID_AXIS, result);
 }
}