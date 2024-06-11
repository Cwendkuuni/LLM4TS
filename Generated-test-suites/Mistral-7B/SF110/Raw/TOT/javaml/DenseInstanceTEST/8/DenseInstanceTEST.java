import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.util.Pair;

import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Assert.assertArrayEquals(attributes, instance.attributes, 0.0);
        Assert.assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        Assert.assertArrayEquals(attributes, instance.attributes, 0.0);
        Assert.assertEquals(classValue, instance.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance instance = new DenseInstance(5);
        Assert.assertNotNull(instance.attributes);
        Assert.assertEquals(5, instance.attributes.length);
        Assert.assertNull(instance.getClassValue());
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            double value = instance.value(i);
            Assert.assertEquals(attributes[i], value, 0.0);
        }
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        Assert.assertArrayEquals(new double[attributes.length], instance.attributes, 0.0);
        Assert.assertNull(instance.getClassValue());
    }

    @Test
    public void testContainsKeyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            boolean result = instance.containsKey(i);
            Assert.assertTrue(result);
        }
        Assert.assertFalse(instance.containsKey(-1));
        Assert.assertFalse(instance.containsKey("Key"));
    }

    @Test
    public void testContainsValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            double value = attributes[i];
            boolean result = instance.containsValue(value);
            Assert.assertTrue(result);
        }
        double epsilon = 1.0E-8;
        double nonExistentValue = 4.0;
        Assert.assertFalse(instance.containsValue(nonExistentValue));
        Assert.assertFalse(instance.containsValue(null));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        int size = entrySet.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<Integer, Double> entry = entrySet.iterator().next();
            int key = entry.getKey();
            double value = entry.getValue();
            Assert.assertEquals(key, (Integer)instance.keyAt(i));
            Assert.assertEquals(value, instance.value(key), epsilon);
        }
    }

    @Test
    public void testGetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            double value = instance.get(i);
            Assert.assertEquals(attributes[i], value, epsilon);
        }
        Assert.assertNull(instance.get(-1));
        Assert.assertNull(instance.get("Key"));
    }

    @Test
    public void testIsEmptyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Assert.assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        int size = keySet.size();
        for (int i = 0; i < size; ++i) {
            int key = keySet.iterator().next();
            Assert.assertEquals(key, (Integer)instance.keyAt(i));
        }
        Assert.assertEquals(3, size);
    }

    @Test
    public void testPutMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        double newValue = 4.0;
        int index = 2;
        double oldValue = instance.put(index, newValue);
        Assert.assertEquals(newValue, instance.value(index), epsilon);
        Assert.assertEquals(oldValue, 3.0, epsilon);
    }

    @Test
    public void testPutAllMethod() {
        double[] originalAttributes = new double[]{1.0, 2.0, 3.0};
        double[] newAttributes = new double[]{4.0, 5.0, 6.0};
        DenseInstance instance = new DenseInstance(originalAttributes);
        Map<Integer, Double> newMap = new HashMap<>();
        for (int i = 0; i < newAttributes.length; ++i) {
            newMap.put(i, newAttributes[i]);
        }
        instance.putAll(newMap);
        Assert.assertArrayEquals(new double[originalAttributes.length + newAttributes.length], instance.attributes, epsilon);
        for (int i = 0; i < originalAttributes.length; ++i) {
            Assert.assertEquals(originalAttributes[i], instance.attributes[i], epsilon);
        }
        for (int i = 0; i < newAttributes.length; ++i) {
            Assert.assertEquals(newAttributes[i], instance.attributes[originalAttributes.length + i], epsilon);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        int index = 1;
        instance.remove(index);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributesMethodWithEmptySet() {
        double[] attributes = new double[]{1.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        instance.removeAttributes(indices);
    }

    @Test
    public void testRemoveAttributesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(2);
        indices.add(4);
        instance.removeAttributes(indices);
        Assert.assertArrayEquals(new double[]{1.0, 3.0, 5.0}, instance.attributes, epsilon);
    }
}