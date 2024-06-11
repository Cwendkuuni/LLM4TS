package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstruction() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(5, denseInstance.noAttributes());
        assertNotNull(denseInstance.getClassValue());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceValue() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(1.0 + i, denseInstance.value(i), 0.0);
        }
        assertEquals(2.0, denseInstance.classValue());
    }

    @Test
    public void testDenseInstanceClear() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertNull(denseInstance.getClassValue());
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceContainsKey() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(6));
    }

    @Test
    public void testDenseInstanceContainsValue() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        for (double value : attributes) {
            assertTrue(denseInstance.containsValue(value));
        }
        assertFalse(denseInstance.containsValue(2.0));
    }

    @Test
    public void testDenseInstanceEntrySet() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(5, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
            assertEquals(attributes[entry.getKey()], entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testDenseInstanceGet() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.get(i));
        }
        assertNull(denseInstance.get(6));
    }

    @Test
    public void testDenseInstanceIsEmpty() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
        denseInstance.clear();
        assertTrue(denseInstance.isEmpty());
    }

    @Test
    public void testDenseInstanceKeySet() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        Set<Integer> keySet = denseInstance.keySet();
        assertEquals(5, keySet.size());
        for (Integer key : keySet) {
            assertEquals(key, denseInstance.value(key).intValue());
        }
    }

    @Test
    public void testDenseInstancePut() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        double newValue = 3.0;
        denseInstance.put(0, newValue);
        assertEquals(newValue, denseInstance.value(0), 0.0);
        assertArrayEquals(Arrays.copyOfRange(attributes, 1, attributes.length), denseInstance.attributes, 0.0);
    }

    @Test
    public void testDenseInstancePutAll() {
        double[] sourceAttributes = new double[5];
        double[] targetAttributes = new double[5];
        Arrays.fill(sourceAttributes, 1.0);
        DenseInstance sourceInstance = new DenseInstance(sourceAttributes, 2.0);
        Arrays.fill(targetAttributes, 2.0);
        DenseInstance targetInstance = new DenseInstance(targetAttributes);
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < sourceAttributes.length; i++) {
            map.put(i, (double) sourceAttributes[i]);
        }
        sourceInstance.putAll(map);
        targetInstance.putAll(sourceInstance);
        assertEquals(Arrays.toString(sourceAttributes), Arrays.toString(targetAttributes));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDenseInstanceRemove() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        denseInstance.remove(0);
    }

    @Test
    public void testDenseInstanceSize() {
        double[] attributes = new double[5];
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        assertEquals(5, denseInstance.noAttributes());
        DenseInstance emptyInstance = new DenseInstance();
        assertEquals(0, emptyInstance.noAttributes());
    }

    @Test
    public void testDenseInstanceValues() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        denseInstance.removeAttributes(indices);
        Set<Double> values = new HashSet<>();
        for (double value : denseInstance.values()) {
            values.add(value);
        }
        assertEquals(3, values.size());
        for (double value : values) {
            assertNotEquals(1.0, value, 0.0);
            assertNotEquals(3.0, value, 0.0);
        }
        assertEquals(2.0, denseInstance.classValue());
    }

    @Test
    public void testDenseInstanceToString() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        String expectedString = "{" + Arrays.toString(attributes) + ";2.0}";
        assertEquals(expectedString, denseInstance.toString());
    }

    @Test
    public void testDenseInstanceHashCode() {
        double[] attributes1 = new double[5];
        Arrays.fill(attributes1, 1.0);
        DenseInstance instance1 = new DenseInstance(attributes1, 2.0);
        double[] attributes2 = new double[5];
        Arrays.fill(attributes2, 1.0);
        DenseInstance instance2 = new DenseInstance(attributes2, 3.0);
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testDenseInstanceEquals() {
        double[] attributes1 = new double[5];
        Arrays.fill(attributes1, 1.0);
        DenseInstance instance1 = new DenseInstance(attributes1, 2.0);
        double[] attributes2 = new double[5];
        Arrays.fill(attributes2, 1.0);
        DenseInstance instance2 = new DenseInstance(attributes2, 3.0);
        assertFalse(instance1.equals(instance2));
        DenseInstance instance3 = new DenseInstance(attributes1, 2.0);
        assertTrue(instance1.equals(instance3));
        DenseInstance instance4 = instance1.copy();
        assertTrue(instance1.equals(instance4));
    }

    @Test
    public void testDenseInstanceCopy() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        DenseInstance copy = denseInstance.copy();
        assertEquals(denseInstance, copy);
        assertNotSame(denseInstance, copy);
        assertArrayEquals(denseInstance.attributes, copy.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copy.classValue());
    }

    @Test
    public void testDenseInstanceRemoveAttributes() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance denseInstance = new DenseInstance(attributes, 2.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        denseInstance.removeAttributes(indices);
        Set<Integer> remainingIndices = new HashSet<>();
        for (int i = 0; i < attributes.length; i++) {
            if (!indices.contains(i)) {
                remainingIndices.add(i);
            }
        }
        double[] expectedRemainingAttributes = new double[remainingIndices.size()];
        for (int i = 0; i < remainingIndices.size(); i++) {
            expectedRemainingAttributes[i] = attributes[remainingIndices.iterator().next()];
        }
        assertArrayEquals(expectedRemainingAttributes, denseInstance.attributes, 0.0);
        assertEquals(denseInstance.noAttributes() - indices.size(), denseInstance.noAttributes());
    }
}