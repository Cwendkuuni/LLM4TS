package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(classValue, denseInstance.classValue());
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testConstructorWithEmptyDoubleArray() {
        DenseInstance denseInstance = new DenseInstance(new double[0]);
        assertEquals(0, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
        try {
            denseInstance.value(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            denseInstance.value(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
    }

    @Test
    public void testContainsKeyMethodInt() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValueMethodDouble() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        int counter = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(counter++, entry.getKey());
            assertEquals(attributes[counter], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGetMethodObject() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(2.0, denseInstance.get(1), 0.0);
        try {
            denseInstance.get(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            denseInstance.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testIsEmptyMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        Iterator<Integer> iterator = keySet.iterator();
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
    }

    @Test
    public void testPutMethodIntegerDouble() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(0, 4.0);
        assertEquals(4.0, denseInstance.value(0), 0.0);
        assertEquals(1.0, denseInstance.value(1), 0.0);
        assertEquals(2.0, denseInstance.value(2), 0.0);
        assertEquals(3.0, denseInstance.value(3), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutMethodIntegerObject() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(0, "value");
    }

    @Test
    public void testPutAllMethodMap() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        denseInstance.putAll(map);
        assertEquals(4.0, denseInstance.value(0), 0.0);
        assertEquals(5.0, denseInstance.value(1), 0.0);
        assertEquals(1.0, denseInstance.value(2), 0.0);
        assertEquals(2.0, denseInstance.value(3), 0.0);
        assertEquals(3.0, denseInstance.value(4), 0.0);
    }

    @Test
    public void testRemoveMethodObject() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double removedValue = denseInstance.remove(1);
        assertEquals(2.0, removedValue, 0.0);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(3.0, denseInstance.value(1), 0.0);
        try {
            denseInstance.remove(2);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSizeMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testValuesMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<>(denseInstance.values());
        Collections.sort(values);
        assertEquals(1.0, values.get(0), 0.0);
        assertEquals(2.0, values.get(1), 0.0);
        assertEquals(3.0, values.get(2), 0.0);
    }

    @Test
    public void testRemoveAttributeMethodInt() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(3.0, denseInstance.value(1), 0.0);
    }

    @Test
    public void testHashCodeMethod() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = new double[] {1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEqualsObjectObject() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));

        double[] attributes3 = new double[] {1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertFalse(denseInstance1.equals(denseInstance3));

        DenseInstance denseInstance4 = new DenseInstance(attributes1);
        assertTrue(denseInstance1.equals(denseInstance4));

        Object other = new Object();
        assertFalse(denseInstance1.equals(other));
    }

    @Test
    public void testCopyMethod() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        assertEquals(attributes.length, copiedInstance.noAttributes());
        assertEquals(attributes[0], copiedInstance.value(0), 0.0);
        assertEquals(attributes[1], copiedInstance.value(1), 0.0);
        assertEquals(attributes[2], copiedInstance.value(2), 0.0);
    }

    @Test
    public void testRemoveAttributesMethodSet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        denseInstance.removeAttributes(indices);
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(4.0, denseInstance.value(1), 0.0);
    }
}