package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceWithEmptyArray() {
		DenseInstance instance = new DenseInstance();
		assertEquals(0, instance.noAttributes());
		assertEquals(0, instance.size());
		assertTrue(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals("{}", instance.toString());
	}

	@Test
	public void testDenseInstanceWithSingleAttribute() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0 });
		assertEquals(1, instance.noAttributes());
		assertEquals(1, instance.size());
		assertFalse(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals("{1.0}", instance.toString());
		assertEquals(1.0, instance.value(0), 0.0);
	}

	@Test
	public void testDenseInstanceWithMultipleAttributes() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertFalse(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals("{1.0, 2.0, 3.0}", instance.toString());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
	}

	@Test
	public void testDenseInstanceWithClassValue() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, "ClassValue");
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertFalse(instance.isEmpty());
		assertEquals("ClassValue", instance.classValue().toString());
		assertEquals("{1.0, 2.0, 3.0;ClassValue}", instance.toString());
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
		try {
			instance.value(3);
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
		assertEquals(0, instance.size());
		assertTrue(instance.isEmpty());
		assertNull(instance.getClassValue());
		assertEquals("{}", instance.toString());
	}

	@Test
	public void testContainsKey() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertFalse(instance.containsKey(3));
		assertFalse(instance.containsKey(-1));
	}

	@Test
	public void testContainsValue() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertTrue(instance.containsValue(1.0));
		assertTrue(instance.containsValue(2.0));
		assertTrue(instance.containsValue(3.0));
		assertFalse(instance.containsValue(0.5));
		assertFalse(instance.containsValue(null));
	}

	@Test
	public void testEntrySet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			assertEquals(entry.getKey(), entry.getValue().intValue());
		}
	}

	@Test
	public void testGet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(1.0, instance.get(0), 0.0);
		assertEquals(2.0, instance.get(1), 0.0);
		assertEquals(3.0, instance.get(2), 0.0);
		try {
			instance.get(-1);
			fail("Expected IllegalArgumenException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
		try {
			instance.get(3);
			fail("Expected IllegalArgumenException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testIsEmpty() {
		DenseInstance instance = new DenseInstance();
		assertTrue(instance.isEmpty());
		instance.put(0, 1.0);
		assertFalse(instance.isEmpty());
		instance.clear();
		assertTrue(instance.isEmpty());
	}

	@Test
	public void testKeySet() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		SortedSet<Integer> keySet = instance.keySet();
		assertEquals(3, keySet.size());
		for (int i : keySet) {
			assertTrue(instance.containsKey(i));
		}
	}

	@Test
	public void testPut() {
		DenseInstance instance = new DenseInstance();
		instance.put(0, 1.0);
		assertEquals(1, instance.noAttributes());
		assertEquals(1, instance.size());
		assertEquals(1.0, instance.value(0), 0.0);
		instance.put(1, 2.0);
		assertEquals(2, instance.noAttributes());
		assertEquals(2, instance.size());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		try {
			instance.put(-1, 3.0);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
		try {
			instance.put(3, 4.0);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
	}

	@Test
	public void testPutAll() {
		DenseInstance instance = new DenseInstance();
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 1.0);
		map.put(1, 2.0);
		map.put(2, 3.0);
		instance.putAll(map);
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
	}

	@Test
	public void testRemoveAttribute() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		instance.removeAttribute(1);
		assertEquals(2, instance.noAttributes());
		assertEquals(2, instance.size());
		assertFalse(instance.containsKey(1));
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(3.0, instance.value(1), 0.0);
		try {
			instance.removeAttribute(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
		try {
			instance.removeAttribute(2);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}
	}

	@Test
	public void testHashCode() {
		DenseInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance3 = new DenseInstance(new double[] { 1.0, 2.0, 4.0 });
		assertEquals(instance1.hashCode(), instance1.hashCode());
		assertEquals(instance1.hashCode(), instance2.hashCode());
		assertNotEquals(instance1.hashCode(), instance3.hashCode());
	}

	@Test
	public void testEquals() {
		DenseInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance instance3 = new DenseInstance(new double[] { 1.0, 2.0, 4.0 });
		assertEquals(instance1, instance1);
		assertEquals(instance1, instance2);
		assertNotEquals(instance1, instance3);
		assertEquals(instance1, instance1.copy());
	}

	@Test
	public void testCopy() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance copiedInstance = instance.copy();
		assertEquals(instance, copiedInstance);
		instance.put(0, 4.0);
		assertNotEquals(instance, copiedInstance);
		assertEquals(1.0, copiedInstance.value(0), 0.0);
		assertEquals(2.0, copiedInstance.value(1), 0.0);
		assertEquals(3.0, copiedInstance.value(2), 0.0);
	}

	@Test
	public void testRemoveAttributes() {
		DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
		Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
		instance.removeAttributes(indicesToRemove);
		assertEquals(3, instance.noAttributes());
		assertEquals(3, instance.size());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(4.0, instance.value(2), 0.0);
		assertEquals(5.0, instance.value(4), 0.0);
		assertFalse(instance.containsKey(1));
		assertFalse(instance.containsKey(3));
	}
}