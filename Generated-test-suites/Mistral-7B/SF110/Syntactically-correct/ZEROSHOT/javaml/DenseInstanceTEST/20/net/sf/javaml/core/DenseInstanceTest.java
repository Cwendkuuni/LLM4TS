package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceWithEmptyAttributeArray() {
		DenseInstance instance = new DenseInstance();
		assertEquals(0, instance.noAttributes());
		assertTrue(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals(0, instance.size());
		assertEquals(Collections.emptyMap(), instance.entrySet());
		assertEquals(Collections.emptyList(), instance.values());
		assertEquals("{}", instance.toString());
	}

	@Test
	public void testDenseInstanceWithSingleAttribute() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0 });
		assertEquals(1, instance.noAttributes());
		assertFalse(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals(1, instance.size());
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(1, entrySet.size());
		Map.Entry<Integer, Double> entry = entrySet.iterator().next();
		assertEquals(0, (int) entry.getKey());
		assertEquals(1.0, entry.getValue(), 0.0);
		assertEquals(Collections.singletonMap(0, 1.0), instance.entrySet());
		assertEquals(Collections.singletonList(1.0), instance.values());
		assertEquals("{1.0}", instance.toString());
	}

	@Test
	public void testDenseInstanceWithMultipleAttributes() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(3, instance.noAttributes());
		assertFalse(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals(3, instance.size());
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			int key = (int) entry.getKey();
			assertEquals(key, entry.getKey());
			assertEquals(instance.value(key), entry.getValue(), 0.0);
		}
		assertEquals(Arrays.asList(1.0, 2.0, 3.0), instance.values());
		assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0}", instance.toString());
	}

	@Test
	public void testDenseInstanceWithClassValue() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, "classValue");
		assertEquals(3, instance.noAttributes());
		assertFalse(instance.isEmpty());
		assertEquals("classValue", instance.classValue().toString());
		assertEquals(3, instance.size());
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			int key = (int) entry.getKey();
			assertEquals(key, entry.getKey());
			assertEquals(instance.value(key), entry.getValue(), 0.0);
		}
		assertEquals(Arrays.asList(1.0, 2.0, 3.0), instance.values());
		assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0}", instance.toString());
	}

	@Test
	public void testGetValue() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		try {
			instance.value(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
	}

	@Test
	public void testClear() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		instance.clear();
		assertEquals(0, instance.noAttributes());
		assertTrue(instance.isEmpty());
		assertEquals(0, instance.size());
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(0, entrySet.size());
		assertEquals(Collections.emptyList(), instance.values());
		assertEquals("{}", instance.toString());
	}

	@Test
	public void testContainsKey() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertFalse(instance.containsKey(-1));
		assertFalse(instance.containsKey(3));
	}

	@Test
	public void testContainsValue() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 1.0, 2.0 });
		assertTrue(instance.containsValue(1.0));
		assertFalse(instance.containsValue(2.5));
	}

	@Test
	public void testEntrySet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		Map.Entry<Integer, Double> entry = entrySet.iterator().next();
		assertEquals(0, (int) entry.getKey());
		assertEquals(1.0, entry.getValue(), 0.0);
		entry = entrySet.iterator().next();
		assertEquals(1, (int) entry.getKey());
		assertEquals(2.0, entry.getValue(), 0.0);
		entry = entrySet.iterator().next();
		assertEquals(2, (int) entry.getKey());
		assertEquals(3.0, entry.getValue(), 0.0);
	}

	@Test
	public void testGet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(1.0, instance.get(0), 0.0);
		assertEquals(2.0, instance.get(1), 0.0);
		assertEquals(3.0, instance.get(2), 0.0);
		try {
			instance.get(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
	}

	@Test
	public void testIsEmpty() {
		DenseInstance instance = new DenseInstance();
		assertTrue(instance.isEmpty());
		instance = new DenseInstance(new double[] {});
		assertTrue(instance.isEmpty());
		instance = new DenseInstance(new double[] { 1.0 });
		assertFalse(instance.isEmpty());
	}

	@Test
	public void testKeySet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		SortedSet<Integer> keySet = instance.keySet();
		assertEquals(3, keySet.size());
		assertEquals(0, (int) keySet.first());
		assertEquals(2, (int) keySet.last());
		for (Integer key : keySet) {
			assertEquals(key, instance.value(key));
		}
	}

	@Test
	public void testPut() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		instance.put(2, 3.0);
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertEquals(3.0, instance.value(2), 0.0);
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		assertEquals(2, (int) entrySet.iterator().next().getKey());
		assertEquals(3.0, entrySet.iterator().next().getValue(), 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutAllWithNullKeySet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		Map<Integer, Double> map = null;
		instance.putAll(map);
	}

	@Test
	public void testPutAll() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 4.0);
		map.put(1, 5.0);
		instance.putAll(map);
		assertEquals(5, instance.noAttributes());
		assertEquals(5, instance.size());
		assertEquals(4.0, instance.value(0), 0.0);
		assertEquals(5.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(5, entrySet.size());
		assertEquals(0, (int) entrySet.iterator().next().getKey());
		assertEquals(4.0, entrySet.iterator().next().getValue(), 0.0);
		assertEquals(1, (int) entrySet.iterator().next().getKey());
		assertEquals(5.0, entrySet.iterator().next().getValue(), 0.0);
		assertEquals(2, (int) entrySet.iterator().next().getKey());
		assertEquals(3.0, entrySet.iterator().next().getValue(), 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		instance.remove(0);
	}

	@Test
	public void testRemoveAttributes() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
		Set<Integer> indices = new HashSet<>();
		indices.add(1);
		instance.removeAttributes(indices);
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		assertEquals(0, (int) entrySet.iterator().next().getKey());
		assertEquals(1.0, entrySet.iterator().next().getValue(), 0.0);
		assertEquals(2, (int) entrySet.iterator().next().getKey());
		assertEquals(3.0, entrySet.iterator().next().getValue(), 0.0);
	}

	@Test
	public void testSize() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(3, instance.size());
		instance = new DenseInstance(new double[] {});
		assertEquals(0, instance.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testValues() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		instance.values();
	}

	@Test
	public void testHashCode() {
		DenseInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance3 = new DenseInstance(new double[] { 1.1, 2.1, 3.1 });

		assertEquals(instance1.hashCode(), instance1.hashCode());
		assertEquals(instance1.hashCode(), instance2.hashCode());
		assertNotEquals(instance1.hashCode(), instance3.hashCode());
	}

	@Test
	public void testEquals() {
		DenseInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance3 = new DenseInstance(new double[] { 1.1, 2.1, 3.1 });

		assertTrue(instance1.equals(instance1));
		assertTrue(instance1.equals(instance2));
		assertFalse(instance1.equals(instance3));
	}

	@Test
	public void testCopy() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance copy = instance.copy();
		assertEquals(instance, copy);
		assertEquals(instance.noAttributes(), copy.noAttributes());
		assertEquals(instance.size(), copy.size());
		for (int i = 0; i < instance.noAttributes(); ++i) {
			assertEquals(instance.value(i), copy.value(i), 0.0);
			assertEquals(instance.getClassValue(), copy.getClassValue());
		}
	}

	@Test
	public void testRemoveAttribute() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
		instance.removeAttribute(1);
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		assertEquals(0, (int) entrySet.iterator().next().getKey());
		assertEquals(1.0, entrySet.iterator().next().getValue(), 0.0);
		assertEquals(2, (int) entrySet.iterator().next().getKey());
		assertEquals(3.0, entrySet.iterator().next().getValue(), 0.0);
	}
}