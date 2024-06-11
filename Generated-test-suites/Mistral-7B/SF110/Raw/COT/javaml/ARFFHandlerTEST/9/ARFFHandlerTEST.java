package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws IOException {
        File file = new File("path/to/your/ARFF/file.arff"); // Replace with the path to your ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(numberOfInstancesInYourARFFFile, dataset.numInstances());
        assertEquals(numberOfAttributesInYourARFFFile, dataset.numAttributes());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistentFile() {
        File file = new File("path/to/non-existent/file.arff"); // Replace with the path to a non-existent file
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        File file = new File("path/to/your/ARFF/file.arff"); // Replace with the path to your ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, 0); // Assuming class is the first attribute
        assertNotNull(dataset);
        assertEquals(numberOfInstancesInYourARFFFile, dataset.numInstances());
        assertEquals(numberOfAttributesInYourARFFFile, dataset.numAttributes());
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadARFFWithInvalidData() throws IOException {
        File file = new File("path/to/your/ARFF/file.arff"); // Replace with the path to your ARFF file
        ARFFHandler.loadARFF(file); // Assuming there is a line with invalid data
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadARFFWithInvalidClassIndex() throws IOException {
        File file = new File("path/to/your/ARFF/file.arff"); // Replace with the path to your ARFF file
        int invalidClassIndex = numberOfAttributesInYourARFFFile + 1; // Assuming an invalid class index
        ARFFHandler.loadARFF(file, invalidClassIndex);
    }
}