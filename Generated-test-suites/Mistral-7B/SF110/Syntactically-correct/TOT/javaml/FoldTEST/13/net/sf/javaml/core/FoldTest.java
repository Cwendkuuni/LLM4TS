package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.DistanceMeasure;
import org.junit.*;
import org.junit.Test;

public class FoldTest {
    @Test
    public void testConstructor() {
        // Given a Dataset and an array of indices
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0, 1.0, 1.0});
        DefaultDataset parentDataset = new DefaultDataset();
        parentDataset.add(instance1);
        int[] indices = new int[]{0, 2};

        // When a new Fold object is created
        Fold fold = new Fold(parentDataset, indices);

        // Then the Fold object is not null
        assertNotNull(fold);

        // Then the parent Dataset and indices of the Fold are set correctly
        assertEquals(parentDataset, fold.parent);
        assertArrayEquals(indices, fold.indices);

        // Given an empty Dataset and an array of indices
        parentDataset = new DefaultDataset();
        indices = new int[0];

        // When a new Fold object is created
        Fold foldEmpty = new Fold(parentDataset, indices);

        // Then the Fold object is not null
        assertNotNull(foldEmpty);

        // Then the parent Dataset and indices of the Fold are set to empty
        assertEquals(parentDataset, foldEmpty.parent);
        assertArrayEquals(new int[0], foldEmpty.indices);
    }

    @Test
    public void testGetInstance() {
        // Given a Dataset and a Fold created from it
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0, 1.0, 1.0});
        parentDataset.add(instance1);
        int[] indices = new int[]{0, 2};
        Fold fold = new Fold(parentDataset, indices);

        // When getInstance method is called with valid indices
        Instance expectedInstance1 = instance1;
        Instance instance = fold.instance(0);

        // Then the instance returned is the expected one
        assertNotNull(instance);
        assertNotNull(instance.classValue(0));
        assertNotNull(instance.value(0));
        assertNotNull(instance.value(1));
        assertNotNull(instance.value(2));
        assertEquals(expectedInstance1.classValue(0), instance.classValue(0));
        assertEquals(expectedInstance1.value(0), instance.value(0), 0.0);
        assertEquals(expectedInstance1.value(1), instance.value(1), 0.0);
        assertEquals(expectedInstance1.value(2), instance.value(2), 0.0);

        // When getInstance method is called with an index out of bounds
        Instance expectedInstance2 = null;
        int index = fold.size();
        instance = fold.instance(index);

        // Then the instance returned is null
        assertNull(instance);
    }

    @Test
    public void testSize() {
        // Given a Dataset and a Fold created from it
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{0.0, 1.0, 1.0});
        parentDataset.add(instance1);
        int[] indices = new int[]{0, 2};
        Fold fold = new Fold(parentDataset, indices);

        // Then the size of the fold is equal to the number of indices
        assertEquals(fold.size(), indices.length);

        // Given a Dataset with more instances and a Fold created from it
        parentDataset.add(new DenseInstance(new double[]{2.0, 3.0, 4.0}, new double[]{1.0, 1.0, 1.0}));
        indices = new int[]{1, 3};
        Fold fold2 = new Fold(parentDataset, indices);

        // Then the size of the fold is still equal to the number of indices
        assertEquals(fold2.size(), indices.length);
    }
}