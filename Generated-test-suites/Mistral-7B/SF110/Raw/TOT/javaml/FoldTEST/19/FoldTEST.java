package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import net.sf.javaml.distance.EuclideanDistanceMeasure;
import net.sf.javaml.distance.SquareEuclideanDistanceMeasure;
import net.sf.javaml.instance.ArrayDatasetType;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.instance.numeric.ArrayRealInstance;
import net.sf.javaml.util.SimpleRandomGenerator;

import org.junit.Test;

public class FoldTest {

 // Test constructor
 @Test
 public void testFoldConstructor() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 assertEquals(2, fold.size());
 }

 // Test instance method
 @Test
 public void testInstance() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 Instance expected = new ArrayRealInstance(new double[]{1.0, 2.0}, new String[]{"class1"});
 Instance result = fold.instance(0);
 assertEquals(expected, result);
 }

 // Test empty instance method
 @Test(expected = UnsupportedOperationException.class)
 public void testEmptyInstance() {
 Dataset parent = new ArrayDatasetType(new double[][]{}, new int[]{}, new String[]{"class1"});
 int[] indices = new int[]{};
 Fold fold = new Fold(parent, indices);
 fold.instance(0);
 }

 // Test add method
 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 Instance instance = new ArrayRealInstance(new double[]{5.0, 6.0}, new String[]{"class2"});
 fold.add(instance);
 }

 // Test classes method
 @Test
 public void testClasses() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 Set<Object> result = fold.classes();
 Set<Object> expected = parent.classes();
 assertEquals(expected, result);
 }

 // Test kNearest method (not yet implemented)
 @Test(expected = UnsupportedOperationException.class)
 public void testKNearest() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 Instance instance = new ArrayRealInstance(new double[]{5.0, 6.0}, new String[]{"class3"});
 DistanceMeasure dm = new EuclideanDistanceMeasure();
 fold.kNearest(2, instance, dm);
 }

 // Test folds method (not yet implemented)
 @Test(expected = UnsupportedOperationException.class)
 public void testFolds() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 Dataset[] result = fold.folds(5, new SimpleRandomGenerator());
 }

 // Test addAll method
 @Test(expected = UnsupportedOperationException.class)
 public void testAddAll() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};Fold fold = new Fold(parent, indices);
 Instance[] instances = new Instance[]{new ArrayRealInstance(new double[]{5.0, 6.0}, new String[]{"class3"}), new ArrayRealInstance(new double[]{7.0, 8.0}, new String[]{"class4"})};
 fold.addAll(Arrays.asList(instances));
 }

 // Test clear method
 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 fold.clear();
 }

 // Test size method
 @Test
 public void testSize() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 assertEquals(2, fold.size());
 }

 // Test copy method
 @Test
 public void testCopy() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 Dataset expected = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 Dataset result = fold.copy();
 assertEquals(expected, result);
 }

 // Test noAttributes method
 @Test
 public void testNoAttributes() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 assertEquals(2, fold.noAttributes());
 }

 // Test classIndex method
 @Test
 public void testClassIndex() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 assertEquals(0, fold.classIndex("class1"));
 assertEquals(1, fold.classIndex("class2"));
 }

 // Test classValue method
 @Test
 public void testClassValue() {
 Dataset parent = new ArrayDatasetType(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new int[]{}, new String[]{"class1", "class2"});
 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parent, indices);
 assertEquals("class1", fold.classValue(0));
 assertEquals("class2", fold.classValue(1));
 }
}