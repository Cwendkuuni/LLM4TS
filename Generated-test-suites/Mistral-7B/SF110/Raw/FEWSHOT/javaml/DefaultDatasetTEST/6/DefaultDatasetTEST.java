package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNull(dataset.classes());
    }

    @Test
    public void testDefaultDatasetAddCollection() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        dataset.addAll(instances);
        assertEquals(2, dataset.size());
        assertEquals(2, dataset.maxAttributes);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(new Double(0.0)));
        assertTrue(classes.contains(new Double(1.0)));
        Instance instanceFromDataset = dataset.instance(0);
        assertEquals(instance1, instanceFromDataset);
        Instance instanceFromCollection = instances.iterator().next();
        assertEquals(instanceFromCollection, instanceFromDataset);
    }

    @Test
    public void testDefaultDatasetAddInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertEquals(2, dataset.maxAttributes);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertTrue(classes.contains(new Double(0.0)));
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        dataset.addAll(instances);
        assertEquals(2, dataset.size());
        assertEquals(2, dataset.maxAttributes);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(new Double(0.0)));
        assertTrue(classes.contains(new Double(1.0)));
        Instance instanceFromDataset = dataset.instance(0);
        assertEquals(instance1, instanceFromDataset);
        Instance instanceFromCollection = instances.iterator().next();
        assertEquals(instanceFromCollection, instanceFromDataset);
        Instance instanceFromSecondElement = instances.iterator().next();
        assertEquals(instanceFromSecondElement, dataset.instance(1));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        dataset.addAll(Arrays.asList(instance1, instance2));
        dataset.clear();
        assertEquals(0, dataset.size());
        assertNull(dataset.classes());
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{2.0, 2.0}, new Double(0.0));
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Set<Instance> nearestInstances = dataset.kNearest(2, instance1, distanceMeasure);
        assertEquals(2, nearestInstances.size());

        Instance nearestInstance1 = nearestInstances.iterator().next();
        Instance nearestInstance2 = nearestInstances.iterator().next();

        if (nearestInstance1.equals(instance1)) {
            fail("The first nearest instance should be different from the query instance.");
        }
        if (nearestInstance2.equals(instance1)) {
            fail("The second nearest instance should be different from the query instance.");
        }

        Instance queryInstance = instance1;
        List<Instance> expectedInstances = new ArrayList<>();
        if (instance2.distance(instance1) < instance3.distance(instance1)) {
            expectedInstances.add(instance2);
            expectedInstances.add(instance3);
        } else {
            expectedInstances.add(instance3);
            expectedInstances.add(instance2);
        }
        expectedInstances.add(instance1);

        List<Instance> actualInstances = new ArrayList<>();
        for (Instance instance : nearestInstances) {
            actualInstances.add(instance);
        }

        Collections.sort(actualInstances, new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                return Double.compare(o1.distance(queryInstance), o2.distance(queryInstance));
            }
        });

        assertEquals(expectedInstances, actualInstances);
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{2.0, 2.0}, new Double(0.0));
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        int numFolds = 3;
        Random random = new Random();
        Dataset[] folds = dataset.folds(numFolds, random);

        assertEquals(numFolds, folds.length);

        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
            Fold expectedFold = new Fold(dataset, new int[dataset.size()]);
            assertEquals(expectedFold, fold);
        }
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(instance1.classValue(), dataset.classValue(0));
        assertEquals(instance2.classValue(), dataset.classValue(1));
        assertEquals(null, dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        DefaultDataset copiedDataset = dataset.copy();
        assertEquals(dataset.size(), copiedDataset.size());
        assertEquals(dataset.maxAttributes, copiedDataset.maxAttributes);

        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.instance(i);
            Instance copiedInstance = copiedDataset.instance(i);
            assertEquals(instance, copiedInstance);
        }
    }
}