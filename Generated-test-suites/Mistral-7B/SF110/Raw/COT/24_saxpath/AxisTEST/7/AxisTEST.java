import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 private static final int[] VALID_AXIS_NUMBERS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

 @Test
 public void testLookupByAxisNumber() {
 for (int i = 0; i < VALID_AXIS_NUMBERS.length; i++) {
 int axisNumber = VALID_AXIS_NUMBERS[i];
 String expectedResult = Axis.lookup(axisNumber);
 String actualResult = Axis.lookup(axisNumber);
 assertEquals(expectedResult, actualResult);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 int invalidAxisNumber = 0;
 String actualResult = Axis.lookup(invalidAxisNumber);
 assertNull(actualResult);
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByNullAxisName() {
 String nullAxisName = null;
 int actualResult = Axis.lookup(nullAxisName);
 assertNull(actualResult);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String invalidAxisName = "invalidAxis";
 int actualResult = Axis.lookup(invalidAxisName);
 assertEquals(0, actualResult);
 }

 @Test
 public void testLookupByEmptyAxisName() {
 String emptyAxisName = "";
 int actualResult = Axis.lookup(emptyAxisName);
 assertEquals(0, actualResult);
 }

 @Test
 public void testLookupByBlankAxisName() {
 String blankAxisName = " ";
 int actualResult = Axis.lookup(blankAxisName);
 assertEquals(0, actualResult);
 }

 @Test
 public void testLookupByWhiteSpacedAxisName() {
 String whiteSpacedAxisName = "child   ";
 int actualResult = Axis.lookup(whiteSpacedAxisName);
 assertEquals(1, actualResult);
 }

 @Test
 public void testLookupByCaseInsensitiveAxisName() {
 String caseInsensitiveAxisName = "child";
 int actualResult = Axis.lookup(caseInsensitiveAxisName);
 assertEquals(1, actualResult);
 }

 @Test
 public void testLookupByAxisNameWithHyphen() {
 String axisNameWithHyphen = "following-sibling";
 int actualResult = Axis.lookup(axisNameWithHyphen);
 assertEquals(5, actualResult);
 }

}