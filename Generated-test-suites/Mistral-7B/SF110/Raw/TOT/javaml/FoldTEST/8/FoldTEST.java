package net.sf.javaml.core;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoldTest extends TestCase {

    private static final int NUM_FOLDS = 5;
    private static final int RANDOM_SEED = 42;

    private Dataset testDataset;
    private int[] indices;
    private Fold fold;

    @Override
    protected void setUp() throws Exception {
        testDataset = createTestDataset();
        indices = createTestIndices(testDataset.size(), NUM_FOLDS);
        fold = new Fold(testDataset, indices);
    }

    @Test
    public void testInstance() {
        final int index = 0;
        final Instance expectedInstance = testDataset.instance(indices[index]);
        final Instance actualInstance = fold.instance(index);
        Assert.assertEquals("Instance at index " + index + " should be equal", expectedInstance, actualInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        final Instance instanceToAdd = createTestInstance();
        fold.add(instanceToAdd);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        final List<Instance> instancesToAdd = Arrays.asList(createTestInstance(), createTestInstance());
        fold.addAll(instancesToAdd);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllWithIndex() {
        final List<Instance> instancesToAdd = Arrays.asList(createTestInstance(), createTestInstance());
        fold.addAll(0, instancesToAdd);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContains() {
        final Instance instanceToCheck = createTestInstance();
        fold.contains(instanceToCheck);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsAll() {
        final List<Instance> instancesToCheck = Arrays.asList(createTestInstance(), createTestInstance());
        fold.containsAll(instancesToCheck);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        fold.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll() {
        final List<Instance> instancesToRemove = Arrays.asList(createTestInstance(), createTestInstance());
        fold.removeAll(instancesToRemove);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll() {
        final List<Instance> instancesToRetain = Arrays.asList(createTestInstance(), createTestInstance());
        fold.retainAll(instancesToRetain);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() {
        final int index = 0;
        final Instance instanceToSet = createTestInstance();
        fold.set(index, instanceToSet);
    }

    @Test
    public void testSize() {
        final int expectedSize = testDataset.size();
        final int actualSize = fold.size();
        Assert.assertEquals("Size of fold should be equal to size of original dataset", expectedSize, actualSize);
    }

    @Test
    public void testClasses() {
        final SortedSet<Object> expectedClasses = testDataset.classes();
        final SortedSet<Object> actualClasses = fold.classes();
        Assert.assertEquals("Classes of fold should be equal to classes of original dataset", expectedClasses, actualClasses);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        fold.folds(NUM_FOLDS, new Random(RANDOM_SEED));
    }

    @Test
    public void testKNearest() {
        final int k = 3;
        final Instance testInstance = createTestInstance();
        final DistanceMeasure dm = new EuclideanDistance();
        final Set<Instance> expectedNeighbors = testDataset.kNearest(k, testInstance, dm);
        final Set<Instance> actualNeighbors = fold.kNearest(k, testInstance, dm);
        Assert.assertEquals("Nearest neighbors of testInstance in fold should be equal to nearest neighbors in original dataset", expectedNeighbors, actualNeighbors);
    }

    private Dataset createTestDataset() {
        // Create a test dataset with known behavior
        // ...
        return new DefaultDataset();
    }

    private Instance createTestInstance() {
        // Create a test instance with known behavior
        // ...
        return new DenseInstance(new double[0]);
    }

    private int[] createTestIndices(final int size, final int numFolds) {
        final int foldSize = size / numFolds;
        final int[] indices = new int[size];
        for (int i = 0, index = 0; i < numFolds; ++i) {
            for (int j = i * foldSize; j < (i + 1) * foldSize; ++j) {
                indices[j] = j;
            }
        }
        return indices;
    }

}