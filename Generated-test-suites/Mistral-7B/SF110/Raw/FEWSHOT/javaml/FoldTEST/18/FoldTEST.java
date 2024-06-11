package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        Fold fold = new Fold(dataset, indices);
        assertEquals(5, fold.size());
    }

    @Test
    public void testGetInstance() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10]);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        for (int i = 0; i < 5; i++) {
            Instance expectedInstance = dataset.instance(indices[i]);
            Instance actualInstance = fold.instance(i);
            assertSame(expectedInstance, actualInstance);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        Fold fold = new Fold(dataset, indices);
        Instance instance = new DenseInstance(new double[10]);
        fold.add(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        Fold fold = new Fold(dataset, indices);
        Instance[] instances = new Instance[5];
        for (int i = 0; i < 5; i++) {
            instances[i] = new DenseInstance(new double[10]);
        }
        fold.addAll(Arrays.asList(instances));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        Fold fold = new Fold(dataset, indices);
        fold.clear();
    }

    @Test
    public void testClasses() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10], i % 2 == 0 ? 0.0 : 1.0);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        Set<Object> expectedClasses = dataset.classes();
        Set<Object> actualClasses = fold.classes();
        assertEquals(expectedClasses, actualClasses);
    }

    @Test
    public void testFolds() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10]);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        Dataset[] expectedFolds = new Dataset[5];
        for (int i = 0; i < 5; i++) {
            expectedFolds[i] = new DefaultInstanceList(Arrays.copyOfRange(indices, i, indices.length));
        }
        Dataset[] actualFolds = fold.folds(5, new Random());
        assertArrayEquals(expectedFolds, actualFolds);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesWithIndex() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        Fold fold = new Fold(dataset, indices);
        Instance[] instances = new Instance[5];
        for (int i = 0; i < 5; i++) {
            instances[i] = new DenseInstance(new double[10]);
        }
        fold.addAll(Arrays.asList(indices), Arrays.asList(instances));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        Fold fold = new Fold(dataset, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10]);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        fold.remove(dataset.instance(indices[0]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10]);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        fold.removeAll(Arrays.asList(dataset.subList(0, 1)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10]);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        fold.retainAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Dataset dataset = new DefaultInstanceList();
        int[] indices = new int[5];
        for (int i = 0; i < 5; i++) {
            Instance instance = new DenseInstance(new double[10]);
            dataset.add(instance);
            indices[i] = i;
        }
        Fold fold = new Fold(dataset, indices);
        fold.set(0, new DenseInstance(new double[10]));
    }
}