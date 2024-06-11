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
		assertEquals(instance.getID(), 0); // default ID

		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0}, 1);
		assertEquals(instance2.getID(), 1); // custom ID
	}

	@Test
	public void testIterator() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		Iterator<Double> iterator = instance.iterator();
		assertEquals(iterator.hasNext(), true);
		assertEquals(iterator.next(), 1.0);
		assertEquals(iterator.hasNext(), true);
		assertEquals(iterator.next(), 2.0);
		assertEquals(iterator.hasNext(), false);
	}

	@Test
	public void testConstructorWithoutClassValue() {
		AbstractInstance instance = new AbstractInstance();
		assertNull(instance.classValue());
	}

	@Test
	public void testConstructorWithClassValue() {
		AbstractInstance instance = new AbstractInstance("TestClass");
		assertEquals(instance.classValue(), "TestClass");
	}

	@Test(expected = NullPointerException.class)
	public void testMinusWithNullInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.minus(null);
	}

	@Test
	public void testMinusWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.minus(2.0);

		Map<String, Double> expected = new HashMap<>();
		expected.put("0", -1.0);
		expected.put("1", -1.5);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testMinusWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {0.5, 1.5});
		AbstractInstance result = instance1.minus(instance2);

		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 0.5);
		expected.put("1", 0.5);
		assertEquals(result.getAttributes(), expected);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDivideWithZero() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		instance.divide(0);
	}

	@Test
	public void testDivideWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {0.5, 1.0});
		AbstractInstance result = instance1.divide(instance2);

		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 2.0);
		expected.put("1", 2.5);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testMultiplyWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.multiply(2.0);

		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 2.0);
		expected.put("1", 4.0);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testMultiplyWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.5, 3.0});
		AbstractInstance result = instance1.multiply(instance2);

		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 1.5);
		expected.put("1", 6.0);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testHashCode() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0});
		assertEquals(instance1.hashCode(), instance2.hashCode());

		instance1.setClassValue("TestClass1");
		instance2.setClassValue("TestClass2");
		assertNotSame(instance1.hashCode(), instance2.hashCode());
	}

	@Test
	public void testEquals() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0});
		assertTrue(instance1.equals(instance2));

		AbstractInstance instance3 = new DenseInstance(new double[] {1.0, 3.0});
		assertFalse(instance1.equals(instance3));
	}

	@Test
	public void testMinusWithEmptyInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance emptyInstance = new DenseInstance(Collections.emptyMap());
		AbstractInstance result = instance.minus(emptyInstance);
		assertEquals(result.getAttributes(), instance.getAttributes());
	}

	@Test
	public void testMinusWithDifferentSizeInstance() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance differentSizeInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
		AbstractInstance result = instance.minus(differentSizeInstance);
		assertEquals(result.getAttributes().size(), instance.getAttributes().size());
		assertEquals(result.getAttributes(), instance.getAttributes());
	}

	@Test
	public void testDivideWithZeroAttributeInstance() {
		AbstractInstance instance = new DenseInstance(Collections.emptyMap());
		try {
			instance.divide(2.0);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testMultiplyWithZeroAttributeInstance() {
		AbstractInstance instance = new DenseInstance(Collections.emptyMap());
		try {
			instance.multiply(2.0);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testEqualsWithNullObject() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		assertFalse(instance.equals(null));
	}

	@Test
	public void testEqualsWithDifferentClass() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		String other = "TestString";
		assertFalse(instance.equals(other));
	}

	@Test
	public void testMinusWithSelf() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.minus(instance);
		assertEquals(result.getAttributes(), Collections.emptyMap());
	}

	@Test
	public void testMinusWithSelfWithCustomID() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0}, 1);
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0}, 1);
		AbstractInstance result = instance1.minus(instance2);
		assertEquals(result.getID(), 0);
	}

	@Test
	public void testMinusWithDifferentID() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0}, 1);
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0}, 2);
		AbstractInstance result = instance1.minus(instance2);
		assertEquals(result.getID(), 1);
	}

	@Test
	public void testDivideWithZeroDivisor() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {0.0, 0.0});
		try {
			instance1.divide(instance2);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testDivideWithZeroDividend() {
		AbstractInstance instance1 = new DenseInstance(new double[] {0.0, 0.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0});
		try {
			instance1.divide(instance2);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testMinusWithNaN() {
		AbstractInstance instance1 = new DenseInstance(new double[] {Double.NaN, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, Double.NaN});
		AbstractInstance result = instance1.minus(instance2);
		assertEquals(result.getAttributes().get("0"), Double.isNaN(instance1.getAttributes().get("0")) ? null : -1.0);
		assertEquals(result.getAttributes().get("1"), Double.isNaN(instance2.getAttributes().get("1")) ? null : 1.0);
	}

	@Test
	public void testMultiplyWithNaN() {
		AbstractInstance instance1 = new DenseInstance(new double[] {Double.NaN, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, Double.NaN});
		AbstractInstance result = instance1.multiply(instance2);
		assertEquals(result.getAttributes().get("0"), Double.isNaN(instance1.getAttributes().get("0")) ? null : Double.NaN);
		assertEquals(result.getAttributes().get("1"), Double.isNaN(instance2.getAttributes().get("1")) ? null : Double.NaN);
	}

	@Test
	public void testDivideWithNaN() {
		AbstractInstance instance1 = new DenseInstance(new double[] {Double.NaN, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, Double.NaN});
		try {
			instance1.divide(instance2);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testSqrtWithNegativeNumber() {
		AbstractInstance instance = new DenseInstance(new double[] {-1.0});
		try {
			instance.sqrt();
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testSqrtWithZeroNumber() {
		AbstractInstance instance = new DenseInstance(new double[] {0.0});
		AbstractInstance result = instance.sqrt();
		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 0.0);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testSqrtWithPositiveNumber() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0});
		AbstractInstance result = instance.sqrt();
		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 1.0);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testSqrtWithNaN() {
		AbstractInstance instance = new DenseInstance(new double[] {Double.NaN});
		try {
			instance.sqrt();
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testAddWithNaN() {
		AbstractInstance instance1 = new DenseInstance(new double[] {Double.NaN, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 3.0});
		AbstractInstance result = instance1.add(instance2);
		assertEquals(result.getAttributes().get("0"), Double.isNaN(instance1.getAttributes().get("0")) ? null : Double.NaN);
		assertEquals(result.getAttributes().get("1"), 4.0);
	}

	@Test
	public void testAddWithZero() {
		AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance result = instance.add(new DenseInstance(new double[] {0.0, 0.0}));
		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 1.0);
		expected.put("1", 2.0);
		assertEquals(result.getAttributes(), expected);
	}

	@Test
	public void testAddWithInstanceWithDifferentSize() {
		AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
		AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});
		AbstractInstance result = instance1.add(instance2);
		Map<String, Double> expected = new HashMap<>();
		expected.put("0", 4.0);
		expected.put("1", 6.0);
		expected.put("2", 4.0);
		expected.put("3", 6.0);
		assertEquals(result.getAttributes(), expected);
	}
}