package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.FoldIterator;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.instancevalue.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoldTest {

    @Test
    public void testConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertNotNull(fold);
        assertEquals(parent, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals(3, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance i = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0));
        fold.add(i);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0)),
                InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(2.0)));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0)),
                InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(2.0)));
        fold.addAll(1, instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstanceAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.remove(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.removeAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0)),
                InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(2.0)));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstanceAtIndex() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance newInstance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0));
        fold.set(1, newInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexOfInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance instance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0));
        fold.indexOf(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance instance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0));
        fold.contains(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAllInstances() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0)),
                InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(2.0)));
        fold.containsAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        int fromIndex = 1;
        int toIndex = 2;
        fold.subList(fromIndex, toIndex);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object[] array = fold.toArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArrayWithArray() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Object[] array = new Object[3];
        fold.toArray(array);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.instance(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Dataset[] folds = fold.folds(3, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance instance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0));
        int k = 1;
        DistanceMeasure dm = new EuclideanDistance();
        fold.kNearest(k, instance, dm);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        fold.classes();
    }

    @Test
    public void testFoldIteratorHasNext() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testFoldIteratorHasPrevious() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.iterator();
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasPrevious());
    }

    @Test
    public void testFoldIteratorNext() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.iterator();
        Instance expectedInstance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(0.0));
        Instance actualInstance = iterator.next();
        assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testFoldIteratorPrevious() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.iterator();
        iterator.next();
        iterator.next();
        iterator.previous();
        Instance expectedInstance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(1.0));
        Instance actualInstance = iterator.previous();
        assertEquals(expectedInstance, actualInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorRemove() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.iterator();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldIteratorAdd() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        FoldIterator iterator = fold.iterator();
        Instance instance = InstanceFactory.createInstance(new Attribute[]{}, new AttributeValue[]{}, new DoubleAttributeValue(3.0));
        iterator.add(instance);
    }
}