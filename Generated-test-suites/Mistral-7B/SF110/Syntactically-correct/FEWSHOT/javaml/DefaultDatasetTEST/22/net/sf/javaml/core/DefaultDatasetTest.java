package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testConstructorEmpty() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testConstructorCollection() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(instances.size(), dataset.size());
        assertEquals(Arrays.asList(0.0, 1.0), Arrays.asList(dataset.classIndex(0.0), dataset.classIndex(1.0)));
    }

    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        dataset.addAll(instances);
        assertEquals(instances.size(), dataset.size());
    }

    @Test
    public void testAddAllIndex() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        dataset.addAll(1, instances);
        assertEquals(instances.size(), dataset.size());
        Instance firstInstance = dataset.instance(0);
        assertEquals(instances.get(0), firstInstance);
    }

    @Test
    public void testClear() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        dataset.clear();
        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testAdd() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        dataset.add(instances.get(0));
        assertEquals(1, dataset.size());
        assertEquals(instances.get(0), dataset.instance(0));
    }

    @Test
    public void testAddIndex() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        dataset.add(1, instances.get(0));
        assertEquals(1, dataset.size());
        assertEquals(instances.get(0), dataset.instance(0));
    }

    @Test
    public void testAddElement() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        dataset.addElement(instances.get(0));
        assertEquals(1, dataset.size());
        assertEquals(instances.get(0), dataset.instance(0));
    }

    @Test
    public void testInsertElementAt() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        dataset.insertElementAt(instances.get(0), 0);
        assertEquals(1, dataset.size());
        assertEquals(instances.get(0), dataset.instance(0));
    }

    @Test
    public void testSetElementAt() {
        List<Instance> instances = new ArrayList<Instance>();
        DefaultDataset dataset = new DefaultDataset();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 1));
        dataset.addAll(instances);
        dataset.setElementAt(new DenseInstance(new double[]{2.0, 3.0, 4.0}, 0), 0);
        assertEquals(1, dataset.size());
        assertEquals(new DenseInstance(new double[]{2.0, 3.0, 4.0}, 0), dataset.instance(0));
    }

    @Test
    public void testKNearest() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance testInstance = new DenseInstance(new double[]{2.5, 3.5, 4.5}, 0);
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        SortedSet<Instance> kNearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
        assertEquals(2, kNearestInstances.size());
        List<Instance> expectedInstances = Arrays.asList(instances.get(1), instances.get(2));
        List<Instance> actualInstances = new ArrayList<Instance>();
        for (Instance instance : kNearestInstances) {
            actualInstances.add(instance);
        }
        Collections.sort(actualInstances, new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                double d1 = distanceMeasure.measure(testInstance, o1);
                double d2 = distanceMeasure.measure(testInstance, o2);
                return Double.compare(d1, d2);
            }
        });
        assertEquals(expectedInstances, actualInstances);
    }

    @Test
    public void testRemoveFarthest() {
        Map<Instance, Double> vector = new HashMap<Instance, Double>();
        vector.put(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0), 2.0);
        vector.put(new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1), 3.0);
        vector.put(new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1), 1.0);
        double max = dataset.removeFarthest(vector);
        assertEquals(2.0, max);
        Instance expectedInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0);
        Instance actualInstance = vector.keySet().iterator().next();
        assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testFolds() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        int numFolds = 4;
        Random random = new Random();
        Dataset[] folds = dataset.folds(numFolds, random);
        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
            Fold expectedFold = new Fold(dataset, new int[instances.size()]);
            Fold actualFold = (Fold) fold;
            assertArrayEquals(expectedFold.indices, actualFold.indices);
        }
    }

    @Test
    public void testNoAttributes() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(instances.get(0).noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(0, dataset.classIndex(0.0));
        assertEquals(1, dataset.classIndex(1.0));
        assertNull(dataset.classIndex(2.0));
    }

    @Test
    public void testClassValue() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(0.0, dataset.classValue(0));
        assertEquals(1.0, dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        List<Instance> instances = Arrays.asList(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 0),
                new DenseInstance(new double[]{2.0, 3.0, 4.0}, 1),
                new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1),
                new DenseInstance(new double[]{4.0, 5.0, 6.0}, 0));
        DefaultDataset dataset = new DefaultDataset(instances);
        DefaultDataset copy = dataset.copy();
        assertEquals(instances.size(), copy.size());
        for (int i = 0; i < instances.size(); ++i) {
            Instance instance = instances.get(i);
            Instance copyInstance = copy.instance(i);
            assertEquals(instance, copyInstance);
        }
    }
}