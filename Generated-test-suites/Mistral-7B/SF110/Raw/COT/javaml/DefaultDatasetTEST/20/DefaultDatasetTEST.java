package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.InstanceLabel;
import net.sf.javaml.instance.Singleton;
import net.sf.javaml.tools.DataTools;

public class DefaultDatasetTest {

	@Test
	public void testDefaultConstructor() {
		DefaultDataset dataset = new DefaultDataset();
		assertNotNull(dataset);
		assertEquals(0, dataset.size());
		assertEquals(0, dataset.maxAttributes);
		assertTrue(dataset.classes.isEmpty());
	}

	@Test
	public void testAddAll() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(2, dataset.size());
		assertEquals(2, dataset.maxAttributes);
		assertTrue(dataset.classes.containsAll(Arrays.asList(instance1.classValue(), instance2.classValue())));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAllNullCollection() {
		DefaultDataset dataset = new DefaultDataset();
		dataset.addAll(null);
	}

	@Test(expected = NullPointerException.class)
	public void testAddNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance nullInstance = null;
		dataset.add(nullInstance);
	}

	@Test
	public void testClear() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(2, dataset.size());
		dataset.clear();
		assertEquals(0, dataset.size());
		assertTrue(dataset.classes.isEmpty());
	}

	@Test
	public void testInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(instance1, dataset.instance(0));
		assertEquals(instance2, dataset.instance(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInstanceNegativeIndex() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2));
		dataset.instance(-1);
	}

	@Test
	public void testClasses() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		Instance instance3 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		dataset.addAll(Arrays.asList(instance1, instance2, instance3));
		SortedSet<Object> classes = dataset.classes();
		assertEquals(2, classes.size());
		assertTrue(classes.containsAll(Arrays.asList(instance1.classValue(), instance2.classValue())));
	}

	@Test(expected = IllegalStateException.class)
	public void testClassesNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance nullInstance = null;
		dataset.add(nullInstance);
		dataset.classes();
	}

	@Test
	public void testGetkNearest() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 4.0, 5.0 }, new double[] { 6.0 }, new double[] { 7.0 }, new InstanceLabel(1));
		Instance instance3 = InstanceFactory.createSingleton(new double[] { 2.0, 3.0 }, new double[] { 5.0 }, new double[] { 6.0 }, new InstanceLabel(2));
		Instance instance4 = InstanceFactory.createSingleton(new double[] { 3.0, 4.0 }, new double[] { 6.0 }, new double[] { 7.0 }, new InstanceLabel(2));
		Instance testInstance = InstanceFactory.createSingleton(new double[] { 2.5, 3.5 }, new double[] { 5.5 }, new double[] { 6.5 }, new InstanceLabel(1));
		dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
		DistanceMeasure distanceMeasure = new EuclideanDistance();
		Set<Instance> kNearestInstances = dataset.kNearest(2, testInstance, distanceMeasure);
		assertEquals(2, kNearestInstances.size());
		assertTrue(kNearestInstances.contains(instance1));
		assertTrue(kNearestInstances.contains(instance2));
		assertFalse(kNearestInstances.contains(instance3));
		assertFalse(kNearestInstances.contains(instance4));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetkNearestNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance nullInstance = null;
		Dataset[] folds = dataset.folds(2, new Random());
		Set<Instance> kNearestInstances = dataset.kNearest(2, nullInstance, new EuclideanDistance());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetkNearestNullDistanceMeasure() {
		DefaultDataset dataset = new DefaultDataset();
		Instance testInstance = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Dataset[] folds = dataset.folds(2, new Random());
		Set<Instance> kNearestInstances = dataset.kNearest(2, testInstance, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetkNearestNegativeK() {
		DefaultDataset dataset = new DefaultDataset();
		Instance testInstance = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Dataset[] folds = dataset.folds(2, new Random());
		Set<Instance> kNearestInstances = dataset.kNearest(-1, testInstance, new EuclideanDistance());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetkNearestZeroK() {
		DefaultDataset dataset = new DefaultDataset();
		Instance testInstance = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Dataset[] folds = dataset.folds(2, new Random());
		Set<Instance> kNearestInstances = dataset.kNearest(0, testInstance, new EuclideanDistance());
	}

	@Test
	public void testGetFolds() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		Instance instance3 = InstanceFactory.createSingleton(new double[] { 2.0, 3.0 }, new double[] { 5.0 }, new double[] { 6.0 }, new InstanceLabel(1));
		Instance instance4 = InstanceFactory.createSingleton(new double[] { 3.0, 4.0 }, new double[] { 6.0 }, new double[] { 7.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
		Dataset[] folds = dataset.folds(2, new Random());
		assertEquals(2, folds.length);
		for (Dataset fold : folds) {
			assertTrue(fold instanceof Fold);
		}
	}

	@Test(expected = NullPointerException.class)
	public void testGetFoldsNullRandom() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		Instance instance3 = InstanceFactory.createSingleton(new double[] { 2.0, 3.0 }, new double[] { 5.0 }, new double[] { 6.0 }, new InstanceLabel(1));
		Instance instance4 = InstanceFactory.createSingleton(new double[] { 3.0, 4.0 }, new double[] { 6.0 }, new double[] { 7.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4));
		Dataset[] folds = dataset.folds(2, null);
	}

	@Test
	public void testNoAttributes() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(instance1.noAttributes(), dataset.noAttributes());
		assertEquals(instance2.noAttributes(), dataset.noAttributes());
		assertEquals(instance1.noAttributes(), dataset.instance(0).noAttributes());
		assertEquals(instance2.noAttributes(), dataset.instance(1).noAttributes());
	}

	@Test(expected = IllegalStateException.class)
	public void testNoAttributesEmptyDataset() {
		DefaultDataset dataset = new DefaultDataset();
		dataset.noAttributes();
	}

	@Test
	public void testClassIndex() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		Instance instance3 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		dataset.addAll(Arrays.asList(instance1, instance2, instance3));
		assertEquals(1, dataset.classIndex(instance1.classValue()));
		assertEquals(2, dataset.classIndex(instance2.classValue()));
		assertEquals(-1, dataset.classIndex(null));
		assertEquals(-1, dataset.classIndex(new Singleton(new double[] { 0.0 })));
	}

	@Test
	public void testClassValue() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		Instance instance3 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		dataset.addAll(Arrays.asList(instance1, instance2, instance3));
		assertEquals(instance1.classValue(), dataset.classValue(0));
		assertEquals(instance2.classValue(), dataset.classValue(1));
		assertEquals(instance3.classValue(), dataset.classValue(2));
		assertEquals(null, dataset.classValue(-1));
	}

	@Test
	public void testCopy() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = InstanceFactory.createSingleton(new double[] { 1.0, 2.0 }, new double[] { 3.0 }, new double[] { 4.0 }, new InstanceLabel(1));
		Instance instance2 = InstanceFactory.createSingleton(new double[] { 5.0, 6.0 }, new double[] { 7.0 }, new double[] { 8.0 }, new InstanceLabel(2));
		dataset.addAll(Arrays.asList(instance1, instance2));
		DefaultDataset copiedDataset = dataset.copy();
		assertEquals(dataset.size(), copiedDataset.size());
		assertEquals(dataset.maxAttributes, copiedDataset.maxAttributes);
		assertEquals(dataset.classes, copiedDataset.classes);
		for (int i = 0; i < dataset.size(); i++) {
			assertEquals(dataset.instance(i), copiedDataset.instance(i));
		}
	}
}