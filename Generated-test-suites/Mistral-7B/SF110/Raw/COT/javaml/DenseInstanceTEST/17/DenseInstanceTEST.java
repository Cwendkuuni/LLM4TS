package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstructorWithArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertNull(denseInstance.classValue());
    }

    @Test
    public void testDenseInstanceConstructorWithArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testDenseInstanceConstructorWithSize() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        assertEquals(size, denseInstance.noAttributes());
        assertArrayEquals(new double[size], denseInstance.attributes, 0.0);
        assertNull(denseInstance.classValue());
    }

    @Test
    public void testDenseInstanceGetValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < denseInstance.noAttributes(); i++) {
            double expectedValue = attributes[i];
            double actualValue = denseInstance.value(i);
            assertEquals(expectedValue, actualValue, 0.0);
        }
    }

    @Test
    public void testDenseInstanceClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDenseInstanceRemoveValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(0);
    }

    @Test
    public void testDenseInstanceContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < denseInstance.noAttributes(); i++) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testDenseInstanceContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < denseInstance.noAttributes(); i++) {
            double expectedValue = attributes[i];
            assertTrue(denseInstance.containsValue(expectedValue));
        }
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testDenseInstanceEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(denseInstance.noAttributes(), entrySet.size());

        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, (int)entry.getKey());
            assertEquals(value, denseInstance.value(key), 0.0);
        }
    }

    @Test
    public void testDenseInstanceGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < denseInstance.noAttributes(); i++) {
            double expectedValue = attributes[i];
            double actualValue = denseInstance.get(i);
            assertEquals(expectedValue, actualValue, 0.0);
        }
        try {
            denseInstance.get(-1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testDenseInstanceIsEmpty() {
        DenseInstance denseInstance = new DenseInstance();
        assertTrue(denseInstance.isEmpty());

        double[] attributes = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testDenseInstanceKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(denseInstance.noAttributes(), keySet.size());

        for (int key : keySet) {
            assertTrue(denseInstance.containsKey(key));
        }
    }

    @Test
    public void testDenseInstancePut() {
        double[] attributes = {1.0, 2.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Double oldValue = denseInstance.put(3, 3.0);
        assertNull(oldValue);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3.0, denseInstance.value(3), 0.0);

        try {
            denseInstance.put(4, 4.0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testDenseInstancePutAll() {
        double[] attributes1 = {1.0, 2.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = {3.0, 4.0};
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(3, 3.0);
        map.put(4, 4.0);
        denseInstance1.putAll(map);

        assertEquals(4, denseInstance1.noAttributes());
        assertEquals(3.0, denseInstance1.value(3), 0.0);
        assertEquals(4.0, denseInstance1.value(4), 0.0);

        double[] expectedAttributes = {1.0, 2.0, 3.0, 4.0};
        assertArrayEquals(expectedAttributes, denseInstance1.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());

        denseInstance.removeAttribute(0);
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(2, denseInstance.size());
    }

    @Test
    public void testDenseInstanceValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> values = denseInstance.values();
        assertEquals(denseInstance.noAttributes(), values.size());

        for (Double value : values) {
            assertTrue(value instanceof Double);
            assertEquals(value, denseInstance.value((int)value.intValue()), 0.0);
        }
    }

    @Test
    public void testDenseInstanceToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        String expectedString = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        assertEquals(expectedString, denseInstance.toString());
    }

    @Test
    public void testDenseInstanceHashCode() {
        double[] attributes1 = {1.0, 2.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = {1.0, 2.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        DenseInstance denseInstance3 = new DenseInstance(attributes1);

        assertEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
        assertNotEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
    }

    @Test
    public void testDenseInstanceEquals() {
        double[] attributes1 = {1.0, 2.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = {1.0, 2.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        double[] attributes3 = {1.1, 2.1};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        Object classValue = "ClassValue";
        DenseInstance denseInstance4 = new DenseInstance(attributes1, classValue);

        assertTrue(denseInstance1.equals(denseInstance3));
        assertTrue(denseInstance1.equals(denseInstance1));
        assertFalse(denseInstance1.equals(denseInstance2));
        assertFalse(denseInstance1.equals(denseInstance4));
    }

    @Test
    public void testDenseInstanceCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Instance copiedInstance = denseInstance.copy();
        assertEquals(denseInstance.noAttributes(), copiedInstance.noAttributes());
        assertArrayEquals(denseInstance.attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testDenseInstanceRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        denseInstance.removeAttributes(indices);

        assertEquals(2, denseInstance.noAttributes());
        assertFalse(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(3));
        for (int i = 0; i < denseInstance.noAttributes(); i++) {
            assertFalse(indices.contains(i));
        }
    }
}