package net.sf.javaml.core.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertEquals(0, dataset.size());
        Assert.assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1]);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        Assert.assertEquals(3, dataset.size());
        Assert.assertFalse(dataset.isEmpty());

        Iterator<Instance> iterator = dataset.iterator();
        Assert.assertEquals(instance1, iterator.next());
        Assert.assertEquals(instance2, iterator.next());
        Assert.assertEquals(instance3, iterator.next());
        Assert.assertNull(iterator.hasNext());
    }

    @Test
    public void testAddAllWithCheck() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1]);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        try {
            dataset.addAll(Arrays.asList(instance1, instance2, instance3));
            Assert.fail("Expected exception not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        Assert.assertEquals(3, dataset.size());
        Assert.assertFalse(dataset.isEmpty());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1]);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        dataset.clear();

        Assert.assertEquals(0, dataset.size());
        Assert.assertTrue(dataset.isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);

        dataset.add(instance1);

        Assert.assertEquals(1, dataset.size());
        Assert.assertFalse(dataset.isEmpty());

        Iterator<Instance> iterator = dataset.iterator();
        Assert.assertEquals(instance1, iterator.next());
        Assert.assertNull(iterator.hasNext());
    }

    @Test
    public void testAddWithCheck() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);

        dataset.add(instance1);

        try {
            dataset.add(instance1);
            Assert.fail("Expected exception not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        Assert.assertEquals(1, dataset.size());
        Assert.assertFalse(dataset.isEmpty());
    }

    @Test
    public void testRemove() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1]);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        dataset.remove(instance1);

        Assert.assertEquals(2, dataset.size());
        Assert.assertFalse(dataset.isEmpty());

        Iterator<Instance> iterator = dataset.iterator();
        Assert.assertEquals(instance2, iterator.next());
        Assert.assertEquals(instance3, iterator.next());
        Assert.assertNull(iterator.hasNext());
    }

    @Test
    public void testRemoveNull() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1]);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        try {
            dataset.remove(null);
            Assert.fail("Expected exception not thrown");
        } catch (NullPointerException e) {
            // Expected
        }

        Assert.assertEquals(3, dataset.size());
        Assert.assertFalse(dataset.isEmpty());
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1], 1.0);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1], 2.0);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1], 1.0);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        SortedSet<Object> classes = dataset.classes();

        Assert.assertEquals(2, classes.size());
        Assert.assertTrue(classes.contains(1.0));
        Assert.assertTrue(classes.contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);

        dataset.add(instance1);

        Assert.assertEquals(instance1.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1], 1.0);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1], 2.0);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1], 1.0);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        Assert.assertEquals(1, dataset.classIndex(1.0));
        Assert.assertEquals(-1, dataset.classIndex(3.0));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1], 1.0);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1], 2.0);
        Instance instance3 = new DenseInstance(new Double[5], new Double[1], 1.0);

        dataset.addAll(Arrays.asList(instance1, instance2, instance3));

        Assert.assertEquals(1.0, dataset.classValue(0));
        Assert.assertEquals(2.0, dataset.classValue(1));
        Assert.assertEquals(1.0, dataset.classValue(2));
        Assert.assertNull(dataset.classValue(3));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[5], new Double[1]);
        Instance instance2 = new DenseInstance(new Double[5], new Double[1]);

        dataset.addAll(Arrays.asList(instance1, instance2));

        DefaultDataset copiedDataset = dataset.copy();

        Assert.assertEquals(2, copiedDataset.size());
        Assert.assertFalse(copiedDataset.isEmpty());

        Iterator<Instance> iterator = copiedDataset.iterator();
        Assert.assertEquals(instance1, iterator.next());
        Assert.assertEquals(instance2, iterator.next());
        Assert.assertNull(iterator.hasNext());
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();

        Instance instance1 = new DenseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new Double[]{0.0});
        Instance instance2 = new DenseInstance(new Double[]{2.0, 3.0, 4.0, 5.0, 6.0}, new Double[]{1.0});
        Instance instance3 = new DenseInstance(new Double[]{3.0, 4.0, 5.0, 6.0, 7.0}, new Double[]{2.0});
        Instance instance4 = new DenseInstance(new Double[]{4.0, 5.0, 6.0, 7.0, 8.0}, new Double[]{3.0});

        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

        Instance testInstance = new DenseInstance(new Double[]{2.5, 3.5, 4.5, 5.5, 6.5}, new Double[]{1.5});

        DistanceMeasure distanceMeasure = new EuclideanDistance();

        Set<Instance> nearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);

        Iterator<Instance> iterator = nearestInstances.iterator();

        Assert.assertEquals(instance2, iterator.next());
        Assert.assertEquals(instance3, iterator.next());
        Assert.assertNull(iterator.hasNext());
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();

        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new Double[5], new Double[1], 1.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 1.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 2.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 2.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 3.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 3.0));

        dataset.addAll(instances);

        Random random = new Random();

        Dataset[] folds = dataset.folds(3, random);

        Assert.assertEquals(3, folds.length);

        for (Dataset fold : folds) {
            Assert.assertNotNull(fold);
            Assert.assertTrue(fold instanceof Fold);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoldsWithInvalidNumberOfFolds() {
        DefaultDataset dataset = new DefaultDataset();

        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new Double[5], new Double[1], 1.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 1.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 2.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 2.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 3.0));
        instances.add(new DenseInstance(new Double[5], new Double[1], 3.0));

        dataset.addAll(instances);

        Random random = new Random();

        dataset.folds(-1, random);
    }
}