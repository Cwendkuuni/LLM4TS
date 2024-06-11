package net.sf.javaml.core;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{5.0, 6.0}, new Double(2.0));

        List<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        instances.add(instance3);

        dataset.addAll(instances);

        Assert.assertEquals(3, dataset.size());
        Assert.assertEquals(instance1, dataset.instance(0));
        Assert.assertEquals(instance2, dataset.instance(1));
        Assert.assertEquals(instance3, dataset.instance(2));
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));

        dataset.add(instance);

        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(instance, dataset.instance(0));
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{5.0, 6.0}, new Double(2.0));

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        SortedSet<Object> classes = dataset.classes();

        Assert.assertEquals(3, classes.size());
        Assert.assertTrue(classes.contains(new Double(0.0)));
        Assert.assertTrue(classes.contains(new Double(1.0)));
        Assert.assertTrue(classes.contains(new Double(2.0)));
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));

        dataset.add(instance);

        Assert.assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{5.0, 6.0}, new Double(2.0));

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        Assert.assertEquals(0, dataset.classIndex(new Double(0.0)));
        Assert.assertEquals(1, dataset.classIndex(new Double(1.0)));
        Assert.assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{5.0, 6.0}, new Double(2.0));

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        Assert.assertEquals(new Double(0.0), dataset.classValue(0));
        Assert.assertEquals(new Double(1.0), dataset.classValue(1));
        Assert.assertEquals(new Double(2.0), dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{5.0, 6.0}, new Double(2.0));

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        DefaultDataset copiedDataset = dataset.copy();

        Assert.assertEquals(3, copiedDataset.size());
        Assert.assertEquals(instance1, copiedDataset.instance(0));
        Assert.assertEquals(instance2, copiedDataset.instance(1));
        Assert.assertEquals(instance3, copiedDataset.instance(2));
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{1.0, 2.0, 4.0}, new Double(0.0));
        Instance instance3 = new DenseInstance(new double[]{1.0, 3.0, 5.0}, new Double(1.0));
        Instance instance4 = new DenseInstance(new double[]{2.0, 2.0, 6.0}, new Double(1.0));
        Instance instance5 = new DenseInstance(new double[]{1.0, 1.0, 7.0}, new Double(2.0));

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        dataset.add(instance5);

        Dataset[] folds = dataset.folds(5, new Random());

        Assert.assertEquals(5, folds.length);
        for (Dataset fold : folds) {
            Assert.assertFalse(fold instanceof DefaultDataset);
            Assert.assertTrue(fold instanceof Fold);
            Assert.assertEquals(instance1, ((Fold) fold).dataset.instance(0));
            Assert.assertEquals(instance2, ((Fold) fold).dataset.instance(1));
            Assert.assertEquals(instance3, ((Fold) fold).dataset.instance(2));
            Assert.assertEquals(instance4, ((Fold) fold).dataset.instance(3));
            Assert.assertEquals(instance5, ((Fold) fold).dataset.instance(4));
        }
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));
        Instance instance2 = new DenseInstance(new double[]{1.5, 2.5, 3.5}, new Double(1.0));
        Instance instance3 = new DenseInstance(new double[]{2.0, 3.0, 4.0}, new Double(1.0));
        Instance instance4 = new DenseInstance(new double[]{2.5, 3.5, 4.5}, new Double(2.0));
        Instance instance5 = new DenseInstance(new double[]{3.0, 4.0, 5.0}, new Double(2.0));

        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        dataset.add(instance5);

        Instance testInstance = new DenseInstance(new double[]{2.0, 2.5, 4.0}, new Double(1.0));

        Set<Instance> nearestInstances = dataset.kNearest(3, testInstance, new EuclideanDistance());

        Assert.assertEquals(3, nearestInstances.size());
        Assert.assertTrue(nearestInstances.contains(instance2));
        Assert.assertTrue(nearestInstances.contains(instance3));
        Assert.assertTrue(nearestInstances.contains(instance4));
    }
}