package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

@RunWith(JUnit4.class)
public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() {
        // Test with a valid ARFF file.
        File file = new File("testdata/validARFFfile.arff");
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1"));
        expected.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "Class2"));

        Dataset actual = ARFFHandler.loadARFF(file);
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadARFFWithValidFileAndClassIndex() {
        // Test with a valid ARFF file and class index.
        File file = new File("testdata/validARFFfile.arff");
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1"));
        expected.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "Class2"));

        Dataset actual = ARFFHandler.loadARFF(file, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadARFFWithEmptyLineAtBeginning() {
        // Test with an ARFF file that has an empty line at the beginning.
        File file = new File("testdata/emptyLineARFFfile.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected.
        }
    }

    @Test
    public void testLoadARFFWithCommentsAtBeginning() {
        // Test with an ARFF file that has comments at the beginning.
        File file = new File("testdata/commentsARFFfile.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected.
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistentFile() {
        // Test with a non-existent ARFF file.
        File file = new File("testdata/nonexistentARFFfile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test(expected = IOException.class)
    public void testLoadARFFWithUnreadableFile() {
        // Test with an unreadable ARFF file.
        File file = new File("testdata/unreadableARFFfile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFWithIncorrectDataFormat() {
        // Test with an ARFF file that has an incorrect data format.
        File file = new File("testdata/incorrectDataFormatARFFfile.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileFormatException");
        } catch (FileNotFoundException e) {
            // Expected.
        } catch (Exception e) {
            // Expected, but not FileNotFoundException.
        }
    }

    @Test
    public void testLoadARFFWithIncorrectClassIndex() {
        // Test with an incorrect class index.
        File file = new File("testdata/validARFFfile.arff");
        try {
            Dataset actual = ARFFHandler.loadARFF(file, 3);
            fail("Expected FileFormatException");
        } catch (FileNotFoundException e) {
            // Expected.
        } catch (Exception e) {
            // Expected, but not FileNotFoundException.
        }
    }

    // Add more test cases as needed.
}