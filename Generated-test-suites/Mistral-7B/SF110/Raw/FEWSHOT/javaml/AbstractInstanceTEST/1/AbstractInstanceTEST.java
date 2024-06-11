package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(0, instance.getID());

        instance = new InstanceFactory().getInstance(new double[]{4.0, 5.0, 6.0}, NominalAttribute.class);
        assertEquals(1, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();
        assertEquals(3, iterator.nextCount());
        assertEquals(1.0, iterator.next());
        assertEquals(2.0, iterator.next());
        assertEquals(3.0, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue());
        assertEquals(0, instance.getID());

        instance = new AbstractInstance(new Object());
        assertEquals(1, instance.getID());
        assertEquals(new Object(), instance.classValue());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});

        AbstractInstance result = instance1.minus(instance2);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) - instance2.value(i), result.value(i), 0.001);
        }

        result = instance1.minus(1.0);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) - 1.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testMinusWithNull() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.minus(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDivide() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(instance1.multiply(0.5), instance1.divide(2.0));

        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 2.0});
        AbstractInstance result = instance1.divide(instance2);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) / instance2.value(i), result.value(i), 0.001);
        }

        result = instance1.divide(1.0);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) / 1.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testDivideWithZero() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.divide(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 2.0});

        AbstractInstance result = instance1.multiply(instance2);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) * instance2.value(i), result.value(i), 0.001);
        }

        result = instance1.multiply(2.0);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) * 2.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testMultiplyWithNull() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.multiply(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDivideWithNull() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.divide(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAdd() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});

        AbstractInstance result = instance1.add(instance2);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) + instance2.value(i), result.value(i), 0.001);
        }

        result = instance1.add(1.0);
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(instance1.value(i) + 1.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testAddWithNull() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});
        AbstractInstance result = instance.sqrt();
        assertNotNull(result);
        assertEquals(3, result.noAttributes());
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(Math.sqrt(instance.value(i)), result.value(i), 0.001);
        }
    }

    @Test
    public void testSqrtWithNegativeValue() {
        AbstractInstance instance = new DenseInstance(new double[]{-1.0, 4.0, 9.0});
        try {
            instance.sqrt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}