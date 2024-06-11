package net.sf.javaml.tools.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ARFFHandlerTest {

    private static final String TEST_FILE = "testData.arff";

    @Parameterized.Parameters(name = "{index}: loadARFF(file)")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new File(TEST_FILE)},
                {new File(TEST_FILE), 0},
                {new File(TEST_FILE), 1}
        });
    }

    private File file;
    private int classIndex;

    @Test
    public void testLoadARFF() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() throws IOException {
        File invalidFile = new File("invalidFile.arff");
        ARFFHandler.loadARFF(invalidFile);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFFDataMode() throws IOException {
        LineIterator lineIterator = new LineIterator(file);
        lineIterator.setSkipBlanks(true);
        lineIterator.setCommentIdentifier("%");
        lineIterator.setSkipComments(true);

        boolean dataMode = false;
        String line;
        while ((line = lineIterator.nextLine()) != null) {
            if (dataMode) {
                String[] arr = line.split(",");
                assertTrue(arr.length > 0);
                break;
            }
            if (line.equalsIgnoreCase("@data")) {
                dataMode = true;
            }
        }

        lineIterator.close();
    }

    @Test(expected = IOException.class)
    public void testLoadARFFWithInvalidData() throws IOException {
        File fileWithInvalidData = new File("testDataInvalid.arff");
        ARFFHandler.loadARFF(fileWithInvalidData);
    }

    @Test
    public void testLoadARFFInstance() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        Instance instance = dataset.instance(0);
        assertNotNull(instance);
        assertTrue(instance instanceof DenseInstance);
        Object classValue = instance.classValue();
        assertNull(classValue);
    }

    @Test
    public void testLoadARFFWithClassValue() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        Instance instance = dataset.instance(0);
        assertNotNull(instance);
        assertTrue(instance instanceof DenseInstance);
        Object classValue = instance.classValue();
        if (classIndex >= 0) {
            assertTrue(classValue != null);
        }
    }

    @Test
    public void testLoadARFFWithMultipleInstances() throws IOException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        int numInstances = dataset.numInstances();
        assertTrue(numInstances > 0);

        for (int i = 0; i < numInstances; ++i) {
            Instance instance = dataset.instance(i);
            assertNotNull(instance);
            assertTrue(instance instanceof DenseInstance);
        }
    }
}