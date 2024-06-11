import org.junit.Test;
import org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupIntPositiveCases() {
 int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 int[] actual;
 for (int i = 0; i < expected.length; i++) {
 actual = Axis.lookup(i + 1);
 assertEquals(expected[i], actual[0]);
 }
 }

 @Test
 public void testLookupIntEdgeCases() {
 // edge case: minimum valid value
 int axisNum = Axis.INVALID_AXIS + 1;
 int[] actual = Axis.lookup(axisNum);
 assertNull(actual);

 // edge case: maximum valid value
 int axisNumMax = Axis.ANCESTOR_OR_SELF.length();
 int[] actualMax = Axis.lookup(axisNumMax);
 assertArrayEquals(new int[]{Axis.ANCIESTOR_OR_SELF}, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupIntInvalidValue() {
 int axisNum = 0;
 try {
 Axis.lookup(axisNum);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test
 public void testLookupStringPositiveCases() {
 String[] expected = {"child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 String[] actual;
 for (int i = 0; i < expected.length; i++) {
 actual = Axis.lookup(expected[i]);
 assertEquals(expected[i], actual[0]);
 }
 }

 @Test
 public void testLookupStringEdgeCases() {
 String axisName = "";
 String[] actual = Axis.lookup(axisName);
 assertArrayEquals(new String[]{null}, actual);

 axisName = "InvalidAxis";
 actual = Axis.lookup(axisName);
 assertArrayEquals(new String[]{null}, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupStringInvalidValue() {
 String axisName = "InvalidAxis";
 try {
 Axis.lookup(axisName);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }
}