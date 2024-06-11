package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

import org.junit.Test;

public class SparseInstanceTest {

	@Test
	public void testEmptySparseInstance() {
		SparseInstance instance = new SparseInstance();
		assertEquals(0, instance.noAttributes());
		assertTrue(instance.isEmpty());
		assertFalse(instance.containsKey(null));
		assertFalse(instance.containsValue(null));
		assertNull(instance.get(null));
		assertEquals(instance.hashCode(), instance.hashCode());
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(new Object()));
	}

	@Test
	public void testSparseInstanceWithDefaultValue() {
		SparseInstance instance = new SparseInstance(10, 0.0);
		assertEquals(10, instance.noAttributes());
		assertFalse(instance.isEmpty());
		assertFalse(instance.containsKey(null));
		assertFalse(instance.containsValue(null));
		assertEquals(0.0, instance.get(null), 0.0);
		assertNull(instance.get(11));
		assertEquals(0.0, instance.get(5), 0.0);
		assertEquals(instance.hashCode(), instance.hashCode());
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(new Object()));
	}

	@Test
	public void testSparseInstanceWithData() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		assertEquals(3, instance.noAttributes());
		assertFalse(instance.isEmpty());
		assertFalse(instance.containsKey(null));
		assertFalse(instance.containsValue(null));
		assertNull(instance.get(null));
		assertEquals(1.1, instance.get(0), 0.01);
		assertEquals(2.2, instance.get(1), 0.01);
		assertEquals(3.3, instance.get(2), 0.01);
		assertEquals(instance.hashCode(), instance.hashCode());
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(new Object()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeNoAttributes() {
		new SparseInstance(-1);
	}

	@Test(expected = NullPointerException.class)
	public void testNullClassValue() {
		new SparseInstance(1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullDataVector() {
		new SparseInstance(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeDataVectorIndex() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		instance.removeAttribute(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveAttributeIndexOutOfBounds() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		instance.noAttributes();
		instance.removeAttribute(instance.noAttributes());
	}

	@Test
	public void testSetNoAttributes() {
		SparseInstance instance = new SparseInstance(10, 0.0);
		instance.setNoAttributes(5);
		assertEquals(5, instance.noAttributes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetNoAttributesNegative() {
		SparseInstance instance = new SparseInstance(10, 0.0);
		instance.setNoAttributes(-1);
	}

	@Test
	public void testClear() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		instance.clear();
		assertEquals(0, instance.noAttributes());
		assertTrue(instance.isEmpty());
		assertFalse(instance.containsKey(null));
		assertFalse(instance.containsValue(null));
		assertNull(instance.get(1));
	}

	@Test
	public void testContainsKey() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertFalse(instance.containsKey(3));
		assertFalse(instance.containsKey(4));
		assertFalse(instance.containsKey(null));
	}

	@Test
	public void testContainsValue() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		assertTrue(instance.containsValue(1.1));
		assertTrue(instance.containsValue(2.2));
		assertTrue(instance.containsValue(3.3));
		assertFalse(instance.containsValue(4.4));
		assertFalse(instance.containsValue(null));
	}

	@Test
	public void testEntrySet() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		Set<Map.Entry<Integer, Double>> set = instance.entrySet();
		assertEquals(3, set.size());
		for (Map.Entry<Integer, Double> entry : set) {
			assertTrue(instance.containsKey(entry.getKey()));
			assertEquals(entry.getValue(), instance.get(entry.getKey()));
		}
	}

	@Test
	public void testPut() {
		SparseInstance instance = new SparseInstance();
		instance.put(Integer.valueOf(0), Double.valueOf(1.1));
		instance.put(Integer.valueOf(1), Double.valueOf(2.2));
		instance.put(Integer.valueOf(2), Double.valueOf(3.3));
		assertEquals(3, instance.noAttributes());
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertEquals(1.1, instance.get(0), 0.01);
		assertEquals(2.2, instance.get(1), 0.01);
		assertEquals(3.3, instance.get(2), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutNullKey() {
		SparseInstance instance = new SparseInstance();
		instance.put(null, Double.valueOf(1.1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPutNullValue() {
		SparseInstance instance = new SparseInstance();
		instance.put(Integer.valueOf(0), null);
	}

	@Test
	public void testPutAll() {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(Integer.valueOf(0), Double.valueOf(1.1));
		map.put(Integer.valueOf(1), Double.valueOf(2.2));
		map.put(Integer.valueOf(2), Double.valueOf(3.3));

		SparseInstance instance = new SparseInstance();
		instance.putAll(map);

		assertEquals(3, instance.noAttributes());
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsKey(1));
		assertTrue(instance.containsKey(2));
		assertEquals(1.1, instance.get(0), 0.01);
		assertEquals(2.2, instance.get(1), 0.01);
		assertEquals(3.3, instance.get(2), 0.01);
	}

	@Test
	public void testRemove() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		Double value = instance.remove(Integer.valueOf(1));
		assertEquals(2.2, value.doubleValue(), 0.01);
		assertEquals(2, instance.noAttributes());
		assertFalse(instance.containsKey(1));
		assertEquals(1.1, instance.get(0), 0.01);
		assertEquals(3.3, instance.get(1), 0.01);
	}

	@Test
	public void testSize() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		assertEquals(3, instance.size());
	}

	@Test(expected = DeprecatedException.class)
	public void testSizeDeprecated() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		instance.size();
	}

	@Test
	public void testValues() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		List<Double> list = new ArrayList<Double>(instance.values());
		assertEquals(3, list.size());
		assertEquals(1.1, list.get(0), 0.01);
		assertEquals(2.2, list.get(1), 0.01);
		assertEquals(3.3, list.get(2), 0.01);
	}

	@Test
	public void testNoAttributes() {
		SparseInstance instance = new SparseInstance(new double[] { 1.1, 2.2, 3.3 });
		assertEquals(3, instance.noAttributes());
		instance.removeAttribute(0);
		assertEquals(2, instance.noAttributes());
		instance.removeAttribute(1);
		assertEquals(1, instance.noAttributes());
		instance.removeAttribute(2);
		assertEquals(0, instance.noAttributes());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveAttributeUnsupported() {
		SparseInstance instance = new SparseInstance();
		instance.removeAttribute(0);
	}
}