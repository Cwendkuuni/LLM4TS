package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.add(parent.getInstance(0)); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.addAll(Collections.singletonList(parent.getInstance(0))); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.addAll(1, Collections.singletonList(parent.getInstance(0))); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.clear(); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.remove(parent.getInstance(0)); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.remove(0); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.retainAll(Collections.emptyList()); //This should throw an exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        fold.set(0, parent.getInstance(0)); //This should throw an exception
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
        fold.folds(2, new Random()); //This should throw an exception
    }

    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        assertEquals(parent.instance(0), fold.get(0));
        assertEquals(parent.instance(1), fold.get(1));
        assertEquals(parent.instance(2), fold.get(2));
    }

    @Test
    public void testIndexOf() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        assertEquals(0, fold.indexOf(parent.instance(0)));
        assertEquals(1, fold.indexOf(parent.instance(1)));
        assertEquals(2, fold.indexOf(parent.instance(2)));
        assertNull(fold.indexOf(parent.getInstance(3)));
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        assertEquals(3, fold.size());
    }

    @Test
    public void testIsEmpty() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[0]);
        assertFalse(fold.isEmpty());

        fold = new Fold(parent, new int[]{});
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        List<Instance> list = new ArrayList<>();
        for (Instance i : fold) {
            list.add(i);
        }
        assertEquals(3, list.size());
        assertEquals(parent.instance(0), list.get(0));
        assertEquals(parent.instance(1), list.get(1));
        assertEquals(parent.instance(2), list.get(2));
    }

    @Test
    public void testListIterator() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        ListIterator<Instance> listIterator = fold.listIterator();
        List<Instance> list = new ArrayList<>();
        while (listIterator.hasNext()) {
            list.add(listIterator.next());
        }
        assertEquals(3, list.size());
        assertEquals(parent.instance(2), list.get(0));
        assertEquals(parent.instance(1), list.get(1));
        assertEquals(parent.instance(0), list.get(2));
    }

    @Test
    public void testListIteratorWithIndex() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        ListIterator<Instance> listIterator = fold.listIterator(1);
        List<Instance> list = new ArrayList<>();
        while (listIterator.hasNext()) {
            list.add(listIterator.next());
        }
        assertEquals(2, list.size());
        assertEquals(parent.instance(1), list.get(0));
        assertEquals(parent.instance(0), list.get(1));
    }

    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        Object[] arr = fold.toArray();
        assertEquals(3, arr.length);
        for (int i = 0; i < arr.length; i++) {
            assertEquals(parent.instance(i), arr[i]);
        }
    }

    @Test
    public void testToArrayWithArray() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        Instance[] arr = new Instance[3];
        arr = fold.toArray(arr);
        assertEquals(3, arr.length);
        for (int i = 0; i < arr.length; i++) {
            assertEquals(parent.instance(i), arr[i]);
        }
    }

    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[]{1.0, 2.0}, 0.0));
        parent.add(new DenseInstance(new double[]{3.0, 4.0}, 1.0));
        Fold fold = new Fold(parent, new int[]{0, 1, 1});
        assertEquals(0, fold.classIndex(0.0));
        assertEquals(1, fold.classIndex(1.0));
        assertNull(fold.classIndex(2.0));
    }

    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[]{1.0, 2.0}, 0.0));
        parent.add(new DenseInstance(new double[]{3.0, 4.0}, 1.0));
        Fold fold = new Fold(parent, new int[]{0, 1, 1});
        assertEquals(0.0, fold.classValue(0), 0.01);
        assertEquals(1.0, fold.classValue(1), 0.01);
        assertNull(fold.classValue(2));
    }

    @Test
    public void testCopy() {
        Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[]{1.0, 2.0}, 0.0));
        parent.add(new DenseInstance(new double[]{3.0, 4.0}, 1.0));
        Fold fold = new Fold(parent, new int[]{0, 1, 1});
        Dataset copy = fold.copy();
        assertEquals(parent.size(), copy.size());
        for (int i = 0; i < parent.size(); i++) {
            assertEquals(parent.instance(i), copy.instance(i));
        }
    }
}