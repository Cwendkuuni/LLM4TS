import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int axisNumber = 1;
 String expectedResult = "child";
 String result = Axis.lookup(axisNumber);
 assertEquals(expectedResult, result);

 axisNumber = 13;
 expectedResult = "ancestor-or-self";
 result = Axis.lookup(axisNumber);
 assertEquals(expectedResult, result);

 axisNumber = 0;
 result = Axis.lookup(axisNumber);
 assertNull(result);
 }

 @Test
 public void testLookupByAxisName() {
 String axisName = "descendant";
 int expectedResult = 2;
 int result = Axis.lookup(axisName);
 assertEquals(expectedResult, result);

 axisName = "following-sibling";
 expectedResult = 5;
 result = Axis.lookup(axisName);
 assertEquals(expectedResult, result);

 axisName = "invalidAxisName";
 result = Axis.lookup(axisName);
 assertEquals(Axis.INVALID_AXIS, result);
 }

 @Test
 public void testLookupInvalidArguments() {
 String result = Axis.lookup((Object)null);
 assertNull(result);

 int axisNumber = Integer.MIN_VALUE;
 result = Axis.lookup(axisNumber);
 assertNull(result);

 String axisName = "";
 result = Axis.lookup(axisName);
 assertNull(result);
 }
}