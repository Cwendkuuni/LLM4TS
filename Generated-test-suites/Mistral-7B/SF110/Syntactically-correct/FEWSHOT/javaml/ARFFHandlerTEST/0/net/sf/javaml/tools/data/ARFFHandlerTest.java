package net.sf.javaml.tools.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        File file = new File("src/test/resources/ARFFFile.arff");
        Dataset ds = ARFFHandler.loadARFF(file);
        assertNotNull(ds);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        File file = new File("src/test/resources/ARFFFile.arff");
        Dataset ds = ARFFHandler.loadARFF(file, 2);
        assertNotNull(ds);
    }

    @Test
    public void testLoadARFFEmptyFile() {
        File file = new File("src/test/resources/EmptyARFFFile.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadARFFInvalidFormat() {
        File file = new File("src/test/resources/InvalidARFFFile.txt"); // not an ARFF file
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException or IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadARFFDataMode() throws IOException {
        File file = new File("src/test/resources/ARFFFile.arff");
        Dataset ds = ARFFHandler.loadARFF(file);
        assertTrue(ds.size() > 0);
        Instance instance = ds.instance(0);
        assertNotNull(instance);
        double[] values = instance.toArray();
        assertTrue(values.length > 0);
        for (int i = 0; i < values.length; ++i) {
            assertTrue(!Double.isNaN(values[i]));
        }
    }

    @Test
    public void testLoadARFFClassIndex() throws IOException {
        File file = new File("src/test/resources/ARFFFile.arff");
        Dataset ds = ARFFHandler.loadARFF(file, 2);
        assertTrue(ds.size() > 0);
        Instance instance = ds.instance(0);
        assertNotNull(instance);
        String classValue = instance.classValue().toString();
        assertTrue(!classValue.isEmpty());
    }
}