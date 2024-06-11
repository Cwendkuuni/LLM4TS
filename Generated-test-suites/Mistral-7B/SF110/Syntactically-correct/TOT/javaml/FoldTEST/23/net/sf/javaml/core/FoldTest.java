package net.sf.javaml.core;

import net.sf.javaml.distance.DistanceMeasure;
import org.junit.Test;

import static org.junit.Assert.*;

public class FoldTest {
    @Test
    public void testFoldConstructor() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = new int[] {0, 1, 2, 3, 4};
        Fold fold = new Fold(parentDataSet, indices);

        assertEquals(parentDataSet, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }

    @Test
    public void testInstanceMethod() {
        Dataset parentDataSet = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1);
        Instance instance2 = new DenseInstance(new double[] {2.0, 3.0, 4.0}, 2);
        int[] indices = new int[] {0, 1};

        parentDataSet.add(instance1);
        parentDataSet.add(instance2);

        Fold fold = new Fold(parentDataSet, indices);

        Instance expectedInstance1 = parentDataSet.instance(0);
        Instance expectedInstance2 = parentDataSet.instance(1);

        Instance actualInstance1 = fold.instance(0);
        Instance actualInstance2 = fold.instance(1);

        assertEquals(expectedInstance1, actualInstance1);
        assertEquals(expectedInstance2, actualInstance2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        Dataset parentDataSet = new DefaultDataset();
        Fold fold = new Fold(parentDataSet, new int[0]);

        fold.add(new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1));
        fold.clear();
        fold.addAll(parentDataSet);
        fold.remove(0);
        fold.retainAll(parentDataSet);
        fold.set(0, new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1));
    }
}