package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructors() {
        new Fold(null, null);
        new Fold(new DefaultDataset(), null);
    }

    @Test
    public void testSize() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] {0, 1, 2, 3};
        final Fold fold = new Fold(parent, indices);
        assertEquals("Size of fold should be equal to length of indices array", 4, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        fold.add(new DefaultInstance(1.0, 2.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        final List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2});
        fold.remove(new DefaultInstance(1.0, 2.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2});
        final List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2});
        final List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.retainAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2});
        fold.set(0, new DefaultInstance(1.0, 2.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2});
        fold.subList(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2});
        fold.addAll(1, Collections.emptyList());
    }

    @Test
    public void testGet() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] {0, 1, 2, 3};
        final Fold fold = new Fold(parent, indices);
        final Instance inst = new DefaultInstance(1.0, 2.0);
        fold.parent.add(inst);
        assertEquals("Instance at index 0 in fold should be the same as instance at index 0 in parent dataset", inst, fold.instance(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2, 3});
        fold.instance(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[] {0, 1, 2, 3});
        fold.instance(4);
    }

    @Test
    public void testIterator() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] {0, 1, 2, 3};
        final Fold fold = new Fold(parent, indices);
        final List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0), new DefaultInstance(7.0, 8.0));
        for (Instance inst : fold) {
            assertEquals("Instance in fold should be the same as instance in parent dataset", inst, fold.parent.instance(fold.indices[fold.iterator.nextIndex()]));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClasses() {
        new Fold(null, new int[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        new Fold(new DefaultDataset(), new int[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        new Fold(new DefaultDataset(), new int[0]);
    }

    @Test
    public void testToString() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] {0, 1, 2, 3};
        final Fold fold = new Fold(parent, indices);
        final String expected = "Fold{" +
                "parent=" + parent +
                ", indices=" + Arrays.toString(indices) +
                '}';
        assertEquals("ToString method should return the expected string representation", expected, fold.toString());
    }
}