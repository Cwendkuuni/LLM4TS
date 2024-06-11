package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(0, instance.getID());

        instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1);
        assertEquals(1, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();

        List<Double> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }

        assertEquals(3, values.size());
        assertEquals(1.0, values.get(0).doubleValue(), 0.01);
        assertEquals(2.0, values.get(1).doubleValue(), 0.01);
        assertEquals(3.0, values.get(2).doubleValue(), 0.01);
    }

    @Test
    public void testConstructorWithoutClassValue() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructorWithClassValue() {
        AbstractInstance instance = new AbstractInstance("ClassLabel");
        assertEquals("ClassLabel", instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});
        AbstractInstance result = instance1.minus(instance2);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(-0.5);
        expectedValues.add(0.5);
        expectedValues.add(-0.5);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.minus(2.0);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(-1.0);
        expectedValues.add(-1.0);
        expectedValues.add(-1.0);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.divide(2.0);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(0.5);
        expectedValues.add(1.0);
        expectedValues.add(1.5);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.multiply(2.0);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(2.0);
        expectedValues.add(4.0);
        expectedValues.add(6.0);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testHashCodeAndEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0});

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 1.5});
        AbstractInstance result = instance1.multiply(instance2);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(0.5);
        expectedValues.add(2.0);
        expectedValues.add(4.5);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 1.0, 1.5});
        AbstractInstance result = instance1.divide(instance2);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(0.5);
        expectedValues.add(2.0);
        expectedValues.add(2.0);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.add(2.0);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(3.0);
        expectedValues.add(4.0);
        expectedValues.add(5.0);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 1.5});
        AbstractInstance result = instance1.add(instance2);

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.5);
        expectedValues.add(3.0);
        expectedValues.add(4.5);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});
        AbstractInstance result = instance.sqrt();

        List<Double> resultValues = new ArrayList<>();
        Iterator<Double> resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            resultValues.add(resultIterator.next());
        }

        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);

        assertEquals(3, resultValues.size());
        for (int i = 0; i < 3; ++i) {
            assertEquals(expectedValues.get(i).doubleValue(), resultValues.get(i).doubleValue(), 0.01);
        }
    }
}