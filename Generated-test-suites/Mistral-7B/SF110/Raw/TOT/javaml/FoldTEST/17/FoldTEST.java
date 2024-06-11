package net.sf.javaml.core.test;

import junit.framework.TestCase;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoldTest extends TestCase {

    private static final double DELTA = 1e-14;

    @Test
    public void testFoldEmptyDataset() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[0]);
        assertEquals(0, fold.size());
    }

    @Test
    public void testFoldValidDataset() {
        Dataset dataset = createDataset();
        Fold fold = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        assertEquals(5, fold.size());

        Instance expectedInstance1 = dataset.instance(0);
        Instance instance1 = fold.instance(0);
        assertEquals(expectedInstance1, instance1);

        Instance expectedInstance2 = dataset.instance(2);
        Instance instance2 = fold.instance(1);
        assertEquals(expectedInstance2, instance2);

        Instance expectedInstance3 = dataset.instance(4);
        Instance instance3 = fold.instance(4);
        assertEquals(expectedInstance3, instance3);

        Instance expectedInstance4 = dataset.instance(1);
        Instance instance4 = fold.instance(2);
        assertEquals(expectedInstance4, instance4);

        Instance expectedInstance5 = dataset.instance(3);
        Instance instance5 = fold.instance(3);
        assertEquals(expectedInstance5, instance5);
    }

    @Test
    public void testGetFolds() {
        Dataset dataset = createDataset();
        Fold fold1 = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        Fold fold2 = new Fold(dataset, new int[]{1, 3, 0, 2, 4});

        List<Dataset> expectedFolds = Arrays.asList(
                new DefaultDataset(Arrays.asList(dataset.instance(0), dataset.instance(1), dataset.instance(4))),
                new DefaultDataset(Arrays.asList(dataset.instance(2), dataset.instance(3)))
        );

        Dataset[] folds = fold1.folds(2, new Random());
        assertEquals(2, folds.length);

        for (int i = 0; i < 2; ++i) {
            assertEquals(expectedFolds.get(i).size(), folds[i].size());
            for (int j = 0; j < folds[i].size(); ++j) {
                Instance expectedInstance = expectedFolds.get(i).instance(j);
                Instance instance = folds[i].instance(j);
                assertEquals(expectedInstance, instance);
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddToFold() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0});
        Instance instance = new DefaultInstance(1.0, new double[1]);
        fold.add(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllToFold() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0});
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, new double[1]), new DefaultInstance(2.0, new double[1]));
        fold.addAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveFromFold() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0});
        Instance instance = new DefaultInstance(1.0, new double[1]);
        fold.remove(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllFromFold() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0});
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, new double[1]), new DefaultInstance(2.0, new double[1]));
        fold.removeAll(instances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllFromFold() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0});
        List<Instance> instances = Arrays.asList(new DefaultInstance(1.0, new double[1]), new DefaultInstance(2.0, new double[1]));
        fold.retainAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetInstanceInFold() {
        Dataset dataset = new DefaultDataset();
        Fold fold = new Fold(dataset, new int[]{0});
        Instance instance = new DefaultInstance(1.0, new double[1]);
        fold.set(0, instance);
    }

    private Dataset createDataset() {
        Dataset dataset = new DefaultDataset();
        dataset.add(new DefaultInstance(1.0, new double[]{1.0, 2.0}));
        dataset.add(new DefaultInstance(2.0, new double[]{1.0, 3.0}));
        dataset.add(new DefaultInstance(3.0, new double[]{3.0, 1.0}));
        dataset.add(new DefaultInstance(4.0, new double[]{2.0, 2.0}));
        dataset.add(new DefaultInstance(5.0, new double[]{3.0, 3.0}));
        return dataset;
    }

    @Test
    public void testKNearestNeighbors() {
        Dataset dataset = createDataset();
        Fold fold = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        Instance testInstance = new DefaultInstance(new double[]{1.5, 2.5}, new String[]{"label"});
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Set<Instance> neighbors = fold.kNearest(3, testInstance, distanceMeasure);

        List<Integer> expectedIndices = Arrays.asList(1, 2, 4);
        List<Instance> expectedInstances = Arrays.asList(
                dataset.instance(1),
                dataset.instance(2),
                dataset.instance(4)
        );

        assertEquals(3, neighbors.size());
        for (int i = 0; i < 3; ++i) {
            Instance neighbor = neighbors.iterator().next();
            int index = Arrays.asList(fold.indices).indexOf(neighbor.getIndex());
            assertEquals(expectedIndices.get(i), index);
            assertEquals(expectedInstances.get(i), neighbor);
        }
    }

    @Test
    public void testGetClassValue() {
        Dataset dataset = createDataset();
        Fold fold = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        Instance testInstance = dataset.instance(0);
        assertEquals("class1", fold.classValue(0));
        assertEquals("class1", fold.classValue(1));
        assertEquals("class1", fold.classValue(2));
        assertEquals("class2", fold.classValue(3));
        assertEquals("class2", fold.classValue(4));
    }

    @Test
    public void testClasses() {
        Dataset dataset = createDataset();
        Fold fold = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        Set<Object> expectedClasses = Collections.singleton("class1");
        Set<Object> classes = fold.classes();
        assertEquals(expectedClasses, classes);
    }

    @Test
    public void testSize() {
        Dataset dataset = createDataset();
        Fold fold = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        assertEquals(5, fold.size());
    }

    @Test
    public void testNoAttributes() {
        Dataset dataset = createDataset();
        Fold fold = new Fold(dataset, new int[]{0, 2, 4, 1, 3});
        assertEquals(2, fold.noAttributes());
    }
}