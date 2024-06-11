package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        assertTrue(fold.size() == 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.add(new DenseInstance(new double[2]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.addAll(Arrays.asList(new DenseInstance[10]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndices() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.addAll(Arrays.asList(new int[10]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.remove(new DenseInstance(new double[2]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.removeAll(Arrays.asList(new DenseInstance[10]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllIndices() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.removeAll(Arrays.asList(new int[10]));
    }

    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[2]));
        parent.add(new DenseInstance(new double[2]));
        int[] indices = new int[2];
        indices[0] = 0;
        indices[1] = 1;
        Fold fold = new Fold(parent, indices);
        Instance instance = fold.instance(0);
        assertTrue(instance.classValue() == parent.instance(0).classValue());
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[2], 0));
        parent.add(new DenseInstance(new double[2], 1));
        int[] indices = new int[2];
        indices[0] = 0;
        indices[1] = 1;
        Fold fold = new Fold(parent, indices);
        Set<Object> classes = fold.classes();
        assertTrue(classes.contains(0));
        assertTrue(classes.contains(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        fold.folds(5, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[5];
        Fold fold = new Fold(parent, indices);
        List<Instance> subList = fold.subList(0, 3);
    }

    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[2]));
        parent.add(new DenseInstance(new double[2]));
        int[] indices = new int[2];
        indices[0] = 0;
        indices[1] = 1;
        Fold fold = new Fold(parent, indices);
        Object[] toArray = fold.toArray();
        assertTrue(toArray[0] instanceof Instance);
        assertTrue(toArray[1] instanceof Instance);
    }

    @Test
    public void testToArrayWithType() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[2]));
        parent.add(new DenseInstance(new double[2]));
        int[] indices = new int[2];
        indices[0] = 0;
        indices[1] = 1;
        Fold fold = new Fold(parent, indices);
        DenseInstance[] toArray = (DenseInstance[]) fold.toArray(new DenseInstance[2]);
        assertTrue(toArray[0] instanceof DenseInstance);
        assertTrue(toArray[1] instanceof DenseInstance);
    }
}