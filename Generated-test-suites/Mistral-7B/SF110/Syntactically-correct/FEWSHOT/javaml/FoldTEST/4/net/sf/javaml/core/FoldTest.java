package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(1.0, 2.0, 3.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(1.0, 2.0, 3.0), new DenseInstance(4.0, 5.0, 6.0));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstanceAt() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(1, new DenseInstance(1.0, 2.0, 3.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(new DenseInstance(1.0, 2.0, 3.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(1.0, 2.0, 3.0), new DenseInstance(4.0, 5.0, 6.0));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(1.0, 2.0, 3.0), new DenseInstance(4.0, 5.0, 6.0));
        fold.retainAll(instances);
    }

    @Test
    public void testSize() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertEquals(0, fold.size());
        Instance instance = new DenseInstance(1.0, 2.0, 3.0);
        fold.add(instance);
        assertEquals(1, fold.size());
    }

    @Test
    public void testClasses() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertEquals(Collections.emptySet(), fold.classes());
        Instance instance = new DenseInstance(1.0, 2.0, 3.0);
        dataset.add(instance);
        assertEquals(instance.classValue(), fold.classes().iterator().next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesWithNullCollection() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.addAll(null);
    }

    @Test
    public void testInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Instance instance = new DenseInstance(1.0, 2.0, 3.0);
        dataset.add(instance);
        fold.add(instance);
        assertEquals(instance, fold.instance(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInstanceWithInvalidIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(1.0, 2.0, 3.0));
        fold.add(new DenseInstance(4.0, 5.0, 6.0));
        fold.instance(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesWithNullInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(null, new DenseInstance(1.0, 2.0, 3.0));
        fold.addAll(instances);
    }

    @Test
    public void testSubList() {
        Dataset dataset = new DefaultDataset();
        Dataset subDataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Instance instance1 = new DenseInstance(1.0, 2.0, 3.0);
        Instance instance2 = new DenseInstance(4.0, 5.0, 6.0);
        dataset.add(instance1);
        dataset.add(instance2);
        fold.add(instance1);
        fold.add(instance2);
        List<Instance> subList = fold.subList(0, 1);
        assertEquals(1, subList.size());
        assertEquals(instance1, subList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSubListWithInvalidStartIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(1.0, 2.0, 3.0));
        fold.add(new DenseInstance(4.0, 5.0, 6.0));
        fold.subList(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSubListWithInvalidEndIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(1.0, 2.0, 3.0));
        fold.add(new DenseInstance(4.0, 5.0, 6.0));
        fold.subList(1, 2);
        fold.subList(1, 3);
        fold.subList(1, 4);
        fold.subList(1, 5);
        fold.subList(1, 6);
    }

    @Test
    public void testToArray() {
        Dataset dataset = new DefaultDataset();
        Dataset subDataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Instance instance1 = new DenseInstance(1.0, 2.0, 3.0);
        Instance instance2 = new DenseInstance(4.0, 5.0, 6.0);
        dataset.add(instance1);
        dataset.add(instance2);
        fold.add(instance1);
        fold.add(instance2);
        Object[] array = fold.toArray();
        assertEquals(2, array.length);
        assertEquals(instance1, array[0]);
        assertEquals(instance2, array[1]);
    }

    @Test
    public void testToArrayWithCapacity() {
        Dataset dataset = new DefaultDataset();
        Dataset subDataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Instance instance1 = new DenseInstance(1.0, 2.0, 3.0);
        Instance instance2 = new DenseInstance(4.0, 5.0, 6.0);
        dataset.add(instance1);
        dataset.add(instance2);
        fold.add(instance1);
        fold.add(instance2);
        Object[] array = new Object[2];
        fold.toArray(array);
        assertEquals(2, array.length);
        assertEquals(instance1, array[0]);
        assertEquals(instance2, array[1]);
    }

    @Test
    public void testFolds() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCopy() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }
}