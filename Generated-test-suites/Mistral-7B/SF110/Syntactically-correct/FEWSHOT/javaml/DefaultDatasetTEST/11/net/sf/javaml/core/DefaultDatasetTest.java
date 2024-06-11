package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

    private Collection<Instance> instances;
    private DefaultDataset dataset;

    @Before
    public void setup() {
        instances = new ArrayList<Instance>();
        instances.add(new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0));
        instances.add(new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0));
        instances.add(new DenseInstance(new double[] {7.0, 8.0, 9.0}, 3.0));
        dataset = new DefaultDataset(instances);
    }

    @Test
    public void testConstructorEmpty() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
    }

    @Test
    public void testConstructorCollection() {
        assertEquals(3, dataset.size());
        assertEquals(3, dataset.noAttributes());
        SortedSet<Object> classes = dataset.classes();
        assertEquals(3, classes.size());
        assertEquals(1.0, classes.first());
        assertEquals(3.0, classes.last());
    }

    @Test
    public void testAddAll() {
        Collection<Instance> newInstances = new ArrayList<Instance>();
        newInstances.add(new DenseInstance(new double[] {11.0, 12.0, 13.0}, 1.0));
        dataset.addAll(newInstances);
        assertEquals(6, dataset.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllThrowException() {
        dataset.addAll(Collections.singleton(new DenseInstance(new double[] {11.0, 12.0, 13.0}, 1.0)));
    }

    @Test
    public void testClear() {
        dataset.clear();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAdd() {
        Instance newInstance = new DenseInstance(new double[] {10.0, 11.0, 12.0}, 4.0);
        dataset.add(newInstance);
        assertEquals(4, dataset.size());
        assertTrue(dataset.contains(newInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddThrowException() {
        Instance newInstance = new DenseInstance(new double[] {10.0, 11.0, 12.0}, 4.0);
        dataset.add(1, newInstance);
    }

    @Test
    public void testAddElement() {
        Instance newInstance = new DenseInstance(new double[] {10.0, 11.0, 12.0}, 4.0);
        dataset.addElement(newInstance);
        assertEquals(4, dataset.size());
        assertTrue(dataset.contains(newInstance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddElementThrowException() {
        Instance newInstance = new DenseInstance(new double[] {10.0, 11.0, 12.0}, 4.0);
        dataset.setElementAt(newInstance, 0);
    }

    @Test
    public void testSetElementAt() {
        Instance newInstance = new DenseInstance(new double[] {10.0, 11.0, 12.0}, 4.0);
        Instance oldInstance = dataset.instance(1);
        dataset.setElementAt(newInstance, 1);
        assertEquals(newInstance, dataset.instance(1));
        assertFalse(dataset.contains(oldInstance));
    }

    @Test
    public void testInstance() {
        assertEquals(instances.get(0), dataset.instance(0));
    }

    @Test
    public void testClasses() {
        assertEquals(3, dataset.classes().size());
        assertEquals(1.0, dataset.classes().first());
        assertEquals(3.0, dataset.classes().last());
    }

    @Test
    public void testKNearest() {
        DistanceMeasure dm = new EuclideanDistance();
        Instance newInstance = new DenseInstance(new double[] {5.0, 5.0, 5.0}, 2.0);
        List<Instance> kNearestInstances = new ArrayList<Instance>(Arrays.asList(instances.get(0), instances.get(1)));
        Collection<Instance> result = dataset.kNearest(2, newInstance, dm);
        assertEquals(kNearestInstances, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKNearestThrowException() {
        Instance newInstance = new DenseInstance(new double[] {5.0, 5.0, 5.0}, 2.0);
        dataset.kNearest(-1, newInstance, new EuclideanDistance());
    }

    @Test
    public void testFolds() {
        Random random = new Random();
        int numFolds = 5;
        Dataset[] folds = dataset.folds(numFolds, random);
        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
            assertEquals(dataset.size(), ((Fold) fold).getSize());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoldsThrowException() {
        Dataset[] folds = dataset.folds(-1, new Random());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(1, dataset.classIndex(1.0));
        assertEquals(-1, dataset.classIndex(null));
        assertEquals(2, dataset.classIndex(3.0));
    }

    @Test
    public void testCopy() {
        DefaultDataset copiedDataset = dataset.copy();
        assertEquals(dataset, copiedDataset);
        for (int i = 0; i < dataset.size(); ++i) {
            Instance datasetInstance = dataset.instance(i);
            Instance copiedDatasetInstance = copiedDataset.instance(i);
            assertSame(datasetInstance, copiedDatasetInstance);
        }
    }
}