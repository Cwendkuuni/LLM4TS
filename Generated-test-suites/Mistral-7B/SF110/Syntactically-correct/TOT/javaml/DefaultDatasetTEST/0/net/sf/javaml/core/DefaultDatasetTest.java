package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {
    @Test
    public void testAddAll() {
        // Given
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0)));
        instances.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, new Double(2.0)));

        DefaultDataset dataset = new DefaultDataset();

        // When
        dataset.addAll(instances);

        // Then
        assertEquals(instances.size(), dataset.size());
        for (Instance instance : instances) {
            Instance instanceInDataset = dataset.instance(dataset.indexOf(instance));
            assertEquals(instance, instanceInDataset);
        }
    }

    @Test
    public void testDefaultDatasetConstructionWithCollection() {
        // Given
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0)));
        instances.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, new Double(2.0)));

        // When
        DefaultDataset dataset = new DefaultDataset(instances);

        // Then
        assertEquals(instances.size(), dataset.size());
        for (Instance instance : instances) {
            Instance instanceInDataset = dataset.instance(dataset.indexOf(instance));
            assertEquals(instance, instanceInDataset);
        }
    }

    @Test
    public void testAdd() {
        // Given
        DefaultDataset dataset = new DefaultDataset();
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0));

        // When
        dataset.add(instance);

        // Then
        assertEquals(1, dataset.size());
        Instance addedInstance = dataset.instance(0);
        assertEquals(instance, addedInstance);
    }

    @Test
    public void testKNearestAndClasses() {
        // Given
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0)));
        dataset.add(new DenseInstance(new double[]{1.1, 2.1, 3.1}, new Double(2.0)));
        dataset.add(new DenseInstance(new double[]{1.2, 2.2, 3.2}, new Double(1.0)));
        dataset.add(new DenseInstance(new double[]{1.3, 2.3, 3.3}, new Double(2.0)));

        // When - kNearest
        Instance targetInstance = new DenseInstance(new double[]{1.15, 2.15, 3.15}, new Double(1.0));
        SortedSet<Instance> kNearestInstances = dataset.kNearest(3, targetInstance, new EuclideanDistanceMeasure());

        // Then - kNearest
        assertEquals(3, kNearestInstances.size());
        Instance nearestInstance1 = kNearestInstances.first();
        Instance nearestInstance2 = kNearestInstances.headSet(nearestInstance1).next();
        Instance nearestInstance3 = kNearestInstances.headSet(nearestInstance2).next();
        assertTrue(new EuclideanDistanceMeasure().measure(targetInstance, nearestInstance1) <= 0.01);
        assertTrue(new EuclideanDistanceMeasure().measure(targetInstance, nearestInstance2) <= 0.01);
        assertTrue(new EuclideanDistanceMeasure().measure(targetInstance, nearestInstance3) <= 0.01);

        // When - classes
        SortedSet<Object> classes = dataset.classes();
        assertEquals(2, classes.size());
        assertTrue(classes.contains(new Double(1.0)));
        assertTrue(classes.contains(new Double(2.0)));
    }
}
//