package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceWithEmptyArray() {
		DenseInstance denseInstance = new DenseInstance();
		assertFalse(denseInstance.isEmpty());
		assertEquals(0, denseInstance.noAttributes());
		assertEquals(0, denseInstance.size());
		assertNull(denseInstance.getClassValue());
		assertEquals(0, denseInstance.attributes.length);
		assertNull(denseInstance.toString());
	}

	@Test
	public void testDenseInstanceWithClassValue() {
		DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 'A');
		assertEquals('A', denseInstance.classValue());
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(3, denseInstance.size());
		assertFalse(denseInstance.isEmpty());
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
		assertEquals(1.0, denseInstance.attributes[0]);
		assertEquals(2.0, denseInstance.attributes[1]);
		assertEquals(3.0, denseInstance.attributes[2]);
		assertEquals("{1.0, 2.0, 3.0;A}", denseInstance.toString());
		assertFalse(denseInstance.containsKey(-1));
		assertFalse(denseInstance.containsKey(3));
		assertTrue(denseInstance.containsKey(0));
		assertTrue(denseInstance.containsKey(1));
		assertTrue(denseInstance.containsKey(2));
		assertFalse(denseInstance.containsValue(4.0));
		assertFalse(denseInstance.containsValue(1.5));
		assertTrue(denseInstance.containsValue(1.0));
		assertTrue(denseInstance.containsValue(2.0));
		assertTrue(denseInstance.containsValue(3.0));
		assertEquals(3, denseInstance.keySet().size());
		assertEquals(3, denseInstance.entrySet().size());
		assertEquals(Arrays.asList(0.0, 1.0, 2.0), new ArrayList<Double>(denseInstance.values()));
		assertFalse(denseInstance.removeAttribute(1));
		assertEquals(3, denseInstance.noAttributes());
		assertEquals(3, denseInstance.size());
		assertEquals("{1.0, 2.0, 3.0;A}", denseInstance.toString());
		DenseInstance instanceWithOneAttributeRemoved = new DenseInstance(denseInstance);
		instanceWithOneAttributeRemoved.removeAttribute(0);
		assertEquals(2, instanceWithOneAttributeRemoved.noAttributes());
		assertEquals(2, instanceWithOneAttributeRemoved.size());
		assertEquals("{2.0, 3.0;A}", instanceWithOneAttributeRemoved.toString());
		assertEquals(1.0, denseInstance.hashCode());
		assertTrue(denseInstance.equals(new DenseInstance(new double[] {1.0, 2.0, 3.0}, 'A')));
		assertFalse(denseInstance.equals(new DenseInstance(new double[] {1.0, 2.0, 4.0}, 'A')));
		Instance instanceToCompare = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 'A');
		assertEquals(instanceToCompare, denseInstance.copy());
		SortedSet<Integer> keys = new TreeSet<>();
		keys.add(0);
		keys.add(1);
		keys.add(2);
		denseInstance.removeAttributes(keys);
		assertEquals(0, denseInstance.noAttributes());
		assertEquals(0, denseInstance.size());
		assertNull(denseInstance.toString());
	}

	@Test
	public void testDenseInstanceWithClassValueAndCustomSize() {
		DenseInstance denseInstance = new DenseInstance(5);
		assertFalse(denseInstance.isEmpty());
		assertEquals(5, denseInstance.noAttributes());
		assertEquals(5, denseInstance.size());
		assertNull(denseInstance.getClassValue());
		assertEquals(0.0, denseInstance.attributes[0], 0.0);
		assertEquals(0.0, denseInstance.attributes[1], 0.0);
		assertEquals(0.0, denseInstance.attributes[2], 0.0);
		assertEquals(0.0, denseInstance.attributes[3], 0.0);
		assertEquals(0.0, denseInstance.attributes[4], 0.0);
		assertNull(denseInstance.toString());
		denseInstance.setClassValue('A');
		assertEquals('A', denseInstance.classValue());
		assertEquals(5, denseInstance.noAttributes());
		assertEquals(5, denseInstance.size());
		assertEquals("{0.0, 0.0, 0.0, 0.0, 0.0;A}", denseInstance.toString());
		denseInstance.setValue(0, 1.0);
		assertEquals(1.0, denseInstance.value(0), 0.0);
		assertEquals("{1.0, 0.0, 0.0, 0.0, 0.0;A}", denseInstance.toString());
		denseInstance.setValue(1, 2.0);
		assertEquals(2.0, denseInstance.value(1), 0.0);
		assertEquals("{1.0, 2.0, 0.0, 0.0, 0.0;A}", denseInstance.toString());
		denseInstance.setValue(2, 3.0);
		assertEquals(3.0, denseInstance.value(2), 0.0);
		assertEquals("{1.0, 2.0, 3.0, 0.0, 0.0;A}", denseInstance.toString());
		denseInstance.setValue(3, 4.0);
		assertEquals(4.0, denseInstance.value(3), 0.0);
		assertEquals("{1.0, 2.0, 3.0, 4.0, 0.0;A}", denseInstance.toString());
		denseInstance.setValue(4, 5.0);
		assertEquals(5.0, denseInstance.value(4), 0.0);
		assertEquals("{1.0, 2.0, 3.0, 4.0, 5.0;A}", denseInstance.toString());
		assertFalse(denseInstance.isEmpty());
		HashMap<Integer, Double> map = new HashMap<>();
		for (int i = 0; i < denseInstance.noAttributes(); ++i) {
			map.put(i, denseInstance.getValue(i));
		}
		assertEquals(map, denseInstance.entrySet());
		assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0), new ArrayList<Double>(denseInstance.values()));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDenseInstanceRemoveValue() {
		DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		instance.remove(2);
	}
}