import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceWithValidAttributes() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		assertEquals(3, denseInstance.noAttributes());
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
		assertFalse(denseInstance.isEmpty());
		assertEquals(Collections.unmodifiableSet(new HashSet<>(Arrays.asList(0, 1, 2))), denseInstance.keySet());
		assertEquals(Arrays.asList(1.0, 2.0, 3.0), new ArrayList<>(denseInstance.values()));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSettingValueThrowsException() {
		DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
		denseInstance.put(1, 4.0);
	}

	@Test
	public void testPutAllMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		Map<Integer, Double> map = new HashMap<>();
		map.put(1, 4.0);
		map.put(2, 5.0);

		denseInstance.putAll(map);

		assertEquals(5.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
	}

	@Test
	public void testRemoveAttributeMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		denseInstance.removeAttribute(2);

		assertEquals(3, denseInstance.noAttributes());
		assertArrayEquals(new double[] { 1.0, 2.0, 4.0 }, denseInstance.attributes, 0.0);
	}

	@Test
	public void testEqualsMethod() {
		double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
		double[] attributes2 = new double[] { 1.0, 2.0, 3.0 };

		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);

		assertTrue(denseInstance1.equals(denseInstance2));
		assertTrue(denseInstance2.equals(denseInstance1));
	}

	@Test
	public void testHashCodeMethod() {
		double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
		double[] attributes2 = new double[] { 1.0, 2.0, 3.0 };

		DenseInstance denseInstance1 = new DenseInstance(attributes1);
		DenseInstance denseInstance2 = new DenseInstance(attributes2);

		assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
	}

	@Test
	public void testCopyMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		DenseInstance copiedInstance = denseInstance.copy();

		assertEquals(denseInstance, copiedInstance);
		assertNotSame(denseInstance, copiedInstance);
	}

	@Test
	public void testContainKeyMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsKey(3));
	}

	@Test
	public void testContainsValueMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertFalse(denseInstance.containsValue(4.0));
	}

	@Test
	public void testEntrySetMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();

		assertEquals(3, entrySet.size());
		assertTrue(entrySet.contains(Map.entry(0, 1.0)));
		assertTrue(entrySet.contains(Map.entry(1, 2.0)));
		assertTrue(entrySet.contains(Map.entry(2, 3.0)));
	}

	@Test
	public void testGetMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		assertEquals(1.0, denseInstance.get(0), 0.0);
		assertEquals(2.0, denseInstance.get(1), 0.0);
		assertEquals(3.0, denseInstance.get(2), 0.0);
		assertNull(denseInstance.get(3));
	}

	@Test
	public void testKeySetMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		Set<Integer> keySet = denseInstance.keySet();

		assertEquals(3, keySet.size());
		assertTrue(keySet.contains(0));
		assertTrue(keySet.contains(1));
		assertTrue(keySet.contains(2));
		assertFalse(keySet.contains(3));
	}

	@Test
	public void testValueMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
		assertNull(denseInstance.value(3));
	}

	@Test
	public void testClearMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		denseInstance.clear();

		assertEquals(0, denseInstance.noAttributes());
		assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
	}

	@Test
	public void testToStringMethod() {
		double[] attributes = new double[] { 1.0, 2.0, 3.0 };
		DenseInstance denseInstance = new DenseInstance(attributes);

		assertEquals("{1.0, 2.0, 3.0;}", denseInstance.toString());
	}
}