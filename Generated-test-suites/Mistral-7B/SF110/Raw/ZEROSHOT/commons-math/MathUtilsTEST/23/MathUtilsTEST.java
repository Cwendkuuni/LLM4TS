package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

 @Test
 public void testHash() {
 // Test double hash
 double value = 3.14;
 int expected = 1314664131; // Hashcode for 3.14
 int actual = MathUtils.hash(value);
 assertEquals("Hashcode for double is incorrect.", expected, actual);

 // Test double array hash
 double[] values = {1.2, 2.3, 3.14};
 int arrayHash = 142933451; // Hashcode for [1.2, 2.3, 3.14]
 int actualArrayHash = MathUtils.hash(values);
 assertEquals("Hashcode for double array is incorrect.", arrayHash, actualArrayHash);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFinite_Double_NaN() {
 MathUtils.checkFinite(Double.NaN);
 }

 @Test(expected = NotFiniteNumberException.class)
 public void testCheckFinite_Double_Infinite() {
 MathUtils.checkFinite(Double.POSITIVE_INFINITY);
 MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
 }

 @Test
 public void testCheckFinite_Double_Finite() {
 double finiteValue = 3.14;
 MathUtils.checkFinite(finiteValue);
 }

 @Test
 public void testCheckFinite_DoubleArray() {
 double[] finiteValues = {1.2, 2.3, 3.14};
 MathUtils.checkFinite(finiteValues);
 }

 @Test(expected = NullArgumentException.class)
 public void testCheckNotNull_NullObject() {
 MathUtils.checkNotNull(null, "Object cannot be null.");
 }

 @Test
 public void testCheckNotNull_ValidObject() {
 Object validObject = new Object();
 MathUtils.checkNotNull(validObject);
 }
}