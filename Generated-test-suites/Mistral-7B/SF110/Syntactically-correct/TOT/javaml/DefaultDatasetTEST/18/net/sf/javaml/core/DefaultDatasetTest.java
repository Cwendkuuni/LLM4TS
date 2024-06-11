package net.sf.javaml.core;

import org.junit.*;
import org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    private DefaultDataset dataset;

    @Before
    public void setUp() {
        dataset = new DefaultDataset();
    }

    @Test
    public void testEmptyConstructor() {
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddAllEmptyCollection() {
        dataset.addAll(Collections.emptyList());
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddAllNullCollection() {
        try {
            dataset.addAll(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAddInstanceWithNullAttributes() {
        Instance instance = new DenseInstance(null, 1.0);
        dataset.add(instance);
        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testAdd() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        dataset.add(instance);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testClear() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, 0.0);
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.clear();
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testGetInstanceWithNegativeIndex() {
        Instance instance = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        dataset.add(instance);
        try {
            dataset.instance(-1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testClasses() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, 0.0);
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(2, dataset.classes().size());
        assertEquals(1.0, dataset.classes().first());
        assertEquals(0.0, dataset.classes().last());
    }

    @Test
    public void testkNearestWithKGreaterThanSize() {
        Instance instance = new DenseInstance(new double[]{0.0, 0.0}, 1.0);
        try {
            dataset.kNearest(10, instance, new EuclideanDistance());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFoldsWithNegativeNumFolds() {
        try {
            dataset.folds(-1, new Random());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddAll() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, 0.0);
        Collection<Instance> coll = new ArrayList<>();
        coll.add(instance1);
        coll.add(instance2);
        dataset.addAll(coll);
        assertEquals(2, dataset.size());
    }

    @Test
    public void testAddAllWithInstanceContainingNullClassValue() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, null);
        Collection<Instance> coll = new ArrayList<>();
        coll.add(instance1);
        coll.add(instance2);
        dataset.addAll(coll);
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testAddAllWithInstanceContainingMoreAttributes() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0.0);
        Collection<Instance> coll = new ArrayList<>();
        coll.add(instance1);
        coll.add(instance2);
        dataset.addAll(coll);
        assertEquals(2, dataset.size());
        assertEquals(3, dataset.get(1).noAttributes());
    }

    @Test
    public void testSetElementAt() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, 0.0);
        dataset.add(instance1);
        dataset.add(instance2);
        try {
            dataset.setElementAt(new DenseInstance(new double[]{7.0, 8.0}, 0.0), 0);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}