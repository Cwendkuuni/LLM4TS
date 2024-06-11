package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertContains;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SparseInstanceTest {

    private static final double DEFAULT_VALUE = 0.0;
    private static final Object CLASS_VALUE = new Object();
    private SparseInstance instance;
    private int noAttributes;
    private Double[] datavector;

    public SparseInstanceTest(int noAttributes, Double[] datavector) {
        this.noAttributes = noAttributes;
        this.datavector = datavector;
    }

    @Parameterized.Parameters(name = "{index}: SparseInstance({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, null},
                {1, new Double[]{DEFAULT_VALUE}},
                {2, new Double[]{DEFAULT_VALUE, 1.0}},
                {3, new Double[]{DEFAULT_VALUE, 1.0, 2.0}},
                {3, new Double[]{1.0, DEFAULT_VALUE, 2.0}},
                {3, new Double[]{1.0, 1.0, 2.0}}
        });
    }

    @Test
    public void testConstructor() {
        instance = new SparseInstance();
        assertEquals(Instance.class, instance.getClass());
        assertNull(instance.getClassValue());
        assertEquals(-1, instance.noAttributes());
        assertNull(instance.data);
        assertTrue(instance.isEmpty());

        instance = new SparseInstance(noAttributes);
        assertEquals(Instance.class, instance.getClass());
        assertNull(instance.getClassValue());
        assertEquals(noAttributes, instance.noAttributes());
        assertNull(instance.data);
        assertTrue(instance.isEmpty());

        instance = new SparseInstance(noAttributes, DEFAULT_VALUE, CLASS_VALUE);
        assertEquals(Instance.class, instance.getClass());
        assertSame(CLASS_VALUE, instance.getClassValue());
        assertEquals(noAttributes, instance.noAttributes());
        assertNull(instance.data);
        assertTrue(instance.isEmpty());

        instance = new SparseInstance(datavector);
        assertEquals(Instance.class, instance.getClass());
        assertNull(instance.getClassValue());
        assertEquals(datavector.length, instance.noAttributes());
        assertNotNull(instance.data);
        assertFalse(instance.isEmpty());

        instance = new SparseInstance(datavector, DEFAULT_VALUE, CLASS_VALUE);
        assertEquals(Instance.class, instance.getClass());
        assertSame(CLASS_VALUE, instance.getClassValue());
        assertEquals(datavector.length, instance.noAttributes());
        assertNotNull(instance.data);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testInitiate() {
        instance = new SparseInstance(datavector);
        instance.initiate(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector.length, instance.data.size());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.get(Integer.valueOf(i)));
        }
    }

    @Test
    public void testSetNoAttributes() {
        instance = new SparseInstance(noAttributes);
        instance.setNoAttributes(noAttributes + 1);
        assertEquals(noAttributes + 1, instance.noAttributes());
    }

    @Test
    public void testValue() {
        instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i));
        }
        assertNull(instance.value(-1));
        assertNull(instance.value(datavector.length));
    }

    @Test
    public void testClear() {
        instance = new SparseInstance(datavector);
        instance.clear();
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(datavector.length - 1));
        assertFalse(instance.containsValue(datavector[0]));
        assertFalse(instance.containsValue(datavector[1]));
        assertFalse(instance.containsValue(datavector[datavector.length - 1]));
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertTrue(instance.containsKey(Integer.valueOf(i)));
        }
        assertFalse(instance.containsKey(null));
        assertFalse(instance.containsKey(new Object()));
        assertFalse(instance.containsKey(10));
    }

    @Test
    public void testContainsValue() {
        instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertTrue(instance.containsValue(datavector[i]));
        }
        assertFalse(instance.containsValue(null));
        assertFalse(instance.containsValue(new Object()));
    }

    @Test
    public void testEntrySet() {
        instance = new SparseInstance(datavector);
        assertNotNull(instance.entrySet());
    }

    @Test
    public void testGet() {
        instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.get(Integer.valueOf(i)));
            assertEquals(datavector[i], instance.get(new Integer(i)));
        }
        assertEquals(DEFAULT_VALUE, instance.get(null));
        assertEquals(DEFAULT_VALUE, instance.get(new Object()));
        assertEquals(DEFAULT_VALUE, instance.get(-1));
        assertEquals(DEFAULT_VALUE, instance.get(datavector.length));
    }

    @Test
    public void testIsEmpty() {
        instance = new SparseInstance();
        assertTrue(instance.isEmpty());

        instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());

        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance = new SparseInstance(datavector);
        assertNotNull(instance.keySet());
        Collection<Integer> keys = instance.keySet();
        int[] expectedKeys = new int[datavector.length];
        for (int i = 0; i < datavector.length; i++) {
            expectedKeys[i] = i;
        }
        assertContains(expectedKeys, keys.toArray());
    }

    @Test
    public void testPut() {
        instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.get(Integer.valueOf(0)));
        assertEquals(1, instance.noAttributes());

        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(2.0, instance.get(Integer.valueOf(1)));
        assertEquals(2, instance.noAttributes());

        instance.put(Integer.valueOf(-1), Double.valueOf(3.0));
        assertNull(instance.get(Integer.valueOf(-1)));
        assertEquals(2, instance.noAttributes());

        instance.put(null, Double.valueOf(4.0));
        assertNull(instance.get(null));
        assertEquals(2, instance.noAttributes());

        instance = new SparseInstance(datavector);
        Double oldValue = instance.put(Integer.valueOf(0), Double.valueOf(5.0));
        assertEquals(5.0, instance.get(Integer.valueOf(0)));
        assertNull(oldValue);
        assertEquals(datavector.length + 1, instance.noAttributes());
    }

    @Test
    public void testPutAll() {
        instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.putAll(map);

        assertEquals(1.0, instance.get(Integer.valueOf(0)));
        assertEquals(2.0, instance.get(Integer.valueOf(1)));
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemove() {
        instance = new SparseInstance(datavector);
        Double oldValue = instance.remove(Integer.valueOf(0));
        assertEquals(datavector[0], oldValue);
        assertEquals(datavector.length - 1, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        instance = new SparseInstance(datavector);
        instance.removeAttribute(0);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveAttributeNull() {
        instance = new SparseInstance(datavector);
        instance.removeAttribute(null);
    }

    @Test
    public void testRemoveAttributeValidIndex() {
        instance = new SparseInstance(datavector);
        instance.removeAttribute(datavector.length - 1);
        assertEquals(datavector.length - 2, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(datavector.length - 1)));
    }

    @Test
    public void testToString() {
        instance = new SparseInstance(datavector);
        String expected = "{";
        for (int i = 0; i < datavector.length; i++) {
            if (i > 0) {
                expected += ", ";
            }
            expected += Integer.toString(i) + ":" + datavector[i];
        }
        expected += "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        instance = new SparseInstance(datavector);
        long hashCode = instance.hashCode();
        long newHashCode = (instance.getClass().hashCode() << 2) ^ (instance.data.hashCode() ^ Double.doubleToLongBits(instance.defaultValue));
        assertEquals(newHashCode, hashCode);
    }

    @Test
    public void testEquals() {
        instance = new SparseInstance(datavector);
        SparseInstance other = new SparseInstance(datavector);
        assertTrue(instance.equals(other));
        assertSame(instance, other);

        other = new SparseInstance(datavector);
        other.data = new HashMap<>();
        assertFalse(instance.equals(other));
        assertNotSame(instance, other);

        other = new SparseInstance(datavector);
        other.defaultValue = 1.0;
        assertFalse(instance.equals(other));
        assertNotSame(instance, other);

        other = new SparseInstance(datavector, CLASS_VALUE);
        assertFalse(instance.equals(other));
        assertNotSame(instance, other);

        other = new SparseInstance(datavector, 1.0, CLASS_VALUE);
        assertFalse(instance.equals(other));
        assertNotSame(instance, other);

        other = new SparseInstance(datavector, 1.0, CLASS_VALUE);
        other.data = new HashMap<>();
        assertFalse(instance.equals(other));
        assertNotSame(instance, other);

        other = new SparseInstance(datavector, 1.0, CLASS_VALUE);
        other.defaultValue = 1.1;
        assertFalse(instance.equals(other));
        assertNotSame(instance, other);
    }

    @Test
    public void testCopy() {
        instance = new SparseInstance(datavector);
        SparseInstance copy = instance.copy();
        assertSame(instance.getClass(), copy.getClass());
        assertSame(instance.data, copy.data);
        assertSame(instance.defaultValue, copy.defaultValue);
        assertSame(instance.noAttributes(), copy.noAttributes());
        assertSame(instance.getClassValue(), copy.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>(Arrays.asList(0, 1));
        instance.removeAttributes(indices);
        assertEquals(datavector.length - 2, instance.noAttributes());
        for (int i = 0; i < datavector.length - 2; i++) {
            assertFalse(instance.containsKey(Integer.valueOf(i)));
        }
    }
}