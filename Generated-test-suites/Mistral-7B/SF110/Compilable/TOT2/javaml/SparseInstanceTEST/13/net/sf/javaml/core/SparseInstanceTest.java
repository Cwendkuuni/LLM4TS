package net.sf.javaml.core;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance emptyInstance = new SparseInstance();
        assertEquals(0, emptyInstance.noAttributes());
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test(expected = NullPointerException.class)
    public void testSparseInstanceWithNullDatavector() {
        double[] datavector = null;
        new SparseInstance(datavector);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSparseInstanceWithNegativeNoAttributes() {
        new SparseInstance(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSparseInstanceWithNullClassValue() {
        new SparseInstance(5, null);
    }

    @Test
    public void testSparseInstanceWithEmptyDataVector() {
        SparseInstance instance = new SparseInstance(new double[0]);
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }
}