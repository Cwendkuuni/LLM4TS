package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

    private DefaultDataset ds;
    private Instance inst1, inst2, inst3;
    private Set<Object> classes;
    private Random random;

    @Before
    public void setUp() {
        ds = new DefaultDataset();
        inst1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, "class1");
        inst2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, "class2");
        inst3 = new DenseInstance(new double[] { 7.0, 8.0, 9.0 }, null);
        classes = new HashSet<Object>() {{
            add("class1");
            add("class2");
        }};
        random = new Random();
    }

    @Test
    public void testEmptyConstructor() {
        DefaultDataset emptyDS = new DefaultDataset();
        assertTrue(emptyDS.isEmpty());
        assertTrue(emptyDS.classes().isEmpty());
    }

    @Test
    public void testCollectionConstructor() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2));
        assertEquals(2, ds.size());
        assertEquals(classes, ds.classes());
        assertEquals(inst1, ds.instance(0));
        assertEquals(inst2, ds.instance(1));
    }

    @Test
    public void testAddAll() {
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(Arrays.asList(inst1, inst2, inst3));
        assertEquals(3, ds.size());
        assertEquals(classes, ds.classes());
        assertEquals(inst1, ds.instance(0));
        assertEquals(inst2, ds.instance(1));
        assertEquals(inst3, ds.instance(2));
    }

    @Test
    public void testAdd() {
        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        assertEquals(1, ds.size());
        assertEquals(classes, ds.classes());
        assertEquals(inst1, ds.instance(0));
        ds.add(inst2);
        assertEquals(2, ds.size());
        assertEquals(classes, ds.classes());
        assertEquals(inst1, ds.instance(0));
        assertEquals(inst2, ds.instance(1));
    }

    @Test
    public void testClear() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        ds.clear();
        assertTrue(ds.isEmpty());
        assertTrue(ds.classes().isEmpty());
    }

    @Test
    public void testAddAllIndex() {
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(Arrays.asList(inst1, inst2, inst3));
        ds.addAll(1, Arrays.asList(inst2, inst3));
        assertEquals(5, ds.size());
        assertEquals(inst2, ds.instance(1));
        assertEquals(inst3, ds.instance(2));
        assertEquals(inst3, ds.instance(3));
        assertEquals(inst3, ds.instance(4));
    }

    @Test
    public void testAddElementAt() {
        DefaultDataset ds = new DefaultDataset();
        ds.addAll(Arrays.asList(inst1, inst2, inst3));
        ds.addElementAt(inst2, 1);
        assertEquals(4, ds.size());
        assertEquals(inst2, ds.instance(1));
    }

    @Test
    public void testSetElementAt() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        ds.setElementAt(inst3, 1);
        assertEquals(inst3, ds.instance(1));
    }

    @Test
    public void testGet() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        assertEquals(inst1, ds.instance(0));
        assertEquals(inst2, ds.instance(1));
        assertEquals(inst3, ds.instance(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        ds.instance(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        DefaultDataset ds = new DefaultDataset();
        ds.instance(10);
    }

    @Test
    public void testClasses() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        assertEquals(classes, ds.classes());
        assertTrue(ds.classes().contains("class1"));
        assertTrue(ds.classes().contains("class2"));
    }

    @Test
    public void testAddInstanceWithNullClass() {
        DefaultDataset ds = new DefaultDataset();
        ds.add(inst3);
        assertEquals(1, ds.size());
        assertNull(ds.instance(0).classValue());
    }

    @Test
    public void testAddInstanceWithDuplicateClass() {
        DefaultDataset ds = new DefaultDataset();
        ds.add(inst1);
        ds.add(inst1);
        assertEquals(2, ds.size());
        assertEquals(classes, ds.classes());
        assertEquals(inst1, ds.instance(0));
        assertEquals(inst1, ds.instance(1));
    }

    @Test
    public void testMaxAttributes() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        assertEquals(inst3.noAttributes(), ds.maxAttributes());
    }

    @Test
    public void testKNearest() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        Instance instQuery = new DenseInstance(new double[] { 5.0, 5.0, 5.0 });
        DistanceMeasure dm = new EuclideanDistance();
        Set<Instance> kNearest = ds.kNearest(2, instQuery, dm);
        List<Instance> expected = new ArrayList<Instance>() {{
            add(inst1);
            add(inst2);
        }};
        assertArrayEquals(expected.toArray(new Instance[0]), kNearest.toArray(new Instance[0]));
    }

    @Test
    public void testFolds() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        Dataset[] folds = ds.folds(3, random);
        assertEquals(3, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
            assertTrue(fold.size() > 0);
            assertTrue(fold.classes().size() > 0);
        }
    }

    @Test
    public void testNoAttributes() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        assertEquals(inst1.noAttributes(), ds.noAttributes());
    }

    @Test
    public void testClassIndex() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        assertEquals(0, ds.classIndex("class1"));
        assertEquals(1, ds.classIndex("class2"));
        assertNull(ds.classIndex(null));
    }

    @Test
    public void testClassValue() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        assertEquals("class1", ds.classValue(0));
        assertEquals("class2", ds.classValue(1));
        assertNull(ds.classValue(2));
    }

    @Test
    public void testCopy() {
        DefaultDataset ds = new DefaultDataset(Arrays.asList(inst1, inst2, inst3));
        DefaultDataset copy = ds.copy();
        assertEquals(ds.size(), copy.size());
        for (int i = 0; i < ds.size(); ++i) {
            Instance dsInstance = ds.instance(i);
            Instance copyInstance = copy.instance(i);
            assertSame(dsInstance.classValue(), copyInstance.classValue());
            assertArrayEquals(dsInstance.toArray(), copyInstance.toArray());
        }
    }

}