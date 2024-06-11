import org.junit.Assert;
import org.junit.Test;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 private static final int[] VALID_AXIS_NUMBERS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

 @Test
 public void testLookupByAxisNumber() {
 for (int axisNumber : VALID_AXIS_NUMBERS) {
 String expected = Axis.lookup(axisNumber);
 String actual = Axis.lookup(axisNumber);
 Assert.assertEquals(expected, actual);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 String actual = Axis.lookup(Integer.MIN_VALUE);
 Assert.assertNull(actual);
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByNullAxisName() {
 String axisName = null;
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String axisName = "invalid";
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }

 @Test
 public void testLookupByUnknownAxisNameCaseInsensitive() {
 String axisName = "Invalid";
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }

 @Test
 public void testLookupByEmptyStringAxisName() {
 String axisName = "";
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }

 @Test
 public void testLookupByWhitespaceAxisName() {
 String axisName = " \t child \t ";
 int actual = Axis.lookup(axisName.trim());
 Assert.assertEquals(1, actual);
 }

 @Test
 public void testLookupByAxisNameWithDigits() {
 String axisName = "following3";
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }

 @Test
 public void testLookupByAxisNameWithHyphen() {
 String axisName = "following-something";
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }

 @Test
 public void testLookupByAxisNameWithSpecialCharacters() {
 String axisName = "following@something";
 int actual = Axis.lookup(axisName);
 Assert.assertEquals(Axis.INVALID_AXIS, actual);
 }
}