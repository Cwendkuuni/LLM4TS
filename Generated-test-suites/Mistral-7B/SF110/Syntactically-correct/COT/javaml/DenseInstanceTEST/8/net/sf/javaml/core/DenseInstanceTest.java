import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.attributes.length);
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        double[] attributes = new double[size];
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.attributes.length);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(denseInstance.containsValue(attributes[i]));
        }
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(attributes.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
            assertEquals(attributes[entry.getKey()], entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], denseInstance.get(i), 0.0);
        }
        try {
            denseInstance.get(-1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(attributes.length, keySet.size());
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(keySet.contains(i));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPut() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        try {
            denseInstance.put(1, 4.0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testPutAll() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        double[] newAttributes = new double[]{0.5, 4.0, 5.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < newAttributes.length; ++i) {
            map.put(i, newAttributes[i]);
        }
        denseInstance.putAll(map);
        assertEquals(newAttributes.length, denseInstance.attributes.length);
        for (int i = 0; i < newAttributes.length; ++i) {
            assertEquals(newAttributes[i], denseInstance.attributes[i], 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        try {
            denseInstance.remove(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(attributes.length, denseInstance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<>();
        for (double value : denseInstance.values()) {
            values.add(value);
        }
        assertEquals(attributes.length, values.size());
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], values.get(i), 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(attributes.length, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        String expected = "{" + Arrays.toString(attributes) + ";ClassValue}";
        assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        assertEquals(3, denseInstance.attributes.length);
        for (int i = 0; i < denseInstance.attributes.length; ++i) {
            if (i != 1) {
                assertEquals(attributes[i], denseInstance.attributes[i], 0.0);
            }
        }
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));

        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertFalse(denseInstance1.equals(denseInstance3));

        DenseInstance denseInstance4 = new DenseInstance(attributes1);
        assertTrue(denseInstance1.equals(denseInstance4));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        assertArrayEquals(attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indices);
        assertEquals(2, denseInstance.attributes.length);
        for (int i = 0; i < denseInstance.attributes.length; ++i) {
            if (i != 1 && i != 2) {
                assertEquals(attributes[i], denseInstance.attributes[i], 0.0);
            }
        }
    }

}