package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.CosineDistanceMeasure;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.add(mock(Instance.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.addAll(Arrays.asList(mock(Instance.class), mock(Instance.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAtIndex() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.addAll(Arrays.asList(mock(Instance.class), mock(Instance.class)), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.remove(mock(Instance.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtIndex() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.removeAll(Collections.singletonList(mock(Instance.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllInstances() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.retainAll(Collections.singletonList(mock(Instance.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstanceAtIndex() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.set(0, mock(Instance.class));
    }

    @Test
    public void testSize() {
        Fold fold = new Fold(mock(Dataset.class), new int[3]);
        assertEquals(3, fold.size());
    }

    @Test
    public void testIsEmpty() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        assertTrue(fold.isEmpty());
        fold = new Fold(mock(Dataset.class), new int[3]);
        assertFalse(fold.isEmpty());
    }

    @Test
    public void testClasses() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        assertEquals(fold.parent.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.folds(1, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstance() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.instance(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        Fold fold = new Fold(mock(Dataset.class), new int[0]);
        fold.kNearest(1, mock(Instance.class), mock(DistanceMeasure.class));
    }

    @Test
    public void testConstructor() {
        Dataset parent = mock(Dataset.class);
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        assertEquals(parent, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test
    public void testIterator() {
        Dataset parent = mock(Dataset.class);
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        List<Instance> expectedInstances = Arrays.asList(parent.instance(indices[0]), parent.instance(indices[1]), parent.instance(indices[2]));
        assertEquals(expectedInstances, Arrays.asList(fold.iterator().next(), fold.iterator().next(), fold.iterator().next()));
    }

    @Test
    public void testFoldIterator() {
        Dataset parent = mock(Dataset.class);
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        Fold.FoldIterator iterator = fold.iterator();
        Fold.FoldIterator expectedIterator = new Fold.FoldIterator(fold, 0) {
            @Override
            public Instance next() {
                return parent.instance(indices[0]);
            }
        };
        assertEquals(expectedIterator.next(), iterator.next());
        assertEquals(expectedIterator.next(), iterator.next());
        assertEquals(expectedIterator.next(), iterator.next());
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testFoldIteratorAtIndex() {
        Dataset parent = mock(Dataset.class);
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        Fold.FoldIterator iterator = fold.iterator(1);
        Fold.FoldIterator expectedIterator = new Fold.FoldIterator(fold, 1) {
            @Override
            public Instance next() {
                return parent.instance(indices[1]);
            }
        };
        assertEquals(expectedIterator.next(), iterator.next());
        assertEquals(expectedIterator.next(), iterator.next());
        Assert.assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testFoldsWithRandom() {
        Dataset parent = mock(Dataset.class);
        int numberOfFolds = 5;
        Random random = new Random();
        Dataset[] folds = fold.folds(numberOfFolds, random);
        assertEquals(numberOfFolds, folds.length);
        for (Dataset fold : folds) {
            assertTrue(fold instanceof Fold);
            assertEquals(parent, fold.parent);
            assertNotSame(fold.indices, fold.parent.indices);
        }
    }

    @Test
    public void testCopy() {
        Dataset parent = mock(Dataset.class);
        int[] indices = new int[] { 0, 1, 2 };
        Fold fold = new Fold(parent, indices);
        Dataset copy = fold.copy();
        assertEquals(parent, copy.parent);
        assertArrayEquals(indices, copy.indices);
    }
}