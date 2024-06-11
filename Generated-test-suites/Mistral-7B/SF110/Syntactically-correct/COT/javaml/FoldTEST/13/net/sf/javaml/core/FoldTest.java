package net.sf.javaml.core;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class FoldTest {
    // Basic test cases

    @Test
    public void testConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals(parent, fold.getParent());
        assertArrayEquals(indices, fold.getIndices());
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals(3, fold.size());
    }

    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parent, indices);
        Instance expectedInstance1 = instance1;
        Instance expectedInstance2 = instance2;
        Instance actualInstance1 = fold.instance(0);
        Instance actualInstance2 = fold.instance(1);
        assertEquals(expectedInstance1, actualInstance1);
        assertEquals(expectedInstance2, actualInstance2);
    }

    // Edge cases and exception handling test cases

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0};
        Fold fold = new Fold(parent, indices);
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1);
        fold.add(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1));
        fold.addAll(1, instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstanceAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.remove(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = new ArrayList<>();
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1);
        fold.set(0, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        List<Instance> subList = fold.subList(0, 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object[] toArray = fold.toArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArrayWithArray() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object[] toArray = new Object[fold.size()];
        fold.toArray(toArray);
    }
}