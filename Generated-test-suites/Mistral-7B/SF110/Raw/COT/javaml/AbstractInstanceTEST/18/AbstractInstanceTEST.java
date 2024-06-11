package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import org.junit.Test;

public class AbstractInstanceTest {

	// Test constructor
	@Test
	public void testAbstractInstanceConstructor() {
		AbstractInstance instance = new AbstractInstance();
		assertNotNull(instance);
		assertEquals(0, instance.getID());
		assertNull(instance.classValue());

		AbstractInstance instanceWithClassValue = new AbstractInstance("testClassValue");
		assertNotNull(instanceWithClassValue);
		assertEquals(AbstractInstance.nextID, instanceWithClassValue.getID());
		assertEquals("testClassValue", instanceWithClassValue.classValue());
	}

	// Test getID method
	@Test
	public void testGetID() {
		AbstractInstance instance = new AbstractInstance();
		assertEquals(0, instance.getID());

		AbstractInstance instanceWithID = new AbstractInstance();
		assertEquals(AbstractInstance.nextID, instanceWithID.getID());
		assertEquals(AbstractInstance.nextID + 1, new AbstractInstance().getID());
	}

	// Test iterator method
	@Test
	public void testIterator() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, 1.0);
		instance.put(1, 2.0);

		Iterator<Double> iterator = instance.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(1.0, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(2.0, iterator.next());
		assertFalse(iterator.hasNext());
	}

	// Test minus method with Instance
	@Test
	public void testMinusInstance() {
		AbstractInstance instance1 = new AbstractInstance();
		instance1.put(0, 1.0);
		instance1.put(1, 2.0);

		AbstractInstance instance2 = new AbstractInstance();
		instance2.put(0, 0.5);
		instance2.put(1, 1.5);

		AbstractInstance result = instance1.minus(instance2);

		assertEquals(2, result.noAttributes());
		assertEquals(-0.5, result.get(0), 0.001);
		assertEquals(-1.5, result.get(1), 0.001);
	}

	// Test minus method with double
	@Test
	public void testMinusDouble() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, 5.0);
		instance.put(1, 2.0);

		AbstractInstance result = instance.minus(3.0);

		assertEquals(2, result.noAttributes());
		assertEquals(2.0, result.get(0), 0.001);
		assertEquals(0.0, result.get(1), 0.001);
	}

	// Test divide method with double
	@Test
	public void testDivideDouble() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, 10.0);
		instance.put(1, 2.0);

		AbstractInstance result = instance.divide(2.0);

		assertEquals(2, result.noAttributes());
		assertEquals(5.0, result.get(0), 0.001);
		assertEquals(0.5, result.get(1), 0.001);
	}

	// Test multiply method with double
	@Test
	public void testMultiplyDouble() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, 2.0);
		instance.put(1, 3.0);

		AbstractInstance result = instance.multiply(3.0);

		assertEquals(2, result.noAttributes());
		assertEquals(6.0, result.get(0), 0.001);
		assertEquals(9.0, result.get(1), 0.001);
	}

	// Test equals method
	@Test
	public void testEquals() {
		AbstractInstance instance1 = new AbstractInstance();
		instance1.put(0, 1.0);
		instance1.put(1, 2.0);

		AbstractInstance instance2 = new AbstractInstance();
		instance2.put(0, 1.0);
		instance2.put(1, 2.0);

		AbstractInstance instance3 = new AbstractInstance();
		instance3.put(0, 1.1);
		instance3.put(1, 2.1);

		assertTrue(instance1.equals(instance1));
		assertTrue(instance1.equals(instance2));
		assertFalse(instance1.equals(instance3));
		assertFalse(instance1.equals(new Object()));
	}

	// Test hashCode method
	@Test
	public void testHashCode() {
		AbstractInstance instance1 = new AbstractInstance();
		instance1.put(0, 1.0);
		instance1.put(1, 2.0);

		AbstractInstance instance2 = new AbstractInstance();
		instance2.put(0, 1.0);
		instance2.put(1, 2.0);

		assertEquals(instance1.getID(), instance1.hashCode());
		assertEquals(instance1.hashCode(), instance2.hashCode());
		assertNotEquals(instance1.hashCode(), new Object().hashCode());
	}

	// Test minus method with negative numbers
	@Test
	public void testMinusNegativeNumbers() {
		AbstractInstance instance1 = new AbstractInstance();
		instance1.put(0, 3.0);
		instance1.put(1, -2.0);

		AbstractInstance instance2 = new AbstractInstance();
		instance2.put(0, -1.5);
		instance2.put(1, 4.5);

		AbstractInstance result = instance1.minus(instance2);

		assertEquals(2, result.noAttributes());
		assertEquals(4.5, result.get(0), 0.001);
		assertEquals(-0.5, result.get(1), 0.001);
	}

	// Test divide method with zero denominator
	@Test(expected = ArithmeticException.class)
	public void testDivideByZero() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, 5.0);
		instance.put(1, 0.0);

		instance.divide(new AbstractInstance());
	}

	// Test multiply method with null instance
	@Test(expected = NullPointerException.class)
	public void testMultiplyByNullInstance() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, 5.0);
		instance.put(1, 2.0);

		instance.multiply(null);
	}

	// Test add method with negative numbers
	@Test
	public void testAddNegativeNumbers() {
		AbstractInstance instance1 = new AbstractInstance();
		instance1.put(0, 3.0);
		instance1.put(1, -2.0);

		AbstractInstance instance2 = new AbstractInstance();
		instance2.put(0, -1.5);
		instance2.put(1, 4.5);

		AbstractInstance result = instance1.add(instance2);

		assertEquals(2, result.noAttributes());
		assertEquals(4.5, result.get(0), 0.001);
		assertEquals(2.0, result.get(1), 0.001);
	}

	// Test add method with large numbers
	@Test
	public void testAddLargeNumbers() {
		AbstractInstance instance1 = new AbstractInstance();
		Map<Integer, Double> map1 = new HashMap<>();
		map1.put(0, Double.MAX_VALUE);
		map1.put(1, Double.MAX_VALUE);
		instance1.putAll(map1);

		AbstractInstance instance2 = new AbstractInstance();
		Map<Integer, Double> map2 = new HashMap<>();
		map2.put(0, Double.MAX_VALUE + 1);
		map2.put(1, Double.MAX_VALUE + 1);
		instance2.putAll(map2);

		AbstractInstance result = instance1.add(instance2);

		assertEquals(2, result.noAttributes());
		assertEquals(2 * (Double.MAX_VALUE + 1), result.get(0), 0.001);
		assertEquals(2 * (Double.MAX_VALUE + 1), result.get(1), 0.001);
	}

	// Test sqrt method with negative numbers
	@Test(expected = IllegalArgumentException.class)
	public void testSqrtNegativeNumbers() {
		AbstractInstance instance = new AbstractInstance();
		instance.put(0, -1.0);

		instance.sqrt();
	}

	// Test sqrt method with large numbers
	@Test
	public void testSqrtLargeNumbers() {
		AbstractInstance instance = new AbstractInstance();
		Map<Integer, Double> map = new HashMap<>();
		map.put(0, Double.MAX_VALUE);
		instance.putAll(map);

		AbstractInstance result = instance.sqrt();

		assertEquals(2, result.noAttributes());
		assertEquals(Math.sqrt(Double.MAX_VALUE), result.get(0), 0.001);
		assertEquals(Math.sqrt(Double.MAX_VALUE), result.get(1), 0.001);
	}
}