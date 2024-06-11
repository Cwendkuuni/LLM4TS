import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.distance.DistanceMeasure;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FoldTest {

 private static final int numFolds = 5;
 private Dataset parentDataSet;
 private Fold fold;

 @Before
 public void setUp() {
 parentDataSet = new DefaultDataset();
 parentDataSet.add(createInstance(0));
 parentDataSet.add(createInstance(1));
 parentDataSet.add(createInstance(2));
 parentDataSet.add(createInstance(3));
 parentDataSet.add(createInstance(4));

 fold = new Fold(parentDataSet, getIndices());
 }

 @Test
 public void testConstructor() {
 assertEquals(parentDataSet, fold.parent);
 assertNotNull(fold.indices);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(createInstance(5));
 }

 @Test
 public void testSize() {
 assertEquals(parentDataSet.size(), fold.size());
 }

 @Test
 public void testGetInstance() {
 for (int i = 0; i < fold.size(); i++) {
 assertEquals(parentDataSet.instance(fold.indices[i]), fold.instance(i));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 final List<Instance> instances = Arrays.asList(createInstance(5), createInstance(6));
 fold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
 final List<Instance> instances = Arrays.asList(createInstance(5), createInstance(6));
 fold.addAll(1, instances);
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
 public void testRemoveAtIndex() {
 fold.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAllInstances() {
 final List<Instance> instances = Arrays.asList(createInstance(5), createInstance(6));
 fold.retainAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetInstance() {
 fold.set(0, createInstance(5));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 final Instance instance = createInstance(0);
 final DistanceMeasure distanceMeasure = new EuclideanDistance();
 fold.kNearest(1, instance, distanceMeasure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fold.folds(numFolds, new Random());
 }

 @Test
 public void testClasses() {
 final Set<Object> expectedClasses = parentDataSet.classes();
 assertEquals(expectedClasses, fold.classes());
 }

 @Test
 public void testNoAttributes() {
 assertEquals(parentDataSet.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 final Object expectedClass = "Class1";
 assertEquals(parentDataSet.classIndex(expectedClass), fold.classIndex(expectedClass));
 }

 @Test
 public void testClassValue() {
 final int index = 0;
 assertEquals(parentDataSet.classValue(index), fold.classValue(index));
 }

 @Test
 public void testCopy() {
 final Dataset expectedCopy = new DefaultDataset();
 for (final Instance instance : parentDataSet) {
 expectedCopy.add(instance.copy());
 }
 assertEquals(expectedCopy, fold.copy());
 }

 private static Instance createInstance(int index) {
 final double[] attributes = new double[5];
 Arrays.fill(attributes, index);
 return new DenseInstance(attributes);
 }

 private static int[] getIndices() {
 final int[] indices = new int[5];
 Arrays.fill(indices, 0);
 return indices;
 }

}