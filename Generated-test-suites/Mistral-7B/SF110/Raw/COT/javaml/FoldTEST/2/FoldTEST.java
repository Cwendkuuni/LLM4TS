package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class FoldTest {

    @Test
    public void testConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        assertEquals(parent, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        fold.add(new DefaultInstance(1.0, 2.0));
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0, "class1"));
        parent.add(new DefaultInstance(3.0, 4.0, "class2"));
        Fold fold = new Fold(parent, new int[] { 0, 1 });
        List<Object> classes = fold.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        fold.folds(5, new Random());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
        fold.instance(3);
    }

    @Test
    public void testGet() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0));
        parent.add(new DefaultInstance(3.0, 4.0));
        Fold fold = new Fold(parent, new int[] { 0, 1 });
        Instance instance = fold.instance(0);
        Instance expected = parent.instance(0);
        assertEquals(expected, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(new DefaultDataset(), new int[0]);
        fold.kNearest(5, new DefaultInstance(0.0, 0.0), new EuclideanDistance());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemove() {
        Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
        fold.remove(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
        fold.removeAll(Arrays.asList(new DefaultInstance(0.0, 0.0), new DefaultInstance(1.0, 1.0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
        fold.retainAll(Arrays.asList(new DefaultInstance(1.0, 1.0), new DefaultInstance(2.0, 2.0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(new DefaultDataset(), new int[] { 0, 1, 2 });
        fold.set(0, new DefaultInstance(3.0, 3.0));
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0));
        parent.add(new DefaultInstance(3.0, 4.0));
        Fold fold = new Fold(parent, new int[] { 0, 1 });
        assertEquals(2, fold.size());
    }

    @Test
    public void testToString() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0, "class1"));
        parent.add(new DefaultInstance(3.0, 4.0, "class2"));
        Fold fold = new Fold(parent, new int[] { 0, 1 });
        String expected = String.format("Fold(%s)", Arrays.toString(fold.indices));
        String actual = fold.toString();
        assertEquals(expected, actual);
    }
}