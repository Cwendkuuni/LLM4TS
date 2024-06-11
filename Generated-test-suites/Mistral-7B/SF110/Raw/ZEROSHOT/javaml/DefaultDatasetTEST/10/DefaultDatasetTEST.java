package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

public class DefaultDatasetTest {

	@Test
	public void testDefaultDataset() {
		DefaultDataset dataset = new DefaultDataset();
		assertTrue(dataset.isEmpty());
		assertEquals(0, dataset.size());
		assertEquals(0, dataset.maxAttributes);
		assertNull(dataset.classes());
	}

	@Test
	public void testAddAll() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);

		List<Instance> instances = new LinkedList<Instance>();
		instances.add(instance1);
		instances.add(instance2);

		dataset.addAll(instances);

		assertEquals(2, dataset.size());
		assertEquals(3, dataset.maxAttributes);
		assertSame(instance1.classValue(), dataset.classes().first());
		assertSame(instance2.classValue(), dataset.classes().last());
		assertEquals(instance1, dataset.instance(0));
		assertEquals(instance2, dataset.instance(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAllNullCollection() {
		DefaultDataset dataset = new DefaultDataset();
		dataset.addAll(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAllNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance = null;
		dataset.addAll(Collections.singletonList(instance));
	}

	@Test
	public void testClear() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		dataset.clear();

		assertTrue(dataset.isEmpty());
		assertEquals(0, dataset.size());
		assertEquals(0, dataset.maxAttributes);
		assertNotNull(dataset.classes());
		assertEquals(0, dataset.classes().size());
	}

	@Test
	public void testAdd() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);

		dataset.add(instance1);

		assertEquals(1, dataset.size());
		assertEquals(3, dataset.maxAttributes);
		assertSame(instance1.classValue(), dataset.classes().first());
		assertEquals(instance1, dataset.instance(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance = null;
		dataset.add(instance);
	}

	@Test
	public void testGet() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		assertEquals(instance1, dataset.instance(0));
		assertEquals(instance2, dataset.instance(1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetNegativeIndex() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		dataset.instance(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetSizeNegativeIndex() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		dataset.size(-1);
	}

	@Test
	public void testClasses() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		assertTrue(dataset.classes().isEmpty());
		assertEquals(2, dataset.classes().size());
		assertSame(instance1.classValue(), dataset.classes().first());
		assertSame(instance2.classValue(), dataset.classes().last());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testClassesModify() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		TreeSet<Object> classes = dataset.classes();
		classes.add("newClass");
	}

	@Test
	public void testKNearest() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0, 5.0 }, 1.0);
		Instance instance3 = new DenseInstance(new double[] { 3.0, 4.0, 5.0, 6.0 }, 1.0);
		Instance instance4 = new DenseInstance(new double[] { 4.0, 5.0, 6.0, 7.0 }, 0.0);
		Instance instance5 = new DenseInstance(new double[] { 5.0, 6.0, 7.0, 8.0 }, 1.0);
		Instance instance6 = new DenseInstance(new double[] { 6.0, 7.0, 8.0, 9.0 }, 0.0);

		dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4, instance5, instance6));

		Instance testInstance = new DenseInstance(new double[] { 5.5, 6.5, 7.5, 8.5 }, 1.0);
		int k = 3;
		Instance[] expected = { instance5, instance3, instance1 };

		List<Instance> result = dataset.kNearest(k, testInstance, new EuclideanDistance());

		assertEquals(k, result.size());
		assertArrayEquals(expected, result.toArray(new Instance[0]));
	}

	@Test
	public void testFolds() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0, 5.0 }, 1.0);
		Instance instance3 = new DenseInstance(new double[] { 3.0, 4.0, 5.0, 6.0 }, 1.0);
		Instance instance4 = new DenseInstance(new double[] { 4.0, 5.0, 6.0, 7.0 }, 0.0);
		Instance instance5 = new DenseInstance(new double[] { 5.0, 6.0, 7.0, 8.0 }, 1.0);
		Instance instance6 = new DenseInstance(new double[] { 6.0, 7.0, 8.0, 9.0 }, 0.0);

		dataset.addAll(Arrays.asList(instance1, instance2, instance3, instance4, instance5, instance6));

		int numFolds = 3;
		Dataset[] expected = new Dataset[numFolds];
		Dataset foldedDataset1 = new DefaultDataset();
		Dataset foldedDataset2 = new DefaultDataset();
		Dataset foldedDataset3 = new DefaultDataset();

		for (int i = 0; i < numFolds; i++) {
			expected[i] = new Fold(dataset, new int[] { 1, 3, 5, 2, 4, 0 });
			assertEquals(instance1, expected[i].instance(0));
			assertEquals(instance3, expected[i].instance(1));
			assertEquals(instance5, expected[i].instance(2));
		}

		Dataset[] result = dataset.folds(numFolds, new Random());

		assertArrayEquals(expected, result);
	}

	@Test
	public void testNoAttributes() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		assertEquals(3, dataset.noAttributes());
	}

	@Test
	public void testClassIndex() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		assertEquals(0, dataset.classIndex(instance1.classValue()));
		assertEquals(1, dataset.classIndex(instance2.classValue()));
		assertEquals(-1, dataset.classIndex(null));
	}

	@Test
	public void testClassValue() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		assertEquals(instance1.classValue(), dataset.classValue(0));
		assertEquals(instance2.classValue(), dataset.classValue(1));
		assertEquals(null, dataset.classValue(2));
	}

	@Test
	public void testCopy() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.0);
		Instance instance2 = new DenseInstance(new double[] { 4.0, 5.0, 6.0 }, 1.0);
		dataset.addAll(Arrays.asList(instance1, instance2));

		DefaultDataset copy = dataset.copy();

		assertEquals(dataset.size(), copy.size());
		assertNotSame(dataset, copy);
		assertArrayEquals(dataset.toArray(), copy.toArray());
	}
}