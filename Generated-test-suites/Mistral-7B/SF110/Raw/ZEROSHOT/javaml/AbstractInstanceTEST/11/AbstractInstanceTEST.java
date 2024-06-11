package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import net.sf.javaml.core.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @Before
 public void setUp() {
 Map<String, Double> map1 = new HashMap<>();
 map1.put("Attribute1", 1.0);
 map1.put("Attribute2", 2.0);
 instance1 = new DenseInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("Attribute1", 3.0);
 map2.put("Attribute2", 4.0);
 instance2 = new DenseInstance(map2);

 Map<String, Double> map3 = new HashMap<>();
 map3.put("Attribute1", 5.0);
 map3.put("Attribute2", 6.0);
 instance3 = new DenseInstance(map3);

 Map<String, Double> map4 = new HashMap<>();
 map4.put("Attribute1", 7.0);
 map4.put("Attribute2", 8.0);
 instance4 = new DenseInstance(map4);
 }

 @Test
 public void testGetID() {
 assertEquals(1, instance1.getID());
 assertEquals(2, instance2.getID());
 }

 @Test
 public void testIterator() {
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = instance1.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(1.0, list.get(0), 0.0);
 assertEquals(2.0, list.get(1), 0.0);
 }

 @Test
 public void testConstructor() {
 assertNotNull(instance1);
 }

 @Test
 public void testConstructorWithClassValue() {
 Object classValue = new Object();
 AbstractInstance instance5 = new AbstractInstance(classValue);
 assertNotNull(instance5);
 assertEquals(instance5.getClassValue(), classValue);
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), null);
 Object classValue = new Object();
 instance2.setClassValue(classValue);
 assertEquals(instance2.classValue(), classValue);
 }

 @Test
 public void testSetClassValue() {
 Object classValue = new Object();
 instance1.setClassValue(classValue);
 assertEquals(instance1.classValue(), classValue);
 }

 @Test
 public void testMinusInstance() {
 AbstractInstance result = instance1.minus(instance2);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(-2.0, list.get(0), 0.0);
 assertEquals(-2.0, list.get(1), 0.0);
 }

 @Test
 public void testMinusDouble() {
 AbstractInstance result = instance1.minus(1.5);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(0.5, list.get(0), 0.0);
 assertEquals(0.5, list.get(1), 0.0);
 }

 @Test
 public void testDivideDouble() {
 AbstractInstance result = instance1.divide(2.0);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(0.5, list.get(0), 0.0);
 assertEquals(0.5, list.get(1), 0.0);
 }

 @Test
 public void testMultiplyDouble() {
 AbstractInstance result = instance1.multiply(2.0);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(2.0, list.get(0), 0.0);
 assertEquals(2.0, list.get(1), 0.0);
 }

 @Test
 public void testEquals() {
 assertTrue(instance1.equals(instance1));
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testHashCode() {
 assertEquals(instance1.hashCode(), instance1.getID());
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertNotEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testMinusDoubleAndInstance() {
 AbstractInstance result = instance1.minus(instance2.add(instance3));
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(-6.0, list.get(0), 0.0);
 assertEquals(-6.0, list.get(1), 0.0);
 }

 @Test
 public void testDivideInstance() {
 AbstractInstance result = instance1.divide(instance2);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(0.5, list.get(0), 0.0);
 assertEquals(0.5, list.get(1), 0.0);
 }

 @Test
 public void testMultiplyInstance() {
 AbstractInstance result = instance1.multiply(instance2);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(14.0, list.get(0), 0.0);
 assertEquals(16.0, list.get(1), 0.0);
 }

 @Test
 public void testAddDouble() {
 AbstractInstance result = instance1.add(1.5);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(2.5, list.get(0), 0.0);
 assertEquals(3.5, list.get(1), 0.0);
 }

 @Test
 public void testAddInstance() {
 AbstractInstance result = instance1.add(instance2);
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(4.0, list.get(0), 0.0);
 assertEquals(6.0, list.get(1), 0.0);
 }

 @Test
 public void testSqrt() {
 AbstractInstance result = instance1.sqrt();
 List<Double> list = new ArrayList<>();
 Iterator<Double> it = result.iterator();
 while (it.hasNext()) {
 list.add(it.next());
 }
 assertEquals(2, list.size());
 assertEquals(1.0, list.get(0), 0.0);
 assertEquals(2.0, list.get(1), 0.0);
 }
}