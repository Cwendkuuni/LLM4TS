package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest {

 private Dataset parentDataset;
 private Fold fold;

 @Before
 public void setUp() {
 // Create a parent dataset with 10 instances
 int[] attributes = { 1, 2, 3 };
 List<Instance> data = Arrays.asList(
 new DefaultInstance(attributes, new double[] { 1.0, 2.0, 3.0 }),
 new DefaultInstance(attributes, new double[] { 2.0, 3.0, 1.0 }),
 new DefaultInstance(attributes, new double[] { 3.0, 1.0, 2.0 }),
 new DefaultInstance(attributes, new double[] { 4.0, 5.0, 6.0 }),
 new DefaultInstance(attributes, new double[] { 5.0, 6.0, 4.0 }),
 new DefaultInstance(attributes, new double[] { 6.0, 7.0, 5.0 }),
 new DefaultInstance(attributes, new double[] { 7.0, 8.0, 9.0 }),
 new DefaultInstance(attributes, new double[] { 8.0, 9.0, 7.0 }),
 new DefaultInstance(attributes, new double[] { 9.0, 10.0, 8.0 }),
 new DefaultInstance(attributes, new double[] { 10.0, 8.0, 9.0 })
 );
 parentDataset = new DefaultDataset("Parent Dataset", attributes, data);

 // Create a fold with indices [2, 4, 1, 0, 5, 3, 6, 7, 8, 9]
 int[] indices = { 2, 4, 1, 0, 5, 3, 6, 7, 8, 9 };
 fold = new Fold(parentDataset, indices);
 }

 @Test
 public void testSize() {
 assertEquals(parentDataset.size(), fold.size());
 }

 @Test
 public void testGet() {
 for (int i = 0; i < fold.size(); i++) {
 Instance expected = parentDataset.instance(fold.indices[i]);
 Instance actual = fold.instance(i);
 assertEquals(expected, actual);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 fold.add(new DefaultInstance(new int[] { 11 }, new double[] {}));
 fail();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 List<Instance> instances = Arrays.asList(new DefaultInstance(new int[] { 11 }, new double[] {}), new DefaultInstance(new int[] { 12 }, new double[] {}));
 fold.addAll(instances);
 fail();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllIndex() {
 List<Instance> instances = Arrays.asList(new DefaultInstance(new int[] { 11 }, new double[] {}), new DefaultInstance(new int[] { 12 }, new double[] {}));
 fold.addAll(1, instances);
 fail();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 fail();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContains() {
 Instance instance = new DefaultInstance(new int[] { 11 }, new double[] {});
 assertFalse(fold.contains(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsAll() {
 List<Instance> instances = Arrays.asList(new DefaultInstance(new int[] { 11 }, new double[] {}), new DefaultInstance(new int[] { 12 }, new double[] {}));
 assertFalse(fold.containsAll(instances));
 }

 @Test
 public void testContainsIndices() {
 for (int index : fold.indices) {
 assertTrue(parentDataset.contains(parentDataset.instance(index)));
 }
 }

 @Test
 public void testCopy() {
 Fold copy = fold.copy();
 assertEquals(fold.size(), copy.size());
 for (int i = 0; i < copy.size(); i++) {
 Instance expected = parentDataset.instance(fold.indices[i]);
 Instance actual = copy.instance(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testClasses() {
 assertEquals(parentDataset.classes(), fold.classes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fail(); // Method is not yet implemented
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 fail(); // Method is not yet implemented
 }

 @Test
 public void testIterator() {
 List<Instance> expected = new ArrayList<Instance>(Arrays.asList(parentDataset.instance(2), parentDataset.instance(4), parentDataset.instance(1), parentDataset.instance(0), parentDataset.instance(5), parentDataset.instance(3), parentDataset.instance(6), parentDataset.instance(7), parentDataset.instance(8), parentDataset.instance(9)));
 List<Instance> actual = new ArrayList<Instance>();
 for (Instance instance : fold) {
 actual.add(instance);
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testListIterator() {
 ListIterator<Instance> expected = parentDataset.listIterator();
 ListIterator<Instance> actual = fold.listIterator();
 while (expected.hasNext()) {
 Instance expectedInstance = expected.next();
 Instance actualInstance = actual.next();
 assertEquals(expectedInstance, actualInstance);
 }
 }

 @Test
 public void testListIteratorIndex() {
 ListIterator<Instance> expected = parentDataset.listIterator(1);
 ListIterator<Instance> actual = fold.listIterator(1);
 while (expected.hasNext()) {
 Instance expectedInstance = expected.next();
 Instance actualInstance = actual.next();
 assertEquals(expectedInstance, actualInstance);
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(parentDataset.noAttributes(), fold.noAttributes());
 }

 @Test
 public void testClassIndex() {
 Object clazz = parentDataset.classValue(0);
 int expectedClassIndex = parentDataset.classIndex(clazz);
 int actualClassIndex = fold.classIndex(clazz);
 assertEquals(expectedClassIndex, actualClassIndex);
 }

 @Test
 public void testClassValue() {
 int index = 0;
 Object expectedClass = parentDataset.classValue(index);
 Object actualClass = fold.classValue(index);
 assertEquals(expectedClass, actualClass);
 }

 @Test
 public void testDistance() {
 DistanceMeasure distance = new EuclideanDistance();
 Instance instance1 = parentDataset.instance(0);
 Instance instance2 = parentDataset.instance(1);
 double expectedDistance = distance.distance(instance1, instance2);
 double actualDistance = distance.distance(fold.instance(0), fold.instance(1));
 assertEquals(expectedDistance, actualDistance, 0.0);
 }

}