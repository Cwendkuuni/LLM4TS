package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(0, instance.getID());

        instance = new DenseInstance(new double[]{1.0, 2.0}, 1);
        assertEquals(1, instance.getID());

        instance = new DenseInstance(new double[]{1.0, 2.0}, 2);
        assertEquals(2, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        assertEquals(1.0, iterator.next());
        assertEquals(2.0, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAbstractInstance() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Map<K, Double> value() {
                Map<K, Double> map = new HashMap<>();
                map.put(K.getInstance(0), 1.0);
                map.put(K.getInstance(1), 2.0);
                return map;
            }
        };

        assertEquals(0, instance.getID());
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);

        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Map<K, Double> value() {
                Map<K, Double> map = new HashMap<>();
                map.put(K.getInstance(0), 3.0);
                map.put(K.getInstance(1), 4.0);
                return map;
            }
        };

        AbstractInstance instance3 = instance.minus(instance2);
        AbstractInstance expectedInstance3 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Map<K, Double> value() {
                Map<K, Double> map = new HashMap<>();
                map.put(K.getInstance(0), -2.0);
                map.put(K.getInstance(1), -1.0);
                return map;
            }
        };

        assertEquals(instance3.getClass(), expectedInstance3.getClass());
        assertArrayEquals(expectedInstance3.value().values().toArray(), instance3.value().values().toArray(), 0.01);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instanceResult = instance.minus(1.0);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{0.0, 1.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 3.0});
        AbstractInstance instanceResult = instance1.minus(instance2);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{-0.0, 1.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instanceResult = instance.divide(2.0);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{0.5, 1.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instanceResult = instance.multiply(2.0);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{2.0, 4.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        AbstractInstance instanceResult = instance1.multiply(instance2);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{3.0, 8.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0});
        AbstractInstance instanceResult = instance1.divide(instance2);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{2.0, 2.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instanceResult = instance.add(1.0);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{2.0, 3.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 1.0});
        AbstractInstance instanceResult = instance1.add(instance2);

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{2.0, 3.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 9.0});
        AbstractInstance instanceResult = instance.sqrt();

        AbstractInstance expectedInstanceResult = new DenseInstance(new double[]{2.0, 3.0});;

        assertEquals(expectedInstanceResult.getClass(), instanceResult.getClass());
        assertArrayEquals(expectedInstanceResult.value().values().toArray(), instanceResult.value().values().toArray(), 0.01);
    }
}