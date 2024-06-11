package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultDataset.Fold;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

@RunWith(Parameterized.class)
public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private List<Instance> instances;
    private DistanceMeasure distanceMeasure;

    public DefaultDatasetTest(List<Instance> instances, DistanceMeasure distanceMeasure) {
        this.instances = instances;
        this.distanceMeasure = distanceMeasure;
        this.dataset = new DefaultDataset(instances);
    }

    @Parameterized.Parameters(name = "{0} with {1} instances and {2} attributes")
    public static Collection<Object[]> data() {
        List<Instance> instances1 = Arrays.asList(new DenseInstance(new double[]{1.0, 1.0, 1.0, 1.0}), new DenseInstance(new double[]{1.1, 1.1, 1.1, 1.1}));
        List<Instance> instances2 = Arrays.asList(new DenseInstance(new double[]{1.0, 1.0, 1.0, 1.0}, "Class1"), new DenseInstance(new double[]{1.1, 1.1, 1.1, 1.1}, "Class2"));
        List<Instance> instances3 = Arrays.asList(new DenseInstance(new double[]{1.0, 1.0, 1.0, 1.0}), new DenseInstance(new double[]{1.0, 1.1, 1.0, 1.1}), new DenseInstance(new double[]{1.0, 1.0, 1.1, 1.1}));

        return Arrays.asList(new Object[][]{
                new Object[]{instances1, new EuclideanDistance()},
                new Object[]{instances2, new EuclideanDistance()},
                new Object[]{instances3, new EuclideanDistance()}
        });
    }

    @Test
    public void testDefaultConstructor() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testCollectionConstructor() {
        assertEquals(instances.size(), dataset.size());
        assertEquals(instances, dataset);
        assertEquals(instances.toArray(new Instance[0]), dataset.toArray(new Instance[0]));
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(instances);
        assertEquals(instances.size(), dataset.size());
        assertEquals(instances, dataset);
        assertEquals(instances.toArray(new Instance[0]), dataset.toArray(new Instance[0]));
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset(instances);
        dataset.clear();
        assertTrue(dataset.isEmpty());
        assertTrue(dataset.classes().isEmpty());
    }

    @Test
    public void testAdd() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instances.get(0));
        assertEquals(1, dataset.size());
        assertEquals(instances.get(0), dataset.get(0));
        assertEquals(instances.get(0).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(0).classValue(), dataset.classValue(0));

        dataset.add(instances.get(1));
        assertEquals(2, dataset.size());
        assertEquals(instances.get(1), dataset.get(1));
        assertEquals(instances.get(1).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(1).classValue(), dataset.classValue(1));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance instance = instances.get(0);
        dataset.setElementAt(instances.get(1), 0);
        assertEquals(instances.get(1), dataset.get(0));
        assertEquals(instances.get(1).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(1).classValue(), dataset.classValue(0));
        assertEquals(instances.get(1), dataset.instance(0));
    }

    @Test
    public void testAddAllWithIndex() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addAll(Arrays.asList(instances.get(0), instances.get(1)).toArray(new Instance[0]));
        assertEquals(instances.subList(0, 2).size(), dataset.size());
        assertEquals(instances.subList(0, 2), Arrays.asList(dataset.subList(0, 2)));
        assertEquals(instances.get(1), dataset.get(1));
        assertEquals(instances.get(1).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(1).classValue(), dataset.classValue(1));

        List<Instance> instances2 = new ArrayList<Instance>(Arrays.asList(instances.get(1), instances.get(0)));
        dataset.addAll(1, instances2.toArray(new Instance[0]));
        assertEquals(instances.size(), dataset.size());
        assertEquals(instances, dataset);
        assertEquals(instances.toArray(new Instance[0]), dataset.toArray(new Instance[0]));
        assertEquals(instances.get(0), dataset.get(0));
        assertEquals(instances.get(0).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(0).classValue(), dataset.classValue(0));
    }

    @Test
    public void testAddElement() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.addElement(instances.get(0));
        assertEquals(1, dataset.size());
        assertEquals(instances.get(0), dataset.get(0));
        assertEquals(instances.get(0).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(0).classValue(), dataset.classValue(0));

        dataset.addElement(instances.get(1));
        assertEquals(2, dataset.size());
        assertEquals(instances.get(1), dataset.get(1));
        assertEquals(instances.get(1).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(1).classValue(), dataset.classValue(1));

        dataset.add(1, instances.get(1));
        assertEquals(2, dataset.size());
        assertEquals(instances.get(1), dataset.get(1));
        assertEquals(instances.get(1).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(1).classValue(), dataset.classValue(1));
    }

    @Test
    public void testInsertElementAt() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(instances.get(0));

        dataset.insertElementAt(instances.get(1), 0);
        assertEquals(instances.get(1), dataset.get(0));
        assertEquals(instances.get(1).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(1).classValue(), dataset.classValue(0));

        assertEquals(instances.get(0), dataset.get(1));
        assertEquals(instances.get(0).noAttributes(), dataset.noAttributes());
        assertEquals(instances.get(0).classValue(), dataset.classValue(1));
    }

    @Test
    public void testRemove() {
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance instance = instances.get(0);
        assertTrue(dataset.remove(instance));
        assertTrue(dataset.size() < instances.size());
        assertFalse(dataset.contains(instance));
        assertNull(dataset.getClassValue(0));
    }

    @Test
    public void testContains() {
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance instance = instances.get(0);
        assertTrue(dataset.contains(instance));
        assertTrue(dataset.contains(instance, true));
        assertFalse(dataset.contains(instance, false));
        assertFalse(dataset.contains(new DenseInstance(new double[]{1.1, 1.1, 1.1, 1.1})));
    }

    @Test
    public void testClassValue() {
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals("Class1", dataset.classValue(0));
        assertEquals("Class2", dataset.classValue(1));
        assertNull(dataset.classValue(2));
    }

    @Test
    public void testClasses() {
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(2, dataset.classes().size());
        assertTrue(dataset.classes().containsAll(Arrays.asList("Class1", "Class2")));
        assertEquals(Arrays.asList("Class1", "Class2"), dataset.classes().stream().sorted().collect(Collectors.toList()));
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.noAttributes());

        dataset.add(new DenseInstance(new double[]{1.0, 1.0, 1.0}));
        assertEquals(3, dataset.noAttributes());

        dataset.add(new DenseInstance(new double[]{1.0, 1.0, 1.0}, "Class1"));
        assertEquals(3, dataset.noAttributes());

        dataset.add(new DenseInstance(new double[]{1.0, 1.1, 1.0, 1.1}));
        assertEquals(4, dataset.noAttributes());
    }

    @Test
    public void testGet() {
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(instances.get(0), dataset.get(0));
        assertEquals(instances.get(1), dataset.get(1));

        instances.set(0, new DenseInstance(new double[]{1.0, 1.0, 1.0, 1.0}, "Class1"));
        assertEquals(instances.get(0), dataset.get(0));
    }

    @Test
    public void testSize() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());

        dataset.add(instances.get(0));
        assertEquals(1, dataset.size());

        dataset.addAll(instances.subList(1, instances.size()));
        assertEquals(instances.size(), dataset.size());
    }

    @Test
    public void testIsEmpty() {
        DefaultDataset dataset = new DefaultDataset();
        assertTrue(dataset.isEmpty());

        dataset.add(instances.get(0));
        assertFalse(dataset.isEmpty());
    }

    @Test
    public void testSubList() {
        DefaultDataset dataset = new DefaultDataset(instances);
        List<Instance> sublist = dataset.subList(0, 1);
        assertEquals(instances.get(0), sublist.get(0));
        assertEquals(1, sublist.size());

        sublist = dataset.subList(1, 2);
        assertEquals(instances.get(1), sublist.get(0));
        assertEquals(1, sublist.size());

        sublist = dataset.subList(2, 3);
        assertNull(sublist.get(0));
        assertEquals(0, sublist.size());

        sublist = dataset.subList(2, 3);
        assertNull(sublist.get(0));
        assertEquals(0, sublist.size());
    }

    @Test
    public void testToArray() {
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance[] array = new Instance[instances.size()];
        array = dataset.toArray(array);
        assertArrayEquals(instances.toArray(new Instance[0]), array);

        Instance[] array2 = new Instance[instances.size() + 1];
        array = dataset.toArray(array2);
        assertArrayEquals(instances.toArray(new Instance[0]), array);
    }

    @Test
    public void testToArrayWithNull() {
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance[] array = dataset.toArray(null);
        assertArrayEquals(instances.toArray(new Instance[0]), array);
    }

    @Test
    public void testRemoveFarthest() {
        DefaultDataset dataset = new DefaultDataset(Arrays.asList(
                new DenseInstance(new double[]{1.0, 1.0, 1.0, 1.0}, "Class1"),
                new DenseInstance(new double[]{2.0, 2.0, 2.0, 2.0}, "Class2"),
                new DenseInstance(new double[]{3.0, 3.0, 3.0, 3.0}, "Class1")).toArray(new Instance[0]));

        Double maxDistance = Double.NaN;
        Instance maxDistanceInstance = null;
        for (Instance instance : dataset) {
            double distance = distanceMeasure.measure(instances.get(0), instance);
            if (!instances.get(0).equals(instance) && (Double.isNaN(maxDistance) || distance > maxDistance)) {
                maxDistance = distance;
                maxDistanceInstance = instance;
            }
        }
        assertEquals(maxDistanceInstance, dataset.removeFarthest(Collections.singletonMap(maxDistanceInstance, maxDistance)));

        assertEquals(instances.size() - 1, dataset.size());
        assertFalse(dataset.contains(maxDistanceInstance));
    }

    @Test
    public void testKNearest() {
        DefaultDataset dataset = new DefaultDataset(instances);
        Instance instance = instances.get(0);

        List<Instance> kNearest = dataset.kNearest(1, instance, distanceMeasure);
        assertEquals(1, kNearest.size());
        assertEquals(instance, kNearest.get(0));

        kNearest = dataset.kNearest(2, instance, distanceMeasure);
        assertTrue(kNearest.size() >= 2);
        assertEquals(instances.get(0), kNearest.get(0));
        assertEquals(instances.get(1), kNearest.get(1));

        kNearest = dataset.kNearest(3, instance, distanceMeasure);
        assertTrue(kNearest.size() >= 3);
        assertEquals(instances.get(0), kNearest.get(0));
        assertEquals(instances.get(1), kNearest.get(1));
        assertEquals(instances.get(2), kNearest.get(2));
    }

    @Test
    public void testFolds() {
        Random random = new Random();
        DefaultDataset dataset = new DefaultDataset(instances);
        Dataset[] folds = dataset.folds(5, random);

        for (Dataset fold : folds) {
            assertTrue(fold instanceof DefaultDataset);
            assertEquals(instances.size() / 5, ((DefaultDataset) fold).size());
        }

        for (Dataset fold : folds) {
            Instance instance = instances.get(0);
            List<Instance> kNearest = ((DefaultDataset) fold).kNearest(1, instance, distanceMeasure);
            assertFalse(kNearest.contains(instance));
            assertEquals(instances.size() / 5, kNearest.size());
        }
    }
}