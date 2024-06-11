import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        Assert.assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        Assert.assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testConstructorWithInt() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        Assert.assertArrayEquals(new double[size], denseInstance.attributes, 0.0);
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            Assert.assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        Assert.assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKeyMethodWithInteger() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            Assert.assertTrue(denseInstance.containsKey(i));
        }
        Assert.assertFalse(denseInstance.containsKey(-1));
        Assert.assertFalse(denseInstance.containsKey(attributes.length));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsValueMethodWithNullValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.containsValue(null);
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
        Assert.assertEquals(3, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            Assert.assertEquals(entry.getKey(), entry.getValue().intValue(), 0);
        }
    }

    @Test
    public void testGetMethodWithIntegerKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            Assert.assertEquals(attributes[i], denseInstance.get(i), 0.0);
        }
    }

    @Test
    public void testIsEmptyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keys = denseInstance.keySet();
        Assert.assertEquals(3, keys.size());
        for (int i = 0; i < keys.size(); i++) {
            Assert.assertEquals(i, keys.toArray()[i]);
        }
    }

    @Test
    public void testPutMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 4.0;
        Integer key = 3;
        denseInstance.put(key, newValue);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, newValue}, denseInstance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutAllMethodWithNullMap() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.putAll(null);
    }

    @Test
    public void testPutAllMethod() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{4.0, 5.0, 6.0};
        DenseInstance denseInstance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        denseInstance.putAll(map);
        Assert.assertArrayEquals(new double[]{1.0, 4.0, 2.0, 5.0, 3.0}, denseInstance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethodWithIntegerKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        int size = denseInstance.size();
        Assert.assertEquals(3, size);
    }

    @Test
    public void testValuesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> values = denseInstance.values();
        Assert.assertEquals(3, values.size());
        for (Double value : values) {
            Assert.assertEquals(value.doubleValue(), attributes[value.intValue()], 0.0);
        }
    }

    @Test
    public void testNoAttributesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToStringMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        String expected = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        Assert.assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttributeMethodWithInvalidIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(-1);
        Assert.assertArrayEquals(attributes, denseInstance.attributes);
    }

    @Test
    public void testRemoveAttributeMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        Assert.assertArrayEquals(new double[]{1.0, 3.0}, denseInstance.attributes);
    }

    @Test
    public void testHashCodeMethod() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        Assert.assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
    }

    @Test
    public void testEqualsMethodWithSameInstance() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes);
        DenseInstance denseInstance2 = denseInstance1;
        Assert.assertTrue(denseInstance1.equals(denseInstance2));
    }

    @Test
    public void testEqualsMethodWithDifferentInstance() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        Assert.assertFalse(denseInstance1.equals(denseInstance2));
    }

    @Test
    public void testEqualsMethodWithDifferentClass() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Object obj = new Object();
        Assert.assertFalse(denseInstance.equals(obj));
    }

    @Test
    public void testCopyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        Assert.assertArrayEquals(attributes, copiedInstance.attributes);
        Assert.assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributesMethodWithEmptySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        denseInstance.removeAttributes(indices);
        Assert.assertArrayEquals(attributes, denseInstance.attributes);
    }

    @Test
    public void testRemoveAttributesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        denseInstance.removeAttributes(indices);
        Assert.assertArrayEquals(new double[]{1.0, 4.0, 5.0}, denseInstance.attributes);
    }
}