import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

 private static final String[] VALID_AXIS_NAMES = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};

 @Test
 public void testLookupByAxisNumber() {
 for (int i = 1; i <= Axis.AXIS_COUNT; i++) {
 int axisNum = i;
 String axisName = Axis.lookup(axisNum);
 assertEquals("Unexpected axis name for given axis number", Axis.lookupNames()[axisNum], axisName);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLookupByInvalidAxisNumber() {
 int axisNum = 0;
 String axisName = Axis.lookup(axisNum);
 }

 @Test(expected = IllegalArgumentException.class)
 @SuppressWarnings("ConstantConditions")
 public void testLookupByInvalidAxisNameNull() {
 String axisName = null;
 int axisNum = Axis.lookup(axisName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupByInvalidAxisName() {
 String axisName = "invalid";
 int axisNum = Axis.lookup(axisName);
 }

 @Test
 public void testLookupByName() {
 for (String axisName : VALID_AXIS_NAMES) {
 int axisNum = Axis.lookup(axisName);
 assertEquals("Unexpected axis number for given axis name", Axis.lookupNames()[Axis.AXIS_COUNT + axisNum], axisName);
 }
 }

 private static final String[] Axis_lookupNames = { "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self" };

 @Test
 public void testLookupAll() {
 for (String axisName : VALID_AXIS_NAMES) {
 int axisNum = Axis.lookup(axisName);
 String expectedAxisName = Axis_lookupNames[axisNum];
 String actualAxisName = Axis_lookupNames[Axis.AXIS_COUNT + axisNum];
 assertEquals("Unexpected axis name for given axis number", expectedAxisName, actualAxisName);
 }
 }

}