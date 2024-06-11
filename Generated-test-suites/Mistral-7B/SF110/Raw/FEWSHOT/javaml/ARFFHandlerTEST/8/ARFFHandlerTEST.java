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

public class ARFFHandlerTest {

    private LineIterator it;
    private File file;

    @Before
    public void setup() {
        file = new File("testFile.arff"); // Create a mock file for testing
        it = mock(LineIterator.class);
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
    public void testLoadARFFWithEmptyFile() throws IOException {
        when(it.hasNext()).thenReturn(false);
        when(it.next()).thenReturn("");

        File file = new File("emptyFile.arff");
        Dataset ds = ARFFHandler.loadARFF(file);
        assertEquals(0, ds.numInstances());
    }

    @Test
    public void testLoadARFFWithSimpleDataset() throws IOException {
        String[] lines = new String[]{"@data", "", "@attribute A1 real", "", "@attribute A2 real", "", "@attribute A3 categorical", "", "5.0, 2.0, blue", "", "1.5, 3.0, red"};
        when(it.hasNext()).thenReturn(true).thenReturn(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6], lines[7], lines[8], lines[9], lines[10]);

        Dataset ds = ARFFHandler.loadARFF(file);
        assertEquals(2, ds.numInstances());
        assertEquals(3, ds.numAttributes());
        Instance expInstance1 = new DenseInstance(new double[]{5.0, 2.0}, "blue");
        Instance instance1 = ds.instance(0);
        assertArrayEquals(expInstance1.toArray(), instance1.toArray());

        Instance expInstance2 = new DenseInstance(new double[]{1.5, 3.0}, "red");
        Instance instance2 = ds.instance(1);
        assertArrayEquals(expInstance2.toArray(), instance2.toArray());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        String[] lines = new String[]{"@data", "", "@attribute A1 real", "", "@attribute A2 real", "", "@attribute A3 categorical", "@attribute class real", "", "5.0, 2.0, blue, 0.5", "", "1.5, 3.0, red, 0.8"};
        when(it.hasNext()).thenReturn(true).thenReturn(lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6], lines[7], lines[8], lines[9], lines[10], lines[11], lines[12]);

        Dataset ds = ARFFHandler.loadARFF(file, 1);
        assertEquals(2, ds.numInstances());
        assertEquals(4, ds.numAttributes());
        Instance expInstance1 = new DenseInstance(new double[]{5.0, 2.0}, "blue", 0.5);
        Instance instance1 = ds.instance(0);
        assertArrayEquals(expInstance1.toArray(), instance1.toArray());

        Instance expInstance2 = new DenseInstance(new double[]{1.5, 3.0}, "red", 0.8);
        Instance instance2 = ds.instance(1);
        assertArrayEquals(expInstance2.toArray(), instance2.toArray());
    }
}