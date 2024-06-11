package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(5, fold.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        fold.add(new DenseInstance(1.0));
    }

    @Test
    public void testGetInstance() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);
        Instance result = fold.instance(0);
        assertEquals(instance1, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        fold.folds(5, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(1.0));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        List<Instance> instances = new ArrayList<>();
        instances.add(new DenseInstance(1.0));
        fold.addAll(indices[0], instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsInstance() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);
        boolean result = fold.contains(instance1);
        assertTrue(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsInstances() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        Instance instance2 = new DenseInstance(2.0);
        parentDataset.add(instance2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);
        List<Instance> instances = Arrays.asList(instance1, instance2);
        boolean result = fold.containsAll(instances);
        assertTrue(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsEmpty() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parentDataset, indices);
        boolean result = fold.isEmpty();
        assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        Iterator<Instance> iterator = fold.iterator();
        iterator.hasNext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        ListIterator<Instance> listIterator = fold.listIterator();
        listIterator.hasNext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIteratorAtIndex() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataset, indices);
        ListIterator<Instance> listIterator = fold.listIterator(1);
        listIterator.hasNext();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);
        fold.remove(instance1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtIndex() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);
        Instance result = fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);
        List<Instance> instances = Arrays.asList(instance1);
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        Instance instance2 = new DenseInstance(2.0);
        parentDataset.add(instance2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);
        List<Instance> instances = Arrays.asList(instance1, instance2);
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0);
        parentDataset.add(instance1);
        int[] indices = {0};
        Fold fold = new Fold(parentDataset, indices);
        Instance newInstance = new DenseInstance(2.0);
        fold.set(0, newInstance);
    }

    @Test
    public void testClasses() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance1.setClassValue(1);
        parentDataset.add(instance1);
        Instance instance2 = new DenseInstance(2.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance2.setClassValue(0);
        parentDataset.add(instance2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);
        Set<Object> result = fold.classes();
        assertEquals(2, result.size());
        assertTrue(result.contains(0));
        assertTrue(result.contains(1));
    }

    @Test
    public void testNoAttributes() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance = new DenseInstance(1.0, new Attribute[]{new Attribute("Feature1")});
        parentDataset.add(instance);
        Fold fold = new Fold(parentDataset, new int[0]);
        assertEquals(1, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance1.setClassValue(1);
        parentDataset.add(instance1);
        Instance instance2 = new DenseInstance(2.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance2.setClassValue(0);
        parentDataset.add(instance2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(1, fold.classIndex(1));
        assertEquals(0, fold.classIndex(0));
    }

    @Test
    public void testClassValue() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance1.setClassValue(1);
        parentDataset.add(instance1);
        Instance instance2 = new DenseInstance(2.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance2.setClassValue(0);
        parentDataset.add(instance2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(1, fold.classValue(0).getClass().getSimpleName());
        assertEquals(0, fold.classValue(1).getClass().getSimpleName());
    }

    @Test
    public void testCopy() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(1.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance1.setClassValue(1);
        parentDataset.add(instance1);
        Instance instance2 = new DenseInstance(2.0, new Attribute[]{"Feature1", "Feature2", "Label"});
        instance2.setClassValue(0);
        parentDataset.add(instance2);
        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);
        Dataset copy = fold.copy();
        assertEquals(2, copy.size());
        Instance copyInstance1 = copy.instance(0);
        Instance originalInstance1 = fold.instance(0);
        assertEquals(originalInstance1.getClass(), copyInstance1.getClass());
        assertEquals(originalInstance1.getDatabase(), copyInstance1.getDatabase());
        assertEquals(originalInstance1.numValues(), copyInstance1.numValues());
        assertEquals(originalInstance1.value(0), copyInstance1.value(0));
        assertEquals(originalInstance1.value(1), copyInstance1.value(1));
        assertEquals(originalInstance1.value(2), copyInstance1.value(2));
        assertEquals(originalInstance1.classValue(), copyInstance1.classValue());

        Instance copyInstance2 = copy.instance(1);
        Instance originalInstance2 = fold.instance(1);
        assertEquals(originalInstance2.getClass(), copyInstance2.getClass());
        assertEquals(originalInstance2.getDatabase(), copyInstance2.getDatabase());
        assertEquals(originalInstance2.numValues(), copyInstance2.numValues());
        assertEquals(originalInstance2.value(0), copyInstance2.value(0));
        assertEquals(originalInstance2.value(1), copyInstance2.value(1));
        assertEquals(originalInstance2.value(2), copyInstance2.value(2));
        assertEquals(originalInstance2.classValue(), copyInstance2.classValue());
    }
}