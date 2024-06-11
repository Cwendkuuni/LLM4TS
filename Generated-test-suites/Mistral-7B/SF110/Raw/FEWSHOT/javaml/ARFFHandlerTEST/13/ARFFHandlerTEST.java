package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws IOException {
        final File file = new File("testData.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset instanceof DefaultDataset);
        assertEquals(6, dataset.numInstances());
        assertEquals(10, dataset.numAttributes());
        dataset.close();
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        final File file = new File("testData.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 4);
        assertTrue(dataset instanceof DefaultDataset);
        assertEquals(6, dataset.numInstances());
        assertEquals(9, dataset.numAttributes());
        dataset.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
        ARFFHandler.loadARFF(new File("nonExistentFile.arff"));
    }

    @Test
    public void testSplitLine() {
        String line = "@relation relationName { attribute1:type1 attribute2:type2 }";
        String[] splitLine = line.split("\\s+");
        assertEquals("relationName", splitLine[0]);
        assertEquals("attribute1:type1", splitLine[1]);
        assertEquals("attribute2:type2", splitLine[2]);
    }

    @Test
    public void testProcessLineDataMode() {
        String line = "instance1, value1, value2, value3, value4, value5, value6, value7, value8, value9";
        String[] arr = line.split(",");
        double[] values = new double[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            double val = Double.parseDouble(arr[i]);
            values[i] = val;
        }
        Instance instance = new DenseInstance(values, null);
        assertEquals(instance.toString(), "[value1, value2, value3, value4, value5, value6, value7, value8, value9]");
    }

    @Test
    public void testProcessLineDataModeWithClassValue() {
        String line = "instance1, value1, value2, value3, value4, value5, value6, value7, value8, classValue";
        String[] arr = line.split(",");
        double[] values = new double[arr.length - 1];
        String classValue = arr[arr.length - 1];
        for (int i = 0; i < arr.length - 1; ++i) {
            double val = Double.parseDouble(arr[i]);
            values[i] = val;
        }
        Instance instance = new DenseInstance(values, classValue);
        assertEquals(instance.toString(), "[value1, value2, value3, value4, value5, value6, value7][" + classValue + "]");
    }

    @Test
    public void testProcessLineComment() {
        String line = "% This is a comment";
        assertNull(ARFFHandler.processLine(line, false));
    }

    @Test
    public void testProcessLineAttributeDefinition() {
        String line = "@attribute attributeName attributeType";
        Attribute attribute = ARFFHandler.processLine(line, true);
        assertEquals("attributeName", attribute.getName());
        assertEquals("attributeType", attribute.getType().getName());
    }

    @Test
    public void testProcessLineDataModeNullValues() {
        String line = "instance1,";
        Instance instance = ARFFHandler.processLine(line, false);
        assertNull(instance);
    }

    @Test
    public void testProcessLineCommentWithData() {
        String line = "% This is a comment instance1, value1, value2, value3, value4, value5, value6, value7, value8, value9";
        Instance instance = ARFFHandler.processLine(line, false);
        assertNull(instance);
    }

    @Test
    public void testProcessLineAttributeDefinitionWithData() {
        String line = "@attribute attributeName attributeType instance1, value1, value2, value3, value4, value5, value6, value7, value8, value9";
        Attribute attribute = ARFFHandler.processLine(line, true);
        assertEquals("attributeName", attribute.getName());
        assertEquals("attributeType", attribute.getType().getName());
        assertNull(attribute.getValue());
    }

    @Test
    public void testProcessLineHeader() {
        String line = "@data";
        boolean dataMode = ARFFHandler.processLine(line, false);
        assertTrue(dataMode);
    }

    @Test
    public void testProcessLineHeaderWithData() {
        String line = "@data instance1, value1, value2, value3, value4, value5, value6, value7, value8, value9";
        Instance instance = ARFFHandler.processLine(line, false);
        assertNotNull(instance);
    }
}