import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        double[] expectedAttributes = new double[size];
        Arrays.fill(expectedAttributes, 0.0);
        assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testClear() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(denseInstance.containsValue(attributes[i]));
        }
        double valueToFind = 4.0;
        assertFalse(denseInstance.containsValue(valueToFind));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(attributes.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.get(i), 0.0);
        }
    }

    @Test
    public void testIsEmpty() {
        DenseInstance denseInstance = new DenseInstance();
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(attributes.length, keySet.size());
        for (Integer key : keySet) {
            assertTrue(denseInstance.containsKey(key));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPut() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(1, 4.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutAll() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Map<Integer, Double> mapToAdd = new HashMap<>();
        mapToAdd.put(1, 4.0);
        denseInstance.putAll(mapToAdd);
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        assertEquals(attributes.length - 1, denseInstance.noAttributes());
        for (int i = 0; i < attributes.length - 1; i++) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = new double[] {1.0, 2.0, 3.1};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));

        double[] attributes3 = new double[] {1.0, 2.1, 3.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertFalse(denseInstance1.equals(denseInstance3));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        assertArrayEquals(attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);
        indicesToRemove.add(4);
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(attributes.length - indicesToRemove.size(), denseInstance.noAttributes());
        for (int i = 0; i < attributes.length - indicesToRemove.size(); i++) {
            if (!indicesToRemove.contains(i)) {
                assertEquals(attributes[i], denseInstance.value(i), 0.0);
            }
        }
    }
}