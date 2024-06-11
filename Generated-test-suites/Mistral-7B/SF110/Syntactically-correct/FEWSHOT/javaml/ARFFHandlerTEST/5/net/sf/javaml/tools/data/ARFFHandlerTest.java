package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import be.abeel.util.LineIterator;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithNullFile() {
        assertNull(ARFFHandler.loadARFF(null));
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws IOException {
        File file = new File("test_empty.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset.size() == 0);
        file.delete();
    }

    @Test
    public void testLoadARFFWithNegativeClassIndex() throws IOException {
        File file = new File("test_simple.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, -2);
        assertEquals(dataset.size(), 3);
        file.delete();
    }

    @Test
    public void testLoadARFFWithValidClassIndex() throws IOException {
        File file = new File("test_simple.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 2);
        assertEquals(dataset.size(), 3);
        Instance instance = dataset.instance(0);
        assertEquals(instance.classValue().toString(), "Iris-setosa");
        file.delete();
    }

    @Test
    public void testLoadARFFWithMissingClassIndex() throws IOException {
        File file = new File("test_simple.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 4);
        assertEquals(dataset.size(), 3);
        file.delete();
    }

    @Test
    public void testLoadARFFWithMixedDataTypes() throws IOException {
        File file = new File("test_mixed.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertEquals(dataset.size(), 2);
        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);
        assertArrayEquals(new double[]{1.1, 2.2, 3.3}, instance1.toDoubleArray(), 0.001);
        assertEquals(instance1.classValue().toString(), "A");
        assertArrayEquals(new double[]{Double.NaN, 4.4, Double.NaN}, instance2.toDoubleArray(), 0.001);
        assertEquals(instance2.classValue().toString(), "B");
        file.delete();
    }

    @Test
    public void testLoadARFFWithMissingAttributeValues() throws IOException {
        File file = new File("test_missing_values.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertEquals(dataset.size(), 3);
        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);
        Instance instance3 = dataset.instance(2);
        assertArrayEquals(new double[]{1.1, 2.2, Double.NaN}, instance1.toDoubleArray(), 0.001);
        assertNull(instance1.classValue());
        assertArrayEquals(new double[]{Double.NaN, 4.4, 3.3}, instance2.toDoubleArray(), 0.001);
        assertEquals(instance2.classValue().toString(), "C");
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, 5.5}, instance3.toDoubleArray(), 0.001);
        assertEquals(instance3.classValue().toString(), "D");
        file.delete();
    }

    @Test
    public void testLoadARFFWithMultipleClasses() throws IOException {
        File file = new File("test_multiple_classes.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertEquals(dataset.size(), 5);
        Instance instance1 = dataset.instance(0);
        Instance instance2 = dataset.instance(1);
        Instance instance3 = dataset.instance(2);
        Instance instance4 = dataset.instance(3);
        Instance instance5 = dataset.instance(4);
        assertArrayEquals(new double[]{1.1, 2.2, 3.3}, instance1.toDoubleArray(), 0.001);
        assertEquals(instance1.classValue().toString(), "Class1");
        assertArrayEquals(new double[]{4.4, 5.5, 6.6}, instance2.toDoubleArray(), 0.001);
        assertEquals(instance2.classValue().toString(), "Class2");
        assertArrayEquals(new double[]{7.7, 8.8, Double.NaN}, instance3.toDoubleArray(), 0.001);
        assertNull(instance3.classValue());
        assertArrayEquals(new double[]{Double.NaN, 10.1, 11.1}, instance4.toDoubleArray(), 0.001);
        assertEquals(instance4.classValue().toString(), "Class3");
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, 12.1}, instance5.toDoubleArray(), 0.001);
        assertEquals(instance5.classValue().toString(), "Class4");
        file.delete();
    }
}