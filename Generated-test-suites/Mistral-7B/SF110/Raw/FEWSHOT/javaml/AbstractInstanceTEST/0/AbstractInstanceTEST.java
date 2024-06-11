package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testAbstractInstanceConstructor() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertEquals(0, instance.getID());
        assertNull(instance.classValue());
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testAbstractInstanceSetClassValue() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testAbstractInstanceGetID() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertEquals(0, instance.getID());

        AbstractInstance instance2 = new DenseInstance(new double[5]);
        instance2.setClassValue("classValue");
        assertEquals(1, instance2.getID());
    }

    @Test
    public void testAbstractInstanceIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Iterator<Double> iterator = instance.iterator();

        List<Double> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }

        assertEquals(5, values.size());
        assertEquals(1.0, values.get(0));
        assertEquals(2.0, values.get(1));
        assertEquals(3.0, values.get(2));
        assertEquals(4.0, values.get(3));
        assertEquals(5.0, values.get(4));
    }

    @Test
    public void testAbstractInstanceMinusInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.5, 2.5, 3.5, 4.5, 5.5});

        AbstractInstance result = instance1.minus(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        Map<Integer, Double> map2 = new HashMap<>();
        for (int i = 0; i < instance1.noAttributes(); i++) {
            map1.put(i, instance1.value(i));
            map2.put(i, instance2.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map1.get(i) - map2.get(i), result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        AbstractInstance result = instance.minus(2.0);

        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); i++) {
            map.put(i, instance.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map.get(i) - 2.0, result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceDivideDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        AbstractInstance result = instance.divide(2.0);

        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); i++) {
            map.put(i, instance.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map.get(i) / 2.0, result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceMultiplyDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        AbstractInstance result = instance.multiply(2.0);

        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); i++) {
            map.put(i, instance.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map.get(i) * 2.0, result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceEqualsObject() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 6.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testAbstractInstanceHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 6.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.getID(), instance2.hashCode());
        assertNotEquals(instance1.getID(), instance3.hashCode());
    }

    @Test
    public void testAbstractInstanceMinusDoubleAndInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});

        AbstractInstance result = instance1.minus(instance2);

        AbstractInstance instance3 = new DenseInstance(new double[]{0.0, -1.0, -1.0, -1.0, -1.0});

        assertEquals(instance3, result);
    }

    @Test
    public void testAbstractInstanceDivideInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 2.0, 2.0, 2.0, 2.0});

        AbstractInstance result = instance1.divide(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        Map<Integer, Double> map2 = new HashMap<>();
        for (int i = 0; i < instance1.noAttributes(); i++) {
            map1.put(i, instance1.value(i));
            map2.put(i, instance2.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map1.get(i) / map2.get(i), result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        AbstractInstance result = instance.add(2.0);

        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); i++) {
            map.put(i, instance.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map.get(i) + 2.0, result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});

        AbstractInstance result = instance1.add(instance2);

        Map<Integer, Double> map1 = new HashMap<>();
        Map<Integer, Double> map2 = new HashMap<>();
        for (int i = 0; i < instance1.noAttributes(); i++) {
            map1.put(i, instance1.value(i));
            map2.put(i, instance2.value(i));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map1.get(i) + map2.get(i), result.value(i), 0.01);
        }
    }

    @Test
    public void testAbstractInstanceSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0, 16.0, 25.0});

        AbstractInstance result = instance.sqrt();

        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < instance.noAttributes(); i++) {
            map.put(i, Math.sqrt(instance.value(i)));
        }

        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(map.get(i), result.value(i), 0.01);
        }
    }
}