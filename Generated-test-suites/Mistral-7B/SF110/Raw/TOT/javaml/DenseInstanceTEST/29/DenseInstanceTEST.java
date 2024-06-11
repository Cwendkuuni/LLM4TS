package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceConstruction() {
		// Test empty constructor
		DenseInstance empty = new DenseInstance();
		assertEquals(0, empty.noAttributes());
		assertEquals(null, empty.getClassValue());

		// Test constructor with attributes
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(attributes, "classValue");
		assertEquals(3, instance.noAttributes());
		assertEquals("classValue", instance.getClassValue());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);

		// Test constructor with size
		instance = new DenseInstance(5);
		assertEquals(5, instance.noAttributes());
		assertEquals(null, instance.getClassValue());
		assertNull(instance.value(5));
	}

	@Test
	public void testValue() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		try {
			instance.value(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
		try {
			instance.value(3);
			fail("Expected ArrayIndexOutOfBoundsException");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testClear() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertEquals(3, instance.noAttributes());
		instance.clear();
		assertEquals(0, instance.noAttributes());
		assertEquals(null, instance.getClassValue());
	}

	@Test
	public void testContainsKey() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertFalse(instance.containsKey(-1));
		assertFalse(instance.containsKey(3));
	}

	@Test
	public void testContainsValue() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertTrue(instance.containsValue(1.0));
		assertTrue(instance.containsValue(2.0));
		assertTrue(instance.containsValue(3.0));
		assertFalse(instance.containsValue(0.5));
	}

	@Test
	public void testEntrySet() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
		assertEquals(3, entries.size());
		for (Map.Entry<Integer, Double> entry : entries) {
			assertEquals(entry.getKey(), entry.getValue().intValue());
		}
	}

	@Test
	public void testGet() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertEquals(1.0, instance.get(0), 0.0);
		assertEquals(2.0, instance.get(1), 0.0);
		assertEquals(3.0, instance.get(2), 0.0);
		try {
			instance.get(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
		try {
			instance.get(3);
			fail("Expected ArrayIndexOutOfBoundsException");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testIsEmpty() {
		DenseInstance empty = new DenseInstance();
		assertTrue(empty.isEmpty());

		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertFalse(instance.isEmpty());
	}

	@Test
	public void testKeySet() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		SortedSet<Integer> keys = instance.keySet();
		assertEquals(3, keys.size());
		for (Integer key : keys) {
			assertEquals(key, instance.value(key.intValue()));
		}
	}

	@Test
	public void testPut() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertEquals(1.0, instance.value(0), 0.0);
		instance.put(0, 5.0);
		assertEquals(5.0, instance.value(0), 0.0);
		try {
			instance.put(-1, 5.0);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
		try {
			instance.put(4, 5.0);
			fail("Expected ArrayIndexOutOfBoundsException");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testPutAll() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(0, 5.0);
		map.put(1, 6.0);
		instance.putAll(map);
		assertEquals(5.0, instance.value(0), 0.0);
		assertEquals(6.0, instance.value(1), 0.0);
		assertEquals(3, instance.noAttributes());
	}

	@Test
	public void testRemove() {
		throw new UnsupportedOperationException("Cannot test remove method as it is not supported by the DenseInstance class.");
	}

	@Test
	public void testSize() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		assertEquals(3, instance.noAttributes());
	}

	@Test
	public void testValues() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		Collection<Double> values = instance.values();
		assertEquals(3, values.size());
		for (Double value : values) {
			assertEquals(value, instance.value(values.indexOf(value)), 0.0);
		}
	}

	@Test
	public void testCopy() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		Instance copy = instance.copy();
		assertEquals(instance.toString(), copy.toString());
	}

	@Test
	public void testRemoveAttributes() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 3));
		instance.removeAttributes(indices);
		assertEquals(3, instance.noAttributes());
		assertFalse(instance.containsKey(1));
		assertFalse(instance.containsKey(3));
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(2));
		assertTrue(instance.containsKey(4));
	}

	@Test
	public void testHashCode() {
		DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		DenseInstance instance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
		assertEquals(instance1.hashCode(), instance1.hashCode());
		assertEquals(instance1.hashCode(), instance2.hashCode());
		assertNotEquals(instance1.hashCode(), instance3.hashCode());
	}

	@Test
	public void testEquals() {
		DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		DenseInstance instance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
		DenseInstance instance4 = instance1.copy();
		assertTrue(instance1.equals(instance1));
		assertTrue(instance1.equals(instance2));
		assertFalse(instance1.equals(instance3));
		assertTrue(instance1.equals(instance4));
	}
}