package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testConstructorWithArray() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(attributes, denseInstance.attributes, 0.0);
	}

	@Test
	public void testConstructorWithArrayAndClassValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		Object classValue = "label1";
		DenseInstance denseInstance = new DenseInstance(attributes, classValue);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(attributes, denseInstance.attributes, 0.0);
		assertEquals(classValue, denseInstance.classValue());
	}

	@Test
	public void testConstructorWithSize() {
		DenseInstance denseInstance = new DenseInstance(5);
		assertEquals(5, denseInstance.noAttributes());
		assertArrayEquals(new double[5], denseInstance.attributes, 0.0);
	}

	@Test
	public void testGetValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
	}

	@Test
	public void testClear() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.clear();
		assertEquals(0, denseInstance.noAttributes());
		assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
	}

	@Test
	public void testContainsKey() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsKey(3));
	}

	@Test
	public void testContainsValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertFalse(denseInstance.containsValue(4.0));
	}

	@Test
	public void testEntrySet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			assertEquals(entry.getKey(), entry.getValue().intValue());
		}
	}

	@Test
	public void testGet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(1.0, denseInstance.get(0), 0.0);
		assertEquals(2.0, denseInstance.get(1), 0.0);
		assertEquals(3.0, denseInstance.get(2), 0.0);
		assertNull(denseInstance.get(3));
	}

	@Test
	public void testIsEmpty() {
		double[] attributes = new double[] {};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.isEmpty());
		double[] nonEmptyAttributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance nonEmptyDenseInstance = new DenseInstance(nonEmptyAttributes);
		assertFalse(nonEmptyDenseInstance.isEmpty());
	}

	@Test
	public void testKeySet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		SortedSet<Integer> keySet = denseInstance.keySet();
		assertEquals(3, keySet.size());
		for (Integer key : keySet) {
			assertTrue(denseInstance.containsKey(key));
		}
	}

	@Test
	public void testPut() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		double newValue = 4.0;
		int index = 1;
		denseInstance.put(index, newValue);
		assertEquals(newValue, denseInstance.value(index), 0.0);
		assertEquals(4, denseInstance.noAttributes());
		assertArrayEquals(new double[] {1.0, 4.0, 3.0}, denseInstance.attributes, 0.0);
	}

	@Test
	public void testPutAll() {
		double[] initialAttributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance initialDenseInstance = new DenseInstance(initialAttributes);
		double[] newAttributes = new double[] {4.0, 5.0, 6.0};
		Map<Integer, Double> newMap = new HashMap<>();
		for (int i = 0; i < newAttributes.length; i++) {
			newMap.put(i, newAttributes[i]);
		}
		initialDenseInstance.putAll(newMap);
		assertEquals(6, initialDenseInstance.noAttributes());
		assertArrayEquals(new double[] {1.0, 4.0, 5.0, 3.0}, initialDenseInstance.attributes, 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.remove(0);
	}

	@Test
	public void testSize() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(3, denseInstance.size()); // Deprecated method
	}

	@Test
	public void testValues() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Collection<Double> values = denseInstance.values();
		assertEquals(3, values.size());
		for (Double value : values) {
			assertTrue(values.contains(value));
		}
	}

	@Test
	public void testRemoveAttribute() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.removeAttribute(1);
		assertEquals(2, denseInstance.noAttributes());
		assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes, 0.0);
	}

	@Test
	public void testHashCode() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		double[] attributes2 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		double[] attributes3 = new double[] {1.0, 2.1, 3.0};
		DenseInstance denseInstance3 = new DenseInstance(attributes3);
		assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
		assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode()); // Same attributes
		assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode()); // Different attributes
	}

	@Test
	public void testEquals() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		double[] attributes2 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		double[] attributes3 = new double[] {1.0, 2.1, 3.0};
		DenseInstance denseInstance3 = new DenseInstance(attributes3);
		assertTrue(denseInstance1.equals(denseInstance1));
		assertTrue(denseInstance1.equals(denseInstance2)); // Same attributes
		assertFalse(denseInstance1.equals(denseInstance3)); // Different attributes
	}

	@Test
	public void testCopy() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Instance copiedInstance = denseInstance.copy();
		assertEquals(denseInstance.noAttributes(), copiedInstance.noAttributes());
		assertArrayEquals(denseInstance.attributes, copiedInstance.attributes, 0.0);
		assertEquals(denseInstance.classValue(), copiedInstance.classValue());
	}

	@Test
	public void testRemoveAttributes() {
		double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
		denseInstance.removeAttributes(indicesToRemove);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] {1.0, 4.0}, denseInstance.attributes, 0.0);
	}
}