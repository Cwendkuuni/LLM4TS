package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithEmptyArray() {
        DenseInstance instance = new DenseInstance(new double[0]);
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertFalse(instance.containsKey(0));
        assertNull(instance.getClassValue());
        assertTrue(instance.values().isEmpty());
    }

    @Test
    public void testDenseInstanceWithOneAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0});
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsKey(0));
        assertNull(instance.getClassValue());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(Arrays.asList(1.0), instance.values());
        assertFalse(instance.containsValue(1.1));
        assertFalse(instance.containsValue(1.2));
        assertNull(instance.get(0));
        assertNull(instance.get(1));
        assertNull(instance.remove(1));
        assertThrows(UnsupportedOperationException.class, () -> instance.removeAttribute(0));
    }

    @Test
    public void testDenseInstanceWithMultipleAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertNull(instance.getClassValue());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), instance.values());
        assertFalse(instance.containsValue(1.1));
        assertFalse(instance.containsValue(1.2));
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
        assertNull(instance.get(3));
        assertNull(instance.remove(3));
        assertThrows(UnsupportedOperationException.class, () -> instance.removeAttribute(0));

        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertTrue(instance.isEmpty());
        assertFalse(instance.containsKey(0));
        assertNull(instance.getClassValue());
        assertTrue(instance.values().isEmpty());
    }

    @Test
    public void testDenseInstanceWithClassValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassLabel");
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals("ClassLabel", instance.classValue().toString());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), instance.values());
        assertFalse(instance.containsValue(1.1));
        assertFalse(instance.containsValue(1.2));
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
        assertNull(instance.get(3));
        assertNull(instance.remove(3));
        assertThrows(UnsupportedOperationException.class, () -> instance.removeAttribute(0));

        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertTrue(instance.isEmpty());
        assertFalse(instance.containsKey(0));
        assertEquals("ClassLabel", instance.classValue().toString());
        assertTrue(instance.values().isEmpty());
    }

    @Test
    public void testDenseInstanceCopy() {
        DenseInstance original = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance copy = original.copy();
        assertEquals(original.noAttributes(), copy.noAttributes());
        assertEquals(original.getClassValue(), copy.getClassValue());
        assertArrayEquals(original.attributes, copy.attributes);
    }
}