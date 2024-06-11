import static org.junit.Assert.*;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import org.junit.Before;
import org.junit.Test;

public class FoldTest {

 private Dataset parentDataset;
 private Fold fold;

 @Before
 public void setup() {
 parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[]{1.0, 2.0}, new double[]{0.0}));
 parentDataset.add(new DenseInstance(new double[]{2.0, 2.0}, new double[]{1.0}));
 parentDataset.add(new DenseInstance(new double[]{3.0, 3.0}, new double[]{0.0}));

 int[] indices = {1, 2};
 fold = new Fold(parentDataset, indices);
 }

 @Test
 public void testConstructor() {
 assertNotNull(fold);
 assertEquals(2, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 fold.add(new DenseInstance(new double[]{1.0, 1.0}, new double[]{0.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 1.0}, new double[]{0.0}));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllIndex() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{1.0, 1.0}, new double[]{0.0}));
 fold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveIndex() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 List<Instance> instances = new ArrayList<>();
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 fold.set(0, new DenseInstance(new double[]{1.0, 1.0}, new double[]{0.0}));
 }

 @Test
 public void testSize() {
 assertEquals(2, fold.size());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(fold.isEmpty());
 }

 @Test
 public void testIterator() {
 Instance instance = null;
 for (Instance i : fold) {
 instance = i;
 break;
 }
 assertEquals(2.0, instance.getValue(0), 0.001);
 assertEquals(2.0, instance.getValue(1), 0.001);
 }

 @Test
 public void testGet() {
 Instance instance = fold.get(0);
 assertEquals(1.0, instance.getValue(0), 0.001);
 assertEquals(1.0, instance.getValue(1), 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 fold.kNearest(1, null, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInstances() {
 fold.instances();
 }

 @Test
 public void testClasses() {
 Set<Object> classes = fold.classes();
 assertEquals(1, classes.size());
 assertEquals("0.0", classes.iterator().next().toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(5, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNoAttributes() {
 fold.noAttributes();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClassIndex() {
 fold.classIndex("1.0");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClassValue() {
 fold.classValue(0);
 }

 @Test
 public void testCopy() {
 Dataset copiedDataset = fold.copy();
 assertEquals(2, copiedDataset.size());
 }

 @Test
 public void testInstance() {
 Instance instance = fold.instance(0);
 assertEquals(1.0, instance.getValue(0), 0.001);
 assertEquals(1.0, instance.getValue(1), 0.001);
 }

 @Test
 public void testClassesEmptyFold() {
 Fold emptyFold = new Fold(new DefaultDataset(), new int[0]);
 Set<Object> classes = emptyFold.classes();
 assertEquals(0, classes.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorOutOfBounds() {
 fold = new Fold(parentDataset, new int[]{0, 1, 2, 3, 4});
 FoldIterator iterator = fold.iterator();
 iterator.next(); // advance to the first element
 iterator.next(); // advance to the second element
 iterator.next(); // advance to the third element
 iterator.next(); // advance to the fourth element
 iterator.next(); // advance to the fifth element
 iterator.next(); // go out of bounds
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructNullParentDataset() {
 try {
 new Fold(null, new int[]{0, 1, 2, 3, 4});
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructEmptyIndices() {
 try {
 new Fold(parentDataset, new int[]{});
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testInstanceOutOfBounds() {
 fold = new Fold(parentDataset, new int[]{1, 2, 3});
 try {
 fold.instance(3);
 fail("Should have thrown an ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testDistance() {
 Instance instance1 = parentDataset.instance(0);
 Instance instance2 = parentDataset.instance(1);
 double distance = new EuclideanDistance().distance(instance1, instance2);
 assertEquals(Math.sqrt(2.0), distance, 0.001);

 Instance foldInstance1 = fold.instance(0);
 Instance foldInstance2 = fold.instance(1);
 distance = new EuclideanDistance().distance(foldInstance1, foldInstance2);
 assertEquals(Math.sqrt(2.0), distance, 0.001);
 }

}