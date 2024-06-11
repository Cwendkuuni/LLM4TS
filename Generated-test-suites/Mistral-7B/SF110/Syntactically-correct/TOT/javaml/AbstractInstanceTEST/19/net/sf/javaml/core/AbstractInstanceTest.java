import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.util.SimpleCouple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractInstanceTest {

 @Test
 public void testGetID() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(AbstractInstance.nextID, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 List<Double> values = new ArrayList<>();
 for (Double value : instance) {
 values.add(value);
 }
 assertEquals(3, values.size());
 assertTrue(values.containsAll(Arrays.asList(1.0, 2.0, 3.0)));
 }

 @Test
 public void testSubtractInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 AbstractInstance result = instance1.minus(instance2);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(-1.0);
 expectedValues.add(-1.0);
 expectedValues.add(-1.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test
 public void testSubtractDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(5.0);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(-4.0);
 expectedValues.add(-2.0);
 expectedValues.add(-1.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test
 public void testDivide() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.divide(2.0);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(0.5);
 expectedValues.add(1.0);
 expectedValues.add(1.5);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideByZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.divide(0.0);
 }

 @Test
 public void testMultiply() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.multiply(2.0);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(2.0, 2.0, 6.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance1.equals(instance2));
 AbstractInstance instance3 = new DenseInstance(new double[]{1.1, 2.1, 3.1});
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(instance1.getID(), instance2.getID());
 }

 @Test
 public void testMultiplyInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 AbstractInstance result = instance1.multiply(instance2);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(4.0, 10.0, 18.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test
 public void testDivideInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 2.0, 3.0});
 AbstractInstance result = instance1.divide(instance2);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(1.0, 1.0, 1.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test
 public void testAddDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.add(5.0);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(6.0, 7.0, 8.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test
 public void testAddInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 AbstractInstance result = instance1.add(instance2);
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(5.0, 7.0, 9.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.add(null);
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});
 AbstractInstance result = instance.sqrt();
 List<Double> resultValues = new ArrayList<>();
 for (Double value : result) {
 resultValues.add(value);
 }
 List<Double> expectedValues = new ArrayList<>();
 expectedValues.add(1.0, 2.0, 3.0);
 assertTrue(resultValues.containsAll(expectedValues));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtNegativeNumber() {
 AbstractInstance instance = new DenseInstance(new double[]{-1.0, 4.0, 9.0});
 instance.sqrt();
 }
}