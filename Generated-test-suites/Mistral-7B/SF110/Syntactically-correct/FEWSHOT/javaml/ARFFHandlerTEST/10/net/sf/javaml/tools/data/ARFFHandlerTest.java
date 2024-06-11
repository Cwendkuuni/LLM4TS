package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() throws IOException {
        File file = new File("testData/validARFFFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(5, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        File file = new File("testData/validARFFFileWithClassIndex.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 2);
        assertNotNull(dataset);
        assertEquals(5, dataset.size());
        Instance instance = dataset.instance(0);
        assertEquals("classValue1", instance.classValue().toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
        File file = new File("testData/invalidARFFFile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws IOException {
        File file = new File("testData/emptyARFFFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFFWithNegativeClassIndex() throws IOException {
        File file = new File("testData/validARFFFileWithClassIndex.arff");
        ARFFHandler.loadARFF(file, -1);
    }

    @Test
    public void testLoadARFFWithNLines() throws IOException {
        File file = new File("testData/NLinesARFFFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(10, dataset.size());
    }

    @Test
    public void testLoadARFFWithNoDataMode() throws IOException {
        File file = new File("testData/validARFFFileNoDataMode.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithMissingDataMode() throws IOException {
        File file = new File("testData/missingDataModeARFFFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithNanValues() throws IOException {
        File file = new File("testData/nanValuesARFFFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        Instance instance = dataset.instance(0);
        assertEquals("classValue1", instance.classValue().toString());
        assertNotNull(instance.value(0));
        assertNotNull(instance.value(1));
        assertNull(instance.value(2));
        assertEquals(Double.NaN, instance.value(3), 0.0);
        assertEquals(Double.NaN, instance.value(4), 0.0);
    }
}