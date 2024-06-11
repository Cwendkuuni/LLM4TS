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
import net.sf.javaml.distance.SquareEuclideanDistance;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        assertEquals("Expected parent to be set", parent, fold.parent);
        assertEquals("Expected indices to be set", indices, fold.indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Instance inst = new DefaultInstance(1.0, 2.0);
        fold.add(inst);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.addAll(1, instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        boolean result = fold.contains(obj);
        assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> list = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        boolean result = fold.containsAll(list);
        assertFalse(result);
    }

    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        for (int i = 0; i < fold.size(); ++i) {
            Instance result = fold.instance(i);
            Instance expected = parent.instance(indices[i]);
            assertEquals("Unexpected result for index " + i, expected, result);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        int result = fold.indexOf(obj);
        assertEquals(-1, result);
    }

    @Test
    public void testIsEmpty() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] {};
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.isEmpty());
        int[] newIndices = new int[] { 0 };
        fold = new Fold(parent, newIndices);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        List<Instance> expectedInstances = Arrays.asList(parent.instance(indices[0]), parent.instance(indices[1]), parent.instance(indices[2]));
        List<Instance> instances = new ArrayList<Instance>();
        for (Instance inst : fold) {
            instances.add(inst);
        }
        assertEquals("Unexpected number of instances", expectedInstances.size(), instances.size());
        for (int i = 0; i < instances.size(); ++i) {
            Instance result = instances.get(i);
            Instance expected = expectedInstances.get(i);
            assertEquals("Unexpected result for index " + i, expected, result);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.listIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.listIterator(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        fold.removeAtIndex(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Collections.singletonList(new DefaultInstance(1.0, 2.0));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Instance newInstance = new DefaultInstance(1.1, 2.1);
        fold.set(0, newInstance);
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        assertEquals(3, fold.size());
    }

    @Test
    public void testSubList() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        List<Instance> expectedInstances = Arrays.asList(parent.instance(indices[0]), parent.instance(indices[1]));
        List<Instance> instances = fold.subList(0, 2);
        assertEquals("Unexpected number of instances", expectedInstances.size(), instances.size());
        for (int i = 0; i < instances.size(); ++i) {
            Instance result = instances.get(i);
            Instance expected = expectedInstances.get(i);
            assertEquals("Unexpected result for index " + i, expected, result);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Object[] result = fold.toArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArrayWithArray() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Object[] result = fold.toArray(new Object[fold.size()]);
    }

    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        int result = fold.classIndex(obj);
        assertEquals(-1, result);
        Instance instance = new DefaultInstance(1.0, 2.0);
        int index = fold.classIndex(instance);
        assertEquals(parent.classIndex(instance), index);
    }

    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0 };
        Fold fold = new Fold(parent, indices);
        Instance instance = new DefaultInstance(1.0, 2.0);
        Object result = fold.classValue(0);
        assertEquals(parent.classValue(0), result);
        result = fold.classValue(instance.index());
        assertEquals(parent.classValue(instance.index()), result);
    }

    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1 };
        Fold fold = new Fold(parent, indices);
        Dataset expectedCopy = new Fold(parent.copy(), Arrays.copyOf(indices, indices.length));
        Dataset resultCopy = fold.copy();
        assertEquals(expectedCopy, resultCopy);
    }

    @Test
    public void testFoldsNullRandom() {
        Dataset parent = new DefaultDataset();
        int numFolds = 3;
        Fold fold = new Fold(parent, new int[0]);
        try {
            fold.folds(numFolds, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldsZeroFolds() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.folds(0, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldsNegativeFolds() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.folds(-1, new Random());
    }

    @Test
    public void testFoldsWithValidParameters() {
        Dataset parent = new DefaultDataset();
        int numFolds = 3;
        Fold fold = new Fold(parent, new int[0]);
        Dataset[] resultFolds = fold.folds(numFolds, new Random());
        assertEquals(numFolds, resultFolds.length);
        for (int i = 0; i < resultFolds.length; ++i) {
            Dataset resultFolder = resultFolds[i];
            Instance instance = parent.instance(i / numFolds);
            assertEquals("Unexpected number of instances in the result folder", parent.size(), resultFolder.size());
            assertEquals("Unexpected instance at index 0 in the result folder", instance, resultFolder.instance(0));
        }
    }

    @Test
    public void testKNearestNullInstance() {
        Dataset parent = new DefaultDataset();
        int k = 3;
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Fold fold = new Fold(parent, new int[0]);
        try {
            fold.kNearest(k, null, distanceMeasure);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearestWithoutImplementation() {
        Dataset parent = new DefaultDataset();
        int k = 3;
        Fold fold = new Fold(parent, new int[0]);
        fold.kNearest(k, new DefaultInstance(1.0, 2.0), null);
    }

    @Test
    public void testKNearestWithOneInstance() {
        Dataset parent = new DefaultDataset();
        int k = 1;
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Fold fold = new Fold(parent, new int[0]);
        Instance instance = parent.instance(0);
        List<Instance> result = fold.kNearest(k, instance, distanceMeasure);
        assertEquals(1, result.size());
        assertEquals(instance, result.get(0));
    }

    @Test
    public void testKNearestWithMultipleInstances() {
        Dataset parent = new DefaultDataset();
        int k = 3;
        DistanceMeasure distanceMeasure = new SquareEuclideanDistance();
        Fold fold = new Fold(parent, new int[0]);
        Instance queryInstance = new DefaultInstance(1.0, 2.0);
        List<Instance> result = fold.kNearest(k, queryInstance, distanceMeasure);
        assertEquals(k, result.size());
        for (Instance instance : result) {
            int index = parent.indexOf(instance);
            double distance = distanceMeasure.distance(queryInstance, instance);
            assertTrue(distance <= 0.0 || distance <= distanceMeasure.distance(queryInstance, parent.instance(index / k)));
        }
    }
}