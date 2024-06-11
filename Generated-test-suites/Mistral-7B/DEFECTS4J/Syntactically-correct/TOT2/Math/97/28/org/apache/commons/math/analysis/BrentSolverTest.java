package org.spaceroots.mantissa.roots.test;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.*;
import org.spaceroots.mantissa.roots.*;

public class BrentSolverTest {

  // Test cases by Expert 1
  @Test
  public void testFindRootNormal() throws FunctionException {
    // Test code here
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    // Test code here
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    // Test code here
  }

  // Test cases by Expert 2
  @Test
  public void testGetRootNormal() {
    // Test code here
  }

  @Test
  public void testGetRootNotCalculated() {
    // Test code here
  }

  @Test(expected = IllegalStateException.class)
  public void testGetRootUninitialized() {
    // Test code here
  }

  // Test cases by Expert 3
  @Test
  public void testFindRootNormal() throws FunctionException {
    // Test code here
  }

  @Test
  public void testFindRootMultipleRoots() throws FunctionException {
    // Test code here
  }

  @Test(expected = RuntimeException.class)
  public void testFindRootInfiniteRoot() throws FunctionException {
    // Test code here
  }

}