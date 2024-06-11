package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoldTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        fold.add(new DefaultInstance(1.0, 2.0));
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 0.0));
        parent.add(new DefaultInstance(2.0, 1.0));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        Set<Object> classes = fold.classes();
        List<Object> expectedClasses = Arrays.asList(parent.classValue(0), parent.classValue(1));
        assertEquals(expectedClasses, classes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[0];
        Fold fold = new Fold(parent, indices);
        fold.folds(5, new Random());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstanceWithNegativeIndex() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0));

        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        fold.instance(-1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstanceWithIndexOutOfBounds() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0));

        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        fold.instance(indices.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        Object obj = new Object();
        boolean contains = fold.contains(obj);
        assertFalse(contains);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0));
        fold.retainAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        Dataset parent = new DefaultDataset();
        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        Instance instance = new DefaultInstance(1.0, 2.0);
        fold.set(0, instance);
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        parent.add(new DefaultInstance(1.0, 2.0));
        parent.add(new DefaultInstance(3.0, 4.0));

        int[] indices = new int[]{0};
        Fold fold = new Fold(parent, indices);
        assertEquals(1, fold.size());
    }

    @Test
    public void testToArray() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        Object[] expectedArray = new Object[2];
        for (int i = 0; i < fold.size(); i++) {
            expectedArray[i] = fold.instance(i);
        }
        assertArrayEquals(expectedArray, fold.toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToArrayWithNonNullArray() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        Object[] array = new Object[2];
        fold.toArray(array);
    }

    @Test
    public void testKNearestWithNullDistanceMeasure() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = null;
        try {
            fold.kNearest(2, new DefaultInstance(), dm);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testKNearestWithEmptyFold() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{-1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new EuclideanDistance();
        try {
            fold.kNearest(2, new DefaultInstance(), dm);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testKNearestWithNullInstance() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new EuclideanDistance();
        try {
            fold.kNearest(2, null, dm);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testKNearestWithNullInstanceList() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new EuclideanDistance();
        try {
            fold.kNearest(2, Collections.emptyList(), dm);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testKNearestWithInvalidNumberOfInstances() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new EuclideanDistance();
        try {
            fold.kNearest(3, new DefaultInstance(), dm);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testKNearestWithInvalidDistanceMeasure() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new Object() {}; // Anonymous inner class with no method implementations
        try {
            fold.kNearest(2, new DefaultInstance(), dm);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testKNearest() {
        Dataset parent = new DefaultDataset();
        parent.addAll(Arrays.asList(new DefaultInstance(1.0, 2.0), new DefaultInstance(3.0, 4.0), new DefaultInstance(5.0, 6.0)));

        int[] indices = new int[]{0, 1};
        Fold fold = new Fold(parent, indices);
        DistanceMeasure dm = new EuclideanDistance();
        List<Instance> expectedInstances = Arrays.asList(parent.instance(1), parent.instance(2));
        List<Instance> kNearestInstances = fold.kNearest(2, new DefaultInstance(), dm);
        assertEquals(expectedInstances, kNearestInstances);
    }
}