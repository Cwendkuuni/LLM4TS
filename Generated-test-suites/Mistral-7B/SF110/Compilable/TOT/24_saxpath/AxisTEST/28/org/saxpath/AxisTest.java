package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int axisNumber = 1;
 String expectedAxisName = "child";
 String result = Axis.lookup(axisNumber);
 assertEquals(expectedAxisName, result);

 axisNumber = 13;
 expectedAxisName = "ancestor-or-self";
 result = Axis.lookup(axisNumber);
 assertEquals(expectedAxisName, result);

 axisNumber = 0; // invalid axis number
 result = Axis.lookup(axisNumber);
 assertEquals(null, result);
 }

 @Test
 public void testLookupByAxisName() {
 String axisName = "child";
 int expectedAxisNumber = 1;
 int result = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, result);

 axisName = "ancestor-or-self";
 expectedAxisNumber = 13;
 result = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, result);

 axisName = "invalid";
 result = Axis.lookup(axisName);
 assertEquals(0, result);
 }
}