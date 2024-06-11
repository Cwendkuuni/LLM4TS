package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FoldTest {

    private Dataset mockParentDataset;
    private Fold fold;
    private int[] indices = {0, 1, 2, 3, 4};

    @Before
    public void setup() {
        mockParentDataset = Mockito.mock(Dataset.class);
        fold = new Fold(mockParentDataset, indices);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fold);
        assertEquals(mockParentDataset, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        fold.add(Mockito.mock(Instance.class));
    }

    @Test
    public void testClasses() {
        when(mockParentDataset.classes()).thenReturn(Arrays.asList("class1", "class2"));
        assertEquals(mockParentDataset.classes(), fold.classes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        fold.folds(1, mock(Random.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        List<Instance> instances = Arrays.asList(Mockito.mock(Instance.class), Mockito.mock(Instance.class), Mockito.mock(Instance.class));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndex() {
        List<Instance> instances = Arrays.asList(Mockito.mock(Instance.class), Mockito.mock(Instance.class), Mockito.mock(Instance.class));
        fold.addAll(1, instances);
    }

    @Test
    public void testGetInstance() {
        Instance instance = Mockito.mock(Instance.class);
        when(mockParentDataset.instance(0)).thenReturn(instance);
        assertEquals(instance, fold.instance(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Instance instance = Mockito.mock(Instance.class);
        fold.set(0, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        List<Instance> instances = Arrays.asList(Mockito.mock(Instance.class), Mockito.mock(Instance.class), Mockito.mock(Instance.class));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        List<Instance> instances = Arrays.asList(Mockito.mock(Instance.class), Mockito.mock(Instance.class), Mockito.mock(Instance.class));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test
    public void testSize() {
        assertEquals(5, fold.size());
    }

    @Test
    public void testIsEmpty() {
        fold = new Fold(mockParentDataset, new int[0]);
        assertTrue(fold.isEmpty());
    }

    @Test
    public void testIterator() {
        Instance instance = Mockito.mock(Instance.class);
        when(mockParentDataset.instance(0)).thenReturn(instance);
        when(mockParentDataset.instance(1)).thenReturn(instance);
        when(mockParentDataset.instance(2)).thenReturn(instance);
        when(mockParentDataset.instance(3)).thenReturn(instance);
        when(mockParentDataset.instance(4)).thenReturn(instance);

        ListIterator<Instance> listIterator = fold.listIterator();
        List<Instance> expectedList = Arrays.asList(instance, instance, instance, instance, instance);

        assertEquals(expectedList, Arrays.asList(listIterator.next(), listIterator.next(), listIterator.next(), listIterator.next(), listIterator.next()));
        assertFalse(listIterator.hasNext());
    }

    @Test
    public void testListIteratorWithIndex() {
        Instance instance = Mockito.mock(Instance.class);
        when(mockParentDataset.instance(0)).thenReturn(instance);
        when(mockParentDataset.instance(1)).thenReturn(instance);
        when(mockParentDataset.instance(2)).thenReturn(instance);
        when(mockParentDataset.instance(3)).thenReturn(instance);
        when(mockParentDataset.instance(4)).thenReturn(instance);

        ListIterator<Instance> listIterator = fold.listIterator(1);
        List<Instance> expectedList = Arrays.asList(instance, instance, instance, instance, instance);

        assertEquals(expectedList.get(1), listIterator.next());
        assertEquals(expectedList.get(2), listIterator.next());
        assertEquals(expectedList.get(3), listIterator.next());
        assertEquals(expectedList.get(4), listIterator.next());
        assertFalse(listIterator.hasNext());
    }

    @Test
    public void testToArray() {
        Instance instance = Mockito.mock(Instance.class);
        when(mockParentDataset.instance(0)).thenReturn(instance);
        when(mockParentDataset.instance(1)).thenReturn(instance);
        when(mockParentDataset.instance(2)).thenReturn(instance);
        when(mockParentDataset.instance(3)).thenReturn(instance);
        when(mockParentDataset.instance(4)).thenReturn(instance);

        Object[] expectedArray = new Object[5];
        for (int i = 0; i < 5; i++) {
            expectedArray[i] = instance;
        }

        assertArrayEquals(expectedArray, fold.toArray());
    }

    @Test
    public void testToArrayWithArray() {
        Instance instance = Mockito.mock(Instance.class);
        when(mockParentDataset.instance(0)).thenReturn(instance);
        when(mockParentDataset.instance(1)).thenReturn(instance);
        when(mockParentDataset.instance(2)).thenReturn(instance);
        when(mockParentDataset.instance(3)).thenReturn(instance);
        when(mockParentDataset.instance(4)).thenReturn(instance);

        Instance[] expectedArray = new Instance[5];
        for (int i = 0; i < 5; i++) {
            expectedArray[i] = instance;
        }

        Instance[] array = new Instance[5];
        fold.toArray(array);

        assertArrayEquals(expectedArray, array);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, fold.noAttributes());
    }

    @Test
    public void testClassIndex() {
        assertEquals(-1, fold.classIndex("nonExistentClass"));
    }

    @Test
    public void testClassValue() {
        when(mockParentDataset.classValue(0)).thenReturn("class1");
        assertEquals("class1", fold.classValue(0));
    }

    @Test
    public void testCopy() {
        Dataset copiedFold = fold.copy();
        assertEquals(fold.parent, copiedFold.parent);
        assertArrayEquals(fold.indices, copiedFold.indices);
    }
}