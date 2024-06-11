package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert. fail;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Instance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "TestClass";
        Instance instance = new DenseInstance(attributes, classValue);
        assertEquals("TestClass", instance.getClassValue());
    }

    @Test
    public void testConstructorWithEmptyArray() {
        Instance instance = new DenseInstance(new double[]{});
        assertEquals(0, instance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        double[] attributes = null;
        Instance instance = new DenseInstance(attributes);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testContainsKeyWithValidKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsKeyWithInvalidKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.containsKey("InvalidKey"));
    }

    @Test
    public void testContainsValueWithValidValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testContainsValueWithInvalidValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(3, entries.size());
    }

    @Test
    public void testGetWithValidKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.get(0));
    }

    @Test
    public void testGetWithInvalidKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertNull(instance.get("InvalidKey"));
    }

    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
    }

    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.put(0, 4.0);
        assertEquals(4.0, instance.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithInvalidKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.put(-1, 4.0);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.put(0, null);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<? extends Integer, ? extends Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(0));
        assertEquals(5.0, instance.get(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributeThrowsException() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithInvalidKey2() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.put(Integer.MAX_VALUE, 4.0);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllWithNullMap() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<? extends Integer, ? extends Double> map = null;
        instance.putAll(map);
    }

    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        Set<Integer> indices = new HashSet<>(Arrays.asList(2));
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributesWithEmptySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = Collections.emptySet();
        instance.removeAttributes(indices);
    }

    @Test
    public void testRemoveAttributesWithNullSet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = null;
        instance.removeAttributes(indices);
    }

    @Test
    public void testHashCodeEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[]{1.0, 2.0, 4.0});
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Instance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        assertEquals(instance.getClassValue(), copiedInstance.getClassValue());
        assertArrayEquals(instance.attributes, copiedInstance.attributes);
    }

    @Test
    public void testRemoveAttributesWithValidSet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        String expected = "{" + Arrays.toString(instance.attributes) + ";" + instance.getClassValue() + "}";
        assertEquals(expected, instance.toString());
    }
}