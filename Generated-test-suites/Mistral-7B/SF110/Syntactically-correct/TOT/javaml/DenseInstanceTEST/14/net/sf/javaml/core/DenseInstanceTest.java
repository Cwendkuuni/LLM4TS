package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceCreation() {
		// Test default constructor
		DenseInstance instance = new DenseInstance();
		assertEquals(0, instance.noAttributes());
		assertNull(instance.classValue());
		assertEquals(0.0, instance.value(0), 0.0);
		assertTrue(instance.isEmpty());

		// Test constructors with double array
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance instance1 = new DenseInstance(attributes);
		assertEquals(3, instance1.noAttributes());
		assertNull(instance1.classValue());
		assertEquals(1.0, instance1.value(0), 0.0);
		assertEquals(2.0, instance1.value(1), 0.0);
		assertEquals(3.0, instance1.value(2), 0.0);
		assertFalse(instance1.isEmpty());

		// Test constructor with double array and classValue
		DenseInstance instance2 = new DenseInstance(attributes, "classValue");
		assertEquals(3, instance2.noAttributes());
		assertEquals("classValue", instance2.classValue().toString());
		assertEquals(1.0, instance2.value(0), 0.0);
		assertEquals(2.0, instance2.value(1), 0.0);
		assertEquals(3.0, instance2.value(2), 0.0);
		assertFalse(instance2.isEmpty());

		// Test constructor with int size
		DenseInstance instance3 = new DenseInstance(5);
		assertEquals(5, instance3.noAttributes());
		assertNull(instance3.classValue());
		assertEquals(0.0, instance3.value(0), 0.0);
		assertTrue(instance3.isEmpty());
	}

	@Test
	public void testValueGetting() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		assertEquals(4.0, instance.value(3), 0.0);
		assertEquals(5.0, instance.value(4), 0.0);
	}

	@Test
	public void testClear() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		instance.clear();
		assertEquals(0, instance.noAttributes());
		assertTrue(instance.isEmpty());
	}

	@Test
	public void testContainsKey() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertTrue(instance.containsKey(3));
		assertTrue(instance.containsKey(4));
		assertFalse(instance.containsKey(5));
		assertFalse(instance.containsKey(-1));
		assertFalse(instance.containsKey("key"));
	}

	@Test
	public void testContainsValue() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertTrue(instance.containsValue(1.0));
		assertTrue(instance.containsValue(2.0));
		assertTrue(instance.containsValue(3.0));
		assertTrue(instance.containsValue(4.0));
		assertTrue(instance.containsValue(5.0));
		assertFalse(instance.containsValue(10.0));
		assertFalse(instance.containsValue(-1.0));
		assertFalse(instance.containsValue(new Double(1.0)));
	}

	@Test
	public void testEntrySet() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(5, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			assertEquals(entry.getKey(), entry.getValue().intValue());
		}
	}

	@Test
	public void testGet() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertEquals(1.0, instance.get(0), 0.0);
		assertEquals(2.0, instance.get(1), 0.0);
		assertEquals(3.0, instance.get(2), 0.0);
		assertEquals(4.0, instance.get(3), 0.0);
		assertEquals(5.0, instance.get(4), 0.0);
		assertNull(instance.get(5));
		assertNull(instance.get(-1));
		assertNull(instance.get("key"));
	}

	@Test
	public void testIsEmpty() {
		DenseInstance instance = new DenseInstance();
		assertTrue(instance.isEmpty());

		instance = new DenseInstance(new double[] {});
		assertTrue(instance.isEmpty());

		instance = new DenseInstance(new double[] {1.0});
		assertFalse(instance.isEmpty());

		instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertFalse(instance.isEmpty());
	}

	@Test
	public void testKeySet() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		SortedSet<Integer> keySet = instance.keySet();
		assertEquals(5, keySet.size());
		assertTrue(keySet.contains(0));
		assertTrue(keySet.contains(1));
		assertTrue(keySet.contains(2));
		assertTrue(keySet.contains(3));
		assertTrue(keySet.contains(4));
		assertFalse(keySet.contains(5));
		assertFalse(keySet.contains(-1));
		assertFalse(keySet.contains("key"));
	}

	@Test
	public void testPut() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		Double oldValue = instance.put(2, 6.0);
		assertEquals(3.0, oldValue, 0.0);
		assertEquals(6.0, instance.value(2), 0.0);
		assertEquals(6.0, instance.get(2), 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutUnsupportedOperationException() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		instance.put(2, "value");
	}

	@Test
	public void testPutAll() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		Map<Integer, Double> map = new HashMap<>();
		map.put(3, 4.0);
		map.put(4, 5.0);
		instance.putAll(map);
		assertEquals(4, instance.noAttributes());
		assertEquals(4.0, instance.value(3), 0.0);
		assertEquals(5.0, instance.value(4), 0.0);
	}

	@Test
	public void testRemoveAttribute() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		instance.removeAttribute(2);
		assertEquals(4, instance.noAttributes());
		assertFalse(instance.containsKey(2));
		assertFalse(instance.containsValue(3.0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveUnsupportedOperationException() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		instance.removeAttribute(2);
		instance.removeAttribute(3);
		instance.removeAttribute(-1);
		instance.removeAttribute("key");
	}

	@Test
	public void testSize() {
		DenseInstance instance = new DenseInstance();
		assertEquals(0, instance.size());

		instance = new DenseInstance(new double[] {1.0});
		assertEquals(1, instance.size());

		instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertEquals(5, instance.size());
	}

	@Test
	public void testValues() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		Collection<Double> values = instance.values();
		assertEquals(5, values.size());
		assertTrue(values.contains(1.0));
		assertTrue(values.contains(2.0));
		assertTrue(values.contains(3.0));
		assertTrue(values.contains(4.0));
		assertTrue(values.contains(5.0));
	}

	@Test
	public void testNoAttributes() {
		DenseInstance instance = new DenseInstance();
		assertEquals(0, instance.noAttributes());

		instance = new DenseInstance(new double[] {1.0});
		assertEquals(1, instance.noAttributes());

		instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
		assertEquals(5, instance.noAttributes());
	}
}