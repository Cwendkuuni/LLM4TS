import junit.framework.Assert;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.euclidean.EuclideanDistance;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FoldTest {

    @Test
    public void testFoldInstanceAccess() {
        Dataset parentDataset = new DefaultDataset();
        Instance instance1 = new DefaultInstance(1.0, 2.0, 3.0);
        Instance instance2 = new DefaultInstance(4.0, 5.0, 6.0);
        parentDataset.add(instance1);
        parentDataset.add(instance2);

        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);

        Instance testInstance1 = fold.instance(0);
        Instance testInstance2 = fold.instance(1);

        Assert.assertEquals(instance1, testInstance1);
        Assert.assertEquals(instance2, testInstance2);
    }

    @Test
    public void testFoldEmptyInstanceAccess() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = new int[0];
        Fold fold = new Fold(parentDataset, indices);

        try {
            fold.instance(0);
            Assert.fail("Expected UnsupportedOperationException, got none.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testFoldSize() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parentDataset, indices);

        Assert.assertEquals(3, fold.size());
    }

    @Test
    public void testFoldClasses() {
        Dataset parentDataset = new DefaultDataset();
        parentDataset.add(new DefaultInstance(1.0, 2.0, 3.0));
        parentDataset.add(new DefaultInstance(4.0, 5.0, 6.0));
        parentDataset.add(new DefaultInstance(7.0, 8.0, 9.0));

        int[] indices = {0, 1};
        Fold fold = new Fold(parentDataset, indices);

        SortedSet<Object> classes = fold.classes();
        List<Object> expectedClasses = Arrays.asList(parentDataset.classValue(0), parentDataset.classValue(1));

        Assert.assertEquals(expectedClasses, collections.unmodifiableSortedSet(classes));
    }

    @Test
    public void testFoldKNearest() {
        Dataset parentDataset = new DefaultDataset();
        Instance instanceToTest = new DefaultInstance(1.0, 2.0, 3.0);
        parentDataset.addAll(Arrays.asList(
                new DefaultInstance(0.5, 0.5, 0.5),
                new DefaultInstance(2.5, 2.5, 2.5),
                new DefaultInstance(instanceToTest),
                new DefaultInstance(4.5, 4.5, 4.5)
        ));

        int k = 2;
        DistanceMeasure distanceMeasure = new EuclideanDistance();
        Fold fold = new Fold(parentDataset, new int[0]);

        Set<Instance> nearestInstances = fold.kNearest(k, instanceToTest, distanceMeasure);

        List<Instance> expectedNearestInstances = Arrays.asList(
                parentDataset.instance(1),
                parentDataset.instance(0)
        );

        Assert.assertEquals(expectedNearestInstances, nearestInstances);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFoldKNearestUnsupportedOperation() {
        Dataset parentDataset = new DefaultDataset();
        Fold fold = new Fold(parentDataset, new int[0]);

        fold.kNearest(1, new DefaultInstance(1.0, 2.0, 3.0), new EuclideanDistance());
    }

    @Test
    public void testFoldNoFolds() {
        Dataset parentDataset = new DefaultDataset();
        Fold fold = new Fold(parentDataset, new int[0]);

        try {
            fold.folds(5, new Random());
            Assert.fail("Expected UnsupportedOperationException, got none.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}