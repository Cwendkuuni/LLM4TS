package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(1.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Collection<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Collection<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
        fold.addAll(1, instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(new DenseInstance(1.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Collection<Instance> instances = Arrays.asList(new DenseInstance(1.0), new DenseInstance(2.0));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.set(0, new DenseInstance(1.0));
    }

    @Test
    public void testSize() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[3]);
        assertEquals(3, fold.size());
    }

    @Test
    public void testClasses() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[3]);
        assertEquals(dataset.classes(), fold.classes());
    }

    @Test
    public void testGet() {
        Dataset dataset = new DefaultDataset();
        for (int i = 0; i < 5; ++i) {
            dataset.add(new DenseInstance(i));
        }
        Fold fold = new Fold(dataset, new int[] { 3, 1, 4, 0, 2 });
        for (int i = 0; i < fold.size(); ++i) {
            assertEquals(i, fold.get(i).classValue(0));
        }
    }

    @Test
    public void testInstance() {
        Dataset dataset = new DefaultDataset();
        for (int i = 0; i < 5; ++i) {
            dataset.add(new DenseInstance(i));
        }
        Fold fold = new Fold(dataset, new int[] { 3, 1, 4, 0, 2 });
        for (int i = 0; i < fold.size(); ++i) {
            assertEquals(dataset.instance(fold.indices[i]), fold.instance(i));
        }
    }

    @Test
    public void testListIterator() {
        Dataset dataset = new DefaultDataset();
        for (int i = 0; i < 5; ++i) {
            dataset.add(new DenseInstance(i));
        }
        Fold fold = new Fold(dataset, new int[] { 3, 1, 4, 0, 2 });
        ListIterator<Instance> iterator = fold.listIterator();
        List<Instance> expected = Arrays.asList(dataset.instance(2), dataset.instance(0), dataset.instance(4),
                dataset.instance(1), dataset.instance(3));
        for (int i = 0; i < fold.size(); ++i) {
            assertTrue(iterator.hasNext());
            Instance current = iterator.next();
            Instance expectedInstance = expected.get(i);
            assertEquals(expectedInstance, current);
        }
    }

    @Test
    public void testListIteratorIndex() {
        Dataset dataset = new DefaultDataset();
        for (int i = 0; i < 5; ++i) {
            dataset.add(new DenseInstance(i));
        }
        Fold fold = new Fold(dataset, new int[] { 3, 1, 4, 0, 2 });
        ListIterator<Instance> iterator = fold.listIterator(1);
        List<Instance> expected = Arrays.asList(dataset.instance(1), dataset.instance(0), dataset.instance(3),
                dataset.instance(4), dataset.instance(2));
        for (int i = 0; i < fold.size(); ++i) {
            assertTrue(iterator.hasNext());
            Instance current = iterator.next();
            Instance expectedInstance = expected.get(i);
            assertEquals(expectedInstance, current);
        }
    }

    @Test
    public void testIterator() {
        Dataset dataset = new DefaultDataset();
        for (int i = 0; i < 5; ++i) {
            dataset.add(new DenseInstance(i));
        }
        Fold fold = new Fold(dataset, new int[] { 3, 1, 4, 0, 2 });
        Iterator<Instance> iterator = fold.iterator();
        List<Instance> expected = Arrays.asList(dataset.instance(2), dataset.instance(0), dataset.instance(4),
                dataset.instance(1), dataset.instance(3));
        for (int i = 0; i < fold.size(); ++i) {
            assertEquals(expected.get(i), iterator.next());
        }
    }

    @Test
    public void testNoAttributes() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[3]);
        assertEquals(dataset.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(1.0, 0.0, 1.0));
        dataset.add(new DenseInstance(1.0, 0.0, 0.0));
        Fold fold = new Fold(dataset, new int[3]);
        assertEquals(0, fold.classIndex("0"));
        assertEquals(1, fold.classIndex("1"));
    }

    @Test
    public void testClassValue() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(1.0, 0.0, "0"));
        dataset.add(new DenseInstance(1.0, 0.0, "1"));
        Fold fold = new Fold(dataset, new int[3]);
        assertEquals("0", fold.instance(0).classValue(0));
        assertEquals("1", fold.instance(1).classValue(0));
    }

    @Test
    public void testCopy() {
        Dataset dataset = new DefaultDataset();
        for (int i = 0; i < 5; ++i) {
            dataset.add(new DenseInstance(i));
        }
        Fold originalFold = new Fold(dataset, new int[] { 3, 1, 4, 0, 2 });
        Fold copiedFold = originalFold.copy();
        for (int i = 0; i < copiedFold.size(); ++i) {
            assertEquals(originalFold.instance(i), copiedFold.instance(i));
        }
    }
}