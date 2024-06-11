package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceHandler;
import net.sf.javaml.core.SimpleDataset;
import net.sf.javaml.util.LineIterator;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFEmptyFile() {
        assertNull(ARFFHandler.loadARFF(new File("empty.arff")));
    }

    @Test
    public void testLoadARFFSingleInstanceFile() throws IOException {
        final File file = new File("single_instance.arff");
        final Dataset ds = ARFFHandler.loadARFF(file);
        assertNotNull(ds);
        assertEquals(1, ds.numInstances());
        Instance instance = ds.instance(0);
        assertNotNull(instance);
        assertEquals(1, instance.numAttributes());
        Attribute attribute = instance.attribute(0);
        assertEquals("A", attribute.name());
        assertEquals(1.0d, instance.value(0), 0.001d);
    }

    @Test
    public void testLoadARFFMultipleInstancesFile() throws IOException {
        final File file = new File("multiple_instances.arff");
        final Dataset ds = ARFFHandler.loadARFF(file);
        assertNotNull(ds);
        assertEquals(3, ds.numInstances());
        InstanceHandler handler = new DefaultInstanceHandler();
        SimpleDataset simpleDataset = new SimpleDataset(ds.attributeNames(), ds.numInstances(), handler);
        Double[][] expectedValues = new Double[][] {
            new Double[] { 1.0d, 3.0d },
            new Double[] { 2.0d, 5.0d },
            new Double[] { 3.0d, 7.0d }
        };
        for (int i = 0; i < ds.numInstances(); ++i) {
            Instance instance = ds.instance(i);
            Double[] actualValues = new Double[instance.numValues()];
            for (int j = 0; j < instance.numValues(); ++j) {
                actualValues[j] = (Double) instance.value(j);
            }
            assertArrayEquals(expectedValues[i], actualValues, 0.001d);
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() throws IOException {
        final File file = new File("multiple_instances.arff");
        final Dataset ds = ARFFHandler.loadARFF(file, 1);
        assertNotNull(ds);
        assertEquals(3, ds.numInstances());
        InstanceHandler handler = new DefaultInstanceHandler();
        SimpleDataset simpleDataset = new SimpleDataset(ds.attributeNames(), ds.numInstances(), handler);
        String[] expectedClassValues = new String[] { "B", "A", "B" };
        for (int i = 0; i < ds.numInstances(); ++i) {
            Instance instance = ds.instance(i);
            String actualClassValue = (String) instance.classValue();
            assertEquals(expectedClassValues[i], actualClassValue);
        }
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() throws IOException {
        final File file = new File("multiple_instances.arff");
        try {
            ARFFHandler.loadARFF(file, ds.numAttributes() + 1);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        assertNull(ARFFHandler.loadARFF(new File("invalid_file.txt")));
    }

    @Test
    public void testLoadARFFWithEmptyLineComment() throws IOException {
        final File file = new File("empty_comment.arff");
        final Dataset ds = ARFFHandler.loadARFF(file);
        assertNotNull(ds);
        assertEquals(1, ds.numInstances());
        assertTrue(ds.instance(0).isMissing());
    }

    @Test
    public void testLoadARFFWithHeaderLine() throws IOException {
        final File file = new File("header.arff");
        final Dataset ds = ARFFHandler.loadARFF(file);
        assertNotNull(ds);
        assertEquals(1, ds.numInstances());
        Instance instance = ds.instance(0);
        assertNotNull(instance);
        assertEquals(2, instance.numAttributes());
        Attribute attribute1 = instance.attribute(0);
        assertEquals("A", attribute1.name());
        assertEquals("Attribute1", attribute1.name());
        assertEquals("Numeric", attribute1.type());
        Attribute attribute2 = instance.attribute(1);
        assertEquals("B", attribute2.name());
        assertEquals("Attribute2", attribute2.name());
        assertEquals("Categorical", attribute2.type());
    }

    @Test
    public void testLoadARFFWithMissingValue() throws IOException {
        final File file = new File("missing_value.arff");
        final Dataset ds = ARFFHandler.loadARFF(file);
        assertNotNull(ds);
        assertEquals(1, ds.numInstances());
        Instance instance = ds.instance(0);
        assertNotNull(instance);
        assertEquals(2, instance.numAttributes());
        Double missingValue = Double.NaN;
        assertEquals(missingValue, instance.value(1), 0.001d);
    }

    @Test
    public void testLoadARFFWithInvalidValue() throws IOException {
        final File file = new File("invalid_value.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}