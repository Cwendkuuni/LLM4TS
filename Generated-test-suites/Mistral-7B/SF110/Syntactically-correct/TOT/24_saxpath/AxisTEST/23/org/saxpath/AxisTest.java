import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int axisNum = 1;
 String expectedResult = "child";
 String actualResult = Axis.lookup(axisNum);
 assertEquals(expectedResult, actualResult);

 axisNum = 10;
 expectedResult = "namespace";
 actualResult = Axis.lookup(axisNum);
 assertEquals(expectedResult, actualResult);

 axisNum = 14; // invalid axis number
 actualResult = Axis.lookup(axisNum);
 assertNull(actualResult);
 }

 @Test
 public void testLookupByAxisName() {
 String axisName = "child";
 int expectedResult = 1;
 int actualResult = Axis.lookup(axisName);
 assertEquals(expectedResult, actualResult);

 axisName = "descendant-or-self";
 expectedResult = 12;
 actualResult = Axis.lookup(axisName);
 assertEquals(expectedResult, actualResult);

 axisName = "invalid_axis"; // invalid axis name
 actualResult = Axis.lookup(axisName);
 assertEquals(Axis.INVALID_AXIS, actualResult);
 }
}