package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceFactory;
import net.sf.javaml.core.distance.EuclideanDistance;
import net.sf.javaml.util.SimpleRandomSource;

public class FoldTest {

 // Test constructor
 @org.junit.Test
 public void testFoldConstructor() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);
 assertEquals("Parent dataset should not be null", parentDataset, fold.getParent());
 assertEquals("Indices should not be null", indices, fold.getIndices());
 }

 // Test size method
 @org.junit.Test
 public void testFoldSize() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);
 assertEquals("Fold size should be equal to the number of indices", 3, fold.size());
 }

 // Test getInstance method
 @org.junit.Test
 public void testGetInstance() {
 Dataset parentDataset = new DefaultDataset();
 InstanceFactory instanceFactory = new DenseInstanceFactory(2);
 List<Instance> instances = Arrays.asList(instanceFactory.generate(10));
 Dataset foldData = new DefaultDataset(instances);
 int[] indices = new int[] { 3, 5, 1 };
 Fold fold = new Fold(foldData, indices);
 for (int i = 0; i < fold.size(); i++) {
 Instance expectedInstance = foldData.instance(indices[i]);
 Instance actualInstance = fold.getInstance(i);
 assertEquals("Instance at index " + i + " should be equal", expectedInstance, actualInstance);
 }
 }

 // Test add method
 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);
 Instance instance = new DenseInstance(new double[] { 1.0, 2.0 });
 fold.add(instance); // This should throw an UnsupportedOperationException
 }

 // Test classes method
 @org.junit.Test
 public void testClasses() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[] { 1.0, 1.0 }, 0));
 parentDataset.add(new DenseInstance(new double[] { 2.0, 2.0 }, 1));
 parentDataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, 0));
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);
 Set<Object> expectedClasses = Collections.singleton(0);
 Set<Object> actualClasses = fold.classes();
 assertEquals("Classes should be equal", expectedClasses, actualClasses);
 }

 // Test kNearest method (not yet implemented)
 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[] { 1.0, 1.0 }, 0));
 parentDataset.add(new DenseInstance(new double[] { 2.0, 2.0 }, 1));
 parentDataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, 0));
 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);
 Instance testInstance = new DenseInstance(new double[] { 1.5, 1.5 });
 DistanceMeasure distanceMeasure = new EuclideanDistance();
 fold.kNearest(2, testInstance, distanceMeasure); // This should throw an UnsupportedOperationException
 }

}