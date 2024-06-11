package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class DefaultDatasetTest {

	@Test
	public void testDefaultDatasetEmpty() {
		DefaultDataset dataset = new DefaultDataset();
		assertEquals(0, dataset.size());
		assertTrue(dataset.classes().isEmpty());
		assertNull(dataset.maxAttributes());
	}

	@Test
	public void testDefaultDatasetWithInstances() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
		assertEquals(2, dataset.size());
		assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());
		assertEquals(3, dataset.maxAttributes());
	}

	@Test
	public void testAddAll() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset();
		dataset.addAll(Arrays.asList(instance1, instance2));
		assertEquals(2, dataset.size());
		assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());
		assertEquals(3, dataset.maxAttributes());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddAllThrowsException() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset();
		dataset.addAll(Arrays.asList(instance1, instance2));
		try {
			dataset.addAll(Arrays.asList(instance1, instance2));
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Test
	public void testAdd() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset();
		dataset.add(instance1);
		dataset.add(instance2);
		assertEquals(2, dataset.size());
		assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());
		assertEquals(3, dataset.maxAttributes());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testAddThrowsException() {
		Instance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		DefaultDataset dataset = new DefaultDataset();
		try {
			dataset.add(instance);
			dataset.add(instance);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

	@Test
	public void testClear() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
		dataset.clear();
		assertEquals(0, dataset.size());
		assertTrue(dataset.classes().isEmpty());
		assertNull(dataset.maxAttributes());
	}

	@Test
	public void testCheck() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset();
		dataset.check(Arrays.asList(instance1, instance2));
		assertEquals(2, dataset.size());
		assertEquals(Arrays.asList(1.0, 2.0), dataset.classes());
		assertEquals(3, dataset.maxAttributes());
	}

	@Test
	public void testKNearest() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		Instance instance3 = new DenseInstance(new double[] {2.5, 2.5, 2.5}, 1.0);
		Instance instance4 = new DenseInstance(new double[] {3.5, 3.5, 3.5}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4));
		DistanceMeasure distanceMeasure = new EuclideanDistance();

		Instance testInstance = new DenseInstance(new double[] {3.0, 3.0, 3.0}, 1.0);
		List<Instance> expectedResult = Arrays.asList(instance1, instance2);
		List<Instance> result = dataset.kNearest(2, testInstance, distanceMeasure);
		assertEquals(expectedResult.size(), result.size());
		for (int i = 0; i < result.size(); ++i) {
			assertEquals(expectedResult.get(i), result.get(i));
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testKNearestThrowsException() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		Instance instance3 = new DenseInstance(new double[] {2.5, 2.5, 2.5}, 1.0);
		Instance instance4 = new DenseInstance(new double[] {3.5, 3.5, 3.5}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4));
		DistanceMeasure distanceMeasure = new EuclideanDistance();

		Instance testInstance = new DenseInstance(new double[] {3.0, 3.0, 3.0}, 1.0);
		try {
			dataset.kNearest(-1, testInstance, distanceMeasure);
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	@Test
	public void testFolds() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		Instance instance3 = new DenseInstance(new double[] {2.5, 2.5, 2.5}, 1.0);
		Instance instance4 = new DenseInstance(new double[] {3.5, 3.5, 3.5}, 2.0);
		Instance instance5 = new DenseInstance(new double[] {5.0, 5.0, 5.0}, 1.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2, instance3, instance4, instance5));

		Fold[] expectedResult = new Fold[5];
		expectedResult[0] = new Fold(dataset, new int[] {2, 4, 0, 1, 3});
		expectedResult[1] = new Fold(dataset, new int[] {3, 1, 4, 0, 2});
		expectedResult[2] = new Fold(dataset, new int[] {0, 3, 2, 1, 4});
		expectedResult[3] = new Fold(dataset, new int[] {1, 2, 4, 3, 0});
		expectedResult[4] = new Fold(dataset, new int[] {4, 0, 1, 3, 2});

		Fold[] result = dataset.folds(5, new Random());
		assertArrayEquals(expectedResult, result);
	}

	@Test
	public void testNoAttributes() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1));
		assertEquals(instance1.noAttributes(), dataset.noAttributes());
	}

	@Test
	public void testClassIndex() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
		assertEquals(1, dataset.classIndex(1.0));
		assertEquals(-1, dataset.classIndex(null));
	}

	@Test
	public void testClassValue() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
		assertEquals(1.0, dataset.classValue(0));
		assertEquals(null, dataset.classValue(1));
	}

	@Test
	public void testCopy() {
		Instance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 1.0);
		Instance instance2 = new DenseInstance(new double[] {4.0, 5.0, 6.0}, 2.0);
		DefaultDataset dataset = new DefaultDataset(Arrays.asList(instance1, instance2));
		DefaultDataset copy = dataset.copy();
		assertEquals(dataset.size(), copy.size());
		assertEquals(dataset.classes(), copy.classes());
		assertEquals(dataset.maxAttributes(), copy.maxAttributes());
		assertNotSame(dataset, copy);
	}
}