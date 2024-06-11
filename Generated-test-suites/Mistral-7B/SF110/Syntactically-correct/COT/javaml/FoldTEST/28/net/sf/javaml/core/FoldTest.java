package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2, 3 };
        Fold fold = new Fold(parent, indices);
        assertEquals(indices.length, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.add(new DenseInstance(new double[0]));
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        assertEquals(parent.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.folds(5, new Random());
    }

    @Test
    public void testGetInstance() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0 });
        Instance instance = fold.instance(0);
        assertNotNull(instance);
        assertEquals(parent.instance(0), instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.kNearest(5, null, null);
    }

    @Test
    public void testAddAll() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);

        List<Instance> instances = Arrays.asList(
                new DenseInstance(new double[] { 1.0, 2.0, 3.0 }),
                new DenseInstance(new double[] { 4.0, 5.0, 6.0 })
        );

        fold.addAll(instances);

        for (Instance instance : instances) {
            assertTrue(fold.contains(instance));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0 });
        fold.remove(new DenseInstance(new double[0]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        List<Instance> list = new ArrayList<>();
        fold.retainAll(list);
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] {});
        assertEquals(0, fold.size());

        fold = new Fold(parent, new int[] { 0 });
        assertEquals(1, fold.size());

        fold = new Fold(parent, new int[] { 0, 1, 2, 3 });
        assertEquals(4, fold.size());
    }

    @Test
    public void testToString() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });
        assertEquals("Fold: size = 4", fold.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSublist() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });
        fold.subList(1, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testInvalidIndexGet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });
        fold.instance(-1);
    }

    @Test
    public void testFoldIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });

        List<Instance> expected = Arrays.asList(
                parent.instance(0), parent.instance(1), parent.instance(2), parent.instance(3)
        );

        FoldIterator iterator = fold.iterator();
        int i = 0;
        for (Instance instance : iterator) {
            assertEquals(expected.get(i++), instance);
        }
    }

    @Test
    public void testFoldIteratorWithIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });

        List<Instance> expected = Arrays.asList(
                parent.instance(0), parent.instance(1), parent.instance(2), parent.instance(3)
        );

        FoldIterator iterator = fold.listIterator(2);
        int i = 3;
        for (Instance instance : iterator) {
            assertEquals(expected.get(--i), instance);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });
        List<Instance> list = new ArrayList<>();
        fold.removeAll(list);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2, 3 });
        fold.set(1, new DenseInstance(new double[0]));
    }
}