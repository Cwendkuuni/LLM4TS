package net.sf.javaml.core.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertEquals(0, dataset.size());
        Assert.assertEquals(0, dataset.maxAttributes());
        Assert.assertNull(dataset.classes());
    }

    @Test
    public void testCollectionConstructor() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));

        DefaultDataset dataset = new DefaultDataset(instances);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(2, dataset.maxAttributes());
        Assert.assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());

        Instance instance = dataset.instance(0);
        Assert.assertEquals(new DenseInstance(new double[]{1.0, 2.0}, 1.0), instance);
    }

    @Test
    public void testAddAll() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);

        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(2, dataset.maxAttributes());
        Assert.assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithDuplicateInstances() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);
    }

    @Test
    public void testKNearest() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        instances.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, 2.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1.0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));
        instances.add(new DenseInstance(new double[]{5.0, 6.0, 7.0}, 1.0));

        DefaultDataset dataset = new DefaultDataset(instances);

        Instance testInstance = new DenseInstance(new double[]{3.5, 4.5, 5.5}, null);
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Set<Instance> nearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);

        Assert.assertEquals(2, nearestInstances.size());
        Assert.assertTrue(nearestInstances.contains(instances.get(1)));
        Assert.assertTrue(nearestInstances.contains(instances.get(2)));

        Instance fartherInstance = instances.get(0);
        double maxDistance = distanceMeasure.measure(testInstance, fartherInstance);
        Assert.assertFalse(nearestInstances.contains(fartherInstance));
        Assert.assertTrue(maxDistance > distanceMeasure.measure(testInstance, instances.get(1)));
        Assert.assertTrue(maxDistance > distanceMeasure.measure(testInstance, instances.get(2)));
    }

    @Test
    public void testFolds() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0));
        instances.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, 2.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0, 5.0}, 1.0));
        instances.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0));
        instances.add(new DenseInstance(new double[]{5.0, 6.0, 7.0}, 1.0));

        DefaultDataset dataset = new DefaultDataset(instances);
        Dataset[] folds = dataset.folds(5, new Random());

        Assert.assertEquals(5, folds.length);
        for (Dataset fold : folds) {
            Assert.assertTrue(fold instanceof Fold);
            Assert.assertFalse(fold.isEmpty());
        }

        int totalSize = 0;
        for (Dataset fold : folds) {
            totalSize += fold.size();
        }
        Assert.assertEquals(instances.size(), totalSize);

        for (int i = 0; i < folds.length; i++) {
            Instance instanceInFold = null;
            for (Instance instance : instances) {
                if (Arrays.asList(folds[i].instanceIndices()).contains(instance.index())) {
                    instanceInFold = instance;
                    break;
                }
            }
            Assert.assertNotNull(instanceInFold);
        }
    }

    @Test
    public void testNoAttributes() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));

        DefaultDataset dataset = new DefaultDataset(instances);
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(2, dataset.maxAttributes());
        Assert.assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());

        Assert.assertEquals(2, instances.get(0).noAttributes());
        Assert.assertEquals(2, instances.get(1).noAttributes());
        Assert.assertEquals(2, dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 1.0));

        DefaultDataset dataset = new DefaultDataset(instances);

        Assert.assertEquals(0, dataset.classIndex(1.0));
        Assert.assertEquals(1, dataset.classIndex(2.0));
        Assert.assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 1.0));

        DefaultDataset dataset = new DefaultDataset(instances);

        Assert.assertEquals(1.0, dataset.classValue(0));
        Assert.assertEquals(2.0, dataset.classValue(1));
        Assert.assertEquals(null, dataset.classValue(2));
    }

    @Test
    public void testCopy() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        instances.add(new DenseInstance(new double[]{3.0, 4.0}, 2.0));

        DefaultDataset originalDataset = new DefaultDataset(instances);
        DefaultDataset copiedDataset = originalDataset.copy();

        Assert.assertEquals(originalDataset.size(), copiedDataset.size());
        Assert.assertEquals(originalDataset.maxAttributes(), copiedDataset.maxAttributes());
        Assert.assertEquals(originalDataset.classes(), copiedDataset.classes());

        for (int i = 0; i < originalDataset.size(); i++) {
            Instance originalInstance = originalDataset.instance(i);
            Instance copiedInstance = copiedDataset.instance(i);
            Assert.assertEquals(originalInstance.noAttributes(), copiedInstance.noAttributes());
            Assert.assertArrayEquals(originalInstance.values(), copiedInstance.values());
            Assert.assertEquals(originalInstance.classValue(), copiedInstance.classValue());
        }
    }
}