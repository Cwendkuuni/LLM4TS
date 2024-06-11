package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });
        assertEquals(4, fold.size());
    }

    @Test
    public void testGetClassIndex() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, new String[] { "A" }));
        dataset.add(new DenseInstance(new double[] { 2.0, 3.0 }, new String[] { "B" }));
        dataset.add(new DenseInstance(new double[] { 3.0, 4.0 }, new String[] { "A" }));
        Dataset fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });

        assertEquals(0, fold.classIndex("A"));
        assertEquals(1, fold.classIndex("B"));
        assertNull(fold.classIndex("C"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] {});
        fold.add(new DenseInstance(new double[] {}, new String[] {}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] {});
        fold.addAll(Arrays.asList(new DenseInstance(new double[] {}, new String[] {})));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] {});
        fold.addAll(1, Arrays.asList(new DenseInstance(new double[] {}, new String[] {})));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] {});
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });
        fold.remove(new DenseInstance(new double[] {}, new String[] {}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });
        fold.remove(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] {});
        fold.retainAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });
        fold.set(0, new DenseInstance(new double[] {}, new String[] {}));
    }

    @Test
    public void testIterator() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, new String[] { "A" }));
        dataset.add(new DenseInstance(new double[] { 2.0, 3.0 }, new String[] { "B" }));
        dataset.add(new DenseInstance(new double[] { 3.0, 4.0 }, new String[] { "A" }));
        Dataset fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });

        List<Instance> instances = new ArrayList<>();
        for (Instance i : fold) {
            instances.add(i);
        }

        assertEquals(4, instances.size());
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, fold.indices);
    }

    @Test
    public void testListIterator() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, new String[] { "A" }));
        dataset.add(new DenseInstance(new double[] { 2.0, 3.0 }, new String[] { "B" }));
        dataset.add(new DenseInstance(new double[] { 3.0, 4.0 }, new String[] { "A" }));
        Dataset fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });

        ListIterator<Instance> iterator = fold.listIterator();
        Instance prev = null;
        while (iterator.hasPrevious()) {
            Instance current = iterator.previous();
            if (prev != null) {
                assertSame(prev, current);
            }
            prev = current;
        }
        assertNull(prev);

        iterator = fold.listIterator(fold.size() - 1);
        Instance next = null;
        while (iterator.hasNext()) {
            Instance current = iterator.next();
            if (next != null) {
                assertSame(next, current);
            }
            next = current;
        }
        assertNull(next);
    }

    @Test
    public void testListIteratorIndex() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, new String[] { "A" }));
        dataset.add(new DenseInstance(new double[] { 2.0, 3.0 }, new String[] { "B" }));
        dataset.add(new DenseInstance(new double[] { 3.0, 4.0 }, new String[] { "A" }));
        Dataset fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });

        ListIterator<Instance> iterator = fold.listIterator();
        int index = 0;
        while (iterator.hasNext()) {
            Instance current = iterator.next();
            assertEquals(index++, Arrays.asList(fold.indices).indexOf(iterator.previousIndex()));
            assertEquals(index++, fold.instance(iterator.previousIndex()).getIndex());
        }
    }

    @Test
    public void testNoAttributes() {
        Dataset dataset = new DefaultDataset();
        Dataset fold = new Fold(dataset, new int[] {});
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testCopy() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] { 1.0, 2.0 }, new String[] { "A" }));
        dataset.add(new DenseInstance(new double[] { 2.0, 3.0 }, new String[] { "B" }));
        dataset.add(new DenseInstance(new double[] { 3.0, 4.0 }, new String[] { "A" }));
        Dataset fold = new Fold(dataset, new int[] { 1, 2, 3, 4 });
        Dataset copy = fold.copy();

        assertEquals(fold.size(), copy.size());
        for (int i = 0; i < fold.size(); ++i) {
            assertEquals(fold.instance(i), copy.instance(i));
        }
    }
}