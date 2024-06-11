package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private int id;
 private Object classValue;
 private double[] attributes;
 private Map<Integer, Double> map;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Test case 1
 new Object[] { new DenseInstance(new double[]{1.0, 2.0, 3.0}), 1, new DenseInstance(new double[]{1.0, 2.0, 3.0}) },
 // Test case 2
 new Object[] { new DenseInstance(new double[]{1.1, 2.2, 3.3}), 2, new DenseInstance(new double[]{1.1, 2.2, 3.3}) },
 // Test case 3
 new Object[] { new SparseInstance(new int[]{0, 2}, new double[]{2.5, 4.0}), 3, new SparseInstance(new int[]{0, 2}, new double[]{2.5, 4.0}) }
 });
 }

 @Parameters(name = "{index}: Instance-1={0}, Instance-2={1}, ID={2}")
 public Object[] getTestData(Object[] data) {
 this.instance = (AbstractInstance) data[0];
 this.instance2 = (AbstractInstance) data[1];
 this.id = (int) data[2];
 this.map = this.instance.asMap();
 return new Object[] { this.instance, this.instance2, this.id };
 }

 @Before
 public void setUp() {
 this.attributes = new double[this.instance.noAttributes()];
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 this.attributes[i] = this.instance.value(i);
 }
 }

 @Test
 public void testGetID() {
 assertEquals("ID should be correct.", this.id, this.instance.getID());
 }

 @Test
 public void testIterator() {
 List<Double> result = new ArrayList<>();
 Iterator<Double> iterator = this.instance.iterator();
 while (iterator.hasNext()) {
 result.add(iterator.next());
 }
 assertEquals("The values returned by the iterator should be the same as the original instance.", Arrays.toString(this.attributes), result.toString());
 }

 @Test
 public void testConstructor() {
 AbstractInstance newInstance = new DenseInstance(new double[this.instance.noAttributes()]);
 newInstance.setClassValue(this.classValue);
 assertEquals("The ID of the new instance should be different than the existing instance.", this.id, this.instance.getID());
 assertEquals("The classValue of the new instance should be the same as the one passed to the constructor.", this.classValue, newInstance.classValue());
 }

 @Test
 public void testClassValue() {
 assertEquals("The classValue should be the same as the one set in the constructor.", this.classValue, this.instance.classValue());
 }

 @Test
 public void testSetClassValue() {
 Object newClassValue = new Object();
 this.instance.setClassValue(newClassValue);
 assertEquals("The classValue should be the same as the one set using the setClassValue method.", newClassValue, this.instance.classValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMinusWithNull() {
 this.instance.minus(null);
 fail("The minus method should throw an UnsupportedOperationException when passed a null Instance.");
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance newInstance = new DenseInstance(new double[this.instance.noAttributes()]);
 Instance result = this.instance.minus(newInstance);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the minus method should be the difference between the two instances.", this.attributes[i], result.value(i) - newInstance.value(i));
 }
 }

 @Test
 public void testMinusWithDouble() {
 Instance result = this.instance.minus(1.0);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the minus method with a double should be the difference between the value of the instance and the double.", this.attributes[i] - 1.0, result.value(i));
 }
 }

 @Test
 public void testDivideWithDouble() {
 Instance result = this.instance.divide(1.0);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the divide method with a double should be the division of the value of the instance and the double.", this.attributes[i] / 1.0, result.value(i), 0.001);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 Instance result = this.instance.multiply(1.0);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the multiply method with a double should be the multiplication of the value of the instance and the double.", this.attributes[i] * 1.0, result.value(i), 0.001);
 }
 }

 @Test
 public void testEquals() {
 assertTrue("The instances should be equal when they have the same ID and attributes.", this.instance.equals(this.instance));
 assertTrue("The instances should be equal when they have the same ID and attributes, even if the order of the attributes is different.", this.instance.equals(new DenseInstance(Arrays.copyOf(this.attributes, this.attributes.length))));
 assertFalse("The instances should not be equal when they have different IDs.", this.instance.equals(this.instance2));
 }

 @Test
 public void testHashCode() {
 int code1 = this.instance.hashCode();
 int code2 = this.instance.hashCode();
 assertEquals("The hashCode should be the same for equal instances.", code1, code2);
 int code3 = this.instance2.hashCode();
 assertNotSame("The hashCode should be different for unequal instances.", code1, code3);
 }

 @Test
 public void testMinusWithInstanceAndAttributes() {
 Instance result = this.instance.minus(this.instance2);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the minus method with instances should be the difference between the corresponding values.", this.map.get(i) - this.instance2.map.get(i), result.map.get(i), 0.001);
 }
 }

 @Test
 public void testMinusWithDoubleAndAttributes() {
 Instance result = this.instance.minus(1.0);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the minus method with a double and attributes should be the difference between the value of the instance and the double.", this.attributes[i] - 1.0, result.value(i), 0.001);
 }
 }

 @Test
 public void testDivideWithInstance() {
 Instance result = this.instance.divide(this.instance2);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the divide method with instances should be the division of the corresponding values.", this.map.get(i) / this.instance2.map.get(i), result.map.get(i), 0.001);
 }
 }

 @Test
 public void testMultiplyWithInstance() {
 Instance result = this.instance.multiply(this.instance2);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the multiply method with instances should be the product of the corresponding values.", this.map.get(i) * this.instance2.map.get(i), result.map.get(i), 0.001);
 }
 }

 @Test
 public void testAddWithDouble() {
 Instance result = this.instance.add(1.0);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the add method with a double should be the addition of the value of the instance and the double.", this.attributes[i] + 1.0, result.value(i), 0.001);
 }
 }

 @Test
 public void testAddWithInstance() {
 Instance result = this.instance.add(this.instance2);
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the add method with instances should be the sum of the corresponding values.", this.map.get(i) + this.instance2.map.get(i), result.map.get(i), 0.001);
 }
 }

 @Test
 public void testSqrt() {
 Instance result = this.instance.sqrt();
 for (int i = 0; i < this.instance.noAttributes(); i++) {
 assertEquals("The result of the sqrt method should be the square root of the corresponding value.", Math.sqrt(this.attributes[i]), result.value(i), 0.001);
 }
 }

 @After
 public void tearDown() {
 this.instance = null;
 this.instance2 = null;
 }
}