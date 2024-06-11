package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultDatasetTest;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;

import org.junit.Test;

public class DefaultDatasetTest {

    // Test addAll() method
    @Test
    public void testAddAll() {
        // Basic test
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(Arrays.asList(new DenseInstance(1.0, 2.0, 3.0)));
        ds.addAll(Arrays.asList(new DenseInstance(4.0, 5.0, 6.0)));

        assertEquals(2, ds.size());
        assertEquals(new DenseInstance(1.0, 2.0, 3.0), ds.instance(0));
        assertEquals(new DenseInstance(4.0, 5.0, 6.0), ds.instance(1));

        // Test adding equal instances
        ds.addAll(Arrays.asList(new DenseInstance(1.0, 2.0, 3.0)));
        assertEquals(3, ds.size());
        assertTrue(ds.contains(new DenseInstance(1.0, 2.0, 3.0)));

        // Test adding an instance with the same attributes but different values
        Instance instance = new DenseInstance(1.0, 2.0, 3.0);
        ds.addAll(Collections.singletonList(new DenseInstance(1.0, 2.1, 3.1)));
        assertEquals(4, ds.size());
        assertTrue(ds.contains(instance));
        assertTrue(ds.contains(new DenseInstance(1.0, 2.1, 3.1)));

        // Test adding an instance with more attributes
        ds.addAll(Arrays.asList(new DenseInstance(1.0, 2.0, 3.0, 4.0)));
        assertEquals(5, ds.size());
        assertTrue(ds.contains(new DenseInstance(1.0, 2.0, 3.0, 4.0)));

        // Test adding an instance with fewer attributes (should be ignored)
        ds.addAll(Arrays.asList(new DenseInstance(1.0, 2.0)));
        assertEquals(5, ds.size());
    }

    // Test edge cases and exception handling for addAll() method
    @Test(expected = NullPointerException.class)
    public void testAddAllNullCollection() {
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(null);
    }

    @Test
    public void testAddAllEmptyCollection() {
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(Collections.emptyList());
        assertEquals(0, ds.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllNullElement() {
        DefaultDataset ds = new DefaultDataset();
        List<Instance> instances = Arrays.asList(
                new DenseInstance(1.0, 2.0, 3.0),
                new DenseInstance(4.0, 5.0, null),
                new DenseInstance(7.0, 8.0, 9.0)
        );
        ds.addAll(instances);
        assertEquals(2, ds.size());
        assertFalse(ds.contains(new DenseInstance(4.0, 5.0, null)));
    }

    @Test
    public void testAddAllDifferentSizes() {
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(Arrays.asList(new DenseInstance(1.0, 2.0, 3.0)));
        List<Instance> instances = Arrays.asList(
                new DenseInstance(4.0, 5.0, 6.0),
                new DenseInstance(7.0, 8.0, 9.0),
                new DenseInstance(10.0, 11.0, 12.0)
        );
        ds.addAll(instances);
        assertEquals(4, ds.size());
    }

    // Add other test cases for duplicate elements, calling addAll() twice,
    // calling addAll() while the dataset is empty, and calling addAll()
    // with a Collection larger than the maximum allowed number of attributes.
}