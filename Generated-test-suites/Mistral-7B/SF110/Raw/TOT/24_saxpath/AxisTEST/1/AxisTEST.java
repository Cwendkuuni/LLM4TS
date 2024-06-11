import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {

 @Test
 public void testLookupByAxisNumber() {
 int[] axisNumbers = {-1, 0, 1, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
 String[] expectedAxisNames = {"", "child", "descendant", "parent", "ancestor", "following-sibling", "preceding-sibling", "following", "preceding", "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self", "invalid"};

 for (int i = 0; i < axisNumbers.length; i++) {
 String actualAxisName = Axis.lookup(axisNumbers[i]);
 assertEquals(expectedAxisNames[i], actualAxisName);
 }
 }

 @Test
 public void testLookupByInvalidAxisNumber() {
 String actualAxisName = Axis.lookup(-1);
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupByInvalidAxisName() {
 String actualAxisName = Axis.lookup("invalid");
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupByUnknownAxisNameWithNumberInString() {
 String actualAxisName = Axis.lookup("following-1");
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupByUnknownAxisNameWithHyphen() {
 String actualAxisName = Axis.lookup("following-sibling-1");
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupByUnknownAxisNameWithUnderscore() {
 String actualAxisName = Axis.lookup("following_sibling-1");
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupByUnknownAxisNameWithSpace() {
 String actualAxisName = Axis.lookup("following sibling-1");
 assertNull(actualAxisName);
 }
}