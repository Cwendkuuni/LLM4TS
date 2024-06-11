package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.instance.vector.ArrayVector;

public class DefaultDatasetTest {

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testConstructorWithCollection() {
        List<Instance> instances = new ArrayList<>();
        instances.add(createInstance(new double[] { 1, 2 }, "class1"));
        instances.add(createInstance(new double[] { 2, 3 }, "class2"));

        DefaultDataset dataset = new DefaultDataset(instances);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.noAttributes());
        Set<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));

        Instance instance = dataset.instance(0);
        assertEquals(new double[] { 1, 2 }, instance.toArray());
        assertEquals("class1", instance.classValue());
    }

    @Test
    public void testAddingInstances() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[] { 1, 2 }, "class1");
        Instance instance2 = createInstance(new double[] { 2, 3 }, "class2");

        dataset.add(instance1);
        dataset.add(instance2);

        assertEquals(2, dataset.size());
        Set<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));

        Instance instanceFromDataset1 = dataset.instance(0);
        assertEquals(instance1, instanceFromDataset1);
        Instance instanceFromDataset2 = dataset.instance(1);
        assertEquals(instance2, instanceFromDataset2);
    }

    @Test
    public void testAddingCollection() {
        List<Instance> instances = new ArrayList<>();
        instances.add(createInstance(new double[] { 1, 2 }, "class1"));
        instances.add(createInstance(new double[] { 2, 3 }, "class2"));

        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);

        assertEquals(2, dataset.size());
        Set<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));

        Instance instanceFromDataset1 = dataset.instance(0);
        assertEquals(instances.get(0), instanceFromDataset1);
        Instance instanceFromDataset2 = dataset.instance(1);
        assertEquals(instances.get(1), instanceFromDataset2);
    }

    @Test
    public void testSettingAndGettingClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[] { 1, 2 }, "class1");
        Instance instance2 = createInstance(new double[] { 2, 3 }, "class2");
        dataset.addAll(Collections.singletonList(instance1));
        dataset.addAll(Collections.singletonList(instance2));

        assertEquals(2, dataset.size());
        Set<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));

        dataset.clear();
        assertEquals(0, dataset.size());
        assertEquals(0, dataset.noAttributes());
        assertTrue(dataset.classes().isEmpty());

        dataset.addAll(instances);
        classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test
    public void testFolds() {
        int numFolds = 3;
        Random random = new Random();

        List<Instance> instances = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            instances.add(createInstance(new double[] { i, i }, "class1"));
        }

        DefaultDataset dataset = new DefaultDataset(instances);
        Dataset[] folds = dataset.folds(numFolds, random);

        assertEquals(numFolds, folds.length);
        for (Dataset fold : folds) {
            assertEquals(3, fold.size());
        }
    }

    @Test
    public void testkNearest() {
        final int k = 2;
        DistanceMeasure distanceMeasure = new EuclideanDistance();

        List<Instance> instances = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            instances.add(createInstance(new double[] { i, i }, "class1"));
        }

        DefaultDataset dataset = new DefaultDataset(instances);

        List<Instance> expected = Arrays.asList(dataset.instance(0), dataset.instance(1));
        List<Instance> result = new ArrayList<>();
        result.addAll(dataset.kNearest(k, dataset.instance(3), distanceMeasure));

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testkNearestWithInvalidK() {
        final int k = -1;
        DistanceMeasure distanceMeasure = new EuclideanDistance();

        List<Instance> instances = new ArrayList<>();
        instances.add(createInstance(new double[] { 1, 2 }, "class1"));
        instances.add(createInstance(new double[] { 2, 3 }, "class2"));

        DefaultDataset dataset = new DefaultDataset(instances);

        dataset.kNearest(k, createInstance(new double[] { 3, 3 }, "class1"), distanceMeasure);
    }

    private Instance createInstance(double[] attributes, Object classValue) {
        Instance instance = new ArrayVector(attributes);
        instance.setClassValue(classValue);
        return instance;
    }
}