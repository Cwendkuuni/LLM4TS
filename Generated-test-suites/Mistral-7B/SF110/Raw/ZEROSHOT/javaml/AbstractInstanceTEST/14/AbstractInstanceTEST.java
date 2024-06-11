package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1, instance2, instance3;

 @Before
 public void setUp() {
 Map<Object, Double> map1 = new HashMap<>();
 Map<Object, Double> map2 = new HashMap<>();
 Map<Object, Double> map3 = new HashMap<>();

 instance1 = new AbstractInstance() {
 @Override
 public Map<Object, Double> toMap() {
 return map1;
 }
 };
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);

 instance2 = new AbstractInstance() {
 @Override
 public Map<Object, Double> toMap() {
 return map2;
 }
 };
 instance2.put(0, 1.1);
 instance2.put(1, 2.1);
 instance2.put(2, 3.1);

 instance3 = new AbstractInstance(3.0);
 instance3.put(0, 1.2);
 instance3.put(1, 2.2);
 instance3.put(2, 3.2);
 }

 @Test
 public void testGetID() {
 assertEquals("ID test for instance1", 0, instance1.getID());
 assertEquals("ID test for instance2", instance1.getID() + 1, instance2.getID());
 assertEquals("ID test for instance3", instance2.getID() + 1, instance3.getID());
 }

 @Test
 public void testIterator() {
 Iterator<Double> it1 = instance1.iterator();
 Iterator<Double> it2 = instance2.iterator();
 Iterator<Double> it3 = instance3.iterator();

 assertEquals("Size test for instance1's iterator", 3, instance1.noAttributes());
 assertEquals("Size test for instance2's iterator", 3, instance2.noAttributes());
 assertEquals("Size test for instance3's iterator", 3, instance3.noAttributes());

 assertTrue("Iterator has next test for instance1", it1.hasNext());
 assertTrue("Iterator has next test for instance2", it2.hasNext());
 assertTrue("Iterator has next test for instance3", it3.hasNext());

 assertEquals("First value test for instance1", 1.0, it1.next());
 assertEquals("First value test for instance2", 1.1, it2.next());
 assertEquals("First value test for instance3", 1.2, it3.next());

 it1.next();
 it1.next();
 it2.next();
 it2.next();
 it3.next();
 it3.next();

 assertFalse("Iterator has next test for instance1 after all values", it1.hasNext());
 assertFalse("Iterator has next test for instance2 after all values", it2.hasNext());
 assertFalse("Iterator has next test for instance3 after all values", it3.hasNext());
 }

 @Test
 public void testConstructor() {
 AbstractInstance emptyInstance = new AbstractInstance();
 assertEquals("ID test for empty instance", 0, emptyInstance.getID());

 assertNull("Class value test for empty instance", emptyInstance.classValue());

 AbstractInstance classValueInstance = new AbstractInstance("test class value");
 assertEquals("ID test for class value instance", 1, classValueInstance.getID());
 assertEquals("Class value test for class value instance", "test class value", classValueInstance.classValue());
 }

 @Test
 public void testClassValue() {
 Object classValue1 = new Object();
 instance1.setClassValue(classValue1);
 assertEquals("Class value test for instance1", classValue1, instance1.classValue());

 Object classValue2 = new Object();
 instance2.setClassValue(classValue2);
 assertEquals("Class value test for instance2", classValue2, instance2.classValue());
 }

 @Test
 public void testMinus() {
 Instance result1 = instance1.minus(instance2);
 Instance result2 = instance1.minus(2.0);

 Assert.assertEquals("Size test for result1", 3, result1.noAttributes());
 Assert.assertEquals("Size test for result2", 3, result2.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedSubtractionResult = instance1.value(i) - instance2.value(i);
 double expectedSubtractionFromInstance1Result = instance1.value(i) - 2.0;

 assertEquals("Value test for result1", expectedSubtractionResult, result1.value(i), 0.001);
 assertEquals("Value test for result2", expectedSubtractionFromInstance1Result, result2.value(i), 0.001);
 }
 }

 @Test
 public void testMinusWithInstance() {
 Instance result = instance1.minus(instance2);

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedSubtractionResult = instance1.value(i) - instance2.value(i);

 assertEquals("Value test for result", expectedSubtractionResult, result.value(i), 0.001);
 }
 }

 @Test
 public void testMinusWithDouble() {
 Instance result = instance1.minus(2.0);

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedSubtractionResult = instance1.value(i) - 2.0;

 assertEquals("Value test for result", expectedSubtractionResult, result.value(i), 0.001);
 }
 }

 @Test
 public void testDivide() {
 Instance result1 = instance1.divide(2.0);
 Instance result2 = instance1.divide(instance2);

 Assert.assertEquals("Size test for result1", 3, result1.noAttributes());
 Assert.assertEquals("Size test for result2", 3, result2.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedDivisionResult = instance1.value(i) / 2.0;
 double expectedDivisionFromInstance1Result = instance1.value(i) / instance2.value(i);

 assertEquals("Value test for result1", expectedDivisionResult, result1.value(i), 0.001);
 assertEquals("Value test for result2", expectedDivisionFromInstance1Result, result2.value(i), 0.001);
 }
 }

 @Test
 public void testDivideWithInstance() {
 Instance result = instance1.divide(instance2);

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedDivisionResult = instance1.value(i) / instance2.value(i);

 assertEquals("Value test for result", expectedDivisionResult, result.value(i), 0.001);
 }
 }

 @Test
 public void testDivideWithDouble() {
 Instance result = instance1.divide(2.0);

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedDivisionResult = instance1.value(i) / 2.0;

 assertEquals("Value test for result", expectedDivisionResult, result.value(i), 0.001);
 }
 }

 @Test
 public void testMultiply() {
 Instance result1 = instance1.multiply(2.0);
 Instance result2 = instance1.multiply(instance2);

 Assert.assertEquals("Size test for result1", 3, result1.noAttributes());
 Assert.assertEquals("Size test for result2", 3, result2.noAttributes());

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedMultiplicationResult = instance1.value(i) * 2.0;
 double expectedMultiplicationFromInstance1Result = instance1.value(i) * instance2.value(i);

 assertEquals("Value test for result1", expectedMultiplicationResult, result1.value(i), 0.001);
 assertEquals("Value test for result2", expectedMultiplicationFromInstance1Result, result2.value(i), 0.001);
 }
 }

 @Test
 public void testMultiplyWithInstance() {
 Instance result = instance1.multiply(instance2);

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedMultiplicationResult = instance1.value(i) * instance2.value(i);

 assertEquals("Value test for result", expectedMultiplicationResult, result.value(i), 0.001);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 Instance result = instance1.multiply(2.0);

 for (int i = 0; i < instance1.noAttributes(); i++) {
 double expectedMultiplicationResult = instance1.value(i) * 2.0;

 assertEquals("Value test for result", expectedMultiplicationResult, result.value(i), 0.001);
 }
 }

 @Test
 public void testEquals() {
 assertTrue("Equals test for same instance", instance1.equals(instance1));
 assertTrue("Equals test for same values instances", instance1.equals(new AbstractInstance() {
 @Override
 public Map<Object, Double> toMap() {
 return new HashMap<>(instance1.toMap());
 }
 }));

 assertFalse("Not equals test for different instances", instance1.equals(instance2));
 assertFalse("Not equals test for different values instances", instance1.equals(new AbstractInstance() {
 @Override
 public Map<Object, Double> toMap() {
 return new HashMap<>(){ {
 put(0, 1.5);
 put(1, 2.5);
 put(2, 3.5);
 }};
 }
 }));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = instance1.hashCode();
 int hashCode2 = instance2.hashCode();
 int hashCode3 = instance1.hashCode();

 assertEquals("Hashcode test for same instance", hashCode1, hashCode3);
 assertNotEquals("Hashcode test for different instances", hashCode1, hashCode2);
 }
}