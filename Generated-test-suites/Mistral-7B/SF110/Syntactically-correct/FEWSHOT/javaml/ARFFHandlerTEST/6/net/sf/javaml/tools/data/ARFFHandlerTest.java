package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import be.abeel.util.LineIterator;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Before;
import org.junit.Test;

public class ARFFHandlerTest {

    private LineIterator lineIteratorMock;
    private File fileMock;

    @Before
    public void setup() {
        lineIteratorMock = mock(LineIterator.class);
        fileMock = mock(File.class);
    }

    @Test
    public void testLoadARFFWithNullFile() {
        try {
            ARFFHandler.loadARFF(null);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testLoadARFF() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.toURI()).thenReturn(new File("test.arff").toURI());
        when(lineIteratorMock.hasNext()).thenReturn(true, true, true, true, false);
        when(lineIteratorMock.next()).thenReturn("@data", "@attribute foo num", "@attribute bar num", "@data", "foo,1.2,3.4,bar,5.6");

        Dataset dataset = ARFFHandler.loadARFF(fileMock);
        assertEquals(1, dataset.numInstances());
        Instance instance = dataset.instance(0);
        assertEquals(2, instance.numAttributes());
        assertEquals("foo", instance.attribute(0).name());
        assertEquals("bar", instance.attribute(1).name());
        assertEquals(1.2, instance.value(0), 0.01);
        assertEquals(3.4, instance.value(1), 0.01);
        assertEquals(5.6, instance.value(2), 0.01);
        assertNull(instance.value(3));
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.toURI()).thenReturn(new File("test.arff").toURI());
        when(lineIteratorMock.hasNext()).thenReturn(true, true, true, true, false);
        when(lineIteratorMock.next()).thenReturn("@data", "@attribute foo num", "@attribute bar num", "@attribute class str", "@data", "foo,1.2,3.4,bar,red", "foo,1.2,3.4,bar,blue");

        Dataset dataset = ARFFHandler.loadARFF(fileMock, 2);
        assertEquals(2, dataset.numInstances());
        Instance instance1 = dataset.instance(0);
        assertEquals(3, instance1.numAttributes());
        assertEquals("foo", instance1.attribute(0).name());
        assertEquals("bar", instance1.attribute(1).name());
        assertEquals("class", instance1.attribute(2).name());
        assertEquals("red", instance1.value(2));
        Instance instance2 = dataset.instance(1);
        assertEquals("foo", instance2.attribute(0).name());
        assertEquals("bar", instance2.attribute(1).name());
        assertEquals("class", instance2.attribute(2).name());
        assertEquals("blue", instance2.value(2));
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws IOException {
        when(fileMock.exists()).thenReturn(true);
        when(lineIteratorMock.hasNext()).thenReturn(false);

        Dataset dataset = ARFFHandler.loadARFF(fileMock);
        assertEquals(0, dataset.numInstances());
    }
}