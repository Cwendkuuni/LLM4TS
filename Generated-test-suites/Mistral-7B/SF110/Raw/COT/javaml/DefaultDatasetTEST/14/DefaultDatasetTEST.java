package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Fold;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

	private DefaultDataset dataset;
	private Instance instance1, instance2, instance3;
	private List<Instance> instances;
	private DistanceMeasure distanceMeasure;

	@Before
	public void setUp() {
		distanceMeasure = new EuclideanDistance();
		instance1 = new DenseInstance(1.0, 2.0, new String[] { "class1" });
		instance2 = new DenseInstance(2.0, 3.0, new String[] { "class2" });
		instance3 = new DenseInstance(1.5, 2.5, new String[] { "class1" });
		instances = new ArrayList<Instance>(Arrays.asList(instance1, instance2, instance3));
		dataset = new DefaultDataset(instances);
	}

	@Test
	public void testDefaultDataset() {
		assertNotNull(dataset);
		assertEquals(3, dataset.size());
		assertEquals(0, dataset.maxAttributes());
		assertTrue(dataset instanceof Dataset);
		assertTrue(dataset instanceof Vector);
	}

	@Test
	public void testDefaultDatasetConstructorWithCollection() {
		DefaultDataset dataset = new DefaultDataset(instances);
		assertEquals(3, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
		assertTrue(dataset instanceof Dataset);
		assertTrue(dataset instanceof Vector);
	}

	@Test
	public void testCheck() {
		Collection<Instance> coll = new ArrayList<Instance>();
		coll.add(instance1);
		dataset.check(coll);
		assertEquals(3, dataset.size());

		Instance invalidInstance = new DenseInstance(1.0, 2.0, new String[] { null });
		coll.add(invalidInstance);
		try {
			dataset.check(coll);
			fail("Expected IllegalArgumentException due to null class value");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testAddAll() {
		DefaultDataset copy = dataset.copy();
		assertTrue(dataset.addAll(copy));
		assertEquals(6, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAllWithEmptyCollection() {
		dataset.addAll(Collections.emptyList());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAllWithNullCollection() {
		dataset.addAll(null);
	}

	@Test
	public void testClear() {
		DefaultDataset copy = dataset.copy();
		dataset.clear();
		assertEquals(0, dataset.size());
		assertEquals(0, dataset.maxAttributes());

		dataset.addAll(copy);
		assertEquals(6, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
	}

	@Test
	public void testAdd() {
		DefaultDataset copy = dataset.copy();
		assertTrue(dataset.add(instance3));
		assertEquals(7, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
		assertEquals(instance3, dataset.getInstance(6));

		assertFalse(dataset.add(instance1));
		assertEquals(7, dataset.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddWithFullCapacity() {
		DefaultDataset dataset = new DefaultDataset(10);
		assertTrue(dataset.add(instance1));
		assertTrue(dataset.add(instance2));

		try {
			dataset.add(instance3);
			fail("Expected UnsupportedOperationException due to full capacity");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
	}

	@Test(expected = NullPointerException.class)
	public void testAddWithNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		assertTrue(dataset.add(instance1));

		try {
			dataset.add(null);
			fail("Expected NullPointerException due to null instance");
		} catch (NullPointerException e) {
			// Expected
		}
	}

	@Test
	public void testAddElement() {
		DefaultDataset copy = dataset.copy();
		assertTrue(dataset.addElement(instance3));
		assertEquals(7, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
		assertEquals(instance3, dataset.getInstance(6));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddElementWithFullCapacity() {
		DefaultDataset dataset = new DefaultDataset(10);
		assertTrue(dataset.add(instance1));
		assertTrue(dataset.add(instance2));

		try {
			dataset.addElement(instance3);
			fail("Expected UnsupportedOperationException due to full capacity");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
	}

	@Test(expected = NullPointerException.class)
	public void testAddElementWithNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		assertTrue(dataset.add(instance1));

		try {
			dataset.addElement(null);
			fail("Expected NullPointerException due to null instance");
		} catch (NullPointerException e) {
			// Expected
		}
	}

	@Test
	public void testInsertElementAt() {
		DefaultDataset copy = dataset.copy();
		assertTrue(dataset.insertElementAt(instance3, 3));
		assertEquals(7, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
		assertEquals(instance3, dataset.getInstance(3));

		assertFalse(dataset.insertElementAt(instance1, 0));
		assertEquals(7, dataset.size());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInsertElementAtWithIndexOutOfBounds() {
		DefaultDataset dataset = new DefaultDataset();
		try {
			dataset.insertElementAt(instance1, -1);
			fail("Expected IndexOutOfBoundsException due to negative index");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}

		try {
			dataset.insertElementAt(instance1, 7);
			fail("Expected IndexOutOfBoundsException due to index out of bounds");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
	}

	@Test(expected = NullPointerException.class)
	public void testSetElementAtWithNullInstance() {
		DefaultDataset dataset = new DefaultDataset();
		Instance instance4 = new DenseInstance(4.0, 5.0, new String[] { "class1" });
		assertTrue(dataset.setElementAt(instance1, 0));

		try {
			dataset.setElementAt(null, 0);
			fail("Expected NullPointerException due to null instance");
		} catch (NullPointerException e) {
			// Expected
		}
	}

	@Test
	public void testSetElementAt() {
		DefaultDataset copy = dataset.copy();
		assertTrue(dataset.setElementAt(instance3, 3));
		assertEquals(7, dataset.size());
		assertEquals(instance1.noAttributes(), dataset.maxAttributes());
		assertEquals(instance3, dataset.getInstance(3));

		assertFalse(dataset.setElementAt(instance1, 0));
		assertEquals(7, dataset.size());
	}

	@Test
	public void testInstance() {
		assertEquals(instance1, dataset.instance(0));
		assertEquals(instance2, dataset.instance(1));
		assertEquals(instance3, dataset.instance(4));
		assertEquals(null, dataset.instance(6));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInstanceWithIndexOutOfBounds() {
		try {
			dataset.instance(7);
			fail("Expected IndexOutOfBoundsException due to index out of bounds");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
	}

	@Test
	public void testClasses() {
		assertEquals(2, dataset.classes().size());
		assertTrue(dataset.classes().contains(instance1.classValue()));
		assertTrue(dataset.classes().contains(instance2.classValue()));
	}

	@Test
	public void testClassesWithEmptyDataset() {
		DefaultDataset emptyDataset = new DefaultDataset();
		assertEquals(0, emptyDataset.classes().size());
	}

	@Test
	public void testKNearest() {
		List<Instance> result = dataset.kNearest(2, instance1, distanceMeasure);
		assertEquals(2, result.size());
		assertTrue(result.contains(instance1));
		assertTrue(result.contains(instance2));
		assertFalse(result.contains(instance3));
	}

	@Test
	public void testKNearestWithKGreaterThanSize() {
		List<Instance> result = dataset.kNearest(10, instance1, distanceMeasure);
		assertEquals(6, result.size());
		assertTrue(result.contains(instance1));
		assertTrue(result.contains(instance2));
		assertTrue(result.contains(instance3));
		assertTrue(result.containsAll(Arrays.asList(instance1, instance2, instance3)));
	}

	@Test
	public void testKNearestWithNullInstance() {
		try {
			dataset.kNearest(2, null, distanceMeasure);
			fail("Expected IllegalArgumentException due to null instance");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testKNearestWithNullDistanceMeasure() {
		try {
			dataset.kNearest(2, instance1, null);
			fail("Expected IllegalArgumentException due to null distance measure");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testFolds() {
		Dataset[] result = dataset.folds(5, new Random());
		assertEquals(5, result.length);
		for (Dataset ds : result) {
			assertTrue(ds instanceof Fold);
			assertTrue(ds instanceof DefaultDataset);
			assertEquals(dataset.size(), ((Fold) ds).getBase().size());
		}
	}

	@Test
	public void testNoAttributes() {
		assertEquals(instance1.noAttributes(), dataset.noAttributes());
	}

	@Test
	public void testClassIndex() {
		assertEquals(1, dataset.classIndex("class2"));
		assertEquals(-1, dataset.classIndex(null));
		assertEquals(-1, dataset.classIndex("nonExistentClass"));
	}

	@Test
	public void testClassValue() {
		assertEquals("class1", dataset.classValue(0).getClass().getName());
		assertEquals("class1", dataset.classValue(1).getClass().getName());
		assertEquals("class1", dataset.classValue(2).getClass().getName());
		assertEquals("class2", dataset.classValue(3).getClass().getName());
		assertEquals("class1", dataset.classValue(4).getClass().getName());
		assertEquals(null, dataset.classValue(5));
	}

	@Test
	public void testCopy() {
		DefaultDataset copy = dataset.copy();
		assertEquals(6, copy.size());
		assertEquals(instance1.noAttributes(), copy.maxAttributes());
		assertEquals(instance1, copy.instance(0));
		assertEquals(instance2, copy.instance(1));
		assertEquals(instance3, copy.instance(4));
	}
}