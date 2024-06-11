package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset dataset = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(dataset, indices);
        assertSame(dataset, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(new double[0]));
    }

    @Test
    public void testClasses() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertSame(dataset.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.folds(5, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.instance(-1);
    }

    @Test
    public void testKNearest() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(new DenseInstance(new double[0], 0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], 0), new DenseInstance(new double[0], 1));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], 0), new DenseInstance(new double[0], 1));
        fold.addAll(2, instances);
    }

    @Test
    public void testClear() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.clear();
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testContains() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1 });
        Instance instance = new DenseInstance(new double[0]);
        assertFalse(fold.contains(instance));
        Instance instance2 = new DenseInstance(new double[0], 1);
        assertTrue(fold.contains(instance2));
        fold.instance(0).setClassValue(2);
        assertFalse(fold.contains(instance2));
    }

    @Test
    public void testContainsAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], 0), new DenseInstance(new double[0], 1));
        assertFalse(fold.containsAll(instances));
        List<Instance> instances2 = Arrays.asList(new DenseInstance(new double[0], 0), new DenseInstance(new double[0], 1), new DenseInstance(new double[0], 2));
        assertTrue(fold.containsAll(instances2));
    }

    @Test
    public void testGet() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1 });
        Instance instance = fold.get(0);
        assertSame(instance, fold.instance(0));
        assertSame(instance, dataset.instance(0));
    }

    @Test
    public void testIndexOf() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        Instance instance = new DenseInstance(new double[0], 1);
        int index = fold.indexOf(instance);
        assertEquals(1, index);
        instance.setClassValue(2);
        int index2 = fold.indexOf(instance);
        assertEquals(-1, index2);
    }

    @Test
    public void testIsEmpty() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertTrue(fold.isEmpty());
        Instance instance = new DenseInstance(new double[0], 0);
        fold.add(instance);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        List<Instance> instances = new ArrayList<>();
        for (Instance instance : fold) {
            instances.add(instance);
        }
        Collections.sort(instances);
        List<Instance> expected = Arrays.asList(dataset.instance(0), dataset.instance(1), dataset.instance(2));
        assertArrayEquals(expected.toArray(), instances.toArray());
    }

    @Test
    public void testListIterator() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        ListIterator<Instance> listIterator = fold.listIterator();
        Instance instance = listIterator.next();
        assertSame(instance, fold.instance(0));
        assertSame(instance, dataset.instance(0));
        listIterator.remove();
        assertTrue(fold.isEmpty());
        Instance instance2 = listIterator.next();
        assertSame(instance2, fold.instance(1));
        assertSame(instance2, dataset.instance(1));
        listIterator.add(new DenseInstance(new double[0], 3));
        Instance instance3 = fold.instance(3);
        assertSame(instance3, listIterator.previous());
    }

    @Test
    public void testListIteratorIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        ListIterator<Instance> listIterator = fold.listIterator(1);
        Instance instance = listIterator.next();
        assertSame(instance, fold.instance(1));
        assertSame(instance, dataset.instance(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(new DenseInstance(new double[0], 0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], 0), new DenseInstance(new double[0], 1));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[0], 0), new DenseInstance(new double[0], 1), new DenseInstance(new double[0], 2));
        fold.retainAll(Collections.singletonList(new DenseInstance(new double[0], 0)));
    }

    @Test
    public void testSet() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        Instance instance = new DenseInstance(new double[0], 2);
        try {
            fold.set(0, instance);
            fail("Should not be able to set an instance in a fold.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSize() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertTrue(fold.size() == 0);
        Instance instance = new DenseInstance(new double[0], 0);
        fold.add(instance);
        assertTrue(fold.size() == 1);
    }

    @Test
    public void testSubList() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        List<Instance> subList = fold.subList(1, 2);
        List<Instance> expected = Arrays.asList(fold.instance(1), fold.instance(2));
        assertArrayEquals(expected.toArray(), subList.toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArray() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        fold.toArray();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testToArrayGeneric() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 0, 1, 2 });
        Instance[] expected = new Instance[fold.size()];
        for (int i = 0; i < fold.size(); ++i) {
            expected[i] = fold.instance(i);
        }
        Object[] actual = fold.toArray();
        assertArrayEquals(expected, (Instance[]) actual);
    }

    @Test
    public void testNoAttributes() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertEquals(dataset.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Object clazz = "class1";
        int index = fold.classIndex(clazz);
        assertEquals(-1, index);
        Instance instance = new DenseInstance(new double[0], 0);
        instance.setClassValue(clazz);
        fold.add(instance);
        index = fold.classIndex(clazz);
        assertEquals(0, index);
    }

    @Test
    public void testClassValue() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Instance instance = new DenseInstance(new double[0], 0);
        instance.setClassValue("class1");
        fold.add(instance);
        assertEquals("class1", fold.classValue(0));
        fold.instance(0).setClassValue("class2");
        assertNull(fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Fold copy = fold.copy();
        for (int i = 0; i < fold.size(); ++i) {
            Instance instance = fold.instance(i);
            Instance copyInstance = copy.instance(i);
            assertSame(instance, copyInstance);
        }
        Instance instance = new DenseInstance(new double[0], 0);
        fold.add(instance);
        assertFalse(copy.contains(instance));
    }
}