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

/**
 * Test class for DenseInstance.
 */
public class DenseInstanceTest {

	@Test
	public void testConstructorWithDoubleArray() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, denseInstance.attributes, 0.0);
	}

	@Test
	public void testConstructorWithDoubleArrayAndClassValue() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, "ClassValue");
		assertEquals("ClassValue", denseInstance.classValue());
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, denseInstance.attributes, 0.0);
	}

	@Test
	public void testEmptyConstructor() {
		DenseInstance denseInstance = new DenseInstance();
		assertEquals(0, denseInstance.noAttributes());
		assertArrayEquals(new double[] {}, denseInstance.attributes, 0.0);
		assertNull(denseInstance.classValue());
	}

	@Test
	public void testConstructorWithSize() {
		DenseInstance denseInstance = new DenseInstance(5);
		assertEquals(5, denseInstance.noAttributes());
		assertArrayEquals(new double[5], denseInstance.attributes, 0.0);
		assertNull(denseInstance.classValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValueOutOfBounds() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		denseInstance.value(-1);
	}

	@Test
	public void testValue() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
	}

	@Test
	public void testClear() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		denseInstance.clear();
		assertEquals(0, denseInstance.noAttributes());
		assertArrayEquals(new double[] {}, denseInstance.attributes, 0.0);
	}

	@Test
	public void testContainsKey() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsKey(3));
		assertFalse(denseInstance.containsKey(new Integer(4)));
	}

	@Test
	public void testContainsValue() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertFalse(denseInstance.containsValue(0.5));
	}

	@Test
	public void testEntrySet() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
		assertEquals(3, entrySet.size());
		for (Map.Entry<Integer, Double> entry : entrySet) {
			assertTrue(entry.getKey() instanceof Integer);
			assertTrue(entry.getValue() instanceof Double);
		}
	}

	@Test
	public void testGet() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertEquals(1.0, denseInstance.get(0), 0.0);
		assertEquals(2.0, denseInstance.get(1), 0.0);
		assertEquals(3.0, denseInstance.get(2), 0.0);
		assertNull(denseInstance.get(3));
	}

	@Test
	public void testIsEmpty() {
		DenseInstance denseInstance = new DenseInstance();
		assertTrue(denseInstance.isEmpty());
		DenseInstance nonEmptyInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		assertFalse(nonEmptyInstance.isEmpty());
	}

	@Test
	public void testKeySet() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		SortedSet<Integer> keySet = denseInstance.keySet();
		assertEquals(3, keySet.size());
		for (Integer key : keySet) {
			assertTrue(key.intValue() >= 0);
			assertTrue(key.intValue() < denseInstance.noAttributes());
		}
	}

	@Test
	public void testPut() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		double oldValue = denseInstance.put(0, 5.0);
		assertEquals(1.0, oldValue, 0.0);
		assertEquals(5.0, denseInstance.value(0), 0.0);
		assertEquals(3, denseInstance.noAttributes());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutAllNotSupported() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(0, 5.0);
		map.put(1, 6.0);
		denseInstance.putAll(map);
	}

	@Test
	public void testRemoveAttribute() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
		denseInstance.removeAttribute(2);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 2.0, 4.0 }, denseInstance.attributes, 0.0);
	}

	@Test
	public void testHashCode() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance anotherInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance differentInstance = new DenseInstance(new double[] { 1.0, 2.1, 3.0 });
		assertEquals(denseInstance.hashCode(), anotherInstance.hashCode());
		assertNotEquals(denseInstance.hashCode(), differentInstance.hashCode());
	}

	@Test
	public void testEquals() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance anotherInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance differentInstance = new DenseInstance(new double[] { 1.0, 2.1, 3.0 });
		assertTrue(denseInstance.equals(anotherInstance));
		assertFalse(denseInstance.equals(differentInstance));
		assertFalse(denseInstance.equals(null));
		assertFalse(denseInstance.equals("String"));
	}

	@Test
	public void testCopy() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		DenseInstance copy = denseInstance.copy();
		assertEquals(3, copy.noAttributes());
		assertArrayEquals(denseInstance.attributes, copy.attributes);
		assertEquals("ClassValue", denseInstance.classValue());
		assertEquals("ClassValue", copy.classValue());
	}

	@Test
	public void testRemoveAttributes() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
		Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 4));
		denseInstance.removeAttributes(indices);
		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 3.0, 5.0 }, denseInstance.attributes);
	}
}