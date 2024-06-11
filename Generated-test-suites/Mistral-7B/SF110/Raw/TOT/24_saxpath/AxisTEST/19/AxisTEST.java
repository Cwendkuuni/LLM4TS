import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupByValidAxisNumber() {
 int axisNum = 1;
 String expected = "child";
 String actual = Axis.lookup(axisNum);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupByValidAxisName() {
 String axisName = "child";
 int expected = 1;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupByInvalidAxisNumber() {
 int axisNum = 14;
 String expected = null;
 String actual = Axis.lookup(axisNum);
 assertNull(actual);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String axisName = "invalid";
 int expected = 0;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupByAttributeAxisName() {
 String axisName = "attribute";
 int expected = 9;
 int actual = Axis.lookup(axisName);
 assertEquals(expected, actual);

 String axisNameLowerCase = "ATTRIBUTE";
 int actualLowerCase = Axis.lookup(axisNameLowerCase);
 assertEquals(expected, actualLowerCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupByUnknownAxisNameThrowsException() {
 String axisName = "unknown";
 Axis.lookup(axisName);
 }
}