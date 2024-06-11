package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
  @Test
  public void testLookupByAxisNumber() {
    String expected = "child";
    int axisNum = 1;
    String result = Axis.lookup(axisNum);
    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testLookupByInvalidAxisNumber() {
    int axisNum = 14;
    String result = Axis.lookup(axisNum);
    assertNull(result);
  }

  @Test
  public void testLookupByValidAxisName() {
    String expected = "following";
    String axisName = "following";
    int axisNum = Axis.lookup(axisName);
    assertEquals(expected, Integer.toString(axisNum));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLookupByInvalidAxisName() {
    String axisName = "invalid";
    int axisNum = Axis.lookup(axisName);
    assertNull(axisNum);
  }

  @Test
  public void testLookupInvalidAxisNumberDefault() {
    int axisNum = Axis.lookup(-1);
    assertNull(axisNum);
  }

  @Test
  public void testLookupValidAxisName() {
    int axisNum = Axis.lookup("following");
    assertEquals(7, axisNum);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLookupEmptyAxisName() {
    int axisNum = Axis.lookup("");
    assertNull(axisNum);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLookupNullAxisName() {
    int axisNum = Axis.lookup(null);
    assertNull(axisNum);
  }
}