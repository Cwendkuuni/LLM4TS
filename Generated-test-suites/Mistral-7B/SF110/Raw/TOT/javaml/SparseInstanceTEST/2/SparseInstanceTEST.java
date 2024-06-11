package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptySparseInstance = new SparseInstance();
 assertEquals(0, emptySparseInstance.noAttributes());
 assertTrue(emptySparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 assertNull(sparseInstance.get(0));
 assertNull(sparseInstance.get(1));
 assertNull(sparseInstance.get(2));
 }

 @Test
 public void testSparseInstanceWithInitializedData() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 assertEquals(1.0, sparseInstance.get(0), 0.01);
 assertEquals(2.0, sparseInstance.get(1), 0.01);
 assertEquals(3.0, sparseInstance.get(2), 0.01);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testAddAttribute() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(1), 0.01);
 }

 @Test
 public void testEmptyAddAttribute() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(1), null);
 assertEquals(1, sparseInstance.noAttributes());
 assertNull(sparseInstance.get(1));
 }

 @Test
 public void testAddMultipleAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(2.0));
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(1), 0.01);
 assertEquals(2.0, sparseInstance.get(2), 0.01);
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 assertNull(sparseInstance.get(1));
 }

 @Test
 public void testRemoveNonExistentAttribute() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(4);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.get(0));
 }
}