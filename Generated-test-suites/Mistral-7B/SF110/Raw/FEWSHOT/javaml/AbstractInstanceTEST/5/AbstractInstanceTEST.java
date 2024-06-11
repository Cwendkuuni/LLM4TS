package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 3.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance1.getClass().hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 3.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(""));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(instance.getID(), instance.hashCode());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        Double value = iterator.next();
        assertEquals(1.0, value.doubleValue(), 0.001);
        value = iterator.next();
        assertEquals(2.0, value.doubleValue(), 0.001);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAdd() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Instance result = instance.add(2.0);
        Instance expected = new DenseInstance(new double[]{3.0, 4.0});

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        Instance result = instance1.add(instance2);
        Instance expected = new DenseInstance(new double[]{4.0, 6.0});

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    @Test
    public void testSubtract() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        Instance result = instance1.minus(instance2);
        Instance expected = new DenseInstance(new double[]{-2.0, -2.0});;

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    @Test
    public void testSubtractDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Instance result = instance.minus(2.0);
        Instance expected = new DenseInstance(new double[]{-1.0, -1.0});;

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        Instance result = instance1.multiply(2.0);
        Instance expected = new DenseInstance(new double[]{2.0, 4.0});

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    @Test
    public void testDivide() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        Double divisor = 2.0;
        Instance result = instance1.divide(divisor);
        Instance expected = new DenseInstance(new double[]{0.5, 1.0});

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveUsingIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0});
        Instance result = instance.sqrt();
        Instance expected = new DenseInstance(new double[]{2.0});

        assertEquals(expected.getClass(), result.getClass());
        assertArrayEquals(expected.getValues(), result.getValues());
    }

    private double[] assertArrayEquals(double[] expected, double[] actual) {
        assertArrayEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0.001);
        }
        return actual;
    }
}