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
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        assertTrue(fold.size() == 4);
    }

    @Test
    public void testGetInstance() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        Instance instance = fold.instance(0);
        assertTrue(instance != null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        Instance instance = new DenseInstance(new double[] {1.0, 2.0});
        fold.add(instance);
    }

    @Test
    public void testClasses() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        List<Object> classes = fold.classes();
        assertTrue(Collections.disjoint(classes, Arrays.asList("class1", "class2")));
    }

    @Test
    public void testFolds() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testKNearest() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testIterator() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        List<Instance> instances = new ArrayList<>();
        for (Instance i : fold) {
            instances.add(i);
        }
        assertArrayEquals(new Object[] {dataset.instance(0), dataset.instance(2), dataset.instance(4), dataset.instance(5)}, instances.toArray());
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(null, new int[0]);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testRemove() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testAddAll() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRetainAll() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testSet() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testSubList() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testToArray() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        Instance[] array = fold.toArray();
        assertArrayEquals(new Object[] {dataset.instance(0), dataset.instance(2), dataset.instance(4), dataset.instance(5)}, array);
    }

    @Test
    public void testToArrayWithCapacity() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        Instance[] array = new Instance[fold.size()];
        fold.toArray(array);
        assertArrayEquals(new Object[] {dataset.instance(0), dataset.instance(2), dataset.instance(4), dataset.instance(5)}, array);
    }

    @Test
    public void testNoAttributes() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        assertEquals(dataset.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        assertEquals(dataset.classIndex("class1"), fold.classIndex("class1"));
        assertNull(fold.classIndex("class3"));
    }

    @Test
    public void testClassValue() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        assertEquals(dataset.classValue(0), fold.classValue(0));
        assertNull(fold.classValue(10));
    }

    @Test
    public void testCopy() {
        Dataset dataset = createDummyDataset();
        Fold fold = new Fold(dataset, new int[] {0, 2, 4, 5});
        Dataset copy = fold.copy();
        assertTrue(copy instanceof Fold);
        for (int i = 0; i < fold.size(); i++) {
            Instance instance1 = fold.instance(i);
            Instance instance2 = copy.instance(i);
            assertArrayEquals(instance1.toArray(), instance2.toArray());
        }
    }

    private Dataset createDummyDataset() {
        Dataset dataset = new ArrayListDataset();
        dataset.add(new DenseInstance(new double[] {1.0, 2.0, "class1"}));
        dataset.add(new DenseInstance(new double[] {2.0, 3.0, "class2"}));
        dataset.add(new DenseInstance(new double[] {3.0, 4.0, "class1"}));
        dataset.add(new DenseInstance(new double[] {4.0, 5.0, "class2"}));
        return dataset;
    }
}