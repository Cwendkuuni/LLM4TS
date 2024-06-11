package net.sf.javaml.core.test;

import junit.framework.Assert;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.SquareEuclideanDistance;
import net.sf.javaml.distance.WardDistance;

import org.junit.Test;

public class FoldTest {

	@Test
	public void testFoldInstanceAccess() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		parent.add(instance1);
		parent.add(instance2);

		int[] indices = { 0, 1 };
		Fold fold = new Fold(parent, indices);

		Instance testInstance1 = fold.instance(0);
		Instance testInstance2 = fold.instance(1);

		Assert.assertEquals(instance1, testInstance1);
		Assert.assertEquals(instance2, testInstance2);
	}

	@Test
	public void testFoldSize() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		parent.add(instance1);
		parent.add(instance2);

		int[] indices = { 0, 1 };
		Fold fold = new Fold(parent, indices);

		Assert.assertEquals(2, fold.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFoldAddInstance() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);

		Instance instance = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		fold.add(instance);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFoldAddAllInstances() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);

		Dataset instances = new DefaultDataset();
		instances.add(new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 }));
		instances.add(new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 }));
		fold.addAll(instances);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFoldClear() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);

		fold.clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFoldRemoveInstance() {
		Dataset parent = new DefaultDataset();
		Instance instance = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		parent.add(instance);
		int[] indices = { 0 };
		Fold fold = new Fold(parent, indices);

		fold.remove(instance);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFoldRemoveAllInstances() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		parent.add(instance1);
		parent.add(instance2);
		int[] indices = { 0, 1 };
		Fold fold = new Fold(parent, indices);

		fold.removeAll(fold);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFoldRetainAllInstances() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		Instance instance3 = new DenseInstance(new double[] { 9.0, 10.0 }, new double[] { 11.0, 12.0 });
		parent.addAll(Arrays.asList(instance1, instance2, instance3));
		int[] indices = { 0, 1 };
		Fold fold = new Fold(parent, indices);

		Dataset otherInstances = new DefaultDataset();
		otherInstances.add(instance3);
		fold.retainAll(otherInstances);
	}

	@Test
	public void testFoldKNearest() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		Instance instance3 = new DenseInstance(new double[] { 1.5, 2.5 }, new double[] { 3.5, 4.5 });
		Instance instance4 = new DenseInstance(new double[] { 2.0, 2.0 }, new double[] { 3.0, 3.0 });
		parent.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

		int[] indices = { 0, 1, 3 };
		Fold fold = new Fold(parent, indices);
		DistanceMeasure euclideanDistance = new EuclideanDistance();

		List<Instance> kNearestInstances = fold.kNearest(2, instance1, euclideanDistance);
		Assert.assertEquals(2, kNearestInstances.size());
		Assert.assertTrue(kNearestInstances.contains(instance1));
		Assert.assertTrue(kNearestInstances.contains(instance2));
		Assert.assertFalse(kNearestInstances.contains(instance3));
		Assert.assertFalse(kNearestInstances.contains(instance4));

		List<Instance> kNearestInstances2 = fold.kNearest(3, instance1, euclideanDistance);
		Assert.assertEquals(3, kNearestInstances2.size());
		Assert.assertTrue(kNearestInstances2.contains(instance1));
		Assert.assertTrue(kNearestInstances2.contains(instance2));
		Assert.assertTrue(kNearestInstances2.contains(instance3));
		Assert.assertTrue(kNearestInstances2.contains(instance4));

		List<Instance> kNearestInstances3 = fold.kNearest(5, instance1, euclideanDistance);
		Assert.assertEquals(5, kNearestInstances3.size());
		Assert.assertTrue(kNearestInstances3.contains(instance1));
		Assert.assertTrue(kNearestInstances3.contains(instance2));
		Assert.assertTrue(kNearestInstances3.contains(instance3));
		Assert.assertTrue(kNearestInstances3.contains(instance4));

		List<Instance> kNearestInstances4 = fold.kNearest(10, instance1, euclideanDistance);
		Assert.assertTrue(kNearestInstances4.size() >= 10);
		Assert.assertTrue(kNearestInstances4.contains(instance1));
		Assert.assertTrue(kNearestInstances4.contains(instance2));
		Assert.assertTrue(kNearestInstances4.contains(instance3));
		Assert.assertTrue(kNearestInstances4.contains(instance4));
	}

	@Test
	public void testFoldKNearestWithSquareEuclideanDistance() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		Instance instance3 = new DenseInstance(new double[] { 1.5, 2.5 }, new double[] { 3.5, 4.5 });
		Instance instance4 = new DenseInstance(new double[] { 2.0, 2.0 }, new double[] { 3.0, 3.0 });
		parent.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

		int[] indices = { 0, 1, 3 };
		Fold fold = new Fold(parent, indices);
		DistanceMeasure squareEuclideanDistance = new SquareEuclideanDistance();

		List<Instance> kNearestInstances = fold.kNearest(2, instance1, squareEuclideanDistance);
		Assert.assertEquals(2, kNearestInstances.size());
		Assert.assertTrue(kNearestInstances.contains(instance1));
		Assert.assertTrue(kNearestInstances.contains(instance2));
		Assert.assertFalse(kNearestInstances.contains(instance3));
		Assert.assertFalse(kNearestInstances.contains(instance4));

		List<Instance> kNearestInstances2 = fold.kNearest(3, instance1, squareEuclideanDistance);
		Assert.assertEquals(3, kNearestInstances2.size());
		Assert.assertTrue(kNearestInstances2.contains(instance1));
		Assert.assertTrue(kNearestInstances2.contains(instance2));
		Assert.assertTrue(kNearestInstances2.contains(instance3));
		Assert.assertTrue(kNearestInstances2.contains(instance4));

		List<Instance> kNearestInstances3 = fold.kNearest(5, instance1, squareEuclideanDistance);
		Assert.assertEquals(5, kNearestInstances3.size());
		Assert.assertTrue(kNearestInstances3.contains(instance1));
		Assert.assertTrue(kNearestInstances3.contains(instance2));
		Assert.assertTrue(kNearestInstances3.contains(instance3));
		Assert.assertTrue(kNearestInstances3.contains(instance4));

		List<Instance> kNearestInstances4 = fold.kNearest(10, instance1, squareEuclideanDistance);
		Assert.assertTrue(kNearestInstances4.size() >= 10);
		Assert.assertTrue(kNearestInstances4.contains(instance1));
		Assert.assertTrue(kNearestInstances4.contains(instance2));
		Assert.assertTrue(kNearestInstances4.contains(instance3));
		Assert.assertTrue(kNearestInstances4.contains(instance4));
	}

	@Test
	public void testFoldKNearestWithWardDistance() {
		Dataset parent = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 }, new double[] { 3.0, 4.0 });
		Instance instance2 = new DenseInstance(new double[] { 5.0, 6.0 }, new double[] { 7.0, 8.0 });
		Instance instance3 = new DenseInstance(new double[] { 1.5, 2.5 }, new double[] { 3.5, 4.5 });
		Instance instance4 = new DenseInstance(new double[] { 2.0, 2.0 }, new double[] { 3.0, 3.0 });
		parent.addAll(Arrays.asList(instance1, instance2, instance3, instance4));

		int[] indices = { 0, 1, 3 };
		Fold fold = new Fold(parent, indices);
		DistanceMeasure wardDistance = new WardDistance();

		List<Instance> kNearestInstances = fold.kNearest(2, instance1, wardDistance);
		Assert.assertEquals(2, kNearestInstances.size());
		Assert.assertTrue(kNearestInstances.contains(instance1));
		Assert.assertTrue(kNearestInstances.contains(instance2));
		Assert.assertFalse(kNearestInstances.contains(instance3));
		Assert.assertFalse(kNearestInstances.contains(instance4));

		List<Instance> kNearestInstances2 = fold.kNearest(3, instance1, wardDistance);
		Assert.assertEquals(3, kNearestInstances2.size());
		Assert.assertTrue(kNearestInstances2.contains(instance1));
		Assert.assertTrue(kNearestInstances2.contains(instance2));
		Assert.assertTrue(kNearestInstances2.contains(instance3));
		Assert.assertTrue(kNearestInstances2.contains(instance4));

		List<Instance> kNearestInstances3 = fold.kNearest(5, instance1, wardDistance);
		Assert.assertEquals(5, kNearestInstances3.size());
		Assert.assertTrue(kNearestInstances3.contains(instance1));
		Assert.assertTrue(kNearestInstances3.contains(instance2));
		Assert.assertTrue(kNearestInstances3.contains(instance3));
		Assert.assertTrue(kNearestInstances3.contains(instance4));

		List<Instance> kNearestInstances4 = fold.kNearest(10, instance1, wardDistance);
		Assert.assertTrue(kNearestInstances4.size() >= 10);
		Assert.assertTrue(kNearestInstances4.contains(instance1));
		Assert.assertTrue(kNearestInstances4.contains(instance2));
		Assert.assertTrue(kNearestInstances4.contains(instance3));
		Assert.assertTrue(kNearestInstances4.contains(instance4));
	}
}