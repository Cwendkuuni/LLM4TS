package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceWithValidArguments() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
		assertEquals("{1.0, 2.0, 3.0; null}", denseInstance.toString());
	}

	@Test
	public void testDenseInstanceWithNullClassValue() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertNull(denseInstance.getClassValue());
	}

	@Test
	public void testDenseInstanceWithValidClassValue() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		String classValue = "Class1";
		DenseInstance denseInstance = new DenseInstance(attributes, classValue);
		assertEquals(classValue, denseInstance.getClassValue());
	}

	@Test
	public void testDenseInstanceWithEmptyArray() {
		DenseInstance denseInstance = new DenseInstance();
		assertEquals(0, denseInstance.noAttributes());
		assertNull(denseInstance.getClassValue());
	}

	@Test
	public void testDenseInstanceWithValidSize() {
		DenseInstance denseInstance = new DenseInstance(5);
		assertEquals(5, denseInstance.noAttributes());
		assertNull(denseInstance.getClassValue());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCantClearDenseInstance() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.clear();
	}

	@Test
	public void testContainsKeyWithValidInteger() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsKey(3));
	}

	@Test
	public void testContainsValueWithValidDouble() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertFalse(denseInstance.containsValue(4.0));
	}

	@Test
	public void testEntrySet() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			assertEquals(entry.getKey(), entry.getValue().intValue());
		}
	}

	@Test
	public void testGetWithValidInteger() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(1.0, denseInstance.get(0), 0.0);
		assertEquals(2.0, denseInstance.get(1), 0.0);
		assertEquals(3.0, denseInstance.get(2), 0.0);
		assertNull(denseInstance.get(3));
	}

	@Test
	public void testIsEmpty() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertFalse(denseInstance.isEmpty());
	}

	@Test
	public void testKeySet() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		SortedSet<Integer> keySet = denseInstance.keySet();
		assertEquals(3, keySet.size());
		for (Integer key : keySet) {
			assertEquals(key, denseInstance.value(key).intValue());
		}
	}

	@Test
	public void testPut() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.put(0, 5.0);
		assertEquals(5.0, denseInstance.value(0), 0.0);
	}

	@Test
	public void testPutAll() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 5.0);
		map.put(1, 6.0);
		denseInstance.putAll(map);
		assertEquals(5.0, denseInstance.value(0), 0.0);
		assertEquals(6.0, denseInstance.value(1), 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testCantRemoveValueFromDenseInstance() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.remove(0);
	}

	@Test
	public void testSize() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(3, denseInstance.size());
		assertEquals(3, denseInstance.noAttributes());
	}

	@Test
	public void testValues() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		Collection<Double> values = denseInstance.values();
		assertEquals(3, values.size());
		for (Double value : values) {
			assertEquals(value, denseInstance.value(values.indexOf(value)), 0.0);
		}
	}

	@Test
	public void testRemoveAttributeWithValidIndex() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.removeAttribute(2);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 2.0, 4.0 }, denseInstance.attributes, 0.01);
	}

	@Test
	public void testHashCode() {
		double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
		double[] attributes2 = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
	}

	@Test
	public void testEqualsWithSameInstance() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance1 = new DenseInstance(attributes);
		DenseInstance denseInstance2 = denseInstance1;
		assertTrue(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testEqualsWithDifferentInstanceButSameAttributes() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance1 = new DenseInstance(attributes);
		DenseInstance denseInstance2 = new DenseInstance(attributes.clone());
		assertTrue(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testEqualsWithDifferentInstanceButDifferentAttributes() {
		double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
		double[] attributes2 = new double[] { 4.0, 5.0, 6.0 };
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		assertFalse(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testCopy() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		DenseInstance copiedInstance = denseInstance.copy();
		assertEquals(denseInstance, copiedInstance);
		assertNotSame(denseInstance, copiedInstance);
		assertEquals(denseInstance.attributes, copiedInstance.attributes);
	}

	@Test
	public void testRemoveAttributesWithValidIndices() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(2, 4));
		denseInstance.removeAttributes(indicesToRemove);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 3.0, 5.0 }, denseInstance.attributes, 0.01);
	}

	@Test
	public void testRemoveAttributesWithEmptySet() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);
		Set<Integer> indicesToRemove = Collections.emptySet();
		denseInstance.removeAttributes(indicesToRemove);
		assertEquals(5, denseInstance.noAttributes());
	}
}