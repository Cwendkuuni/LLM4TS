package net.sf.javaml.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testCollectionConstructor() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class2", new double[]{3.0, 4.0});
        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        DefaultDataset dataset = new DefaultDataset(instances);
        assertNotNull(dataset);
        assertEquals(2, dataset.size());
        assertEquals("class1", dataset.classValue(0));
        assertEquals("class2", dataset.classValue(1));
    }

    @Test
    public void testAddAll() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});
        Instance instance3 = createInstance("class2", new double[]{5.0, 6.0});

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        assertEquals(3, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
        assertTrue(dataset.contains(instance3));
    }

    @Test
    public void testAdd() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});

        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);
        dataset.add(instance2);

        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance1));
        assertTrue(dataset.contains(instance2));
    }

    @Test
    public void testClear() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        dataset.clear();

        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testClasses() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class2", new double[]{3.0, 4.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertEquals("class1", classes.headSet("class2").last());
        assertNull(classes.headSet("class1").last());
    }

    @Test
    public void testKNearest() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});
        Instance instance3 = createInstance("class2", new double[]{5.0, 6.0});
        Instance instance4 = createInstance("class1", new double[]{2.0, 2.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

        List<Instance> kNearest = dataset.kNearest(2, instance2, new EuclideanDistance());
        assertEquals(2, kNearest.size());
        assertTrue(kNearest.contains(instance1));
        assertTrue(kNearest.contains(instance2));

        kNearest = dataset.kNearest(1, instance2, new EuclideanDistance());
        assertEquals(1, kNearest.size());
        assertTrue(kNearest.contains(instance2));

        kNearest = dataset.kNearest(1, instance1, new EuclideanDistance());
        assertEquals(1, kNearest.size());
        assertTrue(kNearest.contains(instance1));
    }

    @Test
    public void testFolds() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});
        Instance instance3 = createInstance("class2", new double[]{5.0, 6.0});
        Instance instance4 = createInstance("class1", new double[]{2.0, 2.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

        Dataset[] folds = dataset.folds(4, new Random());
        assertEquals(4, folds.length);

        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
            assertTrue(fold.size() > 0);
        }
    }

    @Test
    public void testNoAttributes() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instance1);

        assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class2", new double[]{3.0, 4.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals(0, dataset.classIndex("class1"));
        assertEquals(1, dataset.classIndex("class2"));
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        assertEquals("class1", dataset.classValue(0));
        assertEquals(null, dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        Instance instance1 = createInstance("class1", new double[]{1.0, 2.0});
        Instance instance2 = createInstance("class1", new double[]{3.0, 4.0});
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instance1, instance2));

        DefaultDataset copiedDataset = dataset.copy();

        assertEquals(2, copiedDataset.size());
        assertEquals(instance1, copiedDataset.get(0));
        assertEquals(instance2, copiedDataset.get(1));
    }

    private Instance createInstance(String className, double[] values) {
        return new DenseInstance(values, new DoubleArrayAttributesMetaData(values.length), className);
    }
}