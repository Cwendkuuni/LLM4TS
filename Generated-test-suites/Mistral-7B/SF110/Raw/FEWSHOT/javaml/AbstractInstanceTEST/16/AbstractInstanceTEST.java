package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testConstructor() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertEquals(5, instance.noAttributes());
        assertNull(instance.classValue());
        assertEquals(0, instance.getID());
    }

    @Test
    public void testConstructorWithClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new DenseInstance(new double[5], classValue);
        assertEquals(5, instance.noAttributes());
        assertSame(classValue, instance.classValue());
        assertEquals(AbstractInstance.nextID, instance.getID());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertEquals(AbstractInstance.nextID, instance.getID());
        AbstractInstance instance2 = new DenseInstance(new double[5]);
        assertEquals(AbstractInstance.nextID + 1, instance2.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Iterator<Double> iterator = instance.iterator();
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, iterator.toArray());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5, 3.5, 4.5});
        AbstractInstance result = instance1.minus(instance2);
        double[] expected = {0.5, 0.5, 0.5, 0.5, 0.5};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance result = instance.minus(2.0);
        double[] expected = {-1.0, -1.0, -1.0, -1.0, -1.0};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance result = instance.divide(2.0);
        double[] expected = {0.5, 1.0, 1.5, 2.0, 2.5};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance result = instance.multiply(3.0);
        double[] expected = {3.0, 6.0, 9.0, 12.0, 15.0};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertFalse(instance1.hashCode() == instance2.hashCode());
        assertFalse(instance1.hashCode() == instance3.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0, 4.0, 5.0});
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance result = instance1.multiply(instance2);
        double[] expected = {2.0, 4.0, 6.0, 8.0, 10.0};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{4.0, 8.0, 12.0, 16.0, 20.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 4.0, 6.0, 8.0, 10.0});
        AbstractInstance result = instance1.divide(instance2);
        double[] expected = {2.0, 2.0, 2.0, 2.0, 2.0};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance result = instance.add(2.0);
        double[] expected = {3.0, 4.0, 5.0, 6.0, 7.0};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5, 3.5, 4.5});
        AbstractInstance result = instance1.add(instance2);
        double[] expected = {1.5, 3.5, 5.5, 8.0, 9.5};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0, 16.0, 25.0});
        AbstractInstance result = instance.sqrt();
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0};
        assertArrayEquals(expected, result.toArray());
    }
}