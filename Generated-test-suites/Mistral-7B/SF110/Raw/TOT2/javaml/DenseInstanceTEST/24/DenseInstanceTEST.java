package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class DenseInstanceTest {
    @Test
    public void testDenseInstance() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, denseInstance.size());
        // test other basic methods here
    }

    @Test
    public void testDenseInstanceWithClassValue() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 'A');
        assertEquals('A', denseInstance.classValue());
        // test other basic methods here
    }

    @Test
    public void testEmptyArrayInConstructor() {
        try {
            new DenseInstance(new double[0]);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassValueInConstructor() {
        try {
            new DenseInstance(new double[]{1.0, 2.0, 3.0}, null);
            fail("A NullPointerException should have been thrown.");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSizeInConstructor() {
        try {
            new DenseInstance(-1);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testNegativeIndexValue() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            denseInstance.value(-1);
            fail("An IndexOutOfBoundsException should have been thrown.");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
    }

    // test other edge cases and exceptions here

    @Test
    public void testPut() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        denseInstance.put(0, 5.0);
        assertEquals(5.0, denseInstance.get(0), 1.0E-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            denseInstance.put(null, 5.0);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithDuplicateKey() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 1.0});
        try {
            denseInstance.put(0, 5.0);
            denseInstance.put(0, 6.0);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllWithNullMap() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            denseInstance.putAll(null);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllWithDuplicateKeys() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 5.0);
        map.put(0, 6.0);
        try {
            denseInstance.putAll(map);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        try {
            denseInstance.remove(0);
            fail("An UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullKeySet() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = null;
        try {
            denseInstance.removeAttributes(indices);
            fail("A NullPointerException should have been thrown.");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributesWithEmptySet() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<>();
        try {
            denseInstance.removeAttributes(indices);
            fail("An UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testRemoveAttributes() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indices);
        assertEquals(3, denseInstance.size());
    }

    // test other methods here
}