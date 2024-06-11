package org.saxpath;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AxisTest {

 @Test
 public void testLookupInt() {
 int[] axisNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 String[] expectedStrings = {"", "child", "descendant", "parent", "ancestor",
 "following-sibling", "preceding-sibling", "following", "preceding",
 "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 for (int i = 0; i < axisNumbers.length; i++) {
 String result = Axis.lookup(axisNumbers[i]);
 assertEquals(expectedStrings[i], result);
 }
 }

 @Test
 public void testLookupString() {
 String[] axisNames = {"", "child", "descendant", "parent", "ancestor",
 "following-sibling", "preceding-sibling", "following", "preceding",
 "attribute", "namespace", "self", "descendant-or-self", "ancestor-or-self"};
 int[] expectedAxisNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
 for (int i = 0; i < axisNames.length; i++) {
 int result = Axis.lookup(axisNames[i]);
 assertEquals(expectedAxisNumbers[i], result);
 }
 }

}