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
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] { 0, 1, 2 };
        final Fold fold = new Fold(parent, indices);

        assertTrue(fold.size() == indices.length);
    }

    @Test
    public void testGetInstance() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] { 0, 1, 2 };
        final Instance inst1 = new DenseInstance(new double[] { 1.0, 2.0 });
        final Instance inst2 = new DenseInstance(new double[] { 3.0, 4.0 });
        final Instance inst3 = new DenseInstance(new double[] { 5.0, 6.0 });

        parent.add(inst1);
        parent.add(inst2);
        parent.add(inst3);

        final Fold fold = new Fold(parent, indices);

        assertSame(inst1, fold.instance(0));
        assertSame(inst2, fold.instance(1));
        assertSame(inst3, fold.instance(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);

        fold.add(new DenseInstance(new double[] { 1.0, 2.0 }));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        final Dataset parent = new DefaultDataset();
        final Fold fold = new Fold(parent, new int[0]);

        final List<Instance> instances = Arrays.asList(new DenseInstance(new double[] { 1.0, 2.0 }), new DenseInstance(new double[] { 3.0, 4.0 }));

        fold.addAll(instances);
    }

    @Test
    public void testClasses() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] { 0, 1, 2 };
        parent.add(new DenseInstance(new double[] { 1.0, 2.0 }, 0.0));
        parent.add(new DenseInstance(new double[] { 3.0, 4.0 }, 1.0));
        parent.add(new DenseInstance(new double[] { 5.0, 6.0 }, 0.0));

        final Fold fold = new Fold(parent, indices);

        assertArrayEquals(parent.classes().toArray(), fold.classes().toArray());
    }

    @Test
    public void testKNearest() {
        throw new UnsupportedOperationException("Method is not yet implemented.");
    }

    @Test
    public void testNoAttributes() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] { 0, 1, 2 };
        final Fold fold = new Fold(parent, indices);

        assertTrue(fold.noAttributes() == parent.noAttributes());
    }

    @Test
    public void testClassIndex() {
        final Dataset parent = new DefaultDataset();
        parent.add(new DenseInstance(new double[] { 1.0, 2.0 }, 0.0));
        parent.add(new DenseInstance(new double[] { 3.0, 4.0 }, 1.0));
        parent.add(new DenseInstance(new double[] { 5.0, 6.0 }, 0.0));

        final Fold fold = new Fold(parent, new int[] { 1, 0, 2 });

        assertEquals(1, fold.classIndex(1.0));
        assertEquals(0, fold.classIndex(0.0));
        assertNull(fold.classIndex(2.0));
    }

    @Test
    public void testCopy() {
        final Dataset parent = new DefaultDataset();
        final int[] indices = new int[] { 0, 1, 2 };
        parent.add(new DenseInstance(new double[] { 1.0, 2.0 }, 0.0));
        parent.add(new DenseInstance(new double[] { 3.0, 4.0 }, 1.0));
        parent.add(new DenseInstance(new double[] { 5.0, 6.0 }, 0.0));

        final Fold fold = new Fold(parent, indices);

        final Dataset copy = fold.copy();

        assertFalse(fold == copy);
        assertTrue(fold.size() == copy.size());

        for (int i = 0; i < fold.size(); ++i) {
            assertTrue(fold.instance(i).getClass() == copy.instance(i).getClass());
            assertFalse(fold.instance(i) == copy.instance(i));
        }
    }
}