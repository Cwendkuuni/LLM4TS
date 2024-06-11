package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetConstructionWithCollection() {
        DefaultDataset dataset = new DefaultDataset(Collections.singletonList(new DummyInstance(1, 1.0)));
        assertEquals(1, dataset.size());
    }

    @Test
    public void testDefaultDatasetEmptyConstruction() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        Instance instance2 = new DummyInstance(2, 2.0);
        dataset.addAll(Collections.singletonList(instance1));
        dataset.addAll(Collections.singletonList(instance2));
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.get(0));
        assertEquals(instance2, dataset.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllWithNullCollection() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(null);
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        Instance instance2 = new DummyInstance(2, 2.0);
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullInstance() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(null);
    }

    @Test
    public void testAddAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        Instance instance2 = new DummyInstance(2, 2.0);
        dataset.add(instance1);
        dataset.add(index = 0, instance2);
        assertEquals(2, dataset.size());
        assertEquals(instance2, dataset.get(0));
        assertEquals(instance1, dataset.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtNegativeIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAt(-1, new DummyInstance(1, 1.0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexGreaterThanSize() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAt(size = dataset.size() + 1, new DummyInstance(1, 1.0));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        Instance instance2 = new DummyInstance(2, 2.0);
        dataset.add(instance1);
        dataset.setElementAt(instance2, 0);
        assertEquals(1, dataset.size());
        assertEquals(instance2, dataset.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetElementAtNegativeIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.setElementAt(new DummyInstance(1, 1.0), -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetElementAtIndexGreaterThanSize() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.setElementAt(new DummyInstance(1, 1.0), size = dataset.size() + 1);
    }

    @Test
    public void testGetInstance() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        dataset.add(instance1);
        assertEquals(instance1, dataset.getInstance(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInstanceNegativeIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.getInstance(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInstanceIndexGreaterThanSize() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.getInstance(size = dataset.size() + 1);
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0, "Class1");
        Instance instance2 = new DummyInstance(2, 2.0, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.containsAll(Collections.singletonList("Class1")));
        assertTrue(classes.containsAll(Collections.singletonList("Class2")));
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0, 3.0);
        Instance instance2 = new DummyInstance(1, 1.0, 2.0);
        Instance instance3 = new DummyInstance(1, 1.0, 5.0);
        Instance instance4 = new DummyInstance(1, 1.0, 4.0);
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);
        dataset.add(instance4);
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Instance testInstance = new DummyInstance(1, 1.0, 3.5);
        List<Instance> expectedNearestInstances = new ArrayList<>();
        Collections.addAll(expectedNearestInstances, instance2, instance4);
        Collections.sort(expectedNearestInstances, new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                double d1 = distanceMeasure.measure(testInstance, o1);
                double d2 = distanceMeasure.measure(testInstance, o2);
                return Double.compare(d1, d2);
            }
        });
        List<Instance> actualNearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
        assertEquals(expectedNearestInstances.size(), actualNearestInstances.size());
        Collections.sort(actualNearestInstances, new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                double d1 = distanceMeasure.measure(testInstance, o1);
                double d2 = distanceMeasure.measure(testInstance, o2);
                return Double.compare(d1, d2);
            }
        });
        assertTrue(actualNearestInstances.equals(expectedNearestInstances));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKNearestWithNullInstance() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.kNearest(2, null, new EuclideanDistance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKNearestWithDistanceMeasureNull() {
        DefaultDataset dataset = new DefaultDataset();
        Instance testInstance = new DummyInstance(1, 1.0, 3.5);
        dataset.kNearest(2, testInstance, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKNearestWithKLessThanOne() {
        DefaultDataset dataset = new DefaultDataset();
        Instance testInstance = new DummyInstance(1, 1.0, 3.5);
        dataset.kNearest(-1, testInstance, new EuclideanDistance());
    }

    @Test
    public void testFolds() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        Instance instance2 = new DummyInstance(2, 2.0);
        Instance instance3 = new DummyInstance(3, 3.0);
        Instance instance4 = new DummyInstance(4, 4.0);
        Instance instance5 = new DummyInstance(5, 5.0);
        dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4, instance5));
        int numFolds = 3;
        Random random = new Random();
        Dataset[] actualFolds = dataset.folds(numFolds, random);
        int expectedSize = dataset.size() / numFolds;
        assertArrayEquals(new Dataset[numFolds], actualFolds);
        for (int i = 0; i < numFolds; i++) {
            assertEquals(expectedSize, actualFolds[i].size());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFoldsWithNegativeNumFolds() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.folds(-1, new Random());
    }

    @Test(expected = NullPointerException.class)
    public void testFoldsWithNullRandom() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.folds(3, null);
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DummyInstance(1, 1.0);
        dataset.add(instance);
        assertEquals(instance.noAttributes(), dataset.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0, "Class1");
        Instance instance2 = new DummyInstance(2, 2.0, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals(0, dataset.classIndex("Class1"));
        assertEquals(-1, dataset.classIndex(null));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0, "Class1");
        Instance instance2 = new DummyInstance(2, 2.0, "Class2");
        dataset.add(instance1);
        dataset.add(instance2);
        assertEquals("Class1", dataset.classValue(0));
        assertEquals(null, dataset.classValue(1));
    }

    @Test
    public void testCopy() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DummyInstance(1, 1.0);
        Instance instance2 = new DummyInstance(2, 2.0);
        dataset.add(instance1);
        dataset.add(instance2);
        DefaultDataset copy = dataset.copy();
        assertEquals(dataset.size(), copy.size());
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.getInstance(i);
            assertEquals(instance, copy.getInstance(i));
        }
    }
}

class DummyInstance implements Instance {
    private int id;
    private double[] values;
    private String className;

    public DummyInstance(int id, double... values) {
        this.id = id;
        this.values = values;
    }

    public DummyInstance(int id, double value, String className) {
        this.id = id;
        this.values = new double[]{value};
        this.className = className;
    }

    @Override
    public int classIndex() {
        return 0;
    }

    @Override
    public double[] values() {
        return this.values;
    }

    @Override
    public double value(int attributeIndex) {
        return this.values[attributeIndex];
    }

    @Override
    public int noAttributes() {
        return this.values.length;
    }

    @Override
    public Object classValue() {
        return this.className;
    }

    @Override
    public Instance copy() {
        return new DummyInstance(this.id, this.values.clone());
    }
}