package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(instance.getID(), 0);

        instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(instance.getID(), 1);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();
        assertEquals(iterator.next(), 1.0);
        assertEquals(iterator.next(), 2.0);
        assertFalse(iterator.hasNext());

        instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        iterator = instance.iterator();
        assertEquals(iterator.next(), 1.0);
        assertEquals(iterator.next(), 2.0);
        assertEquals(iterator.next(), 3.0);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue());
        assertEquals(instance.getID(), 0);

        instance = new AbstractInstance(new Object());
        assertNotNull(instance.classValue());
        assertEquals(instance.getID(), 1);
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        instance.setClassValue("foo");
        assertEquals("foo", instance.classValue());

        instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.setClassValue(new Object());
        assertEquals(instance.classValue(), new Object());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});

        AbstractInstance result = instance1.minus(instance2);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance1.noAttributes());

        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(instance1.value(i) - instance2.value(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.minus(1.0);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance.noAttributes());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i) - 1.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.divide(2.0);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance.noAttributes());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i) / 2.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.multiply(2.0);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance.noAttributes());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i) * 2.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 1.0});

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 1.0});
        Object obj = new Object();

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(obj));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});

        AbstractInstance result = instance1.multiply(instance2);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance1.noAttributes());

        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(instance1.value(i) * instance2.value(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0});

        AbstractInstance result = instance1.divide(instance2);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance1.noAttributes());

        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(instance1.value(i) / instance2.value(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.add(1.0);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance.noAttributes());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i) + 1.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});

        AbstractInstance result = instance1.add(instance2);
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance1.noAttributes());

        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(instance1.value(i) + instance2.value(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 9.0});

        AbstractInstance result = instance.sqrt();
        assertNotNull(result);
        assertEquals(result.noAttributes(), instance.noAttributes());

        for (int i = 0; i < instance.noAttributes(); i++) {
            double expected = Math.sqrt(instance.value(i));
            assertEquals(expected, result.value(i), 0.001);
        }
    }
}