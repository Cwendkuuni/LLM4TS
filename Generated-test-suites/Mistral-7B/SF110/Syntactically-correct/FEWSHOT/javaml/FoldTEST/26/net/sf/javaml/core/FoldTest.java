package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class FoldTest {

    @Test
    public void testSize() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertEquals(0, fold.size());

        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        dataset.add(instance1);
        dataset.add(instance2);

        int[] indices = {0, 1};
        Fold fold1 = new Fold(dataset, indices);
        assertEquals(2, fold1.size());

        Instance testInstance = fold1.instance(0);
        assertEquals(instance1, testInstance);
    }

    @Test
    public void testGet() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertNull(fold.instance(0));

        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        dataset.add(instance1);
        dataset.add(instance2);

        int[] indices = {0, 1};
        Fold fold1 = new Fold(dataset, indices);
        assertEquals(instance1, fold1.instance(0));
        assertEquals(instance2, fold1.instance(1));
    }

    @Test
    public void testIterator() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertFalse(fold.hasNext());

        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        dataset.add(instance1);
        dataset.add(instance2);

        int[] indices = {0, 1};
        Fold fold1 = new Fold(dataset, indices);
        ListIterator<Instance> iterator = fold1.listIterator();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());
        Instance testInstance = iterator.next();
        assertEquals(instance1, testInstance);

        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSubList() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertNull(fold.subList(0, 1));

        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        Instance instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0});
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        int[] indices = {1, 2};
        Fold fold1 = new Fold(dataset, indices);
        List<Instance> subList = fold1.subList(0, 2);
        assertNotNull(subList);
        assertEquals(2, subList.size());
        assertEquals(instance2, subList.get(0));
        assertEquals(instance3, subList.get(1));

        indices = new int[1];
        try {
            fold1.subList(0, 2);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testToArray() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        Object[] array = fold.toArray();
        assertEquals(0, array.length);

        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        Instance instance3 = new DenseInstance(new double[]{7.0, 8.0, 9.0});
        dataset.add(instance1);
        dataset.add(instance2);
        dataset.add(instance3);

        int[] indices = {1, 2};
        Fold fold1 = new Fold(dataset, indices);
        Object[] array1 = fold1.toArray();
        assertEquals(2, array1.length);

        Instance[] expectedArray = new Instance[2];
        expectedArray[0] = instance2;
        expectedArray[1] = instance3;
        assertArrayEquals(expectedArray, array1);

        // Test toArray with provided array
        Instance[] array2 = new Instance[3];
        Fold fold2 = new Fold(dataset, indices);
        fold2.toArray(array2);
        assertArrayEquals(expectedArray, array2, 0.0);
    }

    @Test
    public void testToArrayWrongType() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        try {
            fold.toArray(new String[1]);
            fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testCopy() {
        Dataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        dataset.add(instance1);
        dataset.add(instance2);

        int[] indices = {0, 1};
        Fold fold = new Fold(dataset, indices);
        Dataset copy = fold.copy();

        Instance testInstance1 = copy.instance(0);
        assertEquals(instance1, testInstance1);
        Instance testInstance2 = copy.instance(1);
        assertEquals(instance2, testInstance2);

        Dataset originalDataset = new DefaultDataset();
        for (Instance i : copy) {
            originalDataset.add(i.copy());
        }

        Dataset testDataset = fold.copy();
        for (Instance i : testDataset) {
            Instance testInstance = testDataset.instance(testDataset.indexOf(i));
            claimedInstance:
            for (Instance originalInstance : originalDataset) {
                if (testInstance.equals(originalInstance)) {
                    break claimedInstance;
                }
            }
            fail("Test dataset does not contain the same instances as the original dataset.");
        }
    }
}