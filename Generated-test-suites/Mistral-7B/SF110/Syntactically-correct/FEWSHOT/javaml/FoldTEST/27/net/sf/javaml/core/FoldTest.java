package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        assertEquals(5, fold.size());
    }

    @Test
    public void testGetInstance() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        Instance instance = fold.instance(0);
        assertEquals(instance, parent.instance(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {});
        fold.add(new DenseInstance(4.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {});
        fold.addAll(Arrays.asList(new DenseInstance(4.0), new DenseInstance(5.0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {});
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstanceByIndex() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        fold.removeAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        fold.retainAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        fold.set(0, new DenseInstance(4.0));
    }

    @Test
    public void testIterator() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {1, 2, 0, 4, 5});
        List<Instance> instances = Arrays.asList(parent.instance(1), parent.instance(2), parent.instance(0),
                parent.instance(4), parent.instance(5));
        List<Instance> list = new ArrayList<>();
        for (Instance instance : fold) {
            list.add(instance);
        }
        assertEquals(instances, list);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        Dataset parent = new RandomInstancesSet(2, 3);
        Fold fold = new Fold(parent, new int[] {});
        fold.addAll(Arrays.asList(new DenseInstance(4.0), new DenseInstance(5.0)), 0);
    }
}