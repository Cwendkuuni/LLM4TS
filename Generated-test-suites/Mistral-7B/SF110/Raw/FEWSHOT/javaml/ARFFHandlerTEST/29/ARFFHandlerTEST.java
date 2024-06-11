package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("test.arff");
        when(mockFile.length()).thenReturn(100L);

        Dataset ds = ARFFHandler.loadARFF(mockFile);
        assertNotNull(ds);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_FileDoesntExist() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);
        ARFFHandler.loadARFF(mockFile);
    }

    @Test
    public void testLoadARFF_EmptyFile() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("test.arff");
        when(mockFile.length()).thenReturn(0L);

        Dataset ds = ARFFHandler.loadARFF(mockFile);
        assertNotNull(ds);
        assertTrue(ds.size() == 0);
    }

    @Test
    public void testLoadARFF_SingleInstance() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("test.arff");
        when(mockFile.getLines()).thenReturn(Arrays.asList(
                "@relation relName",
                "@attribute attr1 num",
                "@attribute attr2 num",
                "@data",
                "1,2.5,3.2"
        ));

        Dataset ds = ARFFHandler.loadARFF(mockFile);
        assertNotNull(ds);
        Instance instance = ds.instance(0);
        assertEquals(2, ds.numInstances());
        assertEquals(3, instance.numAttributes());
        assertArrayEquals(new double[] {1.0, 2.5, 3.2}, instance.values(), 0.0);
    }

    @Test
    public void testLoadARFF_MultipleInstances() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("test.arff");
        when(mockFile.getLines()).thenReturn(Arrays.asList(
                "@relation relName",
                "@attribute attr1 num",
                "@attribute attr2 num",
                "@data",
                "1,2.5,3.2",
                "2,3.5,4.3"
        ));

        Dataset ds = ARFFHandler.loadARFF(mockFile);
        assertNotNull(ds);
        assertEquals(2, ds.numInstances());
        Instance instance1 = ds.instance(0);
        Instance instance2 = ds.instance(1);
        assertEquals(3, instance1.numAttributes());
        assertEquals(3, instance2.numAttributes());
        assertArrayEquals(new double[] {1.0, 2.5, 3.2}, instance1.values(), 0.0);
        assertArrayEquals(new double[] {2.0, 3.5, 4.3}, instance2.values(), 0.0);
    }

    @Test
    public void testLoadARFF_WithClassIndex() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("test.arff");
        when(mockFile.getLines()).thenReturn(Arrays.asList(
                "@relation relName",
                "@attribute attr1 num",
                "@attribute attr2 num",
                "@attribute class str",
                "@data",
                "1,2.5,3.2,red",
                "2,3.5,4.3,blue"
        ));

        Dataset ds = ARFFHandler.loadARFF(mockFile, 2);
        assertNotNull(ds);
        assertEquals(2, ds.numInstances());
        Instance instance1 = ds.instance(0);
        Instance instance2 = ds.instance(1);
        assertEquals(4, instance1.numAttributes());
        assertEquals(4, instance2.numAttributes());
        assertArrayEquals(new double[] {1.0, 2.5, 3.2}, instance1.values(), 0.0);
        assertArrayEquals(new double[] {2.0, 3.5, 4.3}, instance2.values(), 0.0);
        assertEquals("red", instance1.value(3));
        assertEquals("blue", instance2.value(3));
    }

    @Test
    public void testLoadARFF_WithInvalidClassIndex() throws IOException {
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.getAbsolutePath()).thenReturn("test.arff");
        when(mockFile.getLines()).thenReturn(Arrays.asList(
                "@relation relName",
                "@attribute attr1 num",
                "@attribute attr2 num",
                "@attribute class str",
                "@data",
                "1,2.5,3.2,red",
                "2,3.5,4.3,blue"
        ));

        try {
            ARFFHandler.loadARFF(mockFile, 4);
            fail("Expected FileFormatException");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

}