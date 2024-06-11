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
    public void testNextID() {
        AbstractInstance ai1 = new DenseInstance(new double[0]);
        AbstractInstance ai2 = new DenseInstance(new double[0]);
        AbstractInstance ai3 = new DenseInstance(new double[0]);

        assertEquals(0, ai1.getID());
        assertEquals(1, ai2.getID());
        assertEquals(2, ai3.getID());
    }

    @Test
    public void testIterator() {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 1.0);
        map.put("B", 2.0);
        map.put("C", 3.0);

        AbstractInstance instance = new DenseInstance(map);

        Iterator<Double> iterator = instance.iterator();

        List<Double> expectedList = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> actualList = Collections.emptyList();

        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testMinusInstance() {
        Map<String, Double> map1 = new HashMap<>();
        map1.put("A", 2.0);
        map1.put("B", 3.0);
        map1.put("C", 4.0);

        Map<String, Double> map2 = new HashMap<>();
        map2.put("A", 1.0);
        map2.put("B", 2.0);
        map2.put("C", 1.0);

        AbstractInstance instance1 = new DenseInstance(map1);
        AbstractInstance instance2 = new DenseInstance(map2);

        AbstractInstance result = instance1.minus(instance2);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 1.0);
        expectedMap.put("B", 1.0);
        expectedMap.put("C", 3.0);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testMinusDouble() {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 5.0);
        map.put("B", 2.0);
        map.put("C", 3.0);

        AbstractInstance instance = new DenseInstance(map);

        AbstractInstance result = instance.minus(1.5);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 3.5);
        expectedMap.put("B", 0.5);
        expectedMap.put("C", 2.5);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testDivideDouble() {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 6.0);
        map.put("B", 2.0);
        map.put("C", 3.0);

        AbstractInstance instance = new DenseInstance(map);

        AbstractInstance result = instance.divide(2.0);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 3.0);
        expectedMap.put("B", 1.0);
        expectedMap.put("C", 1.5);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testMultiplyDouble() {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 2.0);
        map.put("B", 3.0);
        map.put("C", 4.0);

        AbstractInstance instance = new DenseInstance(map);

        AbstractInstance result = instance.multiply(2.5);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 5.0);
        expectedMap.put("B", 7.5);
        expectedMap.put("C", 10.0);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testHashCodeAndEquals() {
        Map<String, Double> map1 = new HashMap<>();
        map1.put("A", 1.0);
        map1.put("B", 2.0);
        map1.put("C", 3.0);

        Map<String, Double> map2 = new HashMap<>();
        map2.put("A", 1.0);
        map2.put("B", 2.0);
        map2.put("C", 3.0);

        AbstractInstance instance1 = new DenseInstance(map1);
        AbstractInstance instance2 = new DenseInstance(map2);

        AbstractInstance instance3 = new DenseInstance(new double[3]);

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testMultiplyInstance() {
        Map<String, Double> map1 = new HashMap<>();
        map1.put("A", 2.0);
        map1.put("B", 3.0);
        map1.put("C", 4.0);

        Map<String, Double> map2 = new HashMap<>();
        map2.put("A", 1.0);
        map2.put("B", 2.0);
        map2.put("C", 1.0);

        AbstractInstance instance1 = new DenseInstance(map1);
        AbstractInstance instance2 = new DenseInstance(map2);

        AbstractInstance result = instance1.multiply(instance2);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 2.0);
        expectedMap.put("B", 6.0);
        expectedMap.put("C", 4.0);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testDivideInstance() {
        Map<String, Double> map1 = new HashMap<>();
        map1.put("A", 6.0);
        map1.put("B", 2.0);
        map1.put("C", 3.0);

        Map<String, Double> map2 = new HashMap<>();
        map2.put("A", 2.0);
        map2.put("B", 1.0);
        map2.put("C", 1.5);

        AbstractInstance instance1 = new DenseInstance(map1);
        AbstractInstance instance2 = new DenseInstance(map2);

        AbstractInstance result = instance1.divide(instance2);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 3.0);
        expectedMap.put("B", 2.0);
        expectedMap.put("C", 2.0);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testAddInstance() {
        Map<String, Double> map1 = new HashMap<>();
        map1.put("A", 2.0);
        map1.put("B", 3.0);
        map1.put("C", 4.0);

        Map<String, Double> map2 = new HashMap<>();
        map2.put("A", 1.0);
        map2.put("B", 2.0);
        map2.put("C", 1.0);

        AbstractInstance instance1 = new DenseInstance(map1);
        AbstractInstance instance2 = new DenseInstance(map2);

        AbstractInstance result = instance1.add(instance2);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 3.0);
        expectedMap.put("B", 5.0);
        expectedMap.put("C", 5.0);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testAddDouble() {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 5.0);
        map.put("B", 2.0);
        map.put("C", 3.0);

        AbstractInstance instance = new DenseInstance(map);

        AbstractInstance result = instance.add(1.5);

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 6.5);
        expectedMap.put("B", 3.5);
        expectedMap.put("C", 4.5);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testSqrt() {
        Map<String, Double> map = new HashMap<>();
        map.put("A", 9.0);
        map.put("B", 16.0);
        map.put("C", 25.0);

        AbstractInstance instance = new DenseInstance(map);

        AbstractInstance result = instance.sqrt();

        Map<String, Double> expectedMap = new HashMap<>();
        expectedMap.put("A", 3.0);
        expectedMap.put("B", 4.0);
        expectedMap.put("C", 5.0);

        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < result.noAttributes(); ++i) {
            resultMap.put(result.attributeName(i), result.value(i));
        }

        assertEquals(expectedMap, resultMap);
    }
}