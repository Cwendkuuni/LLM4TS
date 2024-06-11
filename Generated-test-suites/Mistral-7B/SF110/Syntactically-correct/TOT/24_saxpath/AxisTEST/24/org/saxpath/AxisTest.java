
package org.saxpath.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.saxpath.Axis;

public class AxisTest {
  @Test
  public void testLookup() {
    // Test lookup(int axisNum) method
    for (int i = 0; i <= 13; i++) {
      String expected = Axis.lookup(i);
      int axisNum = i;
      String resultAxisName = Axis.lookup(axisNum);
      int resultAxisNum = Axis.lookup(resultAxisName);
      assertEquals(expected, resultAxisName);
      assertEquals(axisNum, resultAxisNum);
    }

    // Test lookup(String axisName) method
    String[] validAxisNames = new String[]{"child", "descendant", "parent", "ancestor", "following-sibling",
        "preceding-sibling", "following", "preceding", "attribute", "namespace", "self",
        "descendant-or-self", "ancestor-or-self"};
    for (String axisName : validAxisNames) {
      int expectedAxisNum = Integer.parseInt(axisName.replaceAll("-|_|\\s", "").toUpperCase());
      String resultAxisName = Axis.lookup(axisName);
      int resultAxisNum = Axis.lookup(resultAxisName);
      assertEquals(expectedAxisNum, resultAxisNum);
    }

    // Test invalid axis number
    int invalidAxisNum = -1;
    String resultAxisName1 = Axis.lookup(invalidAxisNum);
    int resultAxisNum1 = Axis.lookup(resultAxisName1);
    assertNull(resultAxisName1);
    assertEquals(0, resultAxisNum1);

    // Test invalid axis name
    String invalidAxisName = "invalidAxis";
    int resultAxisNum2 = Axis.lookup(invalidAxisName);
    assertEquals(0, resultAxisNum2);

    // Test null input
    String nullAxisName = null;
    int resultAxisNum3 = Axis.lookup(nullAxisName);
    assertEquals(0, resultAxisNum3);
  }
}
