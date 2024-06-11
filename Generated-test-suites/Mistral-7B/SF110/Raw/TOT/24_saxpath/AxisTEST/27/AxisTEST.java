import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int[] validAxisNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int axisNum : validAxisNumbers) {
 String expectedResult = Axis.lookup(axisNum);
 String result = Axis.lookup(axisNum);
 assertEquals(expectedResult, result);
 }

 int invalidAxisNumber = 0;
 String result = Axis.lookup(invalidAxisNumber);
 assertNull(result);
 }

 @Test
 public void testLookupByAxisName() {
 String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (String axisName : validAxisNames) {
 int expectedResult = Axis.lookup(axisName);
 int result = Axis.lookup(axisName);
 assertEquals(expectedResult, result);
 }

 String invalidAxisName = "invalidAxisName";
 int result = Axis.lookup(invalidAxisName);
 assertEquals(Axis.INVALID_AXIS, result);
 }
}