package net.sf.javaml.tools.data.test;

import junit.framework.TestCase;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

    @org.junit.Test
    public void testLoadARFFWithValidFile() throws IOException {
        File file = new File("input/test.arff"); // Replace with the path to a valid ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(5, dataset.numInstances()); // Replace 5 with the number of instances in the file
    }

    @org.junit.Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
        File file = new File("input/non-existent-file.arff");
        ARFFHandler.loadARFF(file);
    }

    @org.junit.Test
    public void testLoadARFFWithClassIndex() throws IOException {
        File file = new File("input/test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 2); // Replace 2 with the index of the class attribute
        assertNotNull(dataset);
        assertEquals(5, dataset.numInstances());
        Instance instance = dataset.instance(0);
        String classValue = instance.getClassValue();
        assertNotNull(classValue);
    }

    @org.junit.Test(expected = NumberFormatException.class)
    public void testLoadARFFWithInvalidFormat() throws IOException {
        File file = new File("input/test-invalid-format.arff");
        ARFFHandler.loadARFF(file);
    }

    @org.junit.Test
    public void testLoadARFFWithCommentLine() throws IOException {
        File file = new File("input/test-comment.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        // Add a check to ensure the comments are skipped correctly
        assertEquals(5, dataset.numInstances());
    }

    @org.junit.Test
    public void testLoadARFFWithMissingDataModeLine() throws IOException {
        File file = new File("input/test-missing-data.arff"); // ARFF file without @data line
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @org.junit.Test
    public void testLoadARFFWithEmptyData() throws IOException {
        File file = new File("input/test-empty.arff"); // ARFF file with empty @data section
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.numInstances());
    }

    @org.junit.Test
    public void testLoadARFFWithMissingValues() throws IOException {
        File file = new File("input/test-missing-values.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        Instance instance = dataset.instance(0);
       Instance expectedInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "classLabel");
        assertInstanceNotSame(instance, expectedInstance);
    }

    @org.junit.Test
    public void testLoadARFFWithExtraSpaces() throws IOException {
        File file = new File("input/test-extra-spaces.arff"); // ARFF file with extra spaces
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        // Add a check to ensure the extra spaces are skipped correctly
        assertEquals(5, dataset.numInstances());
    }

}