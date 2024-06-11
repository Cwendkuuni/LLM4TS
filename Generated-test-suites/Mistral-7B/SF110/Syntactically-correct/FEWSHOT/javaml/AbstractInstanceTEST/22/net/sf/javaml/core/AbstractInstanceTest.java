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
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(0, instance.getID());

        instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassLabel");
        assertEquals(1, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        List<Double> expectedValues = Arrays.asList(1.0, 2.0);
        List<Double> actualValues = new ArrayList<>();

        while (iterator.hasNext()) {
            actualValues.add(iterator.next());
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue);
        assertEquals(0, instance.getID());

        instance = new AbstractInstance(new Object());
        assertNotNull(instance.classValue);
        assertEquals(1, instance.getID());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        instance.setClassValue("ClassLabel");
        assertEquals("ClassLabel", instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        instance.setClassValue("ClassLabel");
        assertEquals("ClassLabel", instance.classValue());
    }

    @Test
    public void testMinus_Instance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});

        AbstractInstance result = instance1.minus(instance2);
        List<Double> expectedValues = Arrays.asList(-0.5, -1.5);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testMinus_Double() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.minus(2.0);
        List<Double> expectedValues = Arrays.asList(-1.0, -2.0);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testDivide_Double() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.divide(2.0);
        List<Double> expectedValues = Arrays.asList(0.5, 1.0);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testMultiply_Double() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.multiply(2.0);
        List<Double> expectedValues = Arrays.asList(2.0, 4.0);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});

        assertEquals(instance1.hashCode(), instance2.hashCode());

        instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassLabel");
        instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassLabel");
        assertEquals(instance1.hashCode(), instance2.hashCode());

        instance1 = new DenseInstance(new double[]{1.0, 2.0}, "ClassLabel");
        instance2 = new DenseInstance(new double[]{1.0, 2.0}, "ClassLabel1");
        assertNotEquals(instance1.hashCode(), instance2.hashCode());

        instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
        instance2 = new DenseInstance(new double[]{1.0, 2.0}, "ClassLabel");
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals_Null() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertFalse(instance.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertFalse(instance.equals(new Object()));
    }

    @Test
    public void testEquals_SameID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testEquals_DifferentID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testEquals_SameDataButDifferentID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        instance2.setID(10);
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testMultiply_Instance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});

        AbstractInstance result = instance1.multiply(instance2);
        List<Double> expectedValues = Arrays.asList(2.0, 6.0, 2.0, 6.0);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testDivide_Instance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});

        AbstractInstance result = instance1.divide(instance2);
        List<Double> expectedValues = Arrays.asList(0.5, 0.6666666666666666, 0.5, 0.6666666666666666);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testAdd_Double() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.add(2.0);
        List<Double> expectedValues = Arrays.asList(3.0, 4.0);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testAdd_Instance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5});

        AbstractInstance result = instance1.add(instance2);
        List<Double> expectedValues = Arrays.asList(1.5, 3.5);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0});

        AbstractInstance result = instance.sqrt();
        List<Double> expectedValues = Arrays.asList(1.0, 2.0);
        List<Double> actualValues = new ArrayList<>();

        for (int i = 0; i < result.noAttributes(); ++i) {
            actualValues.add(result.value(i));
        }

        assertEquals(expectedValues, actualValues);
    }
}