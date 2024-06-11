import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @Test
 public void testGetID() {
 // Test with default constructor
 instance1 = new DenseInstance(new double[0]);
 Assert.assertEquals(AbstractInstance.nextID, instance1.getID());
 // Test with custom constructor
 instance2 = new DenseInstance(new double[]{1.0}, new Object[]{String.class});
 instance2.setClassValue(String.class);
 Assert.assertEquals(AbstractInstance.nextID, instance2.getID());
 }

 @Test
 public void testIterator() {
 // Test empty instance
 instance1 = new DenseInstance(new double[0]);
 Assert.assertFalse(instance1.iterator().hasNext());
 // Test instance with values
 instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance2.setClassValue(String.class);
 Assert.assertTrue(instance2.iterator().hasNext());
 Assert.assertEquals(1.0, instance2.iterator().next(), 0.0);
 }

 @Test
 public void testMinus() {
 // Test with another instance
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance2 = new SparseInstance(new int[]{0, 1}, new double[]{2.5, 1.5}, new Object[]{String.class, String.class});
 instance3 = instance1.minus(instance2);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(instance1.value(i) - instance2.value(i), instance3.value(i), 0.1);
 }
 // Test with double
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.minus(2.5);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(instance1.value(i) - 2.5, instance3.value(i), 0.1);
 }
 }

 @Test
 public void testMinusThrowsException() {
 // Test with null instance
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 try {
 instance3 = instance1.minus(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 // Test with invalid type
 instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, Integer.TYPE});
 try {
 instance3 = instance1.minus(instance2);
 } catch (ClassCastException e) {
 // Expected
 }
 }

 @Test
 public void testDivide() {
 // Test with another instance
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance2 = new DenseInstance(new double[]{0.5, 0.5, 0.5}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.divide(instance2);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(instance1.value(i) / instance2.value(i), instance3.value(i), 0.1);
 }
 // Test with zero
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 try {
 instance3 = instance1.divide(0.0);
 } catch (ArithmeticException e) {
 // Expected
 }
 }

 @Test
 public void testMultiply() {
 // Test with another instance
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.multiply(instance2);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(instance1.value(i) * instance2.value(i), instance3.value(i), 0.1);
 }
 // Test with zero
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.multiply(0.0);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(0.0, instance3.value(i), 0.1);
 }
 }

 @Test
 public void testAdd() {
 // Test with another instance
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance2 = new DenseInstance(new double[]{1.0, 1.0, 1.0}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.add(instance2);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(instance1.value(i) + instance2.value(i), instance3.value(i), 0.1);
 }
 // Test with double
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.add(2.5);
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(instance1.value(i) + 2.5, instance3.value(i), 0.1);
 }
 }

 @Test
 public void testSqrt() {
 // Test with valid values
 instance1 = new DenseInstance(new double[]{1.0, 4.0, 9.0}, new Object[]{String.class, String.class, String.class});
 instance3 = instance1.sqrt();
 Assert.assertNotNull(instance3);
 Assert.assertEquals(3, instance3.noAttributes());
 for (int i = 0; i < instance3.noAttributes(); ++i) {
 Assert.assertEquals(Math.sqrt(instance1.value(i)), instance3.value(i), 0.1);
 }
 // Test with negative value
 instance1 = new DenseInstance(new double[]{-1.0, 4.0, 9.0}, new Object[]{String.class, String.class, String.class});
 try {
 instance3 = instance1.sqrt();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}