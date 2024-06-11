package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ARFFHandlerTest {

    private File file;
    private LineIterator lineIterator;
    private Dataset dataset;

    @Before
    public void setup() {
        file = mock(File.class);
        lineIterator = mock(LineIterator.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.length()).thenReturn(1024L);
        when(file.getAbsolutePath()).thenReturn("test.arff");
        when(file.getParent()).thenReturn("/home/user/data");

        when(lineIterator.hasNext()).thenReturn(true, false);
        when(lineIterator.next()).thenAnswer(i -> "test" + i.intValue());

        dataset = new DefaultDataset();
    }

    @Test
    public void testLoadARFFWithNoClassIndex() throws IOException {
        when(lineIterator.next()).thenReturn("@data");
        when(lineIterator.hasNext()).thenReturn(true);
        when(lineIterator.next()).thenReturn("@attribute name1 type1");
        when(lineIterator.next()).thenReturn("@attribute name2 type2");
        when(lineIterator.next()).thenReturn("name1 value1, name2 value2");
        when(lineIterator.next()).thenReturn("");

        ARFFHandler.loadARFF(file, dataset);
        assertEquals(1, dataset.numInstances());
        Instance instance = dataset.instance(0);
        assertArrayEquals(new double[] { 0.0, 0.0 }, instance.toDoubleArray());
        assertEquals("value1", instance.stringValue(0));
        assertEquals("value2", instance.stringValue(1));
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        when(lineIterator.next()).thenReturn("@data");
        when(lineIterator.hasNext()).thenReturn(true);
        when(lineIterator.next()).thenReturn("@attribute name1 type1");
        when(lineIterator.next()).thenReturn("@attribute name2 type2");
        when(lineIterator.next()).thenReturn("name1 value1, name2 value2, name3 value3");
        when(lineIterator.next()).thenReturn("");

        ARFFHandler arffHandler = new ARFFHandler();
        Dataset dataset = arffHandler.loadARFF(file, 2);

        assertEquals(1, dataset.numInstances());
        Instance instance = dataset.instance(0);
        double[] values = instance.toDoubleArray();
        assertArrayEquals(new double[] { 0.0, 0.0, 1.0 }, values);
        assertEquals("value1", instance.stringValue(0));
        assertEquals("value2", instance.stringValue(1));
        assertEquals("value3", instance.stringValue(2));
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithFileNotFoundException() throws IOException {
        when(file.exists()).thenReturn(false);
        ARFFHandler.loadARFF(file, dataset);
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws IOException {
        when(lineIterator.hasNext()).thenReturn(false);
        ARFFHandler.loadARFF(file, dataset);
        assertEquals(0, dataset.numInstances());
    }

    @Test
    public void testLoadARFFWithMalformedLine() throws IOException {
        when(lineIterator.next()).thenReturn("@data");
        when(lineIterator.hasNext()).thenReturn(true);
        when(lineIterator.next()).thenReturn("@attribute name1 type1");
        when(lineIterator.next()).thenReturn("name1 value1, name2 value2, name3");
        ARFFHandler.loadARFF(file, dataset);
        assertEquals(1, dataset.numInstances());
        Instance instance = dataset.instance(0);
        assertArrayEquals(new double[] { 0.0, 0.0, Double.NaN }, instance.toDoubleArray());
        assertEquals("value1", instance.stringValue(0));
        assertEquals("value2", instance.stringValue(1));
        assertEquals(null, instance.stringValue(2));
    }

    @Test
    public void testLoadARFFWithMissingAttributeType() throws IOException {
        when(lineIterator.next()).thenReturn("@data");
        when(lineIterator.hasNext()).thenReturn(true);
        when(lineIterator.next()).thenReturn("@attribute name1");
        when(lineIterator.next()).thenReturn("name1 value1, name2 value2, name3 value3");
        ARFFHandler.loadARFF(file, dataset);
        assertEquals(1, dataset.numInstances());
        Instance instance = dataset.instance(0);
        assertArrayEquals(new double[] { 0.0, 0.0, Double.NaN }, instance.toDoubleArray());
        assertEquals("value1", instance.stringValue(0));
        assertEquals("value2", instance.stringValue(1));
        assertEquals(null, instance.stringValue(2));
    }
}