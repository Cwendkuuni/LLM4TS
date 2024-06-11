
package net.sf.javaml.core.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.Test;

public class FoldTest {
    @Test
    public void testFoldConstructor() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataSet, indices);
        assertEquals(parentDataSet, fold.getParent());
        assertArrayEquals(indices, fold.getIndices());
    }

    @Test
    public void testGetInstance() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        Instance instance1 = fold.getInstance(0);
        Instance instance2 = fold.getInstance(1);

        assertEquals(parentDataSet.instance(0), instance1);
        assertEquals(parentDataSet.instance(1), instance2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFoldConstructor() {
        Dataset parentDataSet = new DefaultDataset();
        Fold fold = new Fold(parentDataSet, new int[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInstanceWithInvalidIndex() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        fold.getInstance(-1);
    }

    @Test
    public void testGetParent() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        assertEquals(parentDataSet, fold.getParent());
    }

    @Test
    public void testGetIndices() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, fold.getIndices());
    }

    @Test
    public void testAddMethod() {
        Dataset parentDataSet = new DefaultDataset();
        Fold fold = new Fold(parentDataSet, new int[0]);
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0});
        fold.add(instance);
        assertTrue(fold.contains(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddMethodUnsupportedOperation() {
        Dataset parentDataSet = new DefaultDataset();
        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0});
        fold.add(instance);
    }

    @Test
    public void testClassesMethod() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        Set<Object> classes = fold.classes();
        assertEquals(parentDataSet.classes(), classes);
    }

    @Test
    public void testKNearestMethod() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));
        parentDataSet.add(new DenseInstance(new double[]{3.0, 4.0, 5.0}, new Object[]{1}));
        parentDataSet.add(new DenseInstance(new double[]{2.5, 2.5, 5.5}, new Object[]{0}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 2, 3, 1, 4});
        Instance testInstance = new DenseInstance(new double[]{3.5, 3.5, 5.5}, new Object[]{0});
        Set<Instance> nearestInstances = fold.kNearest(3, testInstance, new EuclideanDistance());

        List<Instance> expectedInstances = Arrays.asList(parentDataSet.instance(2), parentDataSet.instance(0), parentDataSet.instance(3));
        assertEquals(expectedInstances, new ArrayList<>(nearestInstances));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKNearestMethodUnsupportedOperation() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        Instance testInstance = new DenseInstance(new double[]{3.0, 3.0, 5.5}, new Object[]{0});
        fold.kNearest(3, testInstance, new EuclideanDistance());
    }

    @Test
    public void testSizeMethod() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        assertEquals(parentDataSet.size(), fold.size());
    }

    @Test
    public void testIteratorMethod() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        List<Instance> instances = new ArrayList<>();
        for (Instance instance : fold) {
            instances.add(instance);
        }
        assertEquals(parentDataSet.size(), instances.size());
        for (int i = 0; i < parentDataSet.size(); i++) {
            assertEquals(parentDataSet.instance(i), instances.get(i));
        }
    }

    @Test
    public void testCopyMethod() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        Dataset copiedFold = fold.copy();

        assertEquals(fold.size(), copiedFold.size());
        for (int i = 0; i < fold.size(); i++) {
            Instance foldInstance = fold.getInstance(i);
            Instance copiedFoldInstance = copiedFold.getInstance(i);
            assertEquals(foldInstance, copiedFoldInstance);
        }
    }

    @Test
    public void testToStringMethod() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        String foldToString = fold.toString();
        assertTrue(foldToString.contains("Fold"));
        assertTrue(foldToString.contains("indices"));
        assertTrue(foldToString.contains("parent"));
    }

    @Test
    public void testEmptyDatasetFold() {
        Dataset parentDataSet = new EmptyDataset();
        Fold fold = new Fold(parentDataSet, new int[0]);
        assertEquals(0, fold.size());
    }

    @Test
    public void testNullParentDataSet() {
        int[] indices = {0, 1, 2, 3, 4};
        Dataset parentDataSet = null;
        try {
            new Fold(parentDataSet, indices);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullIndices() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = null;
        try {
            new Fold(parentDataSet, indices);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEmptyIndices() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = {};
        Fold fold = new Fold(parentDataSet, indices);
        assertNull(fold);
    }

    @Test
    public void testGetInstanceWithInvalidIndexOutOfBoundsException() {
        Dataset parentDataSet = new DefaultDataset();
        parentDataSet.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Object[]{0}));
        parentDataSet.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Object[]{1}));

        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});
        try {
            fold.getInstance(5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}
