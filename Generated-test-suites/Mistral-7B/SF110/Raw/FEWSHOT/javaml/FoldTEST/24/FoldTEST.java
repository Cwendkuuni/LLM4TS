package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldTest {

    @Test
    public void testConstructor() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(parentDataset, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test
    public void testSize() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(5, fold.size());
    }

    @Test
    public void testInstance() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        Instance instance2 = new DenseInstance(2.0);
        Instance instance3 = new DenseInstance(3.0);
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        parentDataset.add(instance3);
        int[] indices = {1, 2, 3, 0, 4};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(instance2, fold.instance(0));
        assertEquals(instance3, fold.instance(1));
        assertEquals(instance0, fold.instance(2));
        assertEquals(instance1, fold.instance(3));
        assertEquals(instance4, fold.instance(4));
    }

    @Test
    public void testClasses() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new DoubleArray(new double[]{0.0, 1.0}));
        Instance instance2 = new DenseInstance(2.0, new DoubleArray(new double[]{1.0, 1.0}));
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        Fold fold = new Fold(parentDataset, new int[]{0, 1});
        List<Object> classes = fold.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(0.0));
        assertTrue(classes.contains(1.0));
    }

    @Test
    public void testFolds() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testKNearest() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testAdd() {
        throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
    }

    @Test
    public void testAddAll() {
        throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
    }

    @Test
    public void testClear() {
        throw new UnsupportedOperationException("Cannot do this on a fold of a dataset");
    }

    @Test
    public void testContains() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testContainsAll() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testGet() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testIndexOf() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testIsEmpty() {
        Dataset parentDataset = new DefaultDataset();
        Fold fold = new Fold(parentDataset, new int[0]);
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        Instance instance2 = new DenseInstance(2.0);
        Instance instance3 = new DenseInstance(3.0);
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        parentDataset.add(instance3);
        int[] indices = {1, 2, 3, 0, 4};
        Fold fold = new Fold(parentDataset, indices);
        List<Instance> instances = new ArrayList<>();
        for (Instance instance : fold) {
            instances.add(instance);
        }
        assertEquals(5, instances.size());
        assertTrue(instances.contains(instance2));
        assertTrue(instances.contains(instance3));
        assertTrue(instances.contains(instance0));
        assertTrue(instances.contains(instance1));
        assertTrue(instances.contains(instance4));
    }

    @Test
    public void testLastIndexOf() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testListIterator() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testListIteratorWithIndex() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRemove() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRemoveAt() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRemoveAll() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRetainAll() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testSet() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testToArray() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        Instance instance2 = new DenseInstance(2.0);
        Instance instance3 = new DenseInstance(3.0);
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        parentDataset.add(instance3);
        int[] indices = {1, 2, 3, 0, 4};
        Fold fold = new Fold(parentDataset, indices);
        Object[] expectedArray = new Object[]{instance2, instance3, instance0, instance1, instance4};
        assertArrayEquals(expectedArray, fold.toArray());
    }

    @Test
    public void testToArrayWithCapacity() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        Instance instance2 = new DenseInstance(2.0);
        Instance instance3 = new DenseInstance(3.0);
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        parentDataset.add(instance3);
        int[] indices = {1, 2, 3, 0, 4};
        Fold fold = new Fold(parentDataset, indices);
        Object[] expectedArray = new Object[5];
        fold.toArray(expectedArray);
        assertArrayEquals(expectedArray, fold.toArray());
    }

    @Test
    public void testNoAttributes() {
        Dataset parentDataset = new DefaultDataset();
        Fold fold = new Fold(parentDataset, new int[0]);
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new DoubleArray(new double[]{0.0, 1.0}));
        Instance instance2 = new DenseInstance(2.0, new DoubleArray(new double[]{1.0, 1.0}));
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        Fold fold = new Fold(parentDataset, new int[]{0, 1});
        assertEquals(0, fold.classIndex(0.0));
        assertEquals(1, fold.classIndex(1.0));
    }

    @Test
    public void testClassValue() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new DoubleArray(new double[]{0.0, 1.0}));
        Instance instance2 = new DenseInstance(2.0, new DoubleArray(new double[]{1.0, 1.0}));
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        Fold fold = new Fold(parentDataset, new int[]{0, 1});
        assertEquals(0.0, fold.classValue(0));
        assertEquals(1.0, fold.classValue(1));
    }

    @Test
    public void testCopy() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        Instance instance2 = new DenseInstance(2.0);
        parentDataset.add(instance1);
        parentDataset.add(instance2);
        int[] indices = {1, 2};
        Fold fold = new Fold(parentDataset, indices);
        Dataset copy = fold.copy();
        assertEquals(parentDataset, fold.parent);
        assertEquals(copy.parent, copy.copy().parent);
        assertNotEquals(fold, copy);
        List<Instance> instancesFold = new ArrayList<>();
        List<Instance> instancesCopy = new ArrayList<>();
        for (Instance instance : fold) {
            instancesFold.add(instance);
        }
        for (Instance instance : copy) {
            instancesCopy.add(instance);
        }
        assertEquals(instancesFold.size(), instancesCopy.size());
        for (int i = 0; i < instancesFold.size(); i++) {
            assertEquals(instancesFold.get(i), instancesCopy.get(i));
        }
    }
}