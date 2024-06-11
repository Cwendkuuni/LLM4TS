package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testFold() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 2, 1};
        Fold fold = new Fold(parentDataset, indices);

        assertEquals(parentDataset, fold.getParent());
        assertEquals(3, fold.size());

        List<Instance> instances = Arrays.asList(parentDataset.instance(0), parentDataset.instance(1), parentDataset.instance(2));
        for (int i = 0; i < fold.size(); i++) {
            Instance instance = fold.instance(i);
            assertEquals(instances.get(i), instance);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        fold.add(new DenseInstance(new double[0]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0]));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0]));
        fold.addAll(1, instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0});
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstanceAtIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0});
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0]));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstanceAtIndex() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0});
        Instance instance = new DenseInstance(new double[0]);
        fold.set(0, instance);
    }
}