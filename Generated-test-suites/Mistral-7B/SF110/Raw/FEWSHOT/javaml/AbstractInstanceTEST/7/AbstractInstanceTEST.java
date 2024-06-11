package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testAbstractInstanceConstructor() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(2, instance.getID());
        assertNull(instance.classValue());

        AbstractInstance instanceWithClassValue = new DenseInstance(new double[]{1.0, 2.0}, "classValue");
        assertEquals(3, instanceWithClassValue.getID());
        assertEquals("classValue", instanceWithClassValue.classValue());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(2, instance.getID());

        AbstractInstance instanceWithClassValue = new DenseInstance(new double[]{1.0, 2.0}, "classValue");
        assertEquals(3, instanceWithClassValue.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        List<Double> values = Arrays.asList(1.0, 2.0);
        Iterator<Double> expectedIterator = values.iterator();

        Iterator<Double> actualIterator = instance.iterator();

        assertTrue(actualIterator.hasNext());
        assertEquals(1.0, actualIterator.next());
        assertTrue(actualIterator.hasNext());
        assertEquals(2.0, actualIterator.next());
        assertFalse(actualIterator.hasNext());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance minInstance = new DenseInstance(new double[]{-1.0, -2.0});

        AbstractInstance result = instance.minus(minInstance);

        AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 0.0});;

        assertArrayEquals(new double[]{2.0, 0.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.minus(1.0);

        AbstractInstance expectedResult = new DenseInstance(new double[]{0.0, 1.0});;

        assertArrayEquals(new double[]{0.0, 1.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        double divisor = 2.0;

        AbstractInstance result = instance.divide(divisor);

        AbstractInstance expectedResult = new DenseInstance(new double[]{0.5, 1.0});;

        assertArrayEquals(new double[]{0.5, 1.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        double multiplier = 2.0;

        AbstractInstance result = instance.multiply(multiplier);

        AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 4.0});;

        assertArrayEquals(new double[]{2.0, 4.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 1.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.getID(), instance1.hashCode());
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 1.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);

        AbstractInstance expectedResult = new DenseInstance(new double[]{3.0, 8.0});;

        assertArrayEquals(new double[]{3.0, 8.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0});

        AbstractInstance result = instance1.divide(instance2);

        AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 4.0});;

        assertArrayEquals(new double[]{2.0, 4.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.add(1.0);

        AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 3.0});;

        assertArrayEquals(new double[]{2.0, 3.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 1.0});

        AbstractInstance result = instance1.add(instance2);

        AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 3.0});;

        assertArrayEquals(new double[]{2.0, 3.0}, result.asDense().getData(), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 9.0});

        AbstractInstance result = instance.sqrt();

        AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 3.0});;

        assertArrayEquals(new double[]{2.0, 3.0}, result.asDense().getData(), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();
        iterator.next();
        iterator.remove();
    }
}