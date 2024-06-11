import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.*;
import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;

 @Before
 public void setUp() {
 Collection<Instance> coll = new ArrayList<>();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{0.0});
 instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{1.0});
 coll.add(instance1);
 coll.add(instance2);
 coll.add(instance3);
 dataset = new DefaultDataset(coll);
 }

 @Test
 public void testConstructorWithCollection() {
 assertEquals(3, dataset.size());
 }

 @Test
 public void testConstructorEmpty() {
 assertEquals(0, new DefaultDataset().size());
 }

 @Test
 public void testAddAll() {
 Collection<Instance> coll = new ArrayList<>();
 coll.add(instance1);
 coll.add(instance2);
 dataset.addAll(coll);
 assertEquals(5, dataset.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithThrowException() {
 dataset.addAll(5, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAllWithIllegalArgument() {
 dataset.addAll(null);
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAdd() {
 dataset.add(instance1);
 assertEquals(2, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithIllegalArgument() {
 dataset.add(null);
 }

 @Test
 public void testAddElement() {
 dataset.addElement(instance1);
 assertEquals(2, dataset.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementWithIllegalArgument() {
 dataset.addElement(null);
 }

 @Test
 public void testInsertElementAt() {
 dataset.insertElementAt(instance1, 0);
 assertEquals(2, dataset.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertElementAtWithIndexOutOfBounds() {
 dataset.insertElementAt(instance1, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertElementAtWithIllegalArgument() {
 dataset.insertElementAt(null, 0);
 }

 @Test
 public void testSetElementAt() {
 dataset.setElementAt(instance2, 0);
 assertEquals(1, dataset.size());
 assertEquals(instance2, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetElementAtWithIndexOutOfBounds() {
 dataset.setElementAt(instance2, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetElementAtWithIllegalArgument() {
 dataset.setElementAt(null, 0);
 }

 @Test
 public void testInstance() {
 assertEquals(instance1, dataset.instance(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceWithIndexOutOfBounds() {
 dataset.instance(5);
 }

 @Test
 public void testClasses() {
 Set<Object> classes = dataset.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains(instance1.classValue()));
 assertTrue(classes.contains(instance2.classValue()));
 }

 @Test
 public void testKNearest() {
 Set<Instance> expected = new HashSet<>();
 expected.add(instance1);
 expected.add(instance3);
 Set<Instance> result = dataset.kNearest(2, instance2, new EuclideanDistance());
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestWithIllegalArgument() {
 dataset.kNearest(-1, instance2, new EuclideanDistance());
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullInstance() {
 dataset.kNearest(2, null, new EuclideanDistance());
 }

 @Test(expected = NullPointerException.class)
 public void testKNearestWithNullDistanceMeasure() {
 dataset.kNearest(2, instance2, null);
 }

 @Test
 public void testFolds() {
 Dataset[] expected = new Dataset[3];
 expected[0] = new DefaultDataset(Arrays.asList(instance1, instance3));
 expected[1] = new DefaultDataset(Arrays.asList(instance2));
 expected[2] = new DefaultDataset(Arrays.asList(instance1, instance2));
 Dataset[] result = dataset.folds(3, new Random());
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsWithIllegalArgument() {
 dataset.folds(3, null);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(1, dataset.classIndex(instance1.classValue()));
 }

 @Test
 public void testClassValue() {
 assertEquals(instance1.classValue(), dataset.classValue(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueWithIndexOutOfBounds() {
 dataset.classValue(5);
 }

 @Test
 public void testCopy() {
 DefaultDataset expected = new DefaultDataset();
 for (Instance instance : dataset) {
 expected.add(instance.copy());
 }
 DefaultDataset result = (DefaultDataset) dataset.copy();
 assertEquals(expected, result);
 }
}