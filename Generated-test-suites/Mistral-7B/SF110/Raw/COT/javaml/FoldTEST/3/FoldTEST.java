import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Test;

public class FoldTest {

 private static final Dataset parentDataSet = new DefaultDataset();
 private static final int[] indices = new int[]{0, 1, 2};
 private static final Fold testFold = new Fold(parentDataSet, indices);

 @Test
 public void testConstructor() {
     assertNotNull(testFold);
     assertEquals(3, testFold.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
     testFold.add(new DefaultInstance(new double[0]));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
     final List<Instance> instances = new ArrayList<>();
     instances.add(new DefaultInstance(new double[0]));
     testFold.addAll(instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstancesAtIndex() {
     final List<Instance> instances = new ArrayList<>();
     instances.add(new DefaultInstance(new double[0]));
     testFold.addAll(1, instances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
     testFold.clear();
 }

 @Test
 public void testContainsInstance() {
     for (int i = 0; i < indices.length; ++i) {
         assertTrue(testFold.contains(testFold.instance(i)));
     }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetInstanceOutOfBounds() {
     testFold.getInstance(-1);
 }

 @Test
 public void testGetInstance() {
     for (int i = 0; i < indices.length; ++i) {
         Instance instance = testFold.instance(i);
         assertNotNull(instance);
         assertSame(parentDataSet.instance(indices[i]), instance);
     }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
     testFold.kNearest(1, new DefaultInstance(new double[0]), new DistanceMeasure() {
         @Override
         public double distance(final Instance i, final Instance j) {
             return 0;
         }
     });
 }

 @Test
 public void testSize() {
     assertEquals(3, testFold.size());
 }

 @Test
 public void testToArray() {
     Instance[] expected = new Instance[testFold.size()];
     for (int i = 0; i < testFold.size(); ++i) {
         expected[i] = testFold.instance(i);
     }
     assertArrayEquals(expected, testFold.toArray());
 }

 @Test
 public void testNoAttributes() {
     assertEquals(testFold.parent.noAttributes(), testFold.noAttributes());
 }

 @Test
 public void testClassIndex() {
     Object expectedClass = testFold.parent.classValue(indices[0]);
     assertEquals(expectedClass, testFold.classIndex(expectedClass));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCopy() {
     testFold.copy();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
     testFold.folds(1, new java.util.Random());
 }

}