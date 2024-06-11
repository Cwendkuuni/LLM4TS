package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.*;
import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() {
        File file = new File("testData.arff"); // Replace with valid ARFF file
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "class1"));
        expected.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "class2"));
        Dataset actual = ARFFHandler.loadARFF(file);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("testData.arff"); // Replace with valid ARFF file
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0}, "class1"));
        expected.add(new DenseInstance(new double[]{4.0}, "class2"));
        Dataset actual = ARFFHandler.loadARFF(file, 0);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalidFile.txt");
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFWithEmptyFile() {
        File file = new File("emptyData.arff");
        Dataset expected = new DefaultDataset();
        Dataset actual = ARFFHandler.loadARFF(file);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @BeforeClass
    public static void setup() {
        // Set up
    }

    @AfterClass
    public static void teardown() {
        // Tear down
    }
}