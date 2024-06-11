package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FoldTest {

    private Dataset dataset;
    private Instance[] instances;

    @Before
    public void setup() {
        AttributeOffsetMap attributeMap = new AttributeOffsetMap();
        attributeMap.setClassIndex(14);
        instanceCreator = new ArrayInstanceCreator(15, attributeMap);
        dataset = new DefaultDataset(instanceCreator);
        for (int i = 0; i < 100; i++) {
            dataset.add(instanceCreator.generateInstance());
        }
        instances = dataset.toArray(new Instance[0]);
    }

    @Test
    public void testConstructor() {
        Dataset parent = new DefaultDataset(instanceCreator);
        int[] indices = new int[]{0, 1, 2, 3};
        Fold fold = new Fold(parent, indices);
        assertTrue(fold instanceof Fold);
        assertSame(parent, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Fold fold = new Fold(dataset, new int[0]);
        fold.add(instanceCreator.generateInstance());
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(dataset, new int[0]);
        assertSame(dataset.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(dataset, new int[0]);
        fold.folds(5, new Random());
    }

    @Test
    public void testGetInstance() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertSame(instances[5], fold.instance(0));
        assertSame(instances[7], fold.instance(1));
        assertSame(instances[10], fold.instance(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(dataset, new int[0]);
        fold.kNearest(5, instances[0], new EuclideanDistance());
    }

    @Test
    public void testAdd() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testAddAll() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testAddAllWithIndex() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testClear() {
        throw new UnsupportedOperationException("Method is not yet implemented");
    }

    @Test
    public void testContains() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        for (Instance instance : instances) {
            assertFalse(fold.contains(instance));
        }
        assertFalse(fold.contains(null));
        Instance instance = instances[5];
        assertTrue(fold.contains(instance));
    }

    @Test
    public void testContainsAll() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        List<Instance> sublist = Arrays.asList(instances[5], instances[7], instances[10]);
        assertFalse(fold.containsAll(Collections.emptyList()));
        assertFalse(fold.containsAll(Arrays.asList(instances[2])));
        assertTrue(fold.containsAll(sublist));
    }

    @Test
    public void testGet() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertSame(instances[5], fold.get(0));
        assertSame(instances[7], fold.get(1));
        assertSame(instances[10], fold.get(2));
    }

    @Test
    public void testIndexOf() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        for (Instance instance : instances) {
            if (instance.equals(instances[5])) {
                int index = fold.indexOf(instance);
                assertEquals(0, index);
                break;
            }
        }
        assertNull(fold.indexOf(null));
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(dataset, new int[0]);
        assertFalse(fold.isEmpty());
        Fold emptyFold = new Fold(new DefaultDataset(instanceCreator), new int[0]);
        assertTrue(emptyFold.isEmpty());
    }

    @Test
    public void testIterator() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        List<Instance> list = new ArrayList<>();
        for (Instance instance : fold) {
            list.add(instance);
        }
        Collections.sort(list);
        assertArrayEquals(new Instance[]{instances[5], instances[7], instances[10]}, list.toArray());
    }

    @Test
    public void testLastIndexOf() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        for (int i = 0; i < instances.length; i++) {
            if (instances[i].equals(instances[5])) {
                int index = fold.lastIndexOf(instances[i]);
                assertEquals(0, index);
                break;
            }
        }
        assertNull(fold.lastIndexOf(null));
    }

    @Test
    public void testListIterator() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        ListIterator<Instance> listIterator = fold.listIterator();
        assertNull(listIterator.next());
        assertTrue(listIterator.hasNext());
        listIterator.next();
        assertSame(instances[5], listIterator.next());
        assertSame(instances[7], listIterator.next());
        assertSame(instances[10], listIterator.next());
        listIterator.remove();
        assertArrayEquals(new Instance[]{instances[6], instances[8], instances[11]}, fold.toArray(new Instance[0]));
    }

    @Test
    public void testListIteratorWithIndex() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        ListIterator<Instance> listIterator = fold.listIterator(1);
        assertNull(listIterator.next());
        assertSame(instances[6], listIterator.next());
        assertSame(instances[8], listIterator.next());
        assertSame(instances[11], listIterator.next());
        listIterator.remove();
        assertArrayEquals(new Instance[]{instances[5], instances[7]}, fold.toArray(new Instance[0]));
    }

    @Test
    public void testRemove() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRemoveInstance() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRemoveAll() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testRetainAll() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testSet() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertEquals(3, fold.size());
    }

    @Test
    public void testSubList() {
        throw new UnsupportedOperationException("You cannot do this on a fold.");
    }

    @Test
    public void testToArray() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertArrayEquals(new Instance[]{instances[5], instances[7], instances[10]}, fold.toArray(new Instance[0]));
    }

    @Test
    public void testToArrayWithArray() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertArrayEquals(new Instance[]{instances[5], instances[7], instances[10]}, fold.toArray(new Instance[3]));
    }

    @Test
    public void testNoAttributes() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertEquals(15, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        assertEquals(14, fold.classIndex(instances[5].classValue()));
    }

    @Test
    public void testCopy() {
        Fold fold = new Fold(dataset, new int[]{5, 7, 10});
        Dataset copy = fold.copy();
        assertTrue(copy instanceof Fold);
        Fold copiedFold = (Fold) copy;
        assertArrayEquals(fold.indices, copiedFold.indices);
        assertSame(fold.parent, copiedFold.parent);
        assertEquals(fold.size(), copiedFold.size());
    }
}