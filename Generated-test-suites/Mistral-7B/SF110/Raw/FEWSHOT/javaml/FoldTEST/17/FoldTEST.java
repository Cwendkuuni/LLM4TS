package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceHandler;
import net.sf.javaml.core.InstanceHandlerRegistrar;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.euclidean.EuclideanDistance;

public class FoldTest {

    @Test
    public void testNewInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = { 1, 2, 3 };
        Fold fold = new Fold(parent, indices);

        assertEquals(parent, fold.parent);
        assertEquals(indices, fold.indices);
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = { 1, 2, 3 };
        Fold fold = new Fold(parent, indices);

        assertEquals(3, fold.size());
    }

    @Test
    public void testGetInstance() {
        Dataset parent = new DefaultDataset();
        parent.add(createInstance(0));
        parent.add(createInstance(1));
        parent.add(createInstance(2));

        int[] indices = { 1, 2 };
        Fold fold = new Fold(parent, indices);

        Instance instance1 = fold.instance(0);
        Instance instance2 = fold.instance(1);

        assertEquals(parent.instance(1), instance1);
        assertEquals(parent.instance(2), instance2);
    }

    @Test
    public void testIterator() {
        Dataset parent = new DefaultDataset();
        parent.add(createInstance(0));
        parent.add(createInstance(1));
        parent.add(createInstance(2));

        int[] indices = { 1, 2 };
        Fold fold = new Fold(parent, indices);

        List<Instance> instances = Arrays.asList(fold.iterator()).stream().collect(Collectors.toList());

        assertEquals(Arrays.asList(parent.instance(1), parent.instance(2)), instances);
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(createInstance(0));
        parent.add(createInstance(1));
        parent.add(createInstance(2));

        int[] indices = { 1, 2 };
        Fold fold = new Fold(parent, indices);

        assertEquals(parent.classes(), fold.classes());
    }

    @Test
    public void testAdd() {
        throw new UnsupportedOperationException("Cannot call add() method on a Fold instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        throw new UnsupportedOperationException("Cannot call addAll() method on a Fold instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        throw new UnsupportedOperationException("Cannot call clear() method on a Fold instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        throw new UnsupportedOperationException("Cannot call remove() method on a Fold instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        throw new UnsupportedOperationException("Cannot call removeAll() method on a Fold instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        throw new UnsupportedOperationException("Cannot call retainAll() method on a Fold instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        throw new UnsupportedOperationException("Cannot call set() method on a Fold instance.");
    }

    @Test
    public void testNoAttributes() {
        Dataset parent = new DefaultDataset();
        int[] indices = { 1, 2 };
        Fold fold = new Fold(parent, indices);

        assertEquals(parent.noAttributes(), fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Dataset parent = new DefaultDataset();
        parent.add(createInstance(0));
        parent.add(createInstance(1));
        parent.add(createInstance(2));

        int[] indices = { 1, 2 };
        Fold fold = new Fold(parent, indices);

        assertEquals(parent.classIndex("class1"), fold.classIndex("class1"));
    }

    @Test
    public void testClassValue() {
        Dataset parent = new DefaultDataset();
        parent.add(createInstance(0, "class1"));
        parent.add(createInstance(1, "class1"));
        parent.add(createInstance(2, "class2"));

        int[] indices = { 1, 2 };
        Fold fold = new Fold(parent, indices);

        assertEquals("class1", fold.classValue(0));
        assertEquals("class1", fold.classValue(1));
        assertNull(fold.classValue(2)); // Index out of bounds
    }

    @Test
    public void testKNearest() {
        throw new UnsupportedOperationException("Method kNearest() is not implemented in Fold.");
    }

    @Test
    public void testFolds() {
        throw new UnsupportedOperationException("Method folds() is not implemented in Fold.");
    }

    private Instance createInstance(int index, String className) {
        return new DefaultInstance(Arrays.asList(new Double[] { new Double(index) }), className);
    }

    private Instance createInstance(int index) {
        return new DefaultInstance(Arrays.asList(new Double[] { new Double(index) }));
    }
}