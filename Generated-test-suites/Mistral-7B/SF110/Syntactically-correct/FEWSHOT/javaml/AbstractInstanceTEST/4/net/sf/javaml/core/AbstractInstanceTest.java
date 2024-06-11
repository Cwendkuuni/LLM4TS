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
        AbstractInstance instance3 = new DenseInstance(new double[]{1.1, 2.1});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1, instance1);

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertEquals(instance1, instance2);

        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(AbstractInstance.nextID, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        Double value = iterator.next();
        assertEquals(1.0, value.doubleValue(), 0.0);
        value = iterator.next();
        assertEquals(2.0, value.doubleValue(), 0.0);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNewAbstractInstance() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue());
        assertEquals(AbstractInstance.nextID, instance.getID());
    }

    @Test
    public void testNewAbstractInstanceWithClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue);
        assertEquals(classValue, instance.classValue());
        assertEquals(AbstractInstance.nextID, instance.getID());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});
        AbstractInstance result = instance1.minus(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 0.5);
        map1.put(1, 0.5);

        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 0.5);
        map2.put(1, -1.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map2.get(i), result.value(i), 0.0);
        }
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(2.0);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, -1.0);
        map.put(1, -1.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map.get(i), result.value(i), 0.0);
        }
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 8.0});
        AbstractInstance result = instance.divide(2.0);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 2.0);
        map.put(1, 4.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map.get(i), result.value(i), 0.0);
        }
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.multiply(3.0);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 3.0);
        map.put(1, 6.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map.get(i), result.value(i), 0.0);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{9.0});
        AbstractInstance result = instance.sqrt();

        assertTrue(result.value(0) > 0.0);
        assertEquals(Math.sqrt(9.0), result.value(0), 0.01);
    }
}