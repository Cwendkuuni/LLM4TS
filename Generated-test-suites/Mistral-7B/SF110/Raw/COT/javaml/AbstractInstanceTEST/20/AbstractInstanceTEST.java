import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.*;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private double minValue = -1.0;
 private double maxValue = 1.0;

 @Test
 public void testGetID() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Assert.assertEquals("ID should be correctly initialized", 1, instance.getID());
 }

 @Test
 public void testIterator() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Iterator<Double> iterator = instance.iterator();
     Assert.assertTrue("Iterator should return at least one value", iterator.hasNext());
     Assert.assertEquals("First value should be correct", 1.0, iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetID() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance.setID(2);
 }

 @Test
 public void testConstructorWithNullClassValue() {
     instance = new AbstractInstance();
     Assert.assertNull("Class value should be null in the constructor without classValue argument", instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
     instance = new AbstractInstance("TestClassValue");
     Assert.assertEquals("Class value should be set correctly", "TestClassValue", instance.classValue());
 }

 @Test
 public void testGetClassValue() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance.setClassValue("TestClassValue");
     Assert.assertEquals("Class value should be correctly retrieved", "TestClassValue", instance.classValue());
 }

 @Test
 public void testSetClassValue() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance.setClassValue("TestClassValue");
     Assert.assertEquals("Class value should be correctly set", "TestClassValue", instance.classValue());
 }

 @Test
 public void testMinusWithInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});
     AbstractInstance result = instance.minus(instance2);
     Assert.assertNotNull("Result of minus operation should not be null", result);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Subtraction should be correct for each attribute", instance.value(i) - instance2.value(i), result.value(i), 0.001);
     }
 }

 @Test
 public void testMinusWithDouble() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.minus(1.0);
     Assert.assertNotNull("Result of minus operation should not be null", result);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Subtraction should be correct for each attribute", instance.value(i) - 1.0, result.value(i), 0.001);
     }
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.divide(0.0);
 }

 @Test
 public void testDivide() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.divide(2.0);
     Assert.assertNotNull("Result of divide operation should not be null", result);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Division should be correct for each attribute", instance.value(i) / 2.0, result.value(i), 0.001);
     }
 }

 @Test
 public void testMultiplyWithDouble() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.multiply(2.0);
     Assert.assertNotNull("Result of multiply operation should not be null", result);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Multiplication should be correct for each attribute", instance.value(i) * 2.0, result.value(i), 0.001);
     }
 }

 @Test
 public void testEquals() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});
     Assert.assertTrue("Two identical instances should be equal", instance.equals(instance2));
     Assert.assertFalse("Two different instances should not be equal", instance.equals(instance3));
 }

 @Test
 public void testMinusWithMinusOne() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.minus(-1.0);
     Assert.assertNotNull("Result of minus operation should not be null", result);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Subtraction should be correct for each attribute", instance.value(i) + 1.0, result.value(i), 0.001);
     }
 }

 @Test
 public void testDivideWithZeroInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Assert.assertNull("Division by zero instance should return null", instance.divide((Instance)new Object()));
 }

 @Test
 public void testInstanceValueIteratorHasNext() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     InstanceValueIterator iterator = instance.iterator();
     for (int i = 0; i < instance.noAttributes() - 1; ++i) {
         Assert.assertTrue("Iterator should return true for all valid indices", iterator.hasNext());
     }
 }

 @Test(expected = NoSuchElementException.class)
 public void testInstanceValueIteratorNoNext() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     InstanceValueIterator iterator = instance.iterator();
     for (int i = 0; i < instance.noAttributes(); ++i) {
         iterator.next();
     }
     iterator.next(); // This should throw an exception
 }

 @Test
 public void testMinusWithMinusInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     instance2 = new DenseInstance(new double[]{1.5, 2.5, 3.5});
     AbstractInstance result = instance.minus(instance2);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Subtraction should be correct for each attribute", instance.value(i) - instance2.value(i), result.value(i), 0.001);
     }
 }

 @Test
 public void testMinusWithMaxDouble() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.minus(Double.MAX_VALUE);
     Assert.assertNotNull("Result of minus operation should not be null", result);
     for (int i = 0; i < result.noAttributes(); ++i) {
         double expected = instance.value(i) - Double.MAX_VALUE;
         if (expected < minValue) {
             expected = Double.NaN;
         }
         Assert.assertEquals("Subtraction should be correct for each attribute", expected, result.value(i), 0.001);
     }
 }

 @Test
 public void testDivideWithMinusOneInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance instanceMinusOne = new DenseInstance(new double[]{1.0, 2.0, 3.0}).minus(1.0);
     AbstractInstance result = instance.divide(instanceMinusOne);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Division should be correct for each attribute", instance.value(i) / (instanceMinusOne.value(i) + 1.0), result.value(i), 0.001);
     }
 }

 @Test
 public void testDivideWithMinusOneDouble() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.divide(-1.0);
     Assert.assertNull("Division by negative number should return null", result);
 }

 @Test
 public void testMultiplyWithZeroInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.multiply((Instance)new Object());
     Assert.assertNull("Multiplication by null instance should return null", result);
 }

 @Test
 public void testMultiplyWithMinusOneInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance instanceMinusOne = new DenseInstance(new double[]{1.0, 2.0, 3.0}).minus(1.0);
     AbstractInstance result = instance.multiply(instanceMinusOne);
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Multiplication should be correct for each attribute", instance.value(i) * instanceMinusOne.value(i), result.value(i), 0.001);
     }
 }

 @Test
 public void testSqrtWithNegativeNumber() {
     instance = new DenseInstance(new double[]{-1.0, 2.0, 3.0});
     AbstractInstance result = instance.sqrt();
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Square root should return NaN for negative numbers", Double.NaN, result.value(i), 0.001);
     }
 }

 @Test
 public void testSqrtWithZeroNumber() {
     instance = new DenseInstance(new double[]{0.0, 2.0, 3.0});
     AbstractInstance result = instance.sqrt();
     for (int i = 0; i < result.noAttributes(); ++i) {
         Assert.assertEquals("Square root should return zero for zero numbers", 0.0, result.value(i), 0.001);
     }
 }

 @Test
 public void testSqrtWithPositiveNumber() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance result = instance.sqrt();
     for (int i = 0; i < result.noAttributes(); ++i) {
         double expected = Math.sqrt(instance.value(i));
         Assert.assertEquals("Square root should be correct for each attribute", expected, result.value(i), 0.001);
     }
 }
}