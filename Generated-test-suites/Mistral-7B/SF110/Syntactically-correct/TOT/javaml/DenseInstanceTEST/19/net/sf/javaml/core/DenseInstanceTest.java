import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceCreation() {
        // Test empty DenseInstance creation
        DenseInstance emptyInstance = new DenseInstance();
        assertEquals(0, emptyInstance.noAttributes());
        assertNull(emptyInstance.getClassValue());
        assertArrayEquals(new double[0], emptyInstance.attributes, 0.0);
        
        // Test DenseInstance creation with attributes and classValue
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(classValue, instance.getClassValue());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        
        // Test DenseInstance creation with only attributes
        instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], instance.value(i), 0.0);
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
        assertNull(instance.getClassValue());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testContainsKeyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(instance.containsKey(i));
        }
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(4));
    }

    @Test
    public void testContainsValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(instance.containsValue(attributes[i]));
        }
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(attributes.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
            assertEquals(entry.getValue(), instance.attributes[entry.getKey()]);
        }
    }

    @Test
    public void testGetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], instance.get(i), 0.0);
        }
        assertNull(instance.get(-1));
    }

    @Test
    public void testIsEmptyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
        
        DenseInstance emptyInstance = new DenseInstance();
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(attributes.length, keySet.size());
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(keySet.contains(i));
        }
        
        DenseInstance emptyInstance = new DenseInstance();
        assertTrue(emptyInstance.keySet().isEmpty());
    }

    @Test
    public void testPutMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        double newValue = 4.0;
        int key = 3;
        assertEquals(instance.attributes[key], instance.value(key), 0.0);
        instance.put(key, newValue);
        assertEquals(newValue, instance.value(key), 0.0);
        assertArrayEquals(instance.attributes, new double[]{1.0, 2.0, newValue}, 0.0);
        
        try {
            instance.put(-1, newValue);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testPutAllMethod() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{4.0, 5.0, 6.0};
        DenseInstance instance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(3, 4.0);
        map.put(5, 5.0);
        instance.putAll(map);
        assertArrayEquals(new double[]{1.0, 2.0, 4.0, 5.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testRemoveAttributeMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        int index = 1;
        instance.removeAttribute(index);
        assertEquals(2, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
        
        try {
            instance.removeAttribute(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}