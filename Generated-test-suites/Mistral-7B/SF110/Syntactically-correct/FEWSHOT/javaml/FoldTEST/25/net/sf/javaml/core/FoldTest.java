package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(null);
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllIndex() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(null);
        fold.addAll(1, instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        List<Instance> instances = Arrays.asList(null);
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        fold.set(0, null);
    }

    @Test
    public void testSize() {
        Dataset dataset = Collections.emptyList();
        Fold fold = new Fold(dataset, new int[0]);
        assertEquals(0, fold.size());
    }

    @Test
    public void testSection() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        assertEquals(8, fold.size());
        for (int i = 0; i < 8; i++) {
            Instance inst = fold.instance(i);
            Instance origInst = dataset.instance(fold.indices[i]);
            assertEquals(inst, origInst);
        }
    }

    @Test
    public void testInstance() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        for (int i = 0; i < 8; i++) {
            Instance inst = fold.instance(i);
            assertSame(inst, dataset.instance(fold.indices[i]));
        }
    }

    @Test
    public void testClasses() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        Set<Object> classes = fold.classes();
        assertEquals(dataset.classAttribute().values(), classes);
    }

    @Test
    public void testIterator() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        List<Instance> list = new ArrayList<>();
        for (Instance inst : fold) {
            list.add(inst);
        }
        assertEquals(8, list.size());
        for (int i = 0; i < 8; i++) {
            Instance inst = fold.instance(i);
            assertEquals(inst, list.get(i));
        }
    }

    @Test
    public void testListIterator() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        ListIterator<Instance> iter = fold.listIterator();
        List<Instance> list = new ArrayList<>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        assertEquals(8, list.size());
        for (int i = 0; i < 8; i++) {
            Instance inst = fold.instance(i);
            assertEquals(inst, list.get(i));
        }
    }

    @Test
    public void testListIteratorIndex() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        ListIterator<Instance> iter = fold.listIterator(1);
        List<Instance> list = new ArrayList<>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        assertEquals(7, list.size());
        for (int i = 0; i < 7; i++) {
            Instance inst = fold.instance(i + 1);
            assertEquals(inst, list.get(i));
        }
    }

    @Test
    public void testKNearest() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[0]);
        fold.folds(5, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCopy() {
        Dataset dataset = new Instances(new FileReader("test.arff"));
        Fold fold = new Fold(dataset, new int[0]);
        fold.copy();
    }
}