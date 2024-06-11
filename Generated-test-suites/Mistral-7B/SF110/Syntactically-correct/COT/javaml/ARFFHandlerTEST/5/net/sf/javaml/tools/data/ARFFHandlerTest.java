package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import org.junit.Test;

import java.io.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() throws FileNotFoundException {
        File file = new File("testData.arff"); // Replace with the path to a valid ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertFalse(dataset.isEmpty());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("testData.arff"); // Replace with the path to a valid ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, 0);
        assertNotNull(dataset);
        assertFalse(dataset.isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
        File file = new File("nonExistentFile.arff"); // Replace with the path to an invalid file
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testSplitLineWithValidData() {
        String line = "@data,w1,w2,w3,Class"; // A valid ARFF line
        String[] arr = line.split(",");
        assertEquals(5, arr.length);
        assertEquals("@data", arr[0]);
        assertEquals("w1", arr[1]);
        assertEquals("w2", arr[2]);
        assertEquals("w3", arr[3]);
        assertEquals("Class", arr[4]);
    }

    @Test
    public void testSplitLineWithMissingData() {
        String line = "@data,w1,w2,Class"; // A valid ARFF line without the last value
        String[] arr = line.split(",");
        assertEquals(4, arr.length);
        assertNull(arr[4]);
    }

    @Test
    public void testParseDoubleWithValidValue() {
        String value = "42.0";
        double parsedValue = Double.parseDouble(value);
        assertEquals(42.0, parsedValue, 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleWithInvalidValue() {
        String value = "abc";
        Double.parseDouble(value);
    }

    @Test
    public void testCreateInstanceWithValidValues() {
        double[] values = new double[]{1.1, 2.2, 3.3, 4.4};
        String classValue = "label1";
        Instance instance = new DenseInstance(values, classValue);
        assertTrue(instance instanceof Instance);
        assertEquals(values.length, instance.numValues());
        assertEquals(values, instance.values());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testCreateInstanceWithEmptyClassValue() {
        double[] values = new double[]{1.1, 2.2, 3.3, 4.4};
        Instance instance = new DenseInstance(values, null);
        assertTrue(instance instanceof Instance);
        assertEquals(values.length, instance.numValues());
        assertEquals(values, instance.values());
        assertNull(instance.classValue());
    }

    @Test
    public void testAddInstanceToDatasetWithValidValues() {
        Dataset dataset = new DefaultDataset();
        double[] values = new double[]{1.1, 2.2, 3.3, 4.4};
        String classValue = "label1";
        Instance instance = new DenseInstance(values, classValue);
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertTrue(dataset.get(0) instanceof Instance);
        assertEquals(values.length, dataset.get(0).numValues());
        assertEquals(values, dataset.get(0).values());
        assertEquals(classValue, dataset.get(0).classValue());
    }

    @Test
    public void testAddInstanceToDatasetWithEmptyClassValue() {
        Dataset dataset = new DefaultDataset();
        double[] values = new double[]{1.1, 2.2, 3.3, 4.4};
        Instance instance = new DenseInstance(values, null);
        dataset.add(instance);
        assertEquals(1, dataset.size());
        assertTrue(dataset.get(0) instanceof Instance);
        assertEquals(values.length, dataset.get(0).numValues());
        assertEquals(values, dataset.get(0).values());
        assertNull(dataset.get(0).classValue());
    }
}