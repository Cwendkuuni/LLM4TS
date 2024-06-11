package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import org.junit.Test;

public class AbstractInstanceTest {

	@Test
	public void testGetID() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		assertEquals(instance.getID(), instance.ID);
	}

	@Test
	public void testIterator() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(0, 1.5);
		map.put(1, 2.5);

		AbstractInstance iteratorInstance = new DenseInstance(map.values().toArray());

		for (Double value : instance.iterator()) {
			assertEquals(value, iteratorInstance.iterator().next());
		}
	}

	@Test
	public void testNextID() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});

		assertEquals(instance1.getID(), 0);
		assertEquals(instance2.getID(), 1);
	}

	@Test
	public void testClassValue() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.setClassValue("TestClass");
		assertEquals(instance.classValue(), "TestClass");
	}

	@Test
	public void testSetClassValue() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.setClassValue("TestClass");
		assertEquals(instance.classValue(), "TestClass");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testMinusWithNullInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.minus(null);
	}

	@Test
	public void testMinusWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {0.5, 1.5});
		AbstractInstance result = instance1.minus(instance2);

		Map<Integer, Double> map1 = new HashMap<Integer, Double>();
		map1.put(0, 0.5);
		map1.put(1, 0.5);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertEquals(map1, mapResult);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testMinusWithDoubleAndNullInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.minus(null, 2.0);
	}

	@Test
	public void testMinusWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.minus(2.0);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {-1.0, -1.0}, mapResult.values().toArray(), 0.01);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDivideWithNullInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.divide(null);
	}

	@Test
	public void testDivideWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {2.0, 4.0});
		AbstractInstance result = instance1.divide(instance2);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {-0.5, -0.5}, mapResult.values().toArray(), 0.01);
	}

	@Test(expected = ArithmeticException.class)
	public void testDivideWithInstanceByZero() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {0.0, 0.0});
		instance1.divide(instance2);
	}

	@Test
	public void testMultiplyWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.multiply(3.0);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {3.0, 6.0}, mapResult.values().toArray(), 0.01);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testMultiplyWithNullInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.multiply(null);
	}

	@Test
	public void testEquals() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance3 = new DenseInstance(new double[] {1.5, 2.5});

		assertTrue(instance1.equals(instance2));
		assertFalse(instance1.equals(instance3));
		assertFalse(instance1.equals(null));
	}

	@Test
	public void testMinusWithInstanceAndDouble() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {0.5, 1.5});
		AbstractInstance result = instance1.minus(instance2, 3.0);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {-1.5, -0.5}, mapResult.values().toArray(), 0.01);
	}

	@Test
	public void testAddWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.add(2.0);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {3.0, 4.0}, mapResult.values().toArray(), 0.01);
	}

	@Test
	public void testAddWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.5, 2.5});
		AbstractInstance result = instance1.add(instance2);

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {2.5, 4.5}, mapResult.values().toArray(), 0.01);
	}

	@Test
	public void testSqrt() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 4.0});
		AbstractInstance result = instance.sqrt();

		Map<Integer, Double> mapResult = new HashMap<Integer, Double>();

		for (int i = 0; i < result.noAttributes(); ++i) {
			mapResult.put(i, result.get(i));
		}

		assertArrayEquals(new double[] {1.0, 2.0}, mapResult.values().toArray(), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinusWithWrongArguments() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.minus(new String[] {1.0, 2.0});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddWithWrongArguments() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.add(new String[] {1.0, 2.0});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinusWithInstanceAndWrongArguments() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.minus(new DenseInstance(new double[] {1.0, 2.0}), new String[] {1.0, 2.0});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddWithInstanceAndWrongArguments() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.add(new DenseInstance(new double[] {1.0, 2.0}), new String[] {1.0, 2.0});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultiplyWithWrongArguments() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.multiply(new String[] {1.0, 2.0});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDivideWithWrongArguments() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.divide(new String[] {1.0, 2.0});
	}
}