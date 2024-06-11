import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class FoldTest {

 private Dataset parentDataSet;
 private Fold fold;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 parentDataSet = new DefaultDataset();
 parentDataSet.add(new DefaultInstance(new double[]{1.0, 2.0, 3.0}));
 parentDataSet.add(new DefaultInstance(new double[]{2.0, 3.0, 4.0}));
 parentDataSet.add(new DefaultInstance(new double[]{3.0, 4.0, 5.0}));

 fold = new Fold(parentDataSet, new int[]{0, 1, 2});
 }

 @Test
 public void testConstructor() {
 assertEquals(3, fold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new DefaultInstance(new double[]{0.0, 0.0, 0.0}));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstance() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DefaultInstance(new double[]{0.0, 0.0, 0.0}));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllIndex() {
 List<Instance> instances = new ArrayList<>();
 instances.add(new DefaultInstance(new double[]{0.0, 0.0, 0.0}));
 fold.addAll(2, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveIndex() {
 fold.remove(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 List<Instance> instances = new ArrayList<>();
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 fold.set(0, new DefaultInstance(new double[]{0.0, 0.0, 0.0}));
 }

 @Test
 public void testGetInstance() {
 Instance instance = fold.instance(0);
 assertEquals(1.0, instance.value(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceInvalidIndex() {
 fold.instance(3);
 }

 @Test
 public void testSize() {
 assertEquals(3, fold.size());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(fold.isEmpty());
 }

 @Test
 public void testIterator() {
 List<Instance> expectedInstances = new ArrayList<>();
 expectedInstances.add(parentDataSet.instance(0));
 expectedInstances.add(parentDataSet.instance(1));
 expectedInstances.add(parentDataSet.instance(2));

 List<Instance> instances = new ArrayList<>();
 for (Instance instance : fold) {
 instances.add(instance);
 }
 assertEquals(expectedInstances, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListIteratorNoArgs() {
 fold.listIterator();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testListIteratorInvalidIndex() {
 fold.listIterator(-1);
 }

 @Test
 public void testListIteratorWithIndex() {
 ListIterator<Instance> listIterator = fold.listIterator(1);
 Instance expectedInstance = parentDataSet.instance(1);
 Instance instance = listIterator.next();
 assertEquals(expectedInstance, instance);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Instance instance = parentDataSet.instance(0);
 assertEquals(fold.classIndex(instance.classValue()), 0);
 }

 @Test
 public void testClassValue() {
 Instance instance = fold.instance(0);
 assertEquals(instance.classValue(), parentDataSet.instance(0).classValue());
 }

 @Test
 public void testCopy() {
 Dataset copiedDataset = fold.copy();
 assertEquals(fold.size(), copiedDataset.size());
 for (Instance instance : copiedDataset) {
 assertEquals(instance, fold.instance(fold.indexOf(instance)));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(2, new Random());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 fold.kNearest(2, fold.instance(0), new EuclideanDistance());
 }
}