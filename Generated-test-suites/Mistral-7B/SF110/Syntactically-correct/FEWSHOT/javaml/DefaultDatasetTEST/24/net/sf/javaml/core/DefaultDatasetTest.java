package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{4.0, 5.0, 6.0}, "Class2");
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        dataset.addAll(instances);

        assertEquals(2, dataset.size());
        assertEquals("Class1", dataset.instance(0).classValue());
        assertEquals("Class2", dataset.instance(1).classValue());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{4.0, 5.0, 6.0}, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);

        dataset.clear();

        assertEquals(0, dataset.size());
        assertNull(dataset.classes().iterator().next());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        dataset.add(instance1);

        assertEquals(1, dataset.size());
        assertEquals("Class1", dataset.instance(0).classValue());
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{4.0, 5.0, 6.0}, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);

        SortedSet<Object> classes = dataset.classes();

        assertEquals(2, classes.size());
        assertTrue(classes.contains("Class1"));
        assertTrue(classes.contains("Class2"));
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{2.0, 2.0, 2.0}, "Class2");
        Instance instance3 = createInstance(new double[]{3.0, 3.0, 3.0}, "Class3");
        Instance instance4 = createInstance(new double[]{4.0, 4.0, 4.0}, "Class1");
        Instance instance5 = createInstance(new double[]{5.0, 5.0, 5.0}, "Class3");
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        dataset.add(instance5);

        Dataset[] folds = dataset.folds(5, new Random());

        assertEquals(5, folds.length);
        for (Dataset fold : folds) {
            assertEquals(dataset.size(), fold.size());
        }
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{2.0, 2.0, 2.0}, "Class2");
        Instance instance3 = createInstance(new double[]{3.0, 3.0, 3.0}, "Class3");
        Instance instance4 = createInstance(new double[]{4.0, 4.0, 4.0}, "Class1");
        Instance instance5 = createInstance(new double[]{5.0, 5.0, 5.0}, "Class3");
        Instance instance6 = createInstance(new double[]{6.0, 6.0, 6.0}, "Class1");
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        dataset.add(instance5);
        dataset.add(instance6);

        Set<Instance> nearestInstances = dataset.kNearest(3, instance6, new EuclideanDistanceMeasure());

        assertEquals(3, nearestInstances.size());
        List<Instance> nearestInstancesList = new ArrayList<>(nearestInstances);
        Collections.sort(nearestInstancesList, new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                double distance1 = new EuclideanDistanceMeasure().measure(instance6, o1);
                double distance2 = new EuclideanDistanceMeasure().measure(instance6, o2);
                return Double.compare(distance1, distance2);
            }
        });

        assertEquals(instance1, nearestInstancesList.get(0));
        assertEquals(instance2, nearestInstancesList.get(1));
        assertEquals(instance4, nearestInstancesList.get(2));
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        dataset.add(instance);

        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{2.0, 2.0, 2.0}, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);

        assertEquals(1, dataset.classIndex("Class1"));
        assertEquals(-1, dataset.classIndex(null));
        assertEquals(-1, dataset.classIndex("Class3"));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{2.0, 2.0, 2.0}, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);

        assertEquals("Class1", dataset.classValue(0));
        assertEquals("Class2", dataset.classValue(1));
        assertNull(dataset.classValue(-1));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, "Class1");
        Instance instance2 = createInstance(new double[]{2.0, 2.0, 2.0}, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);

        DefaultDataset copy = dataset.copy();

        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            assertEquals(dataset.instance(i), copy.instance(i));
        }
    }

    private Instance createInstance(double[] attributes, Object clazz) {
        DenseInstance denseInstance = new DenseInstance(1.0, attributes.length);
        for (int i = 0; i < attributes.length; i++) {
            denseInstance.setValue(i, attributes[i]);
        }
        return new InstanceFactory().getInstance(denseInstance, clazz);
    }
}