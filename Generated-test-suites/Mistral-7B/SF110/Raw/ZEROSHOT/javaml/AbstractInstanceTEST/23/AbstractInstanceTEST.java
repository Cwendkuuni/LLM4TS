package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @Before
 public void setUp() {
 // Create instances with different IDs and classValues
 instance1 = new AbstractInstance() {{ setClassValue("Class1"); }};
 instance2 = new AbstractInstance() {{ setClassValue("Class2"); setID(1); }};
 instance3 = new AbstractInstance() {{ setClassValue("Class1"); setID(2); }};
 instance4 = new AbstractInstance() {{ setClassValue("Class2"); setID(3); }};
 }

 @Test
 public void testGetID() {
 // Test getID method
 assertEquals(instance1.getID(), instance1.getID());
 assertEquals(instance1.getID(), instance1.hashCode());
 assertEquals(instance2.getID(), instance2.hashCode());
 assertNotEquals(instance1.getID(), instance2.getID());
 }

 @Test
 public void testIterator() {
 // Test iterator method
 List<Double> list1 = new ArrayList<>();
 Iterator<Double> iterator1 = instance1.iterator();
 while (iterator1.hasNext()) {
 list1.add(iterator1.next());
 }
 assertEquals(instance1.noAttributes(), list1.size());
 list1.clear();

 List<Double> list2 = new ArrayList<>();
 Iterator<Double> iterator2 = instance2.iterator();
 while (iterator2.hasNext()) {
 list2.add(iterator2.next());
 }
 assertEquals(instance2.noAttributes(), list2.size());
 list2.clear();
 }

 @Test
 public void testMinus() {
 // Test minus method with Instance argument
 instance3.put(0, 5.0);
 instance3.put(1, 3.0);
 instance3.put(2, 7.0);

 AbstractInstance minusInstance = instance1.minus(instance3);
 Double[] expectedValues = {-5.0, -2.0, 1.0};
 List<Double> list3 = new ArrayList<>();
 Iterator<Double> iterator3 = minusInstance.iterator();
 while (iterator3.hasNext()) {
 list3.add(iterator3.next());
 }
 assertArrayEquals(expectedValues, list3.stream().mapToDouble(d -> d).toArray());

 // Test minus method with double argument
 AbstractInstance minusDoubleInstance = instance1.minus(2.0);
 Double[] expectedValuesDouble = {-2.0, -2.0, 0.0};
 List<Double> list4 = new ArrayList<>();
 Iterator<Double> iterator4 = minusDoubleInstance.iterator();
 while (iterator4.hasNext()) {
 list4.add(iterator4.next());
 }
 assertArrayEquals(expectedValuesDouble, list4.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testDivide() {
 // Test divide method with double argument
 instance1.put(0, 10.0);
 instance1.put(1, 5.0);
 instance1.put(2, 7.0);

 AbstractInstance divideDoubleInstance = instance1.divide(3.0);
 Double[] expectedValuesDivide = {3.3333333333333335, 1.6666666666666668, 2.3333333333333335};
 List<Double> list5 = new ArrayList<>();
 Iterator<Double> iterator5 = divideDoubleInstance.iterator();
 while (iterator5.hasNext()) {
 list5.add(iterator5.next());
 }
 assertArrayEquals(expectedValuesDivide, list5.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testMultiply() {
 // Test multiply method with Instance argument
 instance1.put(0, 2.0);
 instance1.put(1, 3.0);
 instance1.put(2, 4.0);

 AbstractInstance multiplyInstance = instance1.multiply(instance2);
 Double[] expectedValuesMultiply = {6.0, 9.0, 12.0};
 List<Double> list6 = new ArrayList<>();
 Iterator<Double> iterator6 = multiplyInstance.iterator();
 while (iterator6.hasNext()) {
 list6.add(iterator6.next());
 }
 assertArrayEquals(expectedValuesMultiply, list6.stream().mapToDouble(d -> d).toArray());

 // Test multiply method with double argument
 AbstractInstance multiplyDoubleInstance = instance1.multiply(2.0);
 Double[] expectedValuesMultiplyDouble = {4.0, 6.0, 8.0};
 List<Double> list7 = new ArrayList<>();
 Iterator<Double> iterator7 = multiplyDoubleInstance.iterator();
 while (iterator7.hasNext()) {
 list7.add(iterator7.next());
 }
 assertArrayEquals(expectedValuesMultiplyDouble, list7.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testEquals() {
 // Test equals method
 assertFalse(instance1.equals(instance2));
 assertTrue(instance1.equals(instance1));
 assertTrue(instance2.equals(instance2));

 AbstractInstance equalInstance = new AbstractInstance() {{ setID(instance1.getID()); setClassValue(instance1.getClassValue()); }};
 assertTrue(instance1.equals(equalInstance));
 }

 @Test
 public void testHashCode() {
 // Test hashCode method
 AbstractInstance equalInstance = new AbstractInstance() {{ setID(instance1.getID()); setClassValue(instance1.getClassValue()); }};
 assertEquals(instance1.hashCode(), equalInstance.hashCode());
 assertNotEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testMinusWithInstance() {
 // Test minus method with Instance argument
 AbstractInstance instance5 = new AbstractInstance() {{ put(0, 5.0); put(1, 3.0); put(2, 7.0); }};
 AbstractInstance result = instance1.minus(instance5);
 Double[] expectedValuesMinusInstance = {-5.0, -1.0, 4.0};
 List<Double> list8 = new ArrayList<>();
 Iterator<Double> iterator8 = result.iterator();
 while (iterator8.hasNext()) {
 list8.add(iterator8.next());
 }
 assertArrayEquals(expectedValuesMinusInstance, list8.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testMinusWithDouble() {
 // Test minus method with double argument
 AbstractInstance instance6 = new AbstractInstance() {{ put(0, 5.0); put(1, 3.0); put(2, 7.0); }};
 AbstractInstance result = instance1.minus(5.0);
 Double[] expectedValuesMinusDouble = {-5.0, -2.0, 2.0};
 List<Double> list9 = new ArrayList<>();
 Iterator<Double> iterator9 = result.iterator();
 while (iterator9.hasNext()) {
 list9.add(iterator9.next());
 }
 assertArrayEquals(expectedValuesMinusDouble, list9.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testDivideWithInstance() {
 // Test divide method with Instance argument
 AbstractInstance instance7 = new AbstractInstance() {{ put(0, 5.0); put(1, 3.0); put(2, 7.0); }};
 AbstractInstance result = instance1.divide(instance7);
 Double[] expectedValuesDivideInstance = {1.0, 1.0, 1.0};
 List<Double> list10 = new ArrayList<>();
 Iterator<Double> iterator10 = result.iterator();
 while (iterator10.hasNext()) {
 list10.add(iterator10.next());
 }
 assertArrayEquals(expectedValuesDivideInstance, list10.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testDivideWithDouble() {
 // Test divide method with double argument
 AbstractInstance instance8 = new AbstractInstance() {{ put(0, 5.0); put(1, 3.0); put(2, 7.0); }};
 AbstractInstance result = instance1.divide(2.0);
 Double[] expectedValuesDivideDouble = {2.5, 1.5, 3.5};
 List<Double> list11 = new ArrayList<>();
 Iterator<Double> iterator11 = result.iterator();
 while (iterator11.hasNext()) {
 list11.add(iterator11.next());
 }
 assertArrayEquals(expectedValuesDivideDouble, list11.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testMultiplyWithInstance() {
 // Test multiply method with Instance argument
 AbstractInstance instance9 = new AbstractInstance() {{ put(0, 5.0); put(1, 3.0); put(2, 7.0); }};
 AbstractInstance result = instance1.multiply(instance9);
 Double[] expectedValuesMultiplyInstance = {10.0, 9.0, 14.0};
 List<Double> list12 = new ArrayList<>();
 Iterator<Double> iterator12 = result.iterator();
 while (iterator12.hasNext()) {
 list12.add(iterator12.next());
 }
 assertArrayEquals(expectedValuesMultiplyInstance, list12.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testMultiplyWithDouble() {
 // Test multiply method with double argument
 AbstractInstance result = instance1.multiply(2.0);
 Double[] expectedValuesMultiplyDouble = {4.0, 6.0, 8.0};
 List<Double> list13 = new ArrayList<>();
 Iterator<Double> iterator13 = result.iterator();
 while (iterator13.hasNext()) {
 list13.add(iterator13.next());
 }
 assertArrayEquals(expectedValuesMultiplyDouble, list13.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testAddWithInstance() {
 // Test add method with Instance argument
 AbstractInstance instance10 = new AbstractInstance() {{ put(0, 5.0); put(1, 3.0); put(2, 7.0); }};
 AbstractInstance result = instance1.add(instance10);
 Double[] expectedValuesAddInstance = {10.0, 6.0, 14.0};
 List<Double> list14 = new ArrayList<>();
 Iterator<Double> iterator14 = result.iterator();
 while (iterator14.hasNext()) {
 list14.add(iterator14.next());
 }
 assertArrayEquals(expectedValuesAddInstance, list14.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testAddWithDouble() {
 // Test add method with double argument
 AbstractInstance result = instance1.add(2.0);
 Double[] expectedValuesAddDouble = {6.0, 5.0, 9.0};
 List<Double> list15 = new ArrayList<>();
 Iterator<Double> iterator15 = result.iterator();
 while (iterator15.hasNext()) {
 list15.add(iterator15.next());
 }
 assertArrayEquals(expectedValuesAddDouble, list15.stream().mapToDouble(d -> d).toArray());
 }

 @Test
 public void testSqrt() {
 // Test sqrt method
 AbstractInstance result = instance1.sqrt();
 Double[] expectedValuesSqrt = {2.23606797749979, 1.73205080756888, 2.64575131226763};
 List<Double> list16 = new ArrayList<>();
 Iterator<Double> iterator16 = result.iterator();
 while (iterator16.hasNext()) {
 list16.add(iterator16.next());
 }
 assertArrayEquals(expectedValuesSqrt, list16.stream().mapToDouble(d -> d).toArray());
 }
}