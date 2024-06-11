package net.sf.javaml.core.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

	@Test
	public void testGetID() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		assertSame(instance.getID(), instance.hashCode());
	}

	@Test
	public void testGetIterator() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		assertNotNull(instance.iterator());
	}

	@Test
	public void testEmptyConstructor() {
		AbstractInstance instance = new AbstractInstance();
		assertNotNull(instance);
		assertEquals(instance.getClass(), AbstractInstance.class);
		assertEquals(instance.getClassValue(), null);
		assertEquals(instance.getID(), 0);
	}

	@Test
	public void testConstructorWithClassValue() {
		AbstractInstance instance = new AbstractInstance("TestClassValue");
		assertNotNull(instance);
		assertEquals(instance.getClass(), AbstractInstance.class);
		assertEquals(instance.getClassValue(), "TestClassValue");
		assertEquals(instance.getID(), AbstractInstance.nextID);
		assertEquals(++AbstractInstance.nextID, 2);
	}

	@Test
	public void testClassValue() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		assertNull(instance.classValue());
		instance.setClassValue("TestClassValue");
		assertEquals(instance.classValue(), "TestClassValue");
	}

	@Test
	public void testSetClassValue() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		instance.setClassValue("TestClassValue");
		assertEquals(instance.classValue(), "TestClassValue");
	}

	@Test
	public void testMinusWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 0.5, 1.5 });
		AbstractInstance result = instance1.minus(instance2);
		assertNotNull(result);
		assertArrayEquals(new double[] { -0.5, -1.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testMinusWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance result = instance.minus(2.0);
		assertNotNull(result);
		assertArrayEquals(new double[] { -1.0, -1.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testDivideWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance result = instance.divide(2.0);
		assertNotNull(result);
		assertArrayEquals(new double[] { 0.5, 1.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testMultiplyWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance result = instance.multiply(2.0);
		assertNotNull(result);
		assertArrayEquals(new double[] { 2.0, 4.0 }, result.toArray(), 0.001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinusWithNullInstance() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		instance.minus(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinusWithNullDouble() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		instance.minus((Double)null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDivideWithNullDouble() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		instance.divide((Double)null);
	}

	@Test
	public void testDivideWithZero() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance result = instance.divide(0.0);
		assertNotNull(result);
		assertTrue(Arrays.stream(result.toArray()).allMatch(d -> Double.isInfinite(d)));
	}

	@Test
	public void testMultiplyWithZero() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance result = instance.multiply(0.0);
		assertNotNull(result);
		assertArrayEquals(new double[] { 0.0, 0.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testMultiplyWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
		AbstractInstance result = instance1.multiply(instance2);
		assertNotNull(result);
		assertArrayEquals(new double[] { 3.0*1.0, 4.0*1.0, 3.0*2.0, 4.0*2.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testDivideWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 0.5, 1.0 });
		AbstractInstance result = instance1.divide(instance2);
		assertNotNull(result);
		assertArrayEquals(new double[] { 2.0, 2.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testAddWithDouble() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance result = instance.add(2.0);
		assertNotNull(result);
		assertArrayEquals(new double[] { 3.0, 4.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testAddWithInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 0.5, 1.5 });
		AbstractInstance result = instance1.add(instance2);
		assertNotNull(result);
		assertArrayEquals(new double[] { 1.5, 3.5 }, result.toArray(), 0.001);
	}

	@Test
	public void testSqrt() {
		AbstractInstance instance = new DenseInstance(new double[] { 4.0, 9.0 });
		AbstractInstance result = instance.sqrt();
		assertNotNull(result);
		assertArrayEquals(new double[] { 2.0, 3.0 }, result.toArray(), 0.001);
	}

	@Test
	public void testHashCode() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance3 = new DenseInstance(new double[] { 3.0, 4.0 });
		assertEquals(instance1.hashCode(), instance1.getID());
		assertEquals(instance2.hashCode(), instance2.getID());
		assertEquals(instance1.hashCode(), instance2.hashCode());
		assertNotEquals(instance1.hashCode(), instance3.hashCode());
	}

	@Test
	public void testEqualsWithSameInstance() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		assertTrue(instance1.equals(instance1));
	}

	@Test
	public void testEqualsWithNullObject() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		assertFalse(instance.equals(null));
	}

	@Test
	public void testEqualsWithDifferentClass() {
		AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
		assertFalse(instance.equals(new Object()));
	}

	@Test
	public void testEqualsWithDifferentID() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
		assertFalse(instance1.equals(instance2));
	}

	@Test
	public void testEqualsWithSameIDButDifferentValues() {
		AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
		AbstractInstance instance2 = new DenseInstance(new double[] { 1.1, 2.1 });
		assertFalse(instance1.equals(instance2));
	}

	@Test
	public void testMinusWithEmptyInstance() {
		AbstractInstance instance = new DenseInstance(new double[0]);
		AbstractInstance result = instance.minus(new DenseInstance(new double[] { 1.0, 2.0 }));
		assertNotNull(result);
		assertTrue(result.noAttributes() > 0);
	}

	@Test
	public void testDivideWithZeroLengthInstance() {
		AbstractInstance instance = new DenseInstance(new double[0]);
		assertThrows(ArithmeticException.class, () -> instance.divide(new DenseInstance(new double[0])));
	}

	@Test
	public void testMultiplyWithEmptyInstance() {
		AbstractInstance instance = new DenseInstance(new double[0]);
		AbstractInstance result = instance.multiply(new DenseInstance(new double[] { 1.0, 2.0 }));
		assertNotNull(result);
		assertTrue(result.noAttributes() > 0);
	}
}