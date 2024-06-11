package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertEquals(0, instance.getID());

        instance = new DenseInstance(new double[5], 10);
        assertEquals(10, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        List<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);

        Iterator<Double> iterator = instance.iterator();
        for (Double value : expectedValues) {
            assertEquals(value, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testProtectedConstructor() {
        AbstractInstance instance = new AbstractInstance() {
            // Anonymous class extending AbstractInstance
        };
        assertNotNull(instance);
    }

    @Test
    public void testProtectedConstructorWithClassValue() {
        AbstractInstance instance = new AbstractInstance(new Object()) {
            // Anonymous class extending AbstractInstance
        };
        assertNotNull(instance);
        assertEquals(instance.classValue(), new Object());
    }

    @Test
    public void testGetClassValue() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertNull(instance.classValue());

        instance = new DenseInstance(new double[5], new Object());
        assertEquals(instance.classValue(), new Object());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        instance.setClassValue(new Object());
        assertEquals(instance.classValue(), new Object());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});
        AbstractInstance result = instance1.minus(instance2);

        List<Double> expectedValues = Arrays.asList(-1.0, -1.0);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(3.0);

        List<Double> expectedValues = Arrays.asList(-2.0, -1.0);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.divide(3.0);

        List<Double> expectedValues = Arrays.asList(0.3333333333333333, 0.6666666666666667);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.multiply(3.0);

        List<Double> expectedValues = Arrays.asList(3.0, 6.0);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[5]);
        AbstractInstance instance2 = new DenseInstance(new double[5]);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        instance1 = new DenseInstance(new double[]{1.0, 2.0});
        instance2 = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(instance1.hashCode(), instance2.hashCode());

        instance1 = new DenseInstance(new double[]{1.0, 2.0});
        instance2 = new SparseInstance(new double[]{1.0, 2.0});
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEqualsWithNull() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertFalse(instance.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        AbstractInstance instance1 = new DenseInstance(new double[5]);
        Object obj = new Object();
        assertFalse(instance1.equals(obj));
    }

    @Test
    public void testEqualsWithDifferentIDs() {
        AbstractInstance instance1 = new DenseInstance(new double[5]);
        AbstractInstance instance2 = new DenseInstance(new double[5]);
        assertFalse(instance1.equals(instance2));

        instance1 = new DenseInstance(new double[5]);
        instance2 = new DenseInstance(new double[5], 1);
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithSameIDs() {
        AbstractInstance instance1 = new DenseInstance(new double[5]);
        AbstractInstance instance2 = new DenseInstance(new double[5]);
        instance2.setID(instance1.getID());
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance result = instance1.multiply(instance2);

        List<Double> expectedValues = Arrays.asList(3.0, 8.0);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});
        AbstractInstance result = instance1.divide(instance2);

        List<Double> expectedValues = Arrays.asList(0.5, 0.6666666666666667);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.add(3.0);

        List<Double> expectedValues = Arrays.asList(4.0, 5.0);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance result = instance1.add(instance2);

        List<Double> expectedValues = Arrays.asList(4.0, 6.0);
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0});
        AbstractInstance result = instance.sqrt();

        List<Double> expectedValues = Arrays.asList(Math.sqrt(1.0), Math.sqrt(16.0));
        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.value(i), 0.001);
        }
    }
}