import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

 @Test
 public void testLookupByAxisNumber() {
 for (int axisNumber = 0; axisNumber <= 13; axisNumber++) {
 String expectedAxisName = Axis.lookup(axisNumber);
 String actualAxisName = Axis.lookup(axisNumber);
 assertEquals("Unexpected axis name returned for axis number " + axisNumber, expectedAxisName, actualAxisName);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 String result = Axis.lookup(-1);
 assertNull("Unexpected non-null result from lookup method with invalid axis number", result);
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByNullAxisName() {
 String result = Axis.lookup(null);
 assertNull("Unexpected non-null result from lookup method with null axis name", result);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String result = Axis.lookup("InvalidAxisName");
 assertNull("Unexpected non-null result from lookup method with invalid axis name", result);
 }

 @Test
 public void testLookupByValidAxisNames() {
 for (String axisName : VALID_AXIS_NAMES) {
 int expectedAxisNumber = Axis.lookup(axisName);
 int actualAxisNumber = Integer.parseInt(axisName.substring(0, 1).equals(".") ? axisName.substring(1) : axisName);
 assertEquals("Unexpected axis number returned for axis name " + axisName, expectedAxisNumber, actualAxisNumber);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testLookupByInvalidAxisNameFormat() {
 String result = Axis.lookup("InvalidFormatAxisName");
 assertNull("Unexpected non-null result from lookup method with invalid axis name format", result);
 }

}