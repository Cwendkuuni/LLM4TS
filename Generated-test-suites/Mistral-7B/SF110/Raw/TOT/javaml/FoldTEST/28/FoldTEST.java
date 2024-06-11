package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class FoldTest {

 @Test
 public void testFoldCreation() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = new int[] { 0, 1, 2, 3, 4 };
 Fold fold = new Fold(parentDataset, indices);

 assertSame(parentDataset, fold.parent);
 assertSame(indices, fold.indices);
 }

 @Test
 public void testGetInstance() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[] { 1.0, 2.0 }));
 parentDataset.add(new DenseInstance(new double[] { 2.0, 3.0 }));
 parentDataset.add(new DenseInstance(new double[] { 3.0, 4.0 }));

 int[] indices = new int[] { 0, 2 };
 Fold fold = new Fold(parentDataset, indices);

 Instance expectedInstance = parentDataset.instance(0);
 Instance actualInstance = fold.instance(0);
 assertSame(expectedInstance, actualInstance);

 expectedInstance = parentDataset.instance(1);
 actualInstance = fold.instance(1);
 assertSame(expectedInstance, fold.instance(1)); // Testing with the incorrect index
 assertNull(fold.instance(2)); // Testing with an out-of-bounds index
 }

 @Test
 public void testClassMethodCalls() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[] { 1.0, 2.0, "A" }));
 parentDataset.add(new DenseInstance(new double[] { 2.0, 3.0, "B" }));
 parentDataset.add(new DenseInstance(new double[] { 3.0, 4.0, "C" }));

 int[] indices = new int[] { 0, 2 };
 Fold fold = new Fold(parentDataset, indices);

 Set<Object> classesSet = fold.classes();
 assertSame(parentDataset.classes(), classesSet);
 }

 @Test
 public void testUnsupportedMethods() {
 Dataset parentDataset = new DefaultDataset();
 int[] indices = new int[0];
 Fold fold = new Fold(parentDataset, indices);

 assertThrows(UnsupportedOperationException.class, () -> fold.add(new DenseInstance(new double[0])));
 assertThrows(UnsupportedOperationException.class, () -> fold.addAll(Collections.singletonList(new DenseInstance(new double[0]))));
 assertThrows(UnsupportedOperationException.class, () -> fold.clear());
 }

 @Test
 public void testKNearest() {
 // This method is not yet implemented, so we cannot test it directly.
 // However, we can test the exception that should be thrown when trying to call it.

 Dataset parentDataset = new DefaultDataset();
 parentDataset.add(new DenseInstance(new double[] { 1.0, 2.0, 1.0 }));
 parentDataset.add(new DenseInstance(new double[] { 2.0, 3.0, 2.0 }));
 parentDataset.add(new DenseInstance(new double[] { 3.0, 4.0, 3.0 }));

 int[] indices = new int[] { 0, 1, 2 };
 Fold fold = new Fold(parentDataset, indices);

 assertThrows(UnsupportedOperationException.class, () -> fold.kNearest(1, new DenseInstance(new double[0]), new EuclideanDistance()));
 }

 @Test
 public void testRandomness() {
 Dataset parentDataset = new DefaultDataset();
 parentDataset.addAll(Arrays.asList(new DenseInstance(new double[] { 1.0, 2.0 }), new DenseInstance(new double[] { 2.0, 3.0 }), new DenseInstance(new double[] { 3.0, 4.0 })));
 Random random = new Random(12345);

 int[] indices = new int[3];
 for (int i = 0; i < indices.length; ++i) {
 indices[i] = random.nextInt(parentDataset.size());
 }
 Fold fold = new Fold(parentDataset, indices);

 // Since the indices are randomly generated, the test results will vary.
 // However, we can test that the fold contains the correct number of instances.
 assertSame(parentDataset.size(), fold.size());
 }
}