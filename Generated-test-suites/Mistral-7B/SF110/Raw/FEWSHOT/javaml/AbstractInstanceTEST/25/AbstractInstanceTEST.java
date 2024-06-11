package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance1.getClass().hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(""));
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); ++i) {
            map.put(i, instance.value(i));
        }

        Iterator<Double> iterator = instance.iterator();
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(map.get(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(instance.getID(), instance.hashCode());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.5, 2.5});

        AbstractInstance result = instance1.minus(instance2);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, -0.5);
        expectedMap.put(1, -0.5);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.minus(1.0);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 0.0);
        expectedMap.put(1, 1.0);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        double divisor = 2.0;

        AbstractInstance result = instance1.divide(divisor);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 0.5);
        expectedMap.put(1, 1.0);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        double multiplier = 2.0;

        AbstractInstance result = instance1.multiply(multiplier);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 2.0);
        expectedMap.put(1, 4.0);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.add(1.0);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 2.0);
        expectedMap.put(1, 3.0);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 0.5});

        AbstractInstance result = instance1.add(instance2);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.5);
        expectedMap.put(1, 2.5);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0});

        AbstractInstance result = instance.sqrt();
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 2.0);

        Map<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(i, result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); ++i) {
            map.put(i, instance.value(i));
            instance.iterator().remove();
        }
        fail("Should not reach this point");
    }
}