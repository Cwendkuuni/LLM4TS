package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset ds = new DefaultDataset();
        assertEquals(0, ds.size());
        assertNull(ds.classes());
    }

    @Test
    public void testConstructorWithCollection() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        List<Instance> instances = new ArrayList<>();
        instances.add(inst1);
        instances.add(inst2);

        DefaultDataset ds = new DefaultDataset(instances);
        assertEquals(2, ds.size());
        assertEquals(new HashSet<>(Arrays.asList(1.0, 2.0)), ds.classes());
    }

    @Test
    public void testAddMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);

        assertEquals(2, ds.size());
        assertEquals(new HashSet<>(Arrays.asList(1.0, 2.0)), ds.classes());
    }

    @Test
    public void testClearMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);

        ds.clear();

        assertEquals(0, ds.size());
        assertNull(ds.classes());
    }

    @Test
    public void testAddAllMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);
        Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 3.0);

        DefaultDataset ds1 = new DefaultDataset();
        ds1.add(inst1);

        List<Instance> instances = new ArrayList<>();
        instances.add(inst2);
        instances.add(inst3);

        ds1.addAll(instances);

        assertEquals(3, ds1.size());
        assertEquals(new HashSet<>(Arrays.asList(1.0, 2.0, 3.0)), ds1.classes());
    }

    @Test
    public void testKNearestMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);
        Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 3.0);
        Instance query = new DenseInstance(new double[]{5.0, 5.0, 5.0}, null);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);
        ds.add(inst3);

        List<Instance> result = ds.kNearest(2, query, new EuclideanDistance());

        assertEquals(2, result.size());
        assertTrue(result.contains(inst2));
        assertTrue(result.contains(inst3));
        assertFalse(result.contains(inst1));
    }

    @Test
    public void testFoldsMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);
        Instance inst3 = new DenseInstance(new double[]{7.0, 8.0, 9.0}, 3.0);
        Instance inst4 = new DenseInstance(new double[]{11.0, 12.0, 13.0}, 4.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);
        ds.add(inst3);
        ds.add(inst4);

        Dataset[] result = ds.folds(4, new Random());

        assertEquals(4, result.length);
        for (Dataset dataset : result) {
            assertTrue(dataset instanceof Fold);
        }
    }

    @Test
    public void testNoAttributesMethod() {
        Instance inst = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst);

        assertEquals(inst.noAttributes(), ds.noAttributes());
    }

    @Test
    public void testClassIndexMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);

        assertEquals(1, ds.classIndex(1.0));
        assertEquals(-1, ds.classIndex(3.0));
    }

    @Test
    public void testClassValueMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);

        assertEquals(1.0, ds.classValue(0));
        assertNull(ds.classValue(1));
    }

    @Test
    public void testCopyMethod() {
        Instance inst1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance inst2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst2);

        DefaultDataset copy = ds.copy();

        assertEquals(2, copy.size());
        assertEquals(new HashSet<>(Arrays.asList(1.0, 2.0)), copy.classes());
    }
}