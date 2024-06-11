package saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int axisNumber = 1;
 String expected = "child";
 String actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 5;
 expected = "following-sibling";
 actual = Axis.lookup(axisNumber);
 assertEquals(expected, actual);

 axisNumber = 14; // An invalid axis number for testing error cases
 actual = Axis.lookup(axisNumber);
 assertEquals(null, actual);
 }

 @Test
 public void testLookupByAxisName() {
 String axisName = "parent";
 int expected = 3;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 axisName = "XPath1"; // An invalid axis name for testing error cases
 actual = Axis.lookup(axisName);
 assertEquals(0, actual);
 }
}