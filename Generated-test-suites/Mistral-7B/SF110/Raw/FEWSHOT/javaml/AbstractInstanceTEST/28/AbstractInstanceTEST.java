package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testHashCodeAndEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(instance.getID(), AbstractInstance.nextID);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        assertEquals(2, iterator.nextCount());
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithoutClassValue() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructorWithClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testGetClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance();
        Object newClassValue = new Object();
        instance.setClassValue(newClassValue);
        assertEquals(newClassValue, instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});
        AbstractInstance result = instance1.minus(instance2);

        assertTrue(Arrays.equals(new double[]{0.5, 0.5}, result.toArray()));
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(1.0);

        assertTrue(Arrays.equals(new double[]{0.0, 1.0}, result.toArray()));
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 8.0});
        AbstractInstance result = instance.divide(2.0);

        assertTrue(Arrays.equals(new double[]{2.0, 4.0}, result.toArray()));
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.multiply(2.0);

        assertTrue(Arrays.equals(new double[]{2.0, 4.0}, result.toArray()));
    }

    @Test
    public void testMinusWithDoubleAndInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});
        AbstractInstance result = instance1.minus(instance2.minus(new DenseInstance(new double[]{1.0, 1.0})));

        assertTrue(Arrays.equals(new double[]{1.5, 0.5}, result.toArray()));
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{4.0, 8.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 4.0});
        AbstractInstance result = instance1.divide(instance2);

        assertTrue(Arrays.equals(new double[]{2.0, 2.0}, result.toArray()));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance result = instance1.multiply(instance2);

        assertTrue(Arrays.equals(new double[]{3.0, 8.0}, result.toArray()));
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.add(1.0);

        assertTrue(Arrays.equals(new double[]{2.0, 3.0}, result.toArray()));
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 1.0});
        AbstractInstance result = instance1.add(instance2);

        assertTrue(Arrays.equals(new double[]{2.0, 3.0}, result.toArray()));
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 16.0});
        AbstractInstance result = instance.sqrt();

        Map<Integer, Double> expected = new HashMap<>();
        expected.put(0, 2.0);
        expected.put(1, 4.0);
        assertTrue(Arrays.equals(expected.values().toArray(), result.toArray()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }
}