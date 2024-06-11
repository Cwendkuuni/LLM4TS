package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        assertEquals(instance1.getID(), instance1.getID());
        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance2.getID(), instance2.getID());
        assertEquals(instance2.getID(), instance2.hashCode());
        assertNotEquals(instance1.getID(), instance2.getID());
        assertNotEquals(instance1.getID(), instance2.hashCode());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});
        AbstractInstance result = instance1.minus(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 1.0);
        map1.put(1, 2.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 2.0);
        map2.put(1, 3.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(-map2.get(i), ((Map<Integer, Double>)result).get(i));
        }
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(1.5);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, -0.5);
        map.put(1, -1.5);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(-1.5, ((Map<Integer, Double>)result).get(i));
        }
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 4.0});
        AbstractInstance result = instance1.divide(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 0.5);
        map1.put(1, 0.5);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 2.0);
        map2.put(1, 4.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i) / map2.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.divide(2.0);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.5);
        map.put(1, 1.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(1.0 / 2.0, ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance result = instance1.multiply(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 1.0);
        map1.put(1, 2.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 3.0);
        map2.put(1, 4.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i) * map2.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
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
            assertEquals(3.0, ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{3.0, 4.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{3.0, 4.0});

        assertEquals(instance1.hashCode(), instance1.getID());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
        assertNotEquals(instance1.hashCode(), instance2.getID());
        assertNotEquals(instance1.hashCode(), 1L);
    }

    @Test
    public void testMinusWithDoubleAndInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(new DenseInstance(new double[]{1.5, 2.5}));

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, -0.5);
        map.put(1, -0.5);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(-1.5, ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testMinusWithInstanceAndDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = new DenseInstance(new double[]{2.0, 3.0}).minus(instance);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 1.0);
        map1.put(1, 2.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, -1.0);
        map2.put(1, -1.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(-map1.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testDivideWithDoubleAndInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 4.0});
        AbstractInstance result = instance1.divide(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 0.25);
        map1.put(1, 0.25);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 2.0);
        map2.put(1, 4.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i) / map2.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testDivideWithInstanceAndDouble() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 4.0});
        AbstractInstance result = instance1.divide(2.0);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 0.5);
        map1.put(1, 1.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 2.0);
        map2.put(1, 4.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i) / map2.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testMultiplyWithInstanceAndInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance result = instance1.multiply(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 3.0);
        map1.put(1, 8.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 1.0);
        map2.put(1, 2.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i) * map2.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance result = instance1.add(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 4.0);
        map1.put(1, 6.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 1.0);
        map2.put(1, 2.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.add(3.0);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testAddWithInstanceAndDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = new DenseInstance(new double[]{3.0, 4.0}).add(instance);

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 4.0);
        map1.put(1, 6.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 3.0);
        map2.put(1, 4.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testAddWithDoubleAndInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.add(new DenseInstance(new double[]{3.0, 4.0}));

        Map<Integer, Double> map1 = new HashMap<>();
        map1.put(0, 4.0);
        map1.put(1, 6.0);
        Map<Integer, Double> map2 = new HashMap<>();
        map2.put(0, 1.0);
        map2.put(1, 2.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map1.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0});
        AbstractInstance result = instance.sqrt();

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, Math.sqrt(1.0));
        map.put(1, Math.sqrt(4.0));

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(map.get(i), ((Map<Integer, Double>)result).get(i), 0.001);
        }
    }

    @Test
    public void testNextID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        assertEquals(instance1.getID(), 0);
        assertEquals(instance2.getID(), 1);
    }
}