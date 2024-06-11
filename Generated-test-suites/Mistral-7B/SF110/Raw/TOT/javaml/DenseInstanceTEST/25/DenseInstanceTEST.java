package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

	@Test
	public void testDenseInstanceConstruction() {
		// Test empty instance
		DenseInstance emptyInstance = new DenseInstance();
		assertEquals(0, emptyInstance.noAttributes());
		assertNull(emptyInstance.getClassValue());
		assertNull(emptyInstance.value(0));
		assertTrue(emptyInstance.isEmpty());

		// Test instance with attributes
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(attributes);
		assertEquals(3, instance.noAttributes());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(2.0, instance.value(1), 0.0);
		assertEquals(3.0, instance.value(2), 0.0);
		assertFalse(instance.isEmpty());
		assertNotNull(instance.getClassValue());

		// Test instance with class value and attributes
		Object classValue = "classValue";
		DenseInstance instanceWithClassValue = new DenseInstance(attributes, classValue);
		assertEquals(classValue, instanceWithClassValue.getClassValue());

		// Test instance with size
		DenseInstance instanceWithSize = new DenseInstance(5);
		assertEquals(5, instanceWithSize.noAttributes());

		// Test null attributes
		try {
			new DenseInstance(null);
			fail("Should not be able to create an instance with null attributes.");
		} catch (NullPointerException e) {
			// Expected
		}

		try {
			new DenseInstance(new int[0]);
			fail("Should not be able to create an instance with an empty int array.");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testDenseInstanceMethods() {
		// Test get and set values
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(attributes);
		assertEquals(1.0, instance.value(0), 0.0);
		instance.put(0, 4.0);
		assertEquals(4.0, instance.value(0), 0.0);

		// Test containsKey and containsValue
		assertTrue(instance.containsKey(0));
		assertTrue(instance.containsValue(1.0));
		assertFalse(instance.containsKey(-1));
		assertFalse(instance.containsValue(4.0));

		// Test entrySet, get, and putAll
		Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
		assertEquals(3, entrySet.size());
		Map.Entry<Integer, Double> entry = entrySet.iterator().next();
		assertEquals(0, entry.getKey().intValue());
		assertEquals(1.0, entry.getValue().doubleValue(), 0.0);

		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(1, 5.0);
		instance.putAll(map);
		assertEquals(5.0, instance.value(1), 0.0);

		// Test keySet and values
		Set<Integer> keySet = instance.keySet();
		assertEquals(3, keySet.size());
		Collection<Double> values = instance.values();
		assertEquals(3, values.size());
		assertEquals(1.0, values.iterator().next(), 0.0);
		assertEquals(3.0, values.iterator().nextLast(), 0.0);

		// Test clear
		instance.clear();
		assertEquals(0, instance.noAttributes());
		assertNull(instance.getClassValue());
		assertTrue(instance.isEmpty());

		// Test removeAttribute
		instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
		instance.removeAttribute(2);
		assertEquals(3, instance.noAttributes());
		assertEquals(1.0, instance.value(0), 0.0);
		assertEquals(3.0, instance.value(1), 0.0);
		assertEquals(4.0, instance.value(2), 0.0);
		assertFalse(instance.containsKey(3));
	}

	@Test
	public void testDenseInstanceMethodsEdgeCases() {
		// Test get and set values with out of range index
		double[] attributes = new double[] {1.0, 2.0, 3.0};
		DenseInstance instance = new DenseInstance(attributes);
		try {
			instance.put(-1, 4.0);
			fail("Should not be able to set a value with a negative index.");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}

		try {
			instance.value(-1);
			fail("Should not be able to get a value with a negative index.");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}

		try {
			instance.put(4, 4.0);
			fail("Should not be able to set a value with an index greater than the attributes length.");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}

		try {
			instance.value(4);
			fail("Should not be able to get a value with an index greater than the attributes length.");
		} catch (IndexOutOfBoundsException e) {
			// Expected
		}

		// Test containsKey and containsValue with null and non-numeric keys
		assertFalse(instance.containsKey(null));
		assertFalse(instance.containsKey(""));
		assertFalse(instance.containsValue(null));
		assertFalse(instance.containsValue(""));

		// Test containsKey with an index greater than the attributes length
		assertFalse(instance.containsKey(4));

		// Test containsValue with a value not close enough to an attribute value
		assertFalse(instance.containsValue(4.1));

		// Test put with a null key
		try {
			instance.put(null, 4.0);
			fail("Should not be able to put a value with a null key.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test putAll with a null map
		try {
			instance.putAll(null);
			fail("Should not be able to putAll a null map.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test putAll with a map containing a null key or value
		Map<Integer, Double> mapWithNull = new HashMap<Integer, Double>();
		mapWithNull.put(null, 4.0);
		mapWithNull.put(0, null);
		try {
			instance.putAll(mapWithNull);
			fail("Should not be able to putAll a map with a null key or value.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test removeAttributes with empty set
		Set<Integer> emptySet = Collections.emptySet();
		instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
		instance.removeAttributes(emptySet);
		assertEquals(4, instance.noAttributes());
	}

	@Test
	public void testDenseInstanceMethodsErrorScenarios() {
		// Test get and set values with a null instance
		try {
			DenseInstance nullInstance = null;
			nullInstance.put(0, 4.0);
			fail("Should not be able to set a value on a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		try {
			nullInstance.value(0);
			fail("Should not be able to get a value from a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test containsKey and containsValue with a null instance
		DenseInstance instance = new DenseInstance();
		assertFalse(instance.containsKey(0));
		assertFalse(instance.containsValue(1.0));

		// Test entrySet, get, and putAll with a null instance
		try {
			instance.entrySet();
			fail("Should not be able to get the entry set from a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		try {
			instance.get(0);
			fail("Should not be able to get a value from a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		Map<Integer, Double> map = new HashMap<Integer, Double>();
		instance = null;
		try {
			instance.putAll(map);
			fail("Should not be able to putAll into a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test remove with a null instance
		try {
			instance.remove(0);
			fail("Should not be able to remove a value from a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test removeAttribute with a null instance
		instance = new DenseInstance();
		try {
			instance.removeAttribute(0);
			fail("Should not be able to remove an attribute from a null instance.");
		} catch (NullPointerException e) {
			// Expected
		}

		// Test hashCode and equals with different classes
		DenseInstance otherInstance = new ArrayList<Double>(){{add(1.0); add(2.0);}};
		assertNotEquals(instance.hashCode(), otherInstance.hashCode());
		assertFalse(instance.equals(otherInstance));

		// Test hashCode and equals with the same instance
		DenseInstance sameInstance = new DenseInstance(attributes);
		assertEquals(instance.hashCode(), sameInstance.hashCode());
		assertTrue(instance.equals(sameInstance));

		// Test hashCode and equals with a different instance but same attributes
		DenseInstance differentInstance = new DenseInstance(attributes.clone());
		assertEquals(instance.hashCode(), differentInstance.hashCode());
		assertTrue(instance.equals(differentInstance));
	}
}