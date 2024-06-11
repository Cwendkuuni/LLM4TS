package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

import net.sf.javaml.distance.*;
import java.util.*;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        assertEquals(5, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
    }

    @Test
    public void testClasses() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
        dataset.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, "class2"));
        Fold fold = new Fold(dataset, new int[]{0, 2, 4});
        Set<Object> classes = fold.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.folds(5, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.instance(5);
    }

    @Test
    public void testKNearest() {
        throw new UnsupportedOperationException("Method is not yet tested as it's not yet implemented");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstances() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.addAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}), new DenseInstance(new double[]{2.0, 3.0, 4.0})));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstancesAtIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.addAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}), new DenseInstance(new double[]{2.0, 3.0, 4.0})), 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.contains(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.containsAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}), new DenseInstance(new double[]{2.0, 3.0, 4.0})));
    }

    @Test
    public void testGet() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
        dataset.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, "class2"));
        Fold fold = new Fold(dataset, new int[]{0, 2});
        Instance instance = fold.get(0);
        assertEquals("class1", instance.classValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.indexOf(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
    }

    @Test
    public void testIsEmpty() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{});
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
        dataset.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, "class2"));
        Fold fold = new Fold(dataset, new int[]{0, 2});
        Iterator<Instance> iterator = fold.iterator();
        Instance instance = iterator.next();
        assertEquals("class1", instance.classValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.listIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorAtIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.listIterator(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.remove(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.removeAtIndex(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.removeAll(Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}), new DenseInstance(new double[]{2.0, 3.0, 4.0})));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4});
        fold.set(1, new DenseInstance(new double[]{1.0, 2.0, 3.0}));
    }
}