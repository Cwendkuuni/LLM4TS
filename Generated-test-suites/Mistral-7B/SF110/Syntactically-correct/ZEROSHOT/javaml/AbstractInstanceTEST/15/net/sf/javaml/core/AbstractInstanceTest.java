package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AbstractInstanceTest {

 @Test
 public void testAbstractInstance() {
 AbstractInstance instance1 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 1.0);
 put(K.TWO, 2.0);
 put(K.THREE, 3.0);
 }};
 }};

 AbstractInstance instance2 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 1.5);
 put(K.TWO, 2.5);
 put(K.THREE, 3.5);
 }};
 }};

 AbstractInstance instance3 = new AbstractInstance(null);

 AbstractInstance instance4 = new AbstractInstance(new Object());

 // Test getID
 assertEquals(0, instance3.getID());
 assertEquals(1, instance1.getID());
 assertEquals(2, instance2.getID());

 // Test iterator
 Iterator<Double> iterator1 = instance1.iterator();
 Iterator<Double> iterator2 = instance2.iterator();

 // Order of iteration should be the same for both instances
 while (iterator1.hasNext() && iterator2.hasNext()) {
 Double next1 = iterator1.next();
 Double next2 = iterator2.next();
 assertEquals(next1, next2);
 }

 // Test classValue() and setClassValue()
 Object classValue1 = instance1.classValue();
 Object classValue2 = instance2.classValue();
 assertNull(instance3.classValue());

 instance1.setClassValue(new Object());
 instance2.setClassValue(new Object());
 assertEquals(instance1.classValue(), instance2.classValue());

 // Test minus() with another instance
 AbstractInstance result1 = instance1.minus(instance2);
 AbstractInstance expectedResult1 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, -0.5);
 put(K.TWO, -0.5);
 put(K.THREE, -1.5);
 }};
 }};
 assertEquals(expectedResult1, result1);

 // Test minus() with a double
 AbstractInstance result2 = instance1.minus(1.0);
 AbstractInstance expectedResult2 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 0.0);
 put(K.TWO, 1.5);
 put(K.THREE, 2.0);
 }};
 }};
 assertEquals(expectedResult2, result2);

 // Test divide() with a double
 AbstractInstance result3 = instance1.divide(2.0);
 AbstractInstance expectedResult3 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 0.5);
 put(K.TWO, 1.25);
 put(K.THREE, 1.5);
 }};
 }};
 assertEquals(expectedResult3, result3);

 // Test multiply() with a double
 AbstractInstance result4 = instance1.multiply(2.0);
 AbstractInstance expectedResult4 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 2.0);
 put(K.TWO, 4.0);
 put(K.THREE, 6.0);
 }};
 }};
 assertEquals(expectedResult4, result4);

 // Test equals()
 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance1.hashCode()));
 assertFalse(instance1.equals(instance2));
 assertFalse(instance1.equals(null));

 // Test hashCode()
 int hashCode1 = instance1.hashCode();
 int hashCode2 = instance1.hashCode();
 assertNotSame(hashCode1, hashCode2);

 // Test minus() with an instance of the same class
 AbstractInstance instance5 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 1.0);
 put(K.TWO, 2.0);
 put(K.THREE, 3.0);
 }};
 }};
 AbstractInstance result5 = instance1.minus(instance5);
 AbstractInstance expectedResult5 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 0.0);
 put(K.TWO, 0.0);
 put(K.THREE, 0.0);
 }};
 }};
 assertEquals(expectedResult5, result5);

 // Test divide() with an instance of the same class
 AbstractInstance instance6 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 2.0);
 put(K.TWO, 4.0);
 put(K.THREE, 6.0);
 }};
 }};
 AbstractInstance result6 = instance1.divide(instance6);
 AbstractInstance expectedResult6 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 0.25);
 put(K.TWO, 0.5);
 put(K.THREE, 0.5);
 }};
 }};
 assertEquals(expectedResult6, result6);

 // Test add() with a double
 AbstractInstance result7 = instance1.add(1.0);
 AbstractInstance expectedResult7 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 1.0);
 put(K.TWO, 2.1);
 put(K.THREE, 3.1);
 }};
 }};
 assertEquals(expectedResult7, result7);

 // Test add() with an instance of the same class
 AbstractInstance result8 = instance1.add(instance5);
 AbstractInstance expectedResult8 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, 2.0);
 put(K.TWO, 4.1);
 put(K.THREE, 6.1);
 }};
 }};
 assertEquals(expectedResult8, result8);

 // Test sqrt()
 AbstractInstance result9 = instance1.sqrt();
 AbstractInstance expectedResult9 = new AbstractInstance() {
 @Override
 public Map<K, Double> getAttributeMap() {
 return new LinkedHashMap<K, Double>() {{
 put(K.ONE, Math.sqrt(1.0));
 put(K.TWO, Math.sqrt(2.0));
 put(K.THREE, Math.sqrt(3.0));
 }};
 }};
 assertEquals(expectedResult9, result9);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidMinus() {
 instance1.minus(null);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidDivide() {
 instance1.divide(null);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidMultiply() {
 instance1.multiply(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveIterator() {
 Iterator<Double> iterator = instance1.iterator();
 iterator.next();
 iterator.remove();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstanceValueIterator() {
 InstanceValueIterator iterator = instance1.new InstanceValueIterator();
 iterator.next();
 iterator.remove();
 }
}