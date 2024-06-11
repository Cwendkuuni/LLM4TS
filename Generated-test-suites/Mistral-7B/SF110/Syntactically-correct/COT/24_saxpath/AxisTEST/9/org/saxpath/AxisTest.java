import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 private String[] validAxisNames = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 private int[] expectedValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

 @Test
 public void testLookupByAxisNumber() {
 for (int i = 0; i < validAxisNames.length; i++) {
 int axisNum = Axis.lookup(validAxisNames[i].hashCode());
 String expectedAxisName = validAxisNames[i];
 int expectedValue = expectedValues[i];
 assertEquals(expectedAxisName, Axis.lookupName(axisNum));
 assertEquals(expectedValue, axisNum);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 int axisNum = Integer.MIN_VALUE;
 assertNull(Axis.lookup(axisNum));
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByNameNull() {
 assertNull(Axis.lookup(null));
 }

 @Test
 public void testLookupByNameInvalid() {
 String invalidAxisName = "invalidAxisName";
 int expectedValue = Axis.INVALID_AXIS;
 int actualValue = Axis.lookup(invalidAxisName.hashCode());
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLookupByNameCaseInsensitive() {
 String invalidAxisName = "cHilD";
 int expectedValue = 1;
 int actualValue = Axis.lookup(invalidAxisName.hashCode());
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLookupByNameEmptyString() {
 String emptyAxisName = "";
 int expectedValue = Axis.INVALID_AXIS;
 int actualValue = Axis.lookup(emptyAxisName.hashCode());
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLookupByNameWhiteSpace() {
 String whiteSpaceAxisName = " child ";
 int expectedValue = 1;
 int actualValue = Axis.lookup(whiteSpaceAxisName.hashCode());
 assertEquals(expectedValue, actualValue);
 }

}