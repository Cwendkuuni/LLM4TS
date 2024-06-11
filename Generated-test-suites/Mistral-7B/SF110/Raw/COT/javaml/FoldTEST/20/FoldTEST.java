package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class FoldTest {

	@Test
	public void testFoldConstructor() {
		Dataset parent = new DefaultDataset();
		int[] indices = new int[] { 0, 1, 2, 3, 4 };
		Fold fold = new Fold(parent, indices);
		assertEquals(parent, fold.parent);
		assertArrayEquals(indices, fold.indices);
		assertEquals(5, fold.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddInstanceUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.add(new DenseInstance(new double[0]));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAllUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.addAll(Arrays.asList(new DenseInstance(new double[0])));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testClearUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.clear();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.remove(new Object());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveInstanceUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.remove(0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRetainAllUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.retainAll(Arrays.asList(new DenseInstance(new double[0])));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		fold.set(0, new DenseInstance(new double[0]));
	}

	@Test
	public void testSize() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		assertEquals(0, fold.size());
		fold = new Fold(parent, new int[] { 0, 1, 2 });
		assertEquals(3, fold.size());
	}

	@Test
	public void testGetInstance() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
		Instance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		parent.add(instance1);
		Instance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
		parent.add(instance2);
		Instance expected = instance1;
		Instance actual = fold.instance(0);
		assertEquals(expected, actual);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetInstanceOutOfBounds() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
		fold.instance(3);
	}

	@Test
	public void testClasses() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		assertEquals(parent.classes(), fold.classes());
	}

	@Test
	public void testFoldsUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		try {
			fold.folds(5, new Random());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			// expected
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testKNearestUnsupported() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[0]);
		try {
			fold.kNearest(5, new DenseInstance(new double[] { 0.0, 0.0 }), new EuclideanDistance());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			// expected
		}
	}

	@Test
	public void testIterator() {
		Dataset parent = new DefaultDataset();
		Fold fold = new Fold(parent, new int[] { 0, 1, 2 });
		Instance expected[] = new Instance[] { parent.instance(0), parent.instance(1), parent.instance(2) };
		Instance actual[] = fold.iterator().toArray(new Instance[3]);
		assertArrayEquals(expected, actual);
	}
}