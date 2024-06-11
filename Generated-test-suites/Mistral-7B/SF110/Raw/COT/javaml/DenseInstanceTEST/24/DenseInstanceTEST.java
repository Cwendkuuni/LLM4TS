package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

	@Test
	public void testConstructorWithDoubleArray() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(attributes, denseInstance.attributes, 0.0);
	}

	@Test
	public void testConstructorWithDoubleArrayAndClassValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, "Class Value");
		assertEquals("Class Value", denseInstance.classValue());
		assertArrayEquals(attributes, denseInstance.attributes, 0.0);
	}

	@Test
	public void testConstructorWithEmptyArray() {
		DenseInstance denseInstance = new DenseInstance(new double[0]);
		assertEquals(0, denseInstance.noAttributes());
		assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
	}

	@Test
	public void testValueMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testValueMethodWithNegativeIndex() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.value(-1);
	}

	@Test
	public void testValueMethodWithOutOfBoundsIndex() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertEquals(0.0, denseInstance.value(3), 0.0);
	}

	@Test
	public void testClearMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.clear();
		assertEquals(0, denseInstance.noAttributes());
		assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
	}

	@Test
	public void testContainsKeyMethodWithIntegerKey() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsKey(3));
		assertFalse(denseInstance.containsKey(null));
	}

	@Test
	public void testContainsValueMethodWithDoubleValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertFalse(denseInstance.containsValue(4.0));
		Set<Double> set = new HashSet<>(Arrays.asList(1.1, 2.0, 3.0));
		assertFalse(denseInstance.containsValue(set.toArray()[0]));
	}

	@Test
	public void testEntrySetMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			assertEquals(entry.getKey(), entry.getValue().intValue());
		}
	}

	@Test
	public void testGetMethodWithIntegerKey() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertEquals(1.0, denseInstance.get(0), 0.0);
		assertEquals(2.0, denseInstance.get(1), 0.0);
		assertEquals(3.0, denseInstance.get(2), 0.0);
		assertEquals(null, denseInstance.get(3));
	}

	@Test(expected = NullPointerException.class)
	public void testGetMethodWithNullKey() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.get(null);
	}

	@Test
	public void testIsEmptyMethod() {
		double[] attributes = new double[] {};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertTrue(denseInstance.isEmpty());
		double[] nonEmptyAttributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance nonEmptyDenseInstance = new DenseInstance(nonEmptyAttributes, null);
		assertFalse(nonEmptyDenseInstance.isEmpty());
	}

	@Test
	public void testKeySetMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		SortedSet<Integer> keySet = denseInstance.keySet();
		assertEquals(3, keySet.size());
		for (Integer key : keySet) {
			assertTrue(key >= 0);
			assertTrue(key < attributes.length);
		}
	}

	@Test
	public void testPutMethodWithIntegerKeyAndDoubleValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		double newValue = 4.0;
		double oldValue = denseInstance.attributes[0];
		denseInstance.put(0, newValue);
		assertEquals(newValue, denseInstance.value(0), 0.0);
		assertEquals(oldValue, denseInstance.attributes[0], 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutMethodWithNullKey() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.put(null, 4.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutMethodWithKeyOutOfBounds() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.put(4, 4.0);
	}

	@Test
	public void testPutAllMethodWithMap() {
		double[] initialAttributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance initialDenseInstance = new DenseInstance(initialAttributes, null);
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, 4.0);
		map.put(1, 5.0);
		map.put(2, 6.0);
		initialDenseInstance.putAll(map);
		assertEquals(6, initialDenseInstance.noAttributes());
		for (int i = 0; i < initialAttributes.length; ++i) {
			assertEquals(initialAttributes[i], initialDenseInstance.attributes[i], 0.0);
		}
		assertEquals(4.0, initialDenseInstance.attributes[0], 0.0);
		assertEquals(5.0, initialDenseInstance.attributes[1], 0.0);
		assertEquals(6.0, initialDenseInstance.attributes[2], 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveMethodWithIntegerKey() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.remove(0);
	}

	@Test
	public void testRemoveAttributesMethodWithSet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
		denseInstance.removeAttributes(indicesToRemove);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(Arrays.asList(1.0, 3.0, 5.0), Arrays.asList(denseInstance.attributes));
	}

	@Test
	public void testSizeMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(3, denseInstance.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSizeMethodDeprecated() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.size();
	}

	@Test
	public void testValuesMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		Collection<Double> values = denseInstance.values();
		assertEquals(3, values.size());
		for (Double value : values) {
			assertEquals(value, denseInstance.attributes[Collections.indexof(denseInstance.attributes, value.doubleValue())], 0.0);
		}
	}

	@Test
	public void testRemoveAttributeMethodWithIndex() {
		double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		denseInstance.removeAttribute(2);
		assertEquals(4, denseInstance.noAttributes());
		assertEquals(Arrays.asList(1.0, 2.0, 4.0, 5.0), Arrays.asList(denseInstance.attributes));
	}

	@Test
	public void testHashCodeMethod() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1, null);
		double[] attributes2 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance2 = new DenseInstance(attributes2, null);
		double[] attributes3 = new double[] {1.1, 2.1, 3.1};
		DenseInstance denseInstance3 = new DenseInstance(attributes3, null);
		assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
		assertEquals(denseInstance1.hashCode(), Arrays.hashCode(denseInstance1.attributes));
		assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
		assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
		assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
	}

	@Test
	public void testEqualsMethodWithSameInstance() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes, null);
		assertTrue(denseInstance1.equals(denseInstance1));
	}

	@Test
	public void testEqualsMethodWithDifferentClass() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes, null);
		String string = "Test String";
		assertFalse(denseInstance1.equals(string));
	}

	@Test
	public void testEqualsMethodWithSameAttributesButDifferentClassValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes, "Class Value 1");
		DenseInstance denseInstance2 = new DenseInstance(attributes, "Class Value 2");
		assertFalse(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testEqualsMethodWithDifferentAttributes() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1, null);
		double[] attributes2 = new double[] {1.1, 2.1, 3.1};
		DenseInstance denseInstance2 = new DenseInstance(attributes2, null);
		assertFalse(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testCopyMethod() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes, null);
		DenseInstance denseInstance2 = denseInstance1.copy();
		assertEquals(3, denseInstance2.noAttributes());
		assertEquals(denseInstance1.attributes, denseInstance2.attributes);
		assertNotSame(denseInstance1, denseInstance2);
	}

	@Test
	public void testRemoveAttributesMethodWithEmptySet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes, null);
		Set<Integer> emptySet = Collections.emptySet();
		denseInstance.removeAttributes(emptySet);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(Arrays.asList(attributes), Arrays.asList(denseInstance.attributes));
	}
}