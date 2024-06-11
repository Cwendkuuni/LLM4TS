package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import be.abeel.util.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

@RunWith(JUnit4.class)
public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/sample.arff"));
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.numInstances());
        assertEquals(4, dataset.numAttributes());
        assertEquals("weka.class", dataset.classAttribute().getName());
        Instance instance = dataset.instance(0);
        assertArrayEquals(new double[]{1.3, 0.4, 3.2, 4.5}, instance.toArray(), 0.001);
        assertEquals("setosa", instance.stringValue(dataset.classIndex()));

        instance = dataset.instance(1);
        assertArrayEquals(new double[]{2.1, 0.5, 3.0, 5.1}, instance.toArray(), 0.001);
        assertEquals("versicolor", instance.stringValue(dataset.classIndex()));
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/sample.arff"), 5);
        assertFalse(dataset.isEmpty());
        assertEquals(2, dataset.numInstances());
        assertEquals(5, dataset.numAttributes());
        assertEquals(null, dataset.classAttribute());

        Instance instance = dataset.instance(0);
        assertArrayEquals(new double[]{1.3, 0.4, 3.2, 4.5, Double.NaN}, instance.toArray(), 0.001);
        assertNull(instance.stringValue(dataset.classIndex()));

        instance = dataset.instance(1);
        assertArrayEquals(new double[]{2.1, 0.5, 3.0, 5.1, Double.NaN}, instance.toArray(), 0.001);
        assertNull(instance.stringValue(dataset.classIndex()));
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
        ARFFHandler.loadARFF(new File("src/test/resources/invalidFile.arff"));
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/empty.arff"));
        assertTrue(dataset.isEmpty());
        assertEquals(0, dataset.numInstances());
        assertEquals(0, dataset.numAttributes());
    }

    @Test
    public void testLoadARFFWithInvalidFormat() throws IOException {
        LineIterator it = new LineIterator(new File("src/test/resources/invalidFormat.txt"));
        try {
            ARFFHandler.loadARFF(it.getFileRef(), 0);
        } finally {
            it.close();
        }
    }
}