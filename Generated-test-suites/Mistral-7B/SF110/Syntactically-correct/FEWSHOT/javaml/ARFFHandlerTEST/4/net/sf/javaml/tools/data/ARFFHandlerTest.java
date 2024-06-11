package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.dataset.DatasetWriter;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws IOException {
        File file = new File("testData.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset instanceof DefaultDataset);
        assertTrue(dataset.size() > 0);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        File file = new File("testData.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(dataset instanceof DefaultDataset);
        assertTrue(dataset.size() > 0);
        assertEquals(2, dataset.attributeNames().length);
    }

    @Test
    public void testLoadARFFWithEmptyFile() {
        File file = new File("empty.arff");
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFInvalidFormat() {
        File file = new File("invalidFormat.arff");
        assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFMultipleInstances() throws IOException {
        File file = new File("testDataMultiInstances.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset instanceof DefaultDataset);
        assertTrue(dataset.size() > 0);
        assertEquals(3, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassAttribute() throws IOException {
        File file = new File("testDataWithClassAttribute.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset instanceof DefaultDataset);
        assertTrue(dataset.size() > 0);
        assertEquals(3, dataset.attributeNames().length);
        Instance firstInstance = dataset.instance(0);
        assertEquals("A", firstInstance.value(2));
        assertEquals("B", firstInstance.value(1));
    }

    @Test
    public void testLoadARFFWithNumericAttributes() throws IOException {
        File file = new File("testDataNumericAttributes.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset instanceof DefaultDataset);
        assertTrue(dataset.size() > 0);
        Instance firstInstance = dataset.instance(0);
        double[] values = (double[]) firstInstance.values();
        assertArrayEquals(new double[]{1.0, 2.1, 3.0}, values, 0.001);
    }

    @Test
    public void testSaveARFF() throws IOException {
        File file = new File("testData.arff");
        Dataset dataset = createTestDataset();
        DatasetWriter writer = new ARFFDatasetWriter();
        writer.write(dataset, file.getAbsolutePath());
        Dataset loadedDataset = ARFFHandler.loadARFF(file);
        assertEquals(dataset, loadedDataset);
    }

    private Dataset createTestDataset() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[]{1.0, 2.1}, "A"));
        dataset.add(new DenseInstance(new double[]{1.1, 2.2}, "B"));
        return dataset;
    }
}