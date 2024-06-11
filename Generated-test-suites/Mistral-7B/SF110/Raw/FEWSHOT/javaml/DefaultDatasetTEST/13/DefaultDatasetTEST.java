package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes());
        assertNull(dataset.classes());
    }

    @Test
    public void testDefaultDatasetWithInstances() {
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.toArray()[0].toString());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });

        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.toArray()[0].toString());
    }

    @Test
    public void testAddInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });

        dataset.add(instance1);
        dataset.add(instance2);

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.toArray()[0].toString());
    }

    @Test
    public void testAddInstanceWithClass() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 }, "class1");

        dataset.add(instance);

        assertEquals(1, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.toArray()[0].toString());
    }

    @Test
    public void testAddInstanceWithNoClass() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });

        dataset.add(instance);

        assertEquals(1, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(0, classes.size());
    }

    @Test
    public void testCheck() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });

        List<Instance> instances = Arrays.asList(instance1, instance2);
        dataset.check(instances);

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.toArray()[0].toString());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });
        dataset.addAll(Arrays.asList(instance1, instance2));

        dataset.clear();

        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes());
        assertNull(dataset.classes());
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });
        Instance instance3 = new DenseInstance(3.0, new double[] { 5.0, 6.0, 7.0 });
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        int numFolds = 3;
        Random rg = new Random();
        Dataset[] folds = dataset.folds(numFolds, rg);

        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertEquals(dataset.size() / numFolds, fold.size());
        }
    }

    @Test
    public void testInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        dataset.add(instance1);

        Instance instance = dataset.instance(0);

        assertSame(instance1, instance);
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 }, "class1");
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 }, "class2");
        Instance instance3 = new DenseInstance(3.0, new double[] { 5.0, 6.0, 7.0 }, "class1");
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        Instance queryInstance = new DenseInstance(2.5, new double[] { 2.5, 2.5, 2.5 });
        DistanceMeasure distanceMeasure = new EuclideanDistance();

        Set<Instance> neighbors = dataset.kNearest(2, queryInstance, distanceMeasure);

        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(instance1));
        assertTrue(neighbors.contains(instance3));
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        dataset.add(instance);

        assertEquals(3, dataset.noAttributes());
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 }, "class1");
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 }, "class2");
        dataset.addAll(Arrays.asList(instance1, instance2));

        Set<Object> classes = dataset.classes();

        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 }, "class1");
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 }, "class2");
        dataset.addAll(Arrays.asList(instance1, instance2));

        int classIndex1 = dataset.classIndex("class1");
        int classIndex2 = dataset.classIndex("class2");

        assertEquals(0, classIndex1);
        assertEquals(1, classIndex2);
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 }, "class1");
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 }, "class2");
        dataset.addAll(Arrays.asList(instance1, instance2));

        Object classValue1 = dataset.classValue(0);
        Object classValue2 = dataset.classValue(1);

        assertEquals("class1", classValue1);
        assertEquals("class2", classValue2);
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new double[] { 1.0, 2.0, 3.0 });
        Instance instance2 = new DenseInstance(2.0, new double[] { 3.0, 4.0, 5.0 });
        dataset.addAll(Arrays.asList(instance1, instance2));

        DefaultDataset copiedDataset = dataset.copy();

        assertEquals(2, copiedDataset.size());
        assertEquals(3, copiedDataset.maxAttributes());
        Set<Object> classes = copiedDataset.classes();
        assertEquals(1, classes.size());
        assertEquals("class1", classes.toArray()[0].toString());

        for (int i = 0; i < copiedDataset.size(); i++) {
            Instance instance = copiedDataset.instance(i);
            Instance originalInstance = dataset.instance(i);
            assertEquals(originalInstance.noAttributes(), instance.noAttributes());
            assertArrayEquals(originalInstance.toDoubleArray(), instance.toDoubleArray());
            assertEquals(originalInstance.classValue(), instance.classValue());
        }
    }
}