import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import java.util.Arrays;

public class AbstractInstanceTest {

 @Test
 public void testGetID() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals("ID should be correctly initialized", 0, instance.getID());
 instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1);
 Assert.assertEquals("ID should be correctly incremented", 1, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Iterator<Double> iterator = instance.iterator();
 Assert.assertTrue("Iterator should have elements", iterator.hasNext());
 Double value = iterator.next();
 Assert.assertEquals("First value should be 1.0", 1.0, value);
 Assert.assertTrue("Iterator should have more elements", iterator.hasNext());
 value = iterator.next();
 Assert.assertEquals("Second value should be 2.0", 2.0, value);
 Assert.assertTrue("Iterator should have more elements", iterator.hasNext());
 value = iterator.next();
 Assert.assertEquals("Third value should be 3.0", 3.0, value);
 Assert.assertFalse("Iterator should not have more elements", iterator.hasNext());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance a = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance b = new DenseInstance(new double[]{0.5, 1.0, 1.5});
 AbstractInstance result = a.minus(b);
 Assert.assertEquals("Result should have the same number of attributes as the initial instances", 3, result.noAttributes());
 Double[] expectedResultValues = {0.5, 1.0, 1.5};
 for (int i = 0; i < result.noAttributes(); i++) {
 Double resultValue = result.get(i);
 Double expectedValue = expectedResultValues[i];
 Assert.assertEquals("The value at position " + i + " should be correct", expectedValue, resultValue);
 }
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(2.0);
 Assert.assertEquals("Result should have the same number of attributes as the initial instance", 3, result.noAttributes());
 Double[] expectedResultValues = {-1.0, -1.0, -1.0};
 for (int i = 0; i < result.noAttributes(); i++) {
 Double resultValue = result.get(i);
 Double expectedValue = expectedResultValues[i];
 Assert.assertEquals("The value at position " + i + " should be correct", expectedValue, resultValue);
 }
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.divide(2.0);
 Assert.assertEquals("Result should have the same number of attributes as the initial instance", 3, result.noAttributes());
 Double[] expectedResultValues = {0.5, 1.0, 1.5};
 for (int i = 0; i < result.noAttributes(); i++) {
 Double resultValue = result.get(i);
 Double expectedValue = expectedResultValues[i];
 Assert.assertEquals("The value at position " + i + " should be correct", expectedValue, resultValue);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.multiply(2.0);
 Assert.assertEquals("Result should have the same number of attributes as the initial instance", 3, result.noAttributes());
 Double[] expectedResultValues = {2.0, 4.0, 6.0};
 for (int i = 0; i < result.noAttributes(); i++) {
 Double resultValue = result.get(i);
 Double expectedValue = expectedResultValues[i];
 Assert.assertEquals("The value at position " + i + " should be correct", expectedValue, resultValue);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetClassValueWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.setClassValue(null);
 }

 @Test
 public void testMinusWithSelf() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(instance);
 Assert.assertEquals("Result should have zero attributes since subtracting an instance from itself returns an empty instance", 0, result.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.minus(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.divide(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithDifferentSizes() {
 AbstractInstance a = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance b = new SparseInstance(new double[]{1.0, 2.0});
 instance.minus(b);
 }
}