// JUnit 4 test file for AbstractInstance class

package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(AbstractInstance.nextID, instance.getID());
        AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertEquals(AbstractInstance.nextID + 1, instance2.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next(), 0.0);
        assertEquals(2.0, iterator.next(), 0.0);
        assertEquals(3.0, iterator.next(), 0.0);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 3.0});
        AbstractInstance expected = new DenseInstance(new double[]{-0.0, 1.0});;
        AbstractInstance result = instance1.minus(instance2);
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(1.0);
        AbstractInstance expected = new DenseInstance(new double[]{-1.0, -1.0});;
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMinusNull() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        instance.minus(null);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.divide(2.0);
        AbstractInstance expected = new DenseInstance(new double[]{0.5, 1.0});;
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideZero() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        instance.divide(0.0);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.multiply(2.0);
        AbstractInstance expected = new DenseInstance(new double[]{2.0, 4.0});;
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});
        AbstractInstance expected = new DenseInstance(new double[]{2.0, 6.0, 6.0, 6.0});;
        AbstractInstance result = instance1.multiply(instance2);
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test
    public void testAdd() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 1.0});
        AbstractInstance result = instance1.add(instance2);
        AbstractInstance expected = new DenseInstance(new double[]{2.0, 3.0});;
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.add(1.0);
        AbstractInstance expected = new DenseInstance(new double[]{2.0, 3.0});;
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0});
        AbstractInstance result = instance.sqrt();
        AbstractInstance expected = new DenseInstance(new double[]{1.0, 2.0});;
        assertArrayEquals(expected.getValues(), result.getValues(), 0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void testSqrtNegative() {
        AbstractInstance instance = new DenseInstance(new double[]{-1.0, 4.0});
        instance.sqrt();
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testNotEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 3.0});
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(instance1.getID(), instance2.getID());
    }

    @Test
    public void testHashCodeDifferent() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 3.0});
        assertNotSame(instance1.getID(), instance2.getID());
    }
}