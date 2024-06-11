import static org.junit.Assert.*;
import static net.sf.javaml.core.DefaultDataset.*;
import static net.sf.javaml.distance.EuclideanDistance.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2, instance3;
 private Object class1, class2;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(class1));
 instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(class2));
 dataset.add(instance1);
 dataset.add(instance2);
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = new ArrayList<>();
 instances.add(new DenseInstance(new double[]{5.0, 6.0}, new Double(class1)));
 instances.add(new DenseInstance(new double[]{7.0, 8.0}, new Double(class2)));
 dataset.addAll(instances);
 assertEquals(4, dataset.size());
 }

 @Test
 public void testAddAll_EmptyCollections() {
 dataset.addAll(new ArrayList<Instance>());
 dataset.addAll(new ArrayList<Instance>());
 assertEquals(0, dataset.size());
 }

 @Test
 public void testAddAll_NullCollections() {
 try {
 dataset.addAll(null);
 dataset.addAll(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testAdd() {
 Instance newInstance = new DenseInstance(new double[]{1.1, 2.1}, new Double(class1));
 dataset.add(newInstance);
 assertEquals(3, dataset.size());
 }

 @Test
 public void testAdd_NullInstance() {
 try {
 dataset.add(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testClasses() {
 assertEquals(2, dataset.classes().size());
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testGet() {
 Instance expected = instance1;
 Instance actual = dataset.get(0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGet_OutOfBounds() {
 try {
 dataset.get(10);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSize() {
 assertEquals(2, dataset.size());
 }

 @Test
 public void testKNearest() {
 Instance testInstance = new DenseInstance(new double[]{1.5, 2.5}, null);
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 SortedSet<Instance> expected = new TreeSet<>();
 expected.add(instance1);
 Set<Instance> actual = dataset.kNearest(1, testInstance, distanceMeasure);
 assertEquals(expected, actual);
 }

 @Test
 public void testKNearest_kGreaterThanSize() {
 Instance testInstance = new DenseInstance(new double[]{1.5, 2.5}, null);
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> actual = dataset.kNearest(3, testInstance, distanceMeasure);
 assertEquals(2, actual.size());
 }

 @Test
 public void testKNearest_NullInstance() {
 try {
 dataset.kNearest(1, null, distanceMeasure);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testFolds() {
 int numFolds = 3;
 Random random = new Random();
 Dataset[] expectedFolds = new Dataset[numFolds];
 for (int i = 0; i < numFolds; i++) {
 expectedFolds[i] = new DefaultDataset();
 expectedFolds[i].addAll(dataset.sublist(i*dataset.size()/numFolds, (i+1)*dataset.size()/numFolds));
 }
 Dataset[] actualFolds = dataset.folds(numFolds, random);
 assertArrayEquals(expectedFolds, actualFolds);
 }

 @Test
 public void testNoAttributes() {
 assertEquals(2, instance1.noAttributes());
 }

 @Test
 public void testClassIndex() {
 assertEquals(0, dataset.classIndex(class1));
 }

 @Test
 public void testClassValue() {
 assertEquals(class1, dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 DefaultDataset copiedDataset = (DefaultDataset) dataset.copy();
 assertEquals(dataset, copiedDataset);
 }

}