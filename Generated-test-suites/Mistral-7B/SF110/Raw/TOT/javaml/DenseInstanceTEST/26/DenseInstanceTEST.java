package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstruction() {
        // Test default constructor
        DenseInstance instance = new DenseInstance();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertArrayEquals(new double[0], instance.attributes);

        // Test constructor with attributes
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instanceWithAttributes = new DenseInstance(attributes);
        assertEquals(3, instanceWithAttributes.noAttributes());
        assertArrayEquals(attributes, instanceWithAttributes.attributes);
        assertNull(instanceWithAttributes.getClassValue());

        // Test constructor with attributes and class value
        Object classValue = "classValue";
        DenseInstance instanceWithAttributesAndClassValue = new DenseInstance(attributes, classValue);
        assertEquals(3, instanceWithAttributesAndClassValue.noAttributes());
        assertArrayEquals(attributes, instanceWithAttributesAndClassValue.attributes);
        assertEquals(classValue, instanceWithAttributesAndClassValue.getClassValue());
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), attributes[i], 0.0);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueMethodWithInvalidIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.value(-1);
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes);
    }

    @Test
    public void testContainsKeyMethodWithValidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(instance.containsKey(i));
        }
    }

    @Test
    public void testContainsKeyMethodWithInvalidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsKey(-1));
    }

    @Test
    public void testContainsValueMethodWithValidValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            Double value = instance.attributes[i];
            assertTrue(instance.containsValue(value));
        }
    }

    @Test
    public void testContainsValueMethodWithInvalidValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Double invalidValue = 4.0;
        assertFalse(instance.containsValue(invalidValue));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();

        assertEquals(instance.noAttributes(), entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGetMethodWithValidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.get(i), instance.attributes[i], 0.0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.get(-1);
    }

    @Test
    public void testIsEmptyMethod() {
        double[] attributes = new double[0];
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.isEmpty());

        double[] nonEmptyAttributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance nonEmptyInstance = new DenseInstance(nonEmptyAttributes);
        assertFalse(nonEmptyInstance.isEmpty());
    }

    // Other test methods for keySet(), put(), putAll(), remove(), size(), values(), noAttributes(), toString(), copy(), removeAttributes(Set<Integer> indices) can be added here.

}