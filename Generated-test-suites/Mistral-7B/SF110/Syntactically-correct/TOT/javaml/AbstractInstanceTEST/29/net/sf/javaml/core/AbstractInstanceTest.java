import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.*;

public class AbstractInstanceTest {

 @Test
 public void testGetID() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance anotherInstance = new DenseInstance(new double[]{4.0, 5.0, 6.0});

 AbstractInstance firstInstance = new AbstractInstance(instance.getClass()) {
 @Override
 public Iterator<Double> iterator() {
 return instance.iterator();
 }
 };

 AbstractInstance secondInstance = new AbstractInstance(anotherInstance.getClass()) {
 @Override
 public Iterator<Double> iterator() {
 return anotherInstance.iterator();
 }
 };

 Assert.assertEquals(firstInstance.getID(), secondInstance.getID()); // testing ID from two different instances
 Assert.assertEquals(firstInstance.getID(), firstInstance.getID()); // testing ID is consistent for the same instance
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 Iterator<Double> iterator = instance.iterator();

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(iterator.next(), 1.0, 0.0);

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(iterator.next(), 2.0, 0.0);

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(iterator.next(), 3.0, 0.0);

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance min = new DenseInstance(new double[]{0.5, 0.5, 0.5});

 AbstractInstance result = instance.minus(min);

 Assert.assertEquals(result.noAttributes(), instance.noAttributes());

 for (int i = 0; i < result.noAttributes(); ++i) {
 Assert.assertEquals(result.get(i), instance.get(i) - min.get(i), 0.0);
 }
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 AbstractInstance result = instance.minus(2.0);

 Assert.assertEquals(result.noAttributes(), instance.noAttributes());

 for (int i = 0; i < result.noAttributes(); ++i) {
 Assert.assertEquals(result.get(i), instance.get(i) - 2.0, 0.0);
 }
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 AbstractInstance result = instance.divide(2.0);

 Assert.assertEquals(result.noAttributes(), instance.noAttributes());

 for (int i = 0; i < result.noAttributes(); ++i) {
 Assert.assertEquals(result.get(i), instance.get(i) / 2.0, 0.0);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 AbstractInstance result = instance.multiply(2.0);

 Assert.assertEquals(result.noAttributes(), instance.noAttributes());

 for (int i = 0; i < result.noAttributes(); ++i) {
 Assert.assertEquals(result.get(i), instance.get(i) * 2.0, 0.0);
 }
 }

 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0});

 Assert.assertEquals(instance1.hashCode(), instance1.hashCode());
 Assert.assertEquals(instance1.hashCode(), instance2.hashCode());
 Assert.assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0});

 Assert.assertTrue(instance1.equals(instance1));
 Assert.assertTrue(instance1.equals(instance2));
 Assert.assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testMinusWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 try {
 instance.minus(null);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testDivideWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 try {
 instance.divide(null);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testMultiplyWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

 try {
 instance.multiply(null);
 Assert.fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});

 AbstractInstance result = instance.sqrt();

 Assert.assertEquals(result.noAttributes(), instance.noAttributes());

 for (int i = 0; i < result.noAttributes(); ++i) {
 Assert.assertEquals(Math.sqrt(instance.get(i)), result.get(i), 0.0);
 }
 }
}