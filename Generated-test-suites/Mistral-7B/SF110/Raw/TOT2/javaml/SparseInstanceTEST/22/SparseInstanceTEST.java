import static org.junit.Assert.*;
import net.sf.javaml.core.SparseInstance;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceDefaultConstructor() {
        SparseInstance sparseInstance = new SparseInstance();
        assertEquals(-1, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithArguments() {
        SparseInstance sparseInstance = new SparseInstance(5, 0.0, "classValue");
        assertEquals(5, sparseInstance.noAttributes());
        assertEquals("classValue", sparseInstance.getClassValue());
        assertFalse(sparseInstance.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfAttributes() {
        SparseInstance sparseInstance = new SparseInstance(-5);
    }

    @Test
    public void testSparseInstanceWithDoubleArray() {
        double[] datavector = new double[5];
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(5, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNullClassValue() {
        SparseInstance sparseInstance = new SparseInstance(5, 0.0, null);
        assertNull(sparseInstance.getClassValue());
    }

    @Test
    public void testSparseInstanceWithEmptyDoubleArray() {
        SparseInstance sparseInstance = new SparseInstance(new double[0]);
        assertEquals(-1, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testValue() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(1.0, sparseInstance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        sparseInstance.clear();
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(sparseInstance.containsKey(0));
        assertFalse(sparseInstance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(sparseInstance.containsValue(1.0));
        assertFalse(sparseInstance.containsValue(5.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
        assertEquals(3, entries.size());
    }

    @Test
    public void testGet() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(1.0, sparseInstance.get(0), 0.001);
        assertEquals(0.0, sparseInstance.get(3), 0.001);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance sparseInstance = new SparseInstance();
        assertTrue(sparseInstance.isEmpty());

        SparseInstance sparseInstanceWithData = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertFalse(sparseInstanceWithData.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> keys = sparseInstance.keySet();
        assertEquals(3, keys.size());
    }

    @Test
    public void testPut() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(0, Double.valueOf(1.0));
        assertEquals(1.0, sparseInstance.value(0), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance sparseInstance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, Double.valueOf(1.0));
        map.put(1, Double.valueOf(2.0));
        sparseInstance.putAll(map);

        assertEquals(1.0, sparseInstance.value(0), 0.001);
        assertEquals(2.0, sparseInstance.value(1), 0.001);
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testRemove() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        Double oldValue = sparseInstance.remove(1);
        assertEquals(2.0, oldValue, 0.001);
        assertFalse(sparseInstance.containsValue(2.0));
    }

    @Test
    public void testSize() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(3, sparseInstance.size());
    }

    @Test
    public void testValues() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        Collection<Double> values = sparseInstance.values();
        assertEquals(3, values.size());
    }

    @Test
    public void testNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(3, sparseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        sparseInstance.removeAttribute(1);
        assertEquals(2, sparseInstance.noAttributes());
        assertEquals(1.0, sparseInstance.value(0), 0.001);
        assertEquals(3.0, sparseInstance.value(2), 0.001);
    }

    @Test
    public void testToString() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0;classValue}", sparseInstance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance sparseInstance1 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        SparseInstance sparseInstance2 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        SparseInstance sparseInstance3 = new SparseInstance(new double[] {1.1, 2.1, 3.1});

        assertEquals(hashCode(), sparseInstance1.hashCode());
        assertNotEquals(hashCode(), sparseInstance2.hashCode());
        assertNotEquals(hashCode(), sparseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance sparseInstance1 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        SparseInstance sparseInstance2 = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        SparseInstance sparseInstance3 = new SparseInstance(new double[] {1.1, 2.1, 3.1});
        Instance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});

        assertTrue(sparseInstance1.equals(sparseInstance1));
        assertTrue(sparseInstance1.equals(sparseInstance2));
        assertFalse(sparseInstance1.equals(sparseInstance3));
        assertFalse(sparseInstance1.equals(instance));
    }

    @Test
    public void testCopy() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        SparseInstance copiedInstance = sparseInstance.copy();

        assertEquals(sparseInstance.noAttributes(), copiedInstance.noAttributes());
        assertEquals(sparseInstance.getClassValue(), copiedInstance.getClassValue());

        for (Map.Entry<Integer, Double> entry : sparseInstance.entrySet()) {
            assertEquals(entry.getKey(), copiedInstance.get(entry.getKey()));
            assertEquals(entry.getValue(), copiedInstance.get(entry.getKey()), 0.001);
        }
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<>(Arrays.asList(0, 2));
        sparseInstance.removeAttributes(indices);
        assertEquals(1, sparseInstance.noAttributes());
        assertFalse(sparseInstance.containsKey(0));
        assertFalse(sparseInstance.containsKey(2));
        assertEquals(3.0, sparseInstance.value(1), 0.001);
    }
}