package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNull(dataset.classes());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(2, dataset.size());
        assertEquals(3, dataset.maxAttributes);
        assertEquals(new HashSet<>(Arrays.asList(new Double(1.0), new Double(2.0))), dataset.classes());

        Instance expectedInstance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance expectedInstance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));

        Instance actualInstance1 = dataset.instance(0);
        Instance actualInstance2 = dataset.instance(1);

        assertEquals(expectedInstance1, actualInstance1);
        assertEquals(expectedInstance2, actualInstance2);
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        dataset.add(instance);

        assertEquals(1, dataset.size());
        assertEquals(3, dataset.maxAttributes);
        assertEquals(new HashSet<>(Arrays.asList(new Double(1.0))), dataset.classes());
        assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        dataset.clear();

        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.maxAttributes);
        assertNull(dataset.classes());
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new Double(2.0));
        Instance instance3 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new Double(3.0));
        Instance instance4 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(4.0));
        Instance instance5 = new DenseInstance(new double[]{5.0, 6.0, 7.0}, new Double(5.0));

        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4, instance5));

        Dataset[] actualFolds = dataset.folds(5, new Random());

        assertEquals(5, actualFolds.length);

        for (Dataset fold : actualFolds) {
            assertTrue(fold instanceof Fold);
            Fold expectedFold = new Fold(dataset, new int[5]);
            assertEquals(expectedFold.dataset, fold);
        }
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        dataset.add(instance);

        assertEquals(3, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(1, dataset.classIndex(instance1.classValue()));
        assertEquals(2, dataset.classIndex(instance2.classValue()));
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(instance1.classValue(), dataset.classValue(0));
        assertEquals(instance2.classValue(), dataset.classValue(1));
        assertEquals(null, dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(2.0));
        dataset.addAll(Arrays.asList(instance1, instance2));

        DefaultDataset copy = dataset.copy();

        assertEquals(dataset.size(), copy.size());
        assertEquals(dataset.maxAttributes, copy.maxAttributes);
        assertEquals(dataset.classes(), copy.classes());

        for (int i = 0; i < dataset.size(); i++) {
            Instance actualInstance = copy.instance(i);
            Instance expectedInstance = dataset.instance(i);
            assertEquals(expectedInstance, actualInstance);
        }
    }
}