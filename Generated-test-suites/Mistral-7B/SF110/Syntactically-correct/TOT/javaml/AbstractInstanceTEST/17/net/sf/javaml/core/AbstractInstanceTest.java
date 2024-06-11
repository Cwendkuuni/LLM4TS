package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1, instance.getID());

        AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertEquals(2, instance2.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            Double next = iterator.next();
            assertEquals(index + 1, next.doubleValue());
            ++index;
        }
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instanceToSubtract = new DenseInstance(new double[]{0.5, 1.0, 1.5});

        AbstractInstance result = instance.minus(instanceToSubtract);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() - instanceToSubtract.getID(), resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.minus(2.0);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() - 2.0, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instanceToDivide = new DenseInstance(new double[]{2.0, 4.0, 6.0});

        AbstractInstance result = instance.divide(instanceToDivide);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() / 2.0, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.divide(2.0);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() / 2.0, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instanceToMultiply = new DenseInstance(new double[]{2.0, 4.0, 6.0});

        AbstractInstance result = instance.multiply(instanceToMultiply);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() * 2.0, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.multiply(2.0);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() * 2.0, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instanceToAdd = new DenseInstance(new double[]{0.5, 1.0, 1.5});

        AbstractInstance result = instance.add(instanceToAdd);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() + 0.5, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.add(2.0);

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, instanceValue.doubleValue() + 2.0, resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});

        AbstractInstance result = instance.sqrt();

        Iterator<Double> instanceIterator = instance.iterator();
        Iterator<Double> resultIterator = result.iterator();

        int index = 0;
        while (instanceIterator.hasNext() && resultIterator.hasNext()) {
            Double instanceValue = instanceIterator.next();
            Double resultValue = resultIterator.next();
            assertEquals("Unexpected result at index " + index, Math.sqrt(instanceValue.doubleValue()), resultValue.doubleValue(), 0.001);
            ++index;
        }

        assertEquals(instance.getID(), result.getID());
    }

    @Test
    public void testHashCodeAndEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertTrue(instance1.equals(instance2));

        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

        assertFalse(instance1.equals(instance3));
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
}