package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;

public class SparseInstanceTest {

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 int noAttributes = 5;
 instance.setNoAttributes(noAttributes);
 assertEquals(noAttributes, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegative() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesMaxInt() {
 SparseInstance instance = new SparseInstance();
 int maxInt = Integer.MAX_VALUE;
 instance.setNoAttributes(maxInt);
 }

 // Add test cases for other methods here
}