package net.sf.javaml.core.test;

import junit.framework.TestCase;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoldTest extends TestCase {

    private static final int NUM_FOLDS = 5;
    private static final int NUM_INSTANCES = 10;
    private static final Object[] CLASSES = {"Class1", "Class2", "Class3"};

    @org.junit.Before
    public void setUp() {
        Random rg = new Random();
        Dataset parentDataset = new DefaultDataset();
        for (int i = 0; i < NUM_INSTANCES; ++i) {
            Instance instance = new DefaultInstance(Arrays.asList(new Double[]{i, Math.random(), Math.random()}));
            instance.setClassValue(CLASSES[i % CLASSES.length]);
            parentDataset.add(instance);
        }

        // Test with random indices
        int[] indices = new int[NUM_INSTANCES];
        for (int i = 0; i < NUM_INSTANCES; ++i) {
            indices[i] = i;
        }
        Collections.shuffle(Arrays.asList(indices), rg);

        Fold fold = new Fold(parentDataset, indices);
    }

    @org.junit.Test
    public void testFolds() {
        Dataset[] folds = fold.folds(NUM_FOLDS, new Random());
        assertEquals(NUM_FOLDS, folds.length);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        fold.add(new DefaultInstance(Arrays.asList(new Double[]{1.0, 2.0, 3.0}), CLASSES[0]));
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        fold.remove(0);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DefaultInstance(Arrays.asList(new Double[]{1.0, 2.0, 3.0}), CLASSES[0]));
        fold.addAll(instances);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        List<Instance> instances = new ArrayList<>();
        instances.add(new DefaultInstance(Arrays.asList(new Double[]{1.0, 2.0, 3.0}), CLASSES[0]));
        fold.addAll(5, instances);
    }

    @org.junit.Test
    public void testInstance() {
        Instance inst = fold.instance(0);
        assertNotNull(inst);
        assertEquals(fold.parent.classValue(0), inst.classValue());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Instance newInst = new DefaultInstance(Arrays.asList(new Double[]{1.0, 2.0, 3.0}), "NewClass");
        fold.set(0, newInst);
    }

    @org.junit.Test
    public void testSize() {
        assertEquals(NUM_INSTANCES, fold.size());
    }

    @org.junit.Test
    public void testGet() {
        Instance inst = fold.get(0);
        assertNotNull(inst);
        assertEquals(fold.parent.classValue(0), inst.classValue());
    }

    @org.junit.Test
    public void testContains() {
        Instance inst = fold.instance(0);
        assertTrue(fold.contains(inst));
    }

    @org.junit.Test
    public void testContainsAll() {
        Dataset parent = fold.parent;
        assertTrue(fold.containsAll(parent));
    }

    @org.junit.Test
    public void testKNearest() {
        DistanceMeasure dm = new EuclideanDistance();
        List<Instance> kNearest = fold.kNearest(3, fold.instance(0), dm);
        assertEquals(3, kNearest.size());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddIndex() {
        fold.add(0);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        fold.remove(0);
    }
}