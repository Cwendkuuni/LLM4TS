package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.instance.ArrayInstance;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest {

 private Dataset parentDataset;
 private Fold fold;

 @Before
 public void setUp() {
 int numInstances = 5;
 int numAttributes = 2;
 double[][] data = new double[numInstances][numAttributes];

 for (int i = 0; i < numInstances; i++) {
 for (int j = 0; j < numAttributes; j++) {
 data[i][j] = Math.random();
 }
 }

 parentDataset = new DefaultDataset();
 for (double[] instanceData : data) {
 parentDataset.add(new ArrayInstance(instanceData));
 }

 int[] foldIndices = new int[] { 1, 2, 3 };
 fold = new Fold(parentDataset, foldIndices);
 }

 @Test
 public void testSize() {
 assertEquals(fold.size(), 3);
 }

 @Test
 public void testGetInstance() {
 Instance instance = fold.instance(0);
 assertNotNull(instance);
 assertEquals(parentDataset.instance(foldIndices[0]), instance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInstance() {
 fold.add(new ArrayInstance(new double[] { 0.1, 0.2 }));
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllInstances() {
 List<Instance> instances = Arrays.asList(new ArrayInstance(new double[] { 0.1, 0.2 }));
 fold.addAll(instances);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 fail("Method is not yet implemented but should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Instance queryInstance = new ArrayInstance(new double[] { 0.5, 0.5 });
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 fail("Method is not yet implemented but should throw UnsupportedOperationException");
 }

 @Test
 public void testClasses() {
 Set<Object> expectedClasses = parentDataset.classes();
 Set<Object> actualClasses = fold.classes();
 assertEquals(expectedClasses, actualClasses);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInstance() {
 Instance instance = fold.instance(0);
 fold.remove(instance);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveIndex() {
 fold.remove(0);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 List<Instance> instances = Collections.singletonList(new ArrayInstance(new double[] { 0.1, 0.2 }));
 fold.addAll(instances);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 fold.clear();
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRetainAll() {
 List<Instance> instances = Collections.singletonList(new ArrayInstance(new double[] { 0.1, 0.2 }));
 fold.retainAll(instances);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() {
 Instance instance = fold.instance(0);
 fold.set(0, new ArrayInstance(new double[] { 0.6, 0.6 }));
 fail("Should throw UnsupportedOperationException");
 }
}