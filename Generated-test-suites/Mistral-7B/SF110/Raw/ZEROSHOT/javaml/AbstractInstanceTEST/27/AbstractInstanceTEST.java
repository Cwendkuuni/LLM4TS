package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @Test
 public void testConstructor() {
 // Test default constructor
 instance1 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertNull(instance1.classValue());

 // Test constructor with classValue
 instance2 = new AbstractInstance("testClassValue");
 assertEquals(1, instance2.getID());
 assertSame("testClassValue", instance2.classValue());
 }

 @Test
 public void testGetID() {
 // Test getID for different instances
 instance1 = new AbstractInstance();
 instance2 = new AbstractInstance("testClassValue");
 instance3 = new AbstractInstance("testClassValue2");
 instance4 = new AbstractInstance(instance2);
 assertEquals(0, instance1.getID());
 assertEquals(1, instance2.getID());
 assertEquals(2, instance3.getID());
 assertEquals(1, instance4.getID());
 }

 @Test
 public void testIterator() {
 // Test iterator for empty instance
 instance1 = new AbstractInstance();
 assertNotNull(instance1.iterator());
 assertEquals(0, instance1.size());

 // Test iterator for instance with values
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 Iterator<Double> iterator = instance2.iterator();
 assertEquals(3, instance2.size());
 assertTrue(iterator.hasNext());
 assertEquals(1.0, iterator.next(), 0.0);
 assertTrue(iterator.hasNext());
 assertEquals(2.0, iterator.next(), 0.0);
 assertTrue(iterator.hasNext());
 assertEquals(3.0, iterator.next(), 0.0);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMinusWithInstance() {
 // Test minus with another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 5.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 1.0);
 }} );
 AbstractInstance result = instance1.minus(instance2);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 4.0);
 put(new Double(1.0), 1.0);
 put(new Double(2.0), 2.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testMinusWithDouble() {
 // Test minus with double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 5.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 AbstractInstance result = instance1.minus(3.0);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), -1.0);
 put(new Double(2.0), 0.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testDivideWithInstance() {
 // Test divide with another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 10.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), 1.0);
 put(new Double(2.0), 1.5);
 }} );
 AbstractInstance result = instance1.divide(instance2);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 5.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 2.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testDivideWithDouble() {
 // Test divide with double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 10.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 AbstractInstance result = instance1.divide(2.0);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 5.0);
 put(new Double(1.0), 1.0);
 put(new Double(2.0), 1.5);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testMultiplyWithInstance() {
 // Test multiply with another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 4.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 3.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 1.5);
 }} );
 AbstractInstance result = instance1.multiply(instance2);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 6.0);
 put(new Double(1.0), 6.0);
 put(new Double(2.0), 6.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testMultiplyWithDouble() {
 // Test multiply with double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 4.0);
 }} );
 AbstractInstance result = instance1.multiply(3.0);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 6.0);
 put(new Double(1.0), 9.0);
 put(new Double(2.0), 12.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testMinusWithDoubleAndInstance() {
 // Test minus with double and another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 5.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 1.0);
 }} );
 AbstractInstance result = instance1.minus(2.0, instance2);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 3.0);
 put(new Double(1.0), 1.0);
 put(new Double(2.0), 2.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testMinusWithInstanceAndDouble() {
 // Test minus with another instance and double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 5.0);
 put(new Double(1.0), 2.0);
 put(new Double(2.0), 3.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 1.0);
 }} );
 AbstractInstance result = instance2.minus(instance1, 2.0);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), -3.0);
 put(new Double(1.0), -1.0);
 put(new Double(2.0), -1.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testAddWithInstance() {
 // Test add with another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 4.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 1.0);
 }} );
 AbstractInstance result = instance1.add(instance2);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 3.0);
 put(new Double(1.0), 6.0);
 put(new Double(2.0), 5.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testAddWithDoubleAndInstance() {
 // Test add with double and another instance
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 4.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 1.0);
 }} );
 AbstractInstance result = instance1.add(2.0, instance2);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 4.0);
 put(new Double(1.0), 6.0);
 put(new Double(2.0), 5.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testAddWithInstanceAndDouble() {
 // Test add with another instance and double
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 2.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 4.0);
 }} );
 instance2 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 1.0);
 put(new Double(1.0), 3.0);
 put(new Double(2.0), 1.0);
 }} );
 AbstractInstance result = instance2.add(instance1, 2.0);
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 4.0);
 put(new Double(1.0), 6.0);
 put(new Double(2.0), 5.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }

 @Test
 public void testSqrt() {
 // Test sqrt
 instance1 = new AbstractInstance(new HashMap<Object, Double>() {{
 put(new Double(0.0), 9.0);
 put(new Double(1.0), 16.0);
 put(new Double(2.0), 25.0);
 }} );
 AbstractInstance result = instance1.sqrt();
 Iterator<Double> resultIterator = result.iterator();
 HashMap<Object, Double> expectedMap = new HashMap<Object, Double>() {{
 put(new Double(0.0), 3.0);
 put(new Double(1.0), 4.0);
 put(new Double(2.0), 5.0);
 }} ;
 Map<Object, Double> resultMap = new HashMap<Object, Double>();
 while (resultIterator.hasNext()) {
 resultMap.put(resultIterator.next(), resultIterator.next());
 }
 assertEquals(resultMap, expectedMap);
 }
}