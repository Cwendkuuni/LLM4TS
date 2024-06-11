package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws IOException {
        File file = new File("testData.arff"); // Replace with valid ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() {
        File file = new File("nonExistentFile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        File file = new File("testData.arff");
        int classIndex = 2; // Replace with valid class index
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFFEmptyDataset() throws IOException {
        File file = new File("emptyTestData.arff"); // Replace with valid empty ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertTrue(dataset.size() == 0);
    }

    @Test
    public void testLoadARFFSingleInstance() throws IOException {
        File file = new File("singleInstanceTestData.arff"); // Replace with valid single instance ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testLoadARFFMultipleInstances() throws IOException {
        File file = new File("multipleInstancesTestData.arff"); // Replace with valid multiple instances ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertTrue(dataset.size() > 1);
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadARFFWithInvalidNumberFormat() throws IOException {
        File file = new File("invalidNumberFormatTestData.arff"); // Replace with valid ARFF file containing an instance with an invalid number format
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFWithMissingClassIndex() throws IOException {
        File file = new File("missingClassIndexTestData.arff"); // Replace with valid ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, -2);
        assertNotNull(dataset);
    }
}