import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.util.Arrays;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "Label1";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        Assert.assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        Assert.assertEquals(classValue, denseInstance.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        double[] expectedAttributes = new double[size];
        Assert.assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            Assert.assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueMethodWithInvalidIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.value(attributes.length);
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        Assert.assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKeyMethodWithValidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            Assert.assertTrue(denseInstance.containsKey(i));
        }
    }

    @Test
    public void testContainsKeyMethodWithInvalidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValueMethodWithValidValue() {
        double[] attributes = new double[]{1.0, 1.5, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (double value : attributes) {
            Assert.assertTrue(denseInstance.containsValue(value));
        }
    }

    @Test
    public void testContainsValueMethodWithInvalidValue() {
        double[] attributes = new double[]{1.0, 1.5, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> expectedEntrySet = new HashSet<>();
        for (int i = 0; i < attributes.length; i++) {
            expectedEntrySet.add(Map.entry(i, (double) i));
        }
        Set<Map.Entry<Integer, Double>> actualEntrySet = denseInstance.entrySet();
        Assert.assertEquals(expectedEntrySet, actualEntrySet);
    }

    @Test
    public void testGetMethodWithValidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            Assert.assertEquals(attributes[i], denseInstance.get(i), 0.0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.get(4);
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
        SortedSet<Integer> expectedKeys = new TreeSet<>(Arrays.asList(0, 1, 2));
        SortedSet<Integer> actualKeys = denseInstance.keySet();
        Assert.assertEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testPutMethodWithValidKeyAndValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 5.0;
        int index = 3;
        denseInstance.put(index, newValue);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0, newValue}, denseInstance.attributes, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutMethodWithInvalidKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(4, 5.0);
    }

    @Test
    public void testPutAllMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double[] newAttributes = new double[]{4.0, 5.0, 6.0};
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < newAttributes.length; i++) {
            map.put(i, (double) i + 3.0);
        }
        denseInstance.putAll(map);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, denseInstance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.remove(0);
    }

    @Test
    public void testSizeMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Assert.assertEquals(attributes.length, denseInstance.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeMethodDeprecated() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.noAttributes();
    }

    @Test
    public void testValuesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);
        Collection<Double> actualValues = denseInstance.values();
        Assert.assertEquals(expectedValues, actualValues);
    }
}