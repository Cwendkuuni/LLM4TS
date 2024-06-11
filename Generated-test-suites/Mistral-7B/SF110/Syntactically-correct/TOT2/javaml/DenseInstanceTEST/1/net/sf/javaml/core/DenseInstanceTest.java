import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.noAttributes());
    }

    @Test
    public void testConstructorWithClassValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertEquals(att.length, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance instance = new DenseInstance();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance instance = new DenseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKeyWithNegativeIndex() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.containsKey(-1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributeWithInvalidIndex() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(-1);
    }

    @Test
    public void testContainsValueWithZeroTolerance() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 1.0, 2.0});
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testContainsValueWithNonNumberValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(instance.containsValue("not a number"));
    }

    @Test
    public void testPutWithNullKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            instance.put(null, 4.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPutWithDuplicateKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 2.0});
        instance.put(1, 5.0);
        try {
            instance.put(1, 6.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPutAll() {
        double[] att1 = new double[]{1.0, 2.0};
        double[] att2 = new double[]{3.0, 4.0};
        DenseInstance instance = new DenseInstance(att1);
        Map<Integer, Double> map = new HashMap<>();
        map.put(2, 4.0);
        map.put(3, 5.0);
        instance.putAll(map);
        Double[] expected = new Double[]{1.0, 2.0, 4.0, 5.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Double removedValue = instance.remove(1);
        assertEquals(2.0, removedValue, 0.0);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        Double[] expected = new Double[]{1.0, 2.0, 3.0};
        assertEquals(expected.length, values.size());
        assertTrue(values.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> keys = instance.keySet();
        TreeSet<Integer> expected = new TreeSet<>();
        expected.addAll(Arrays.asList(0, 1, 2));
        assertEquals(expected.size(), keys.size());
        assertTrue(keys.containsAll(expected));
    }

    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        HashSet<Map.Entry<Integer, Double>> expected = new HashSet<>();
        for (int i = 0; i < instance.noAttributes(); i++) {
            expected.add(Map.entry(i, (double) i));
        }
        assertEquals(expected.size(), entrySet.size());
        assertTrue(entrySet.containsAll(expected));
    }

    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        String expected = "{1.0, 2.0, 3.0;classValue}";
        String toString = instance.toString();
        assertEquals(expected, toString);
    }

    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        Double[] expected = new Double[]{1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(instance1.attributes.clone());
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(instance1.attributes.clone());
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance copy = instance.copy();
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.attributes, copy.attributes, 0.0);
    }

    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        Double[] expected = new Double[]{1.0, 4.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    // Add more test cases here if necessary
}