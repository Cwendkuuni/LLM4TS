package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFEmptyFile() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/ARFFHandler/empty.arff"));
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File should exist: " + e.getMessage());
        }
    }

    @Test
    public void testLoadARFFSimple() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/ARFFHandler/simple.arff"));
            assertEquals(3, dataset.size());
            Instance instance1 = dataset.instance(0);
            Instance instance2 = dataset.instance(1);
            Instance instance3 = dataset.instance(2);

            assertEquals(2, instance1.numAttributes());
            assertEquals("a", instance1.attribute(0).name());
            assertEquals("b", instance1.attribute(1).name());
            assertEquals(2.0, instance1.value(0), 0.0);
            assertEquals(3.0, instance1.value(1), 0.0);
            assertEquals("c", instance1.value(2));

            assertEquals(2, instance2.numAttributes());
            assertEquals("a", instance2.attribute(0).name());
            assertEquals("b", instance2.attribute(1).name());
            assertEquals(1.0, instance2.value(0), 0.0);
            assertEquals(4.0, instance2.value(1), 0.0);
            assertNull(instance2.value(2));

            assertEquals(2, instance3.numAttributes());
            assertEquals("a", instance3.attribute(0).name());
            assertEquals("b", instance3.attribute(1).name());
            assertEquals(Double.NaN, instance3.value(0), 0.0);
            assertEquals(Double.NaN, instance3.value(1), 0.0);
            assertNull(instance3.value(2));

        } catch (FileNotFoundException e) {
            fail("File should exist: " + e.getMessage());
        }
    }

    @Test
    public void testLoadARFFClassIndex() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/ARFFHandler/simple.arff"), 1);
            assertEquals(3, dataset.size());
            Instance instance1 = dataset.instance(0);
            Instance instance2 = dataset.instance(1);
            Instance instance3 = dataset.instance(2);

            assertEquals(2, instance1.numAttributes());
            assertEquals("a", instance1.attribute(0).name());
            assertEquals("b", instance1.attribute(1).name());
            assertEquals(2.0, instance1.value(0), 0.0);
            assertEquals(3.0, instance1.value(1), 0.0);
            assertNull(instance1.value(2));

            assertEquals(1, instance2.numAttributes());
            assertEquals("b", instance2.attribute(0).name());
            assertEquals("c", instance2.value(0));

            assertEquals(2, instance3.numAttributes());
            assertEquals("a", instance3.attribute(0).name());
            assertEquals("b", instance3.attribute(1).name());
            assertEquals(Double.NaN, instance3.value(0), 0.0);
            assertEquals(Double.NaN, instance3.value(1), 0.0);
            assertEquals("d", instance3.value(2));

        } catch (FileNotFoundException e) {
            fail("File should exist: " + e.getMessage());
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFNonexistentFile() {
        ARFFHandler.loadARFF(new File("src/test/resources/ARFFHandler/nonexistent.arff"));
    }

    @Test
    public void testLoadARFFInvalidFormat() {
        try {
            File invalidFile = new File("src/test/resources/ARFFHandler/invalid.arff");
            LineIterator it = new LineIterator(invalidFile);
            it.setSkipBlanks(true);
            it.setCommentIdentifier("%");
            it.setSkipComments(true);

            String line = it.nextLine();
            assertEquals("@relation myRelation", line);

            line = it.nextLine();
            it.add(line); // Add the line to the iterator to mimic reading from a file
            fail("Expected FileNotFoundException, but got: " + line);

        } catch (FileNotFoundException e) {
            // Expected
        } catch (IOException e) {
            fail("Unexpected: " + e.getMessage());
        }
    }
}