package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class FoldTest {

    private Dataset parentDataSet;
    private int[] indices;
    private Fold fold;

    @Before
    public void setUp() {
        parentDataSet = new ArrayListDataset();
        indices = new int[]{0, 1, 2, 3};
        fold = new Fold(parentDataSet, indices);
    }

    @Test
    public void testConstructor() {
        assertSame(parentDataSet, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        fold.add(new DenseInstance(new double[0]));
    }

    @Test
    public void testClasses() {
        assertSame(parentDataSet.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        new Random(); // Initialize random for the test
        fold.folds(5, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstanceByWrongIndex() {
        assertNull(fold.instance(-1));
    }

    @Test
    public void testGetInstanceByIndex() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        parentDataSet.add(instance);
        assertSame(instance, fold.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        new Random(); // Initialize random for the test
        fold.kNearest(5, new DenseInstance(new double[0]), new EuclideanDistance());
    }

    @Test
    public void testSize() {
        assertTrue(fold.size() == indices.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0]), new DenseInstance(new double[0]));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test
    public void testIsEmpty() {
        assertFalse(fold.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        List<Instance> instances = Collections.singletonList(new DenseInstance(new double[0]));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        List<Instance> instances = Collections.singletonList(new DenseInstance(new double[0]));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Instance newInstance = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        fold.set(0, newInstance);
    }

    @Test
    public void testToArray() {
        Instance[] expectedInstances = new Instance[indices.length];
        for (int i = 0; i < indices.length; i++) {
            expectedInstances[i] = parentDataSet.instance(indices[i]);
        }
        assertArrayEquals(expectedInstances, fold.toArray());
    }

    @Test
    public void testToArrayWithCustomType() {
        DenseInstance[] expectedInstances = new DenseInstance[indices.length];
        for (int i = 0; i < indices.length; i++) {
            expectedInstances[i] = (DenseInstance)parentDataSet.instance(indices[i]);
        }
        DenseInstance[] resultInstances = (DenseInstance[])fold.toArray(new DenseInstance[indices.length]);
        assertArrayEquals(expectedInstances, resultInstances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoAttributes() {
        fold.noAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClassIndex() {
        fold.classIndex("testClass");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClassValue() {
        fold.classValue(0);
    }

    @Test
    public void testCopy() {
        Dataset copy = fold.copy();
        for (int i = 0; i < fold.size(); i++) {
            Instance instance = fold.instance(i);
            Instance copyInstance = copy.instance(i);
            assertSame(instance, copyInstance);
        }
    }
}