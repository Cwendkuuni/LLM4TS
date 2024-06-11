package net.sf.javaml.core;

import org.junit.Assert;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance sparseInstance = new SparseInstance();
        assertEquals(0, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes(int noAttributes) {
        SparseInstance sparseInstance = new SparseInstance(noAttributes);
        assertEquals(noAttributes, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
        Double expectedValue = 1.0;
        assertEquals(expectedValue, sparseInstance.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testSparseInstanceWithNullDatavector() {
        double[] datavector = null;
        new SparseInstance(datavector);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSparseInstanceWithNegativeNoAttributes() {
        new SparseInstance(-1);
    }
}