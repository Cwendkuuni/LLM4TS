package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.euclidean.EuclideanDistance;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.InstanceLoader;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.instance.nominal.ArrayRealAttributeVector;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Fold fold = new Fold(createInstances(2), new int[]{0});
        fold.add(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Fold fold = new Fold(createInstances(2), new int[]{0});
        fold.addAll(Arrays.asList(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0), InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllAt() {
        Fold fold = new Fold(createInstances(2), new int[]{0});
        fold.addAll(1, Arrays.asList(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0), InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(createInstances(2), new int[]{0});
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        fold.remove(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        fold.retainAll(Arrays.asList(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Fold fold = new Fold(createInstances(2), new int[]{0});
        fold.set(0, InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1));
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        Set<Object> expected = new HashSet<>(Arrays.asList("class1", "class2"));
        Set<Object> actual = fold.classes();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(createInstances(2), new int[]{0});
        fold.folds(3, new Random());
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        Instance expected = InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0);
        Instance actual = fold.instance(0);
        assertEquals(expected, actual);

        expected = InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1);
        actual = fold.instance(1);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstance() {
        Fold fold = new Fold(null, new int[]{0});
        fold.instance(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInstanceInvalidIndex() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        fold.instance(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInstanceInvalidIndex2() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        fold.instance(2);
    }

    @Test
    public void testInstanceCount() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        int expected = 2;
        int actual = fold.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testKNearest() {
        Dataset dataset = createInstances(10);
        DistanceMeasure distance = new EuclideanDistance();
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        Instance instance = InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 10);
        List<Instance> expected = Arrays.asList(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0), InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1));
        List<Instance> actual = fold.kNearest(2, instance, distance);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearestUnsupported() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        fold.kNearest(2, InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0), new EuclideanDistance());
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        int expected = 2;
        int actual = fold.noAttributes();
        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        int expected = 2;
        int actual = fold.size();
        assertEquals(expected, actual);
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        Object[] expected = {InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0), InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1)};
        Object[] actual = fold.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToArrayWrapped() {
        Fold fold = new Fold(createInstances(2), new int[]{0, 1});
        Instance[] expected = {InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 0), InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), 1)};
        Instance[] actual = fold.toArray(new Instance[0]);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopy() {
        Dataset dataset = createInstances(2);
        Fold original = new Fold(dataset, new int[]{0, 1});
        Fold copy = original.copy();
        Dataset originalDataset = original.parent;
        Dataset copyDataset = copy.parent;
        int originalSize = original.size();
        int copySize = copy.size();
        for (int i = 0; i < originalSize; ++i) {
            Instance originalInstance = original.instance(i);
            Instance copyInstance = copy.instance(i);
            assertEquals(originalInstance, copyInstance);
        }
        assertNotSame(originalDataset, copyDataset);
    }

    private Dataset createInstances(int numberOfInstances) {
        List<Instance> instances = new ArrayList<Instance>();
        for (int i = 0; i < numberOfInstances; ++i) {
            instances.add(InstanceFactory.create(new ArrayRealAttributeVector(2.0, 3.0), i));
        }
        return new Instances("testInstances", new String[]{"attribute1", "attribute2"}, instances.toArray(new Instance[0]));
    }
}