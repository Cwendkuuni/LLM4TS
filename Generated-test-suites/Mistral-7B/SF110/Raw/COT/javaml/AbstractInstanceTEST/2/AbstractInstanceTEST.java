import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.SimpleDataset;

public class AbstractInstanceTest {

 private static final int NUM_ATTRIBUTES = 5;
 private static final double DELTA = 0.001;

 @Test
 public void testGetID() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 Assert.assertEquals(0, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 instance.put(0, 1.0);
 instance.put(1, 2.0);

 Iterator<Double> iterator = instance.iterator();
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(1.0, iterator.next(), DELTA);
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(2.0, iterator.next(), DELTA);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testDefaultConstructor() {
 AbstractInstance instance = new AbstractInstance();
 Assert.assertEquals(0, instance.getID());
 Assert.assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
 AbstractInstance instance = new AbstractInstance("TestClassValue");
 Assert.assertEquals(1, instance.getID());
 Assert.assertEquals("TestClassValue", instance.classValue().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 instance.minus(null);
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{-1.0, -2.0, -3.0, -4.0, -5.0});
 AbstractInstance result = instance1.minus(instance2.getID());

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {-1.0, -2.0, -3.0, -4.0, -5.0},
         {0.0, 0.0, 0.0, 0.0, 0.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(1);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidID() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 instance.minus(-1);
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});
 AbstractInstance result = instance1.minus(instance2);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {2.0, 3.0, 4.0, 5.0, 6.0},
         {-1.0, -1.0, -1.0, -1.0, -1.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(0);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 instance.divide(null);
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance result = instance.divide(2.0);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {1.0 / 2.0, 2.0 / 2.0, 3.0 / 2.0, 4.0 / 2.0, 5.0 / 2.0},
         {1.0, 2.0, 3.0, 4.0, 5.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(0);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance result = instance.divide(0.0);
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance result = instance.multiply(2.0);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {2.0, 4.0, 6.0, 8.0, 10.0},
         {1.0, 2.0, 3.0, 4.0, 5.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(0);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});
 AbstractInstance result = instance1.multiply(instance2);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {2.0 * 1.0, 3.0 * 2.0, 4.0 * 3.0, 5.0 * 4.0, 6.0 * 5.0},
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {2.0, 3.0, 4.0, 5.0, 6.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(1);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});
 AbstractInstance result = instance1.divide(instance2);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {1.0 / 2.0, 2.0 / 3.0, 3.0 / 4.0, 4.0 / 5.0, 5.0 / 6.0},
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {0.5, 1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(2);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance result = instance.add(2.0);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {3.0, 4.0, 5.0, 6.0, 7.0},
         {1.0, 2.0, 3.0, 4.0, 5.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(0);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});
 AbstractInstance result = instance1.add(instance2);

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {3.0, 5.0, 7.0, 9.0, 11.0},
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {2.0, 3.0, 4.0, 5.0, 6.0}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(1);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0, 16.0, 25.0});
 AbstractInstance result = instance.sqrt();

 SimpleDataset dataset = new SimpleDataset(new String[]{"Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5"}, new double[][]{
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {1.0, 2.0, 3.0, 4.0, 5.0},
         {1.0, 2.0, 3.0, 4.0, Math.sqrt(5.0)}
 });

 AbstractInstance expectedResult = (AbstractInstance) dataset.instance(2);

 for (int i = 0; i < NUM_ATTRIBUTES; ++i) {
 Assert.assertEquals(expectedResult.get(i), result.get(i), DELTA);
 }
 }

}