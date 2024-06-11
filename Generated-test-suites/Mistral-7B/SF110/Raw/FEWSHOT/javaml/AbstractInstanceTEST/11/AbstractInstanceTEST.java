package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testAbstractInstanceEmptyConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertEquals(0, instance.noAttributes());
        assertEquals(null, instance.classValue());
    }

    @Test
    public void testAbstractInstanceWithClassValueConstructor() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue);
        assertEquals(0, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance();
        assertEquals(0, instance.getID());

        AbstractInstance instance2 = new AbstractInstance();
        assertEquals(1, instance2.getID());
        assertEquals(instance.getID(), instance2.getID() - 1);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        Iterator<Double> iterator = instance.iterator();
        List<Double> expectedValues = Arrays.asList(1.0, 2.0);

        assertEquals(expectedValues.size(), iterator.asList().size());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), expectedValues.get(0));
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), expectedValues.get(1));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 0.5);
        instance2.put(1, 1.5);

        AbstractInstance result = instance1.minus(instance2);
        List<Double> expectedValues = Arrays.asList(-0.5, 0.5);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new AbstractInstance();
        instance.put(0, 1.0);

        AbstractInstance result = instance.minus(2.0);
        List<Double> expectedValues = Arrays.asList(-1.0);

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new AbstractInstance();
        instance.put(0, 6.0);

        AbstractInstance result = instance.divide(3.0);
        List<Double> expectedValues = Arrays.asList(2.0);

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new AbstractInstance();
        instance.put(0, 3.0);

        AbstractInstance result = instance.multiply(2.0);
        List<Double> expectedValues = Arrays.asList(6.0);

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        AbstractInstance instance3 = new AbstractInstance();
        instance3.put(0, 1.0);
        instance3.put(1, 3.0);

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 3.0);
        instance2.put(1, 4.0);

        AbstractInstance result = instance1.multiply(instance2);
        List<Double> expectedValues = Arrays.asList(3.0, 8.0);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 6.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 3.0);

        AbstractInstance result = instance1.divide(instance2);
        List<Double> expectedValues = Arrays.asList(2.0);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new AbstractInstance();
        instance.put(0, 1.0);

        AbstractInstance result = instance.add(2.0);
        List<Double> expectedValues = Arrays.asList(3.0);

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 0.5);
        instance2.put(1, 1.5);

        AbstractInstance result = instance1.add(instance2);
        List<Double> expectedValues = Arrays.asList(1.5, 3.5);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance();
        instance.put(0, 9.0);

        AbstractInstance result = instance.sqrt();
        List<Double> expectedValues = Arrays.asList(3.0, 3.0);

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertArrayEquals(expectedValues.toArray(), result.toArray());
    }

    @Test
    public void testNextID() {
        AbstractInstance instance1 = new AbstractInstance();
        AbstractInstance instance2 = new AbstractInstance();

        assertEquals(0, instance1.getID());
        assertEquals(1, instance2.getID());
        assertEquals(2, new AbstractInstance().getID());
    }
}