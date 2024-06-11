import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

 @Test
 public void testLookupByAxisNumber() {
 for (int axisNum : new int[] {-1, 0, 1, 14}) {
 String expected = null;
 int axis = Axis.lookup(axisNum);
 if (axis >= 0) {
 expected = Axis.lookup(VALID_AXIS_NAMES[axis]);
 }
 String actual = Axis.lookup(axisNum);
 assertEquals(expected, actual);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 String actual = Axis.lookup(-1);
 fail("Expected NullPointerException but got: " + actual);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String actual = Axis.lookup("invalidAxis");
 assertNull(actual);
 }

 @Test
 public void testLookupByUnknownAxisName() {
 String actual = Axis.lookup("unknownAxis");
 assertNull(actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupByNegativeAxisNameLength() {
 String axisName = new String(new char[0]);
 String actual = Axis.lookup(axisName);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testLookupByEmptyAxisName() {
 String actual = Axis.lookup("");
 assertNull(actual);
 }

 @Test
 public void testLookupByAxisNameWithLeadingOrTrailingWhiteSpaces() {
 String actual = Axis.lookup(" child ");
 assertEquals("child", actual);
 }

 @Test
 public void testLookupByAxisNameWithMultipleWhiteSpaces() {
 String actual = Axis.lookup("following   sibling");
 assertEquals("following-sibling", actual);
 }
}