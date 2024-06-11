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

import org.junit.Test;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.add(null);
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        assertSame(parent.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.folds(1, null);
    }

    @Test
    public void testFoldIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });

        List<Instance> expected = Arrays.asList(parent.instance(0), parent.instance(1), parent.instance(2));
        List<Instance> actual = new ArrayList<>();

        for (Instance instance : fold) {
            actual.add(instance);
        }

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllCollection() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.addAll(Collections.singletonList(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.contains(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAllCollection() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.containsAll(Collections.singletonList(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.get(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOf() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.indexOf(null);
    }

    @Test
    public void testIsEmpty() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });

        List<Instance> expected = Arrays.asList(parent.instance(0), parent.instance(1), parent.instance(2));
        List<Instance> actual = new ArrayList<>();

        for (Instance instance : fold) {
            actual.add(instance);
        }

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.listIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.listIterator(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
        fold.remove(parent.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllCollection() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
        fold.removeAll(Collections.singletonList(parent.instance(0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllCollection() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
        fold.retainAll(Collections.singletonList(parent.instance(0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
        fold.set(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.size();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
        fold.subList(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.toArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArrayWithArray() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.toArray(new Object[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.noAttributes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.classIndex(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.classValue(0);
    }

    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[] { 0, 1, 2 });

        List<Instance> expected = Arrays.asList(parent.instance(0), parent.instance(1), parent.instance(2));
        Dataset copy = fold.copy();

        List<Instance> actual = new ArrayList<>();
        for (Instance instance : copy) {
            actual.add(instance);
        }

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}