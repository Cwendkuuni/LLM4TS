package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
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
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);

        assertTrue(fold.size() == indices.length);
    }

    @Test
    public void testGet() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);

        final Instance instance = new DenseInstance(2);
        parent.add(instance);

        for (int i = 0; i < fold.size(); ++i) {
            assertTrue(fold.get(i).equals(parent.instance(indices[i])));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        fold.add(new DenseInstance(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        fold.addAll(Arrays.asList(new DenseInstance(1), new DenseInstance(2)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);
        fold.removeAll(Collections.singleton(new DenseInstance(0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);
        fold.retainAll(Collections.singleton(new DenseInstance(0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);
        fold.set(0, new DenseInstance(1));
    }

    @Test
    public void testSubList() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[3];
        Arrays.fill(indices, 0);
        final Fold fold = new Fold(parent, indices);

        final Instance instance1 = new DenseInstance(1);
        final Instance instance2 = new DenseInstance(2);
        final Instance instance3 = new DenseInstance(3);
        parent.add(instance1);
        parent.add(instance2);
        parent.add(instance3);

        final List<Instance> list = Arrays.asList(instance1, instance2);
        assertArrayEquals(list.toArray(), fold.subList(0, 2).toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll_Collection() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        fold.addAll(new ArrayList<Instance>());
    }
}