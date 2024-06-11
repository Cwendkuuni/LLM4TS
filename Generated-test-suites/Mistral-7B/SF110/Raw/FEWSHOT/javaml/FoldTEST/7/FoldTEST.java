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
        Dataset parent = createMockDataset(10);
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        assertTrue(fold.size() == 4);
    }

    @Test
    public void testGetInstance() {
        Dataset parent = createMockDataset(10);
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        Instance inst = fold.instance(0);
        assertSame(inst, parent.instance(2));
    }

    @Test
    public void testAdd() {
        throw new UnsupportedOperationException("Cannot test add method on a Fold object.");
    }

    @Test
    public void testRemove() {
        throw new UnsupportedOperationException("Cannot test remove method on a Fold object.");
    }

    @Test
    public void testAddAll() {
        throw new UnsupportedOperationException("Cannot test addAll method on a Fold object.");
    }

    @Test
    public void testClear() {
        throw new UnsupportedOperationException("Cannot test clear method on a Fold object.");
    }

    @Test
    public void testClasses() {
        Dataset parent = createMockDataset(10);
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        Set<Object> classes = fold.classes();
        assertTrue(classes.equals(parent.classes()));
    }

    @Test
    public void testNoAttributes() {
        Dataset parent = createMockDataset(10);
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset parent = createMockDataset(10);
        parent.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 1));
        parent.add(new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, 2));
        parent.add(new DenseInstance(new double[] { 3.0, 4.0, 5.0 }, 3));
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        assertEquals(parent.classIndex(2), fold.classIndex(2));
        assertEquals(parent.classIndex(3), fold.classIndex(1));
    }

    @Test
    public void testClassValue() {
        Dataset parent = createMockDataset(10);
        parent.add(new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, "Class1"));
        parent.add(new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, "Class2"));
        parent.add(new DenseInstance(new double[] { 3.0, 4.0, 5.0 }, "Class3"));
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        assertEquals(parent.classValue(2), fold.classValue(0));
        assertEquals(parent.classValue(3), fold.classValue(2));
    }

    @Test
    public void testCopy() {
        Dataset parent = createMockDataset(10);
        Fold fold = new Fold(parent, new int[] { 2, 4, 6, 8 });
        Dataset copy = fold.copy();
        assertFalse(fold == copy);
        assertTrue(fold.size() == copy.size());
        for (int i = 0; i < fold.size(); i++) {
            Instance instFold = fold.instance(i);
            Instance instCopy = copy.instance(i);
            assertSame(instFold, instCopy);
        }
    }

    @Test
    public void testFolds() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    private Dataset createMockDataset(int size) {
        Dataset ds = new DefaultDataset();
        for (int i = 0; i < size; i++) {
            ds.add(new DenseInstance(new double[] { i + 1.0, i + 2.0, i + 3.0 }, i % 2 == 0 ? "Class1" : "Class2"));
        }
        return ds;
    }
}