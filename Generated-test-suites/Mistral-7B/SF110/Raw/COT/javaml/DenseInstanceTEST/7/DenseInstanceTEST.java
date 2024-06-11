import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertEquals(3, denseInstance.noAttributes());
        Assert.assertEquals(attributes, denseInstance.attributes);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        Assert.assertEquals(3, denseInstance.noAttributes());
        Assert.assertEquals(attributes, denseInstance.attributes);
        Assert.assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testConstructorWithIntSize() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        Assert.assertEquals(size, denseInstance.noAttributes());
        Assert.assertNotNull(denseInstance.attributes);
        Assert.assertEquals(0.0, denseInstance.attributes[0]);
        Assert.assertEquals(0.0, denseInstance.attributes[1]);
        Assert.assertEquals(0.0, denseInstance.attributes[2]);
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
        Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
        Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueMethodWithNegativeIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.value(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueMethodWithIndexGreaterThanSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.value(3);
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        Assert.assertEquals(0, denseInstance.noAttributes());
        Assert.assertNotNull(denseInstance.attributes);
        Assert.assertEquals(0.0, denseInstance.attributes[0]);
        Assert.assertEquals(0.0, denseInstance.attributes[1]);
        Assert.assertEquals(0.0, denseInstance.attributes[2]);
    }

    @Test
    public void testContainsKeyMethodWithInteger() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            Assert.assertTrue(denseInstance.containsKey(i));
        }
        Assert.assertFalse(denseInstance.containsKey(-1));
        Assert.assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValueMethodWithNumber() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            Assert.assertTrue(denseInstance.containsValue(attributes[i]));
        }
        Assert.assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Assert.assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            Assert.assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGetMethodWithInteger() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            Assert.assertEquals(attributes[i], denseInstance.get(i));
        }
        Assert.assertNull(denseInstance.get(-1));
        Assert.assertNull(denseInstance.get(4));
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
        SortedSet<Integer> keySet = denseInstance.keySet();
        Assert.assertEquals(3, keySet.size());
        for (Integer key : keySet) {
            Assert.assertTrue(denseInstance.containsKey(key));
        }
        Assert.assertFalse(keySet.contains(-1));
        Assert.assertFalse(keySet.contains(4));
    }

    @Test
    public void testPutMethodWithIntegerAndDouble() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
        denseInstance.put(0, 4.0);
        Assert.assertEquals(4.0, denseInstance.value(0), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutMethodWithIntegerAndNull() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutMethodWithIntegerAndString() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(0, "StringValue");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutMethodWithInvalidIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(-1, 4.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutMethodWithIndexGreaterThanSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(4, 4.0);
    }

    @Test
    public void testPutAllMethodWithMap() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        denseInstance.putAll(map);
        Assert.assertEquals(4.0, denseInstance.value(0), 0.0);
        Assert.assertEquals(5.0, denseInstance.value(1), 0.0);
        Assert.assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttributeMethodWithInt() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        Assert.assertEquals(2, denseInstance.noAttributes());
        Assert.assertNotNull(denseInstance.attributes);
        Assert.assertEquals(1.0, denseInstance.attributes[0], 0.0);
        Assert.assertEquals(3.0, denseInstance.attributes[1], 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeMethodWithInvalidIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeMethodWithIndexGreaterThanSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(4);
    }

    @Test
    public void testHashCodeMethod() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        Assert.assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        Assert.assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEqualsMethodWithSameObject() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes);
        DenseInstance denseInstance2 = new DenseInstance(attributes);
        Assert.assertTrue(denseInstance1.equals(denseInstance2));
    }

    @Test
    public void testEqualsMethodWithDifferentClass() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Object anotherObject = new Object();
        Assert.assertFalse(denseInstance.equals(anotherObject));
    }

    @Test
    public void testEqualsMethodWithDifferentAttributes() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        Assert.assertFalse(denseInstance1.equals(denseInstance2));
    }

    @Test
    public void testEqualsMethodWithDenseInstanceWithSameAttributesButDifferentClassValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue1 = "ClassValue1";
        Object classValue2 = "ClassValue2";
        DenseInstance denseInstance1 = new DenseInstance(attributes, classValue1);
        DenseInstance denseInstance2 = new DenseInstance(attributes, classValue2);
        Assert.assertFalse(denseInstance1.equals(denseInstance2));
    }

    @Test
    public void testEqualsMethodWithSameInstance() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertTrue(denseInstance.equals(denseInstance));
    }

    @Test
    public void testCopyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        Assert.assertEquals(attributes, copiedInstance.attributes);
        Assert.assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributesMethodWithSet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);
        indicesToRemove.add(3);
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttributes(indicesToRemove);
        Assert.assertEquals(2, denseInstance.noAttributes());
        Assert.assertNotNull(denseInstance.attributes);
        Assert.assertEquals(1.0, denseInstance.attributes[0], 0.0);
        Assert.assertEquals(3.0, denseInstance.attributes[1], 0.0);
    }

    @Test
    public void testToStringMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        String expectedToString = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        String resultToString = denseInstance.toString();
        Assert.assertEquals(expectedToString, resultToString);
    }
}