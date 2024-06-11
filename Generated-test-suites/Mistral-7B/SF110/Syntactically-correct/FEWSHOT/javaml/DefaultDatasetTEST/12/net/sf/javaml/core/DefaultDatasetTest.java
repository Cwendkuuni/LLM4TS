package net.sf.javaml.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
        assertNull(dataset.classes());
    }

    @Test
    public void testCollectionConstructor() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        DefaultDataset dataset = new DefaultDataset(instances);
        assertNotNull(dataset);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.size());
        assertSame(instance1, dataset.get(0));
        assertSame(instance2, dataset.get(1));
        assertEquals(Arrays.asList(new Double[]{1.0, 2.0, 3.0}, 0.0), instance1.toMap().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));
        assertEquals(Arrays.asList(new Double[]{4.0, 5.0, 6.0}, 1.0), instance2.toMap().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));
        assertEquals(Arrays.asList("0.0", "1.0"), dataset.stream().map(Instance::classValue).collect(Collectors.toList()));
    }

    @Test
    public void testAddAll() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.size());
        assertEquals(Arrays.asList(instance1, instance2), dataset);
        assertEquals(Arrays.asList("0.0", "1.0"), dataset.stream().map(Instance::classValue).collect(Collectors.toList()));
    }

    @Test
    public void testAddAllWithDuplicates() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance1);
        instances.add(instance2);

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);
        assertFalse(dataset.isEmpty());
        assertEquals(3, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertEquals(Arrays.asList(instance1, instance1, instance2), dataset);
        assertEquals(Arrays.asList("0.0", "0.0", "1.0"), dataset.stream().map(Instance::classValue).collect(Collectors.toList()));
    }

    @Test
    public void testAdd() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.size());
        assertEquals(Arrays.asList(instance1, instance2), dataset);
    }

    @Test
    public void testClear() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        DefaultDataset dataset = new DefaultDataset(instances);
        dataset.clear();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
    }

    @Test
    public void testCheck() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, null);
        DefaultDataset dataset = new DefaultDataset();

        dataset.check(Collections.singleton(instance1));
        assertEquals(1, dataset.size());
        dataset.check(instance2);
        assertEquals(2, dataset.size());

        try {
            dataset.check(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testKNearest() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        Instance instance3 = new DenseInstance(new Double[]{1.0, 2.0, 3.1}, 0.0);
        Instance instance4 = new DenseInstance(new Double[]{4.1, 5.1, 6.1}, 1.0);
        Instance instance5 = new DenseInstance(new Double[]{1.1, 2.1, 3.2}, 0.0);
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4, instance5));

        double distance1 = new EuclideanDistance().measure(instance1, instance3);
        double distance2 = new EuclideanDistance().measure(instance1, instance5);

        List<Instance> kNearest = dataset.kNearest(2, instance1, new EuclideanDistance());
        assertEquals(2, kNearest.size());
        assertTrue(kNearest.contains(instance3));
        assertTrue(kNearest.contains(instance5));
        List<Instance> kFarthest = dataset.kNearest(2, instance1, new EuclideanDistance()).stream().sorted(Comparator.comparingDouble(instance -> new EuclideanDistance().measure(instance1, instance))).collect(Collectors.toList());
        assertTrue(kFarthest.get(0).equals(instance1) || kFarthest.get(1).equals(instance1));
        assertTrue(kFarthest.get(1).equals(instance4) || kFarthest.get(0).equals(instance4));
    }

    @Test
    public void testFolds() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        Instance instance3 = new DenseInstance(new Double[]{1.1, 2.1, 3.1}, 0.0);
        Instance instance4 = new DenseInstance(new Double[]{4.1, 5.1, 6.1}, 1.0);
        Instance instance5 = new DenseInstance(new Double[]{1.2, 2.2, 3.2}, 0.0);
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4, instance5));

        int numFolds = 3;
        Random random = new Random();
        Dataset[] folds = dataset.folds(numFolds, random);

        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertNotNull(fold);
            assertTrue(fold instanceof Fold);
            assertTrue(fold.size() > 0);
            assertTrue(fold.getClass() == dataset.getClass());
        }
    }

    @Test
    public void testNoAttributes() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        DefaultDataset dataset = new DefaultDataset(instance1);
        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        assertEquals(0, dataset.classIndex(0.0));
        assertEquals(1, dataset.classIndex(1.0));
        assertNull(dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        assertEquals(0.0, dataset.classValue(0));
        assertEquals(1.0, dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0}, 0.0);
        Instance instance2 = new DenseInstance(new Double[]{4.0, 5.0, 6.0}, 1.0);
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
        DefaultDataset copiedDataset = dataset.copy();
        assertEquals(dataset, copiedDataset);
        assertTrue(copiedDataset.getClass() == dataset.getClass());
    }
}