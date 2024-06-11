package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstruction() {
        // empty constructor
        DenseInstance instance = new DenseInstance();
        assertNotNull(instance);
        assertEquals(0, instance.noAttributes());
        assertNull(instance.classValue());
        assertNull(instance.attributes);

        // constructor with attributes
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        assertEquals(att.length, denseInstance.noAttributes());
        assertEquals(att, denseInstance.attributes);
        assertEquals(null, denseInstance.classValue());

        // constructor with class value
        Object classValue = new Object();
        DenseInstance denseInstanceWithClassValue = new DenseInstance(att, classValue);
        assertEquals(att.length, denseInstanceWithClassValue.noAttributes());
        assertEquals(att, denseInstanceWithClassValue.attributes);
        assertEquals(classValue, denseInstanceWithClassValue.classValue());

        // constructor with size
        DenseInstance denseInstanceWithSize = new DenseInstance(att.length);
        assertEquals(att.length, denseInstanceWithSize.noAttributes());
        assertNotNull(denseInstanceWithSize.attributes);
        assertNull(denseInstanceWithSize.classValue());
    }

    @Test
    public void testValueMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertEquals(att[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testClearMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        denseInstance.clear();
        assertNotNull(denseInstance.attributes);
        assertEquals(0, denseInstance.noAttributes());
    }

    @Test
    public void testContainsKeyMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValueMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertTrue(denseInstance.containsValue(att[i]));
        }
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
        assertEquals(att.length, entries.size());
        for (int i = 0; i < att.length; i++) {
            Map.Entry<Integer, Double> entry = entries.iterator().next();
            assertEquals(i, entry.getKey());
            assertEquals(att[i], entry.getValue());
        }
    }

    @Test
    public void testGetMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertEquals(att[i], denseInstance.get(i));
        }
    }

    @Test
    public void testIsEmptyMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        Set<Integer> keys = denseInstance.keySet();
        assertEquals(att.length, keys.size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(keys.contains(i));
        }
    }

    @Test
    public void testPutMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        double newValue = 4.0;
        int key = 3;
        double oldValue = denseInstance.put(key, newValue);

        assertEquals(newValue, denseInstance.get(key));
        assertEquals(oldValue, att[key]);
    }

    @Test
    public void testPutAllMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        double[] newAtt = new double[] {4.0, 5.0, 6.0};
        DenseInstance denseInstance = new DenseInstance(att);

        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < newAtt.length; i++) {
            map.put(i, newAtt[i]);
        }
        denseInstance.putAll(map);

        for (int i = 0; i < newAtt.length; i++) {
            assertEquals(newAtt[i], denseInstance.get(i));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        int key = 1;
        denseInstance.remove(key);
    }

    @Test
    public void testSizeMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        assertEquals(att.length, denseInstance.noAttributes());
    }

    @Test
    public void testValuesMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        assertEquals(Collections.singletonList(att), denseInstance.values());
    }

    @Test
    public void testNoAttributesMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        assertEquals(att.length, denseInstance.noAttributes());
    }

    @Test
    public void testToStringMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        String expected = "{" + Arrays.toString(att) + ";" + null + "}";
        assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttributeMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        denseInstance.removeAttribute(1);

        double[] expectedAtt = new double[] {1.0, 3.0};
        assertEquals(expectedAtt.length, denseInstance.noAttributes());
        assertArrayEquals(expectedAtt, Arrays.copyOfRange(denseInstance.attributes, 0, denseInstance.noAttributes()));
    }

    @Test
    public void testHashCodeMethod() {
        double[] att1 = new double[] {1.0, 2.0, 3.0};
        double[] att2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        DenseInstance denseInstance2 = new DenseInstance(att2);

        int expectedHashCode = Arrays.hashCode(att1);
        assertEquals(expectedHashCode, denseInstance1.hashCode());
        assertEquals(expectedHashCode, denseInstance2.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        double[] att1 = new double[] {1.0, 2.0, 3.0};
        double[] att2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        DenseInstance denseInstance2 = new DenseInstance(att2);

        DenseInstance denseInstance3 = new DenseInstance(att1);
        DenseInstance denseInstance4 = new DenseInstance(att1);
        DenseInstance denseInstance5 = new DenseInstance(att2);
        DenseInstance denseInstance6 = new DenseInstance();

        assertTrue(denseInstance1.equals(denseInstance1));
        assertTrue(denseInstance1.equals(denseInstance3));
        assertTrue(denseInstance1.equals(denseInstance4));
        assertFalse(denseInstance1.equals(denseInstance2));
        assertFalse(denseInstance1.equals(denseInstance5));
        assertFalse(denseInstance1.equals(denseInstance6));
    }

    @Test
    public void testCopyMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);

        DenseInstance copiedInstance = denseInstance.copy();

        assertEquals(att.length, copiedInstance.noAttributes());
        assertArrayEquals(att, copiedInstance.attributes);
        assertNull(copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributesMethod() {
        double[] att = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance denseInstance = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>(Arrays.asList(2, 4));
        denseInstance.removeAttributes(indices);

        double[] expectedAtt = new double[] {1.0, 3.0, 5.0};
        assertEquals(expectedAtt.length, denseInstance.noAttributes());
        assertArrayEquals(expectedAtt, Arrays.copyOfRange(denseInstance.attributes, 0, denseInstance.noAttributes()));
    }
}