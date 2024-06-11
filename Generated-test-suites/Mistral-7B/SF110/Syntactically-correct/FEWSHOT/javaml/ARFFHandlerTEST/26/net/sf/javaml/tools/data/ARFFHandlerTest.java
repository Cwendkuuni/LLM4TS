package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Attribute.DataType;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute attribute2 {value1, value2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset expectedDataset = new DefaultDataset();
        expectedDataset.add(new DenseInstance(new double[]{1.0}, "value1"));
        expectedDataset.add(new DenseInstance(new double[]{2.0}, "value2"));
        expectedDataset.add(new DenseInstance(new double[]{3.0}, "value1"));

        Dataset actualDataset = ARFFHandler.loadARFF(file);
        assertEquals(expectedDataset, actualDataset);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute attribute2 {value1, value2}\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1, class1\n" +
                "2.0, value2, class2\n" +
                "3.0, value1, class1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset expectedDataset = new DefaultDataset();
        Attribute attribute1 = new Attribute("attribute1", DataType.NUMERIC);
        Attribute attribute2 = new Attribute("attribute2", DataType.NOMINAL);
        Attribute classAttribute = new Attribute("classAttribute", DataType.NOMINAL);
        expectedDataset.add(new DenseInstance(new double[]{1.0}, "value1", "class1"));
        expectedDataset.add(new DenseInstance(new double[]{2.0}, "value2", "class2"));
        expectedDataset.add(new DenseInstance(new double[]{3.0}, "value1", "class1"));

        Dataset actualDataset = ARFFHandler.loadARFF(file, 2);
        assertEquals(expectedDataset, actualDataset);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithNullFile() {
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(null));
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws FileNotFoundException {
        File file = createTempFile("test_ARFFHandler.arff");
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithInvalidARFFFormat() throws FileNotFoundException, IOException {
        String arffContent = "invalid_format";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingDataSection() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute attribute2 {value1, value2}\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file));
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingAttributeDefinition() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file));
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMisalignedData() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@data\n" +
                "1.0 value1\n" +
                "2.0, value2\n" +
                "3.0 value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file));
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingClassAttribute() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 0);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingClassValue() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, \n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 2);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingNumericValue() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2. value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingNumericValueInFirstInstance() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "value1, 1.0\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingNumericValueInLastInstance() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                ",\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithMissingNumericValueInLastInstanceAndClassValue() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0,\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithTruncatedData() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n" +
                "4.0,\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithDuplicateInstances() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class2}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
        assertTrue(actualDataset.size() == 3);
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithDuplicateClassValues() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {class1, class1}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file, 1));
        file.deleteOnExit();
    }

    @Test
    public void testLoadARFFWithEmptyClassAttribute() throws FileNotFoundException, IOException {
        String arffContent = "@relation testRel\n" +
                "@attribute attribute1 real\n" +
                "@attribute classAttribute {}\n" +
                "@data\n" +
                "1.0, value1\n" +
                "2.0, value2\n" +
                "3.0, value1\n";

        File file = createTempFile("test_ARFFHandler.arff", arffContent);
        assertThrows(IOException.class, () -> ARFFHandler.loadARFF(file, 1));
        file.deleteOnExit();
    }

    private File createTempFile(String fileName, String content) throws IOException {
        File file = File.createTempFile(fileName, ".arff");
        OutputStream os = new FileOutputStream(file);
        InputStream is = new ByteArrayInputStream(content.getBytes());
        byte[] buffer = new byte[1024];
        int length;

        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        os.close();
        is.close();
        return file;
    }
}