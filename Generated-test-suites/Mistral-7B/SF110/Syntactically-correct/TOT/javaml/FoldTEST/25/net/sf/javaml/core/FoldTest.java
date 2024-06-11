package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import org.junit.Test;

public class FoldTest {
    @Test
    public void testFold() {
        // Test constructor
        Dataset parentDataset = new DefaultDataset();
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals("Parent dataset should not be null", parentDataset, fold.getParent());
        assertEquals("Indices should not be null", indices, fold.getIndices());
        assertEquals("Size of fold should match size of indices", indices.length, fold.size());

        // Test getInstance method
        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0});
        parentDataset.add(instance);

        Instance expectedInstance = instance;
        Instance actualInstance = fold.getInstance(0);

        assertEquals("Expected and actual instances should be the same", expectedInstance, actualInstance);
    }

    @Test
    public void testFolds() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0}));
        dataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{2.0, 2.0, 2.0}));
        dataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, new double[]{3.0, 3.0, 3.0}));

        int numFolds = 2;
        Random rg = new Random();
        Fold[] folds = fold.folds(numFolds, rg);

        assertEquals("Expected number of folds to match provided number", numFolds, folds.length);
        for (Fold fold : folds) {
            assertEquals("Expected size of fold to match half the size of the dataset", dataset.size() / 2, fold.size());
        }
    }

    @Test
    public void testkNearest() {
        Dataset parentDataset = new DefaultDataset();
        parentDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 1.0, 1.0}));
        parentDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, new double[]{2.0, 2.0, 2.0}));
        parentDataset.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}, new double[]{3.0, 3.0, 3.0}));
        parentDataset.add(new DenseInstance(new double[]{2.0, 2.0, 2.0}, new double[]{4.0, 4.0, 4.0}));
        parentDataset.add(new DenseInstance(new double[]{3.0, 3.0, 3.0}, new double[]{5.0, 5.0, 5.0}));

        Fold fold = new Fold(parentDataset, new int[]{0, 2, 3, 4});
        Instance testInstance = new DenseInstance(new double[]{5.0, 5.0, 5.0}, new double[]{5.0, 5.0, 5.0});
        DistanceMeasure euclideanDistance = new EuclideanDistance();

        Set<Instance> expectedNeighbors = new TreeSet<>();
        expectedNeighbors.add(parentDataset.instance(1));
        expectedNeighbors.add(parentDataset.instance(3));
        expectedNeighbors.add(parentDataset.instance(4));

        Set<Instance> actualNeighbors = fold.kNearest(3, testInstance, euclideanDistance);

        assertEquals("Expected and actual number of neighbors should match", 3, actualNeighbors.size());
        assertTrue("Expected neighbors should be in actual neighbors", actualNeighbors.containsAll(expectedNeighbors));
    }
}