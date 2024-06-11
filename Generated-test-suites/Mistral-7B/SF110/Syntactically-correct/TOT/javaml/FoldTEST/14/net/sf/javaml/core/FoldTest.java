package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

public class FoldTest {

 // Test constructor
 @org.junit.Test
 public void testFoldConstruction() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parentDataSet, indices);

 assertEquals(parentDataSet, fold.getParent());
 assertEquals(2, fold.size());
 }

 // Test getParent method
 @org.junit.Test
 public void testGetParent() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parentDataSet, indices);

 Dataset expectedParent = parentDataSet;
 assertEquals(expectedParent, fold.getParent());
 }

 // Test instance method with valid index
 @org.junit.Test
 public void testInstanceValidIndex() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parentDataSet, indices);

 Instance expectedInstance = instance1;
 Instance instance = fold.instance(0);

 assertEquals(expectedInstance, instance);
 }

 // Test instance method with invalid index
 @org.junit.Test(expected = IndexOutOfBoundsException.class)
 public void testInstanceInvalidIndex() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parentDataSet, indices);

 fold.instance(2);
 }

 // Test size method
 @org.junit.Test
 public void testSize() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parentDataSet, indices);

 int expectedSize = 2;
 assertEquals(expectedSize, fold.size());
 }

 // Test classes method
 @org.junit.Test
 public void testClasses() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 }, new double[] { 0.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 }, new double[] { 1.0 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int[] indices = new int[] { 0, 1 };
 Fold fold = new Fold(parentDataSet, indices);

 Set<Object> expectedClasses = parentDataSet.classes();
 Set<Object> classes = fold.classes();

 assertEquals(expectedClasses, classes);
 }

 // Test kNearest method (not implemented)
 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Dataset parentDataSet = new DefaultDataset();
 Instance instance1 = new DefaultInstance(new double[] { 1.0, 1.0, 1.0 });
 Instance instance2 = new DefaultInstance(new double[] { 2.0, 2.0, 2.0 });
 Instance queryInstance = new DefaultInstance(new double[] { 1.5, 1.5, 1.5 });
 parentDataSet.add(instance1);
 parentDataSet.add(instance2);

 int k = 2;
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Fold fold = new Fold(parentDataSet, new int[0]);
 Set<Instance> expectedNeighbors = new HashSet<Instance>();
 expectedNeighbors.addAll(parentDataSet);
 fold.kNearest(k, queryInstance, distanceMeasure);
 }
}