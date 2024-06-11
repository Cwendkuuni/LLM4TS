package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class SparseInstanceTest {

   @Test
   public void testEmptyConstructor() {
      SparseInstance sparseInstance = new SparseInstance();
      assertEquals(0, sparseInstance.noAttributes());
      assertNull(sparseInstance.getValue(0));
   }

   @Test
   public void testConstructorWithIntArgument() {
      SparseInstance sparseInstance = new SparseInstance(5);
      assertEquals(5, sparseInstance.noAttributes());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testMaxIntSize() {
      SparseInstance sparseInstance = new SparseInstance(Integer.MAX_VALUE + 1);
   }

   @Test
   public void testConstructorWithDoubleArray() {
      double[] datavector = {1.0, 2.0, 3.0};
      SparseInstance sparseInstance = new SparseInstance(datavector);
      assertEquals(3, sparseInstance.noAttributes());
      assertEquals(1.0, sparseInstance.getValue(0), 0.001);
      assertEquals(2.0, sparseInstance.getValue(1), 0.001);
      assertEquals(3.0, sparseInstance.getValue(2), 0.001);
      for (int i = 3; i < datavector.length; ++i) {
         assertNull(sparseInstance.getValue(i));
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullDoubleArray() {
      SparseInstance sparseInstance = new SparseInstance(null);
   }

   @Test
   public void testGetMethodWithValidKey() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      assertEquals(1.0, sparseInstance.get(0), 0.001);
   }

   @Test
   public void testGetMethodWithInvalidKey() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      assertEquals(0.0, sparseInstance.get(3), 0.001);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullKey() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.get(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetMethodWithNullKey() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.put(null, 4.0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetMethodWithInvalidKey() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.put(3, null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetMethodWithInvalidValue() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.put(0, new Object());
   }

   @Test
   public void testSetMethodWithValidKeyAndValue() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.put(4, 4.0);
      assertEquals(4.0, sparseInstance.getValue(4), 0.001);
   }

   @Test
   public void testRemoveAttribute() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.removeAttribute(0);
      assertEquals(2, sparseInstance.noAttributes());
      assertNull(sparseInstance.getValue(0));
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testRemoveAttributeWithInvalidIndex() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.removeAttribute(3);
   }

   @Test
   public void testClear() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.clear();
      assertEquals(0, sparseInstance.noAttributes());
      for (int i = 0; i < 3; ++i) {
         assertNull(sparseInstance.getValue(i));
      }
   }

   @Test
   public void testSize() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      assertEquals(3, sparseInstance.size());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testValuesUnmodifiable() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.values().add(1.5);
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testEntrySetUnmodifiable() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.entrySet().add(new AbstractMap.SimpleEntry<>(1, 1.5));
   }

   @Test(expected = UnsupportedOperationException.class)
   public void testKeySetUnmodifiable() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      sparseInstance.keySet().add(1);
   }

   @Test
   public void testToString() {
      SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0}", sparseInstance.toString());
   }

   @Test(expected = NullPointerException.class)
   public void testNullClassValueConstructor() {
      new SparseInstance(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNegativeNoAttributes() {
      new SparseInstance(-1);
   }

   @Test
   public void testCopy() {
      double[] datavector = {1.0, 2.0, 3.0};
      SparseInstance original = new SparseInstance(datavector);
      SparseInstance copy = original.copy();
      assertEquals(original, copy);
   }

   @Test(expected = NullPointerException.class)
   public void testNullDoubleArrayCopy() {
      SparseInstance original = new SparseInstance(new double[]{1.0, 2.0, 3.0});
      new SparseInstance(null, original.defaultValue, original.classValue());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNegativeDefaultValue() {
      new SparseInstance(5, -1.0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullClassValue() {
      new SparseInstance(5, 1.0, null);
   }
}