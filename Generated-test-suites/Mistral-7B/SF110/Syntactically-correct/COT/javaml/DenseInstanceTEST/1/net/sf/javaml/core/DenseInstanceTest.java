package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceConstructionWithDoubleArray() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertArrayEquals(attributes, denseInstance.attributes, 0.0);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(attributes.length, denseInstance.size());
	}

	@Test
	public void testDenseInstanceConstructionWithDoubleArrayAndClassValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		Object classValue = "Class1";
		DenseInstance denseInstance = new DenseInstance(attributes, classValue);
		assertArrayEquals(attributes, denseInstance.attributes, 0.0);
		assertEquals(classValue, denseInstance.getClassValue());
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(attributes.length, denseInstance.size());
	}

	@Test
	public void testDenseInstanceConstructionWithEmptyDoubleArrayAndClassValue() {
		double[] attributes = new double[0];
		Object classValue = "Class1";
		DenseInstance denseInstance = new DenseInstance(attributes, classValue);
		assertEquals(0, denseInstance.noAttributes());
		assertEquals(0, denseInstance.size());
		assertEquals(classValue, denseInstance.getClassValue());
	}

	@Test
	public void testDenseInstanceConstructionWithInt() {
		int size = 5;
		DenseInstance denseInstance = new DenseInstance(size);
		double[] expectedAttributes = new double[size];
		assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
		assertEquals(size, denseInstance.noAttributes());
		assertEquals(size, denseInstance.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDenseInstanceValueGettingOutOfBounds() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.value(-1);
	}

	@Test
	public void testDenseInstanceValueGetting() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
	}

	@Test
	public void testDenseInstanceClear() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.clear();
		assertEquals(0, denseInstance.noAttributes());
		assertEquals(0, denseInstance.size());
		assertNull(denseInstance.getClassValue());
		assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
	}

	@Test
	public void testDenseInstanceContainsKeyWithValidInteger() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsKey("invalidKey"));
	}

	@Test
	public void testDenseInstanceContainsKeyWithOutOfBoundsInteger() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertFalse(denseInstance.containsKey(-1));
		assertFalse(denseInstance.containsKey(3));
	}

	@Test
	public void testDenseInstanceContainsValueWithValidDouble() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertFalse(denseInstance.containsValue(4.0));
	}

	@Test
	public void testDenseInstanceContainsValueWithNearDouble() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertTrue(denseInstance.containsValue(1.5)); // 0.5 epsilone is considered
		// as equal
		assertFalse(denseInstance.containsValue(0.5));
	}

	@Test
	public void testDenseInstanceEntrySet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Set<Map.Entry<Integer, Double>> expectedSet = new HashSet<Map.Entry<Integer, Double>>();
		for (int i = 0; i < attributes.length; i++) {
			expectedSet.add(Map.entry(i, attributes[i]));
		}
		Set<Map.Entry<Integer, Double>> actualSet = denseInstance.entrySet();
		assertEquals(expectedSet, actualSet);
	}

	@Test
	public void testDenseInstanceGet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertEquals(1.0, denseInstance.get(0), 0.0);
		assertEquals(2.0, denseInstance.get(1), 0.0);
		assertEquals(3.0, denseInstance.get(2), 0.0);
		assertNull(denseInstance.get(-1));
		assertNull(denseInstance.get(3));
	}

	@Test
	public void testDenseInstanceIsEmpty() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		assertFalse(denseInstance.isEmpty());
	}

	@Test
	public void testDenseInstanceKeySet() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		SortedSet<Integer> expectedKeys = new TreeSet<Integer>();
		for (int i = 0; i < attributes.length; i++) {
			expectedKeys.add(i);
		}
		SortedSet<Integer> actualKeys = denseInstance.keySet();
		assertEquals(expectedKeys, actualKeys);
	}

	@Test
	public void testDenseInstancePut() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Double oldValue = denseInstance.put(0, 5.0);
		assertEquals(1.0, oldValue, 0.0);
		assertEquals(5.0, denseInstance.value(0), 0.0);
		assertEquals(5.0, denseInstance.get(0), 0.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDenseInstancePutWithOutOfBoundsInteger() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.put(-1, 5.0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDenseInstancePutWithNullValue() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.put(0, null);
	}

	@Test
	public void testDenseInstancePutAll() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		double[] attributes2 = new double[] {4.0, 5.0, 6.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(0, 7.0);
		map.put(1, 8.0);
		map.put(2, 9.0);
		denseInstance1.putAll(map);
		assertEquals(Arrays.toString(new double[] {7.0, 2.0, 3.0, 8.0, 9.0}),
				Arrays.toString(denseInstance1.attributes), 0.0);
		assertEquals(5, denseInstance1.noAttributes());
		assertEquals(5, denseInstance1.size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDenseInstanceRemove() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.remove(0);
	}

	@Test
	public void testDenseInstanceRemoveAttributes() {
		double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		Set<Integer> indicesToRemove = new HashSet<Integer>(Arrays.asList(1, 3));
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.removeAttributes(indicesToRemove);
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(3, denseInstance.size());
		assertArrayEquals(new double[] {1.0, 3.0, 5.0}, denseInstance.attributes, 0.0);
	}

	@Test
	public void testDenseInstanceHashCode() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		double[] attributes2 = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
	}

	@Test
	public void testDenseInstanceEqualsWithSameInstance() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes);
		DenseInstance denseInstance2 = denseInstance1;
		assertTrue(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testDenseInstanceEqualsWithDifferentInstanceButSameContent() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes);
		double[] attributesCopy = attributes.clone();
		DenseInstance denseInstance2 = new DenseInstance(attributesCopy);
		assertTrue(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testDenseInstanceEqualsWithDifferentInstanceButDifferentContent() {
		double[] attributes1 = new double[] {1.0, 2.0, 3.0};
		double[] attributes2 = new double[] {1.0, 2.0, 4.0};
		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);
		assertFalse(denseInstance1.equals(denseInstance2));
	}

	@Test
	public void testDenseInstanceCopy() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Instance copiedInstance = denseInstance.copy();
		assertEquals(attributes, copiedInstance.attributes, 0.0);
		assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
		assertEquals(denseInstance.noAttributes(), copiedInstance.noAttributes());
		assertEquals(denseInstance.size(), copiedInstance.size());
	}

	@Test
	public void testDenseInstanceCopyWithEmptyInstance() {
		DenseInstance denseInstance = new DenseInstance();
		Instance copiedInstance = denseInstance.copy();
		assertNull(copiedInstance.getClassValue());
		assertEquals(0, copiedInstance.noAttributes());
		assertEquals(0, copiedInstance.size());
		assertArrayEquals(new double[0], copiedInstance.attributes, 0.0);
	}

	@Test
	public void testDenseInstanceCopyWithAttributes() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		Instance copiedInstance = denseInstance.copy();
		assertEquals(attributes, copiedInstance.attributes, 0.0);
		assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
		assertEquals(denseInstance.noAttributes(), copiedInstance.noAttributes());
		assertEquals(denseInstance.size(), copiedInstance.size());
	}

	@Test
	public void testDenseInstanceRemoveAttributeWithOutOfBoundsIndex() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.removeAttribute(-1);
	}

	@Test
	public void testDenseInstanceRemoveAttributeWithValidIndex() {
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance denseInstance = new DenseInstance(attributes);
		denseInstance.removeAttribute(1);
		assertEquals(2, denseInstance.noAttributes());
		assertEquals(2, denseInstance.size());
		assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes, 0.0);
	}
}