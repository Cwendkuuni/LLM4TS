package net.sf.javaml.core.test;

import junit.framework.TestCase;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.EuclideanDistanceMeasure;

import java.util.Arrays;
import java.util.Random;

public class FoldTest extends TestCase {

    private Dataset originalDataset;
    private int[] indices;
    private Fold fold;

    @org.junit.Before
    public void setUp() {
        Instances data = new Instances(new ByteArrayInputStream(
                new byte[]{"@relation iris\n" +
                        "@attribute sepal-length numeric\n" +
                        "@attribute sepal-width numeric\n" +
                        "@attribute petal-length numeric\n" +
                        "@attribute petal-width numeric\n" +
                        "@attribute class {setosa, versicolor, virginica}\n" +
                        "@data\n" +
                        "4.9,3.0,1.4,0.2,Iris-setosa\n" +
                        "7.0,3.4,4.5,1.2,Iris-versicolor\n" +
                        "6.5,3.0,5.5,2.0,Iris-versicolor\n" +
                        "7.2,3.6,4.8,1.8,Iris-versicolor\n" +
                        "6.3,3.4,4.9,1.3,Iris-versicolor\n" +
                        "6.8,3.2,4.7,1.8,Iris-versicolor\n" +
                        "7.1,3.0,5.9,2.1,Iris-versicolor\n" +
                        "6.1,3.5,5.6,1.4,Iris-versicolor\n" +
                        "6.4,3.2,4.5,1.5,Iris-versicolor\n" +
                        "6.6,3.1,4.8,1.6,Iris-versicolor\n" +
                        "7.7,3.8,6.7,2.2,Iris-versicolor\n" +
                        "7.7,3.0,6.1,2.3,Iris-versicolor\n" +
                        "6.3,3.4,5.6,2.3,Iris-virginica\n" +
                        "6.4,3.2,4.5,2.3,Iris-virginica\n" +
                        "6.9,3.2,4.8,1.8,Iris-virginica\n" +
                        "6.0,3.1,4.9,1.4,Iris-setosa\n" +
                        "6.8,3.2,5.7,2.1,Iris-virginica\n" +
                        "6.7,3.3,5.7,2.5,Iris-virginica\n" +
                        "6.7,3.0,5.2,2.3,Iris-virginica\n" +
                        "6.3,2.5,5.0,1.9,Iris-virginica\n" +
                        "6.5,2.8,4.8,1.8,Iris-virginica\n" +
                        "6.8,3.2,5.1,2.4,Iris-virginica\n" +
                        "7.1,3.3,4.8,1.6,Iris-virginica"}, "UTF-8"));
        this.originalDataset = data;
    }

    @org.junit.Test
    public void testFoldCreation() {
        int numFolds = 3;
        Random random = new Random();
        indices = createFoldIndices(numFolds, random, originalDataset.size());
        fold = new Fold(originalDataset, indices);
        assertEquals(originalDataset.size(), fold.size());
    }

    @org.junit.Test
    public void testGetInstance() {
        int index = 0;
        Instance expectedInstance = originalDataset.instance(index);
        Instance actualInstance = fold.instance(index);
        assertEquals(expectedInstance, actualInstance);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddInstance() {
        Instance newInstance = createRandomInstance(originalDataset);
        fold.add(newInstance);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstances() {
        List<Instance> newInstances = new ArrayList<>();
        newInstances.add(createRandomInstance(originalDataset));
        fold.addAll(newInstances);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        fold.clear();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstance() {
        int index = 0;
        fold.remove(index);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllInstances() {
        List<Instance> instancesToRemove = Arrays.asList(fold.instance(0), fold.instance(1));
        fold.removeAll(instancesToRemove);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testRemoveInstanceAt() {
        fold.remove(0);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddAllInstancesAt() {
        List<Instance> newInstances = Arrays.asList(createRandomInstance(originalDataset), createRandomInstance(originalDataset));
        fold.addAll(newInstances, 0);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddInstanceAt() {
        Instance newInstance = createRandomInstance(originalDataset);
        fold.add(newInstance, 0);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetInstance() {
        Instance newInstance = createRandomInstance(originalDataset);
        fold.set(0, newInstance);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        int start = 0;
        int end = 5;
        List<Instance> expectedSubList = Arrays.asList(originalDataset.instance(0), originalDataset.instance(1), originalDataset.instance(2), originalDataset.instance(3), originalDataset.instance(4));
        List<Instance> actualSubList = Arrays.asList(fold.subList(start, end).toArray(new Instance[0]));
        assertEquals(expectedSubList, actualSubList);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testToArray() {
        Object[] expectedArray = new Object[originalDataset.size()];
        for (int i = 0; i < originalDataset.size(); ++i) {
            expectedArray[i] = originalDataset.instance(i);
        }
        Object[] actualArray = fold.toArray();
        assertArrayEquals(expectedArray, actualArray);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testToArrayWithCapacity() {
        int capacity = originalDataset.size();
        Object[] expectedArray = new Object[capacity];
        for (int i = 0; i < originalDataset.size(); ++i) {
            expectedArray[i] = originalDataset.instance(i);
        }
        Object[] actualArray = fold.toArray(expectedArray);
        assertArrayEquals(expectedArray, actualArray);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testClasses() {
        SortedSet<Object> actualClasses = fold.classes();
        SortedSet<Object> expectedClasses = originalDataset.classes();
        assertEquals(expectedClasses, actualClasses);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        int expectedSize = originalDataset.size();
        int actualSize = fold.size();
        assertEquals(expectedSize, actualSize);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testNoAttributes() {
        int expectedNoAttributes = originalDataset.noAttributes();
        int actualNoAttributes = fold.noAttributes();
        assertEquals(expectedNoAttributes, actualNoAttributes);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testClassIndex() {
        Object expectedClass = Instances.classValue("Iris-setosa");
        Object actualClass = fold.classIndex(expectedClass);
        assertEquals(expectedClass, actualClass);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testInstanceClassValue() {
        int index = 0;
        Object expectedClass = originalDataset.classValue(index);
        Object actualClass = fold.classValue(index);
        assertEquals(expectedClass, actualClass);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testKNearest() {
        int k = 5;
        Instance queryInstance = createRandomInstance(originalDataset);
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        Set<Instance> expectedNearestInstances = new TreeSet<>();
        for (int i = 0; i < k; ++i) {
            Instance nearestInstance = originalDataset.nearestNeighbor(queryInstance, distanceMeasure);
            expectedNearestInstances.add(nearestInstance);
            originalDataset.remove(nearestInstance);
        }
        Set<Instance> actualNearestInstances = fold.kNearest(k, queryInstance, distanceMeasure);
        assertEquals(expectedNearestInstances, actualNearestInstances);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testFolds() {
        int numFolds = 5;
        Dataset[] actualFolds = fold.folds(numFolds, new Random());
        assertEquals(numFolds, actualFolds.length);
        for (Dataset dataset : actualFolds) {
            assertTrue(dataset instanceof Fold);
        }
    }

    private int[] createFoldIndices(int numFolds, Random random, int totalInstances) {
        int instancesPerFold = totalInstances / numFolds;
        int[] indices = new int[totalInstances];
        for (int i = 0, index = 0; i < numFolds; ++i) {
            for (int j = 0; j < instancesPerFold; ++j) {
                indices[index++] = i * instancesPerFold + j;
            }
        }
        for (int i = 0; i < totalInstances % numFolds; ++i) {
            int rollingFoldIndex = random.nextInt(numFolds);
            indices[totalInstances - i - 1] = indices[rollingFoldIndex] + 1;
            indices[rollingFoldIndex]++;
        }
        return indices;
    }

    private Instance createRandomInstance(Dataset dataset) {
        Instances data = (Instances) dataset;
        return data.instance(new Random().nextInt(data.size()));
    }
}