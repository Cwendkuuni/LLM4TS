package net.sf.javaml.tools.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFFile() throws Exception {
        File file = new File("testData.arff"); // Replace with the path to your ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFFFileWithClassIndex() throws Exception {
        File file = new File("testData.arff"); // Replace with the path to your ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, 2);
        assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws Exception {
        File file = new File("nonexistentfile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFDataMode() throws Exception {
        File file = new File("testData.arff"); // Replace with the path to your ARFF file
        ARFFHandler handler = new ARFFHandler();
        LineIterator it = new LineIterator(file);
        it.setSkipBlanks(true);
        it.setCommentIdentifier("%");
        it.setSkipComments(true);
        boolean dataMode = false;
        String line;
        while ((line = it.nextLine()) != null) {
            if (dataMode) {
                String[] arr = line.split(",");
                int length = arr.length;
                if (length < 3) {
                    fail("Invalid data line: " + line);
                }
                String classValue = arr[length - 1];
                if (length > 3) {
                    double[] values = new double[length - 1];
                    for (int i = 0; i < length - 1; ++i) {
                        double value;
                        try {
                            value = Double.parseDouble(arr[i]);
                        } catch (NumberFormatException e) {
                            value = Double.NaN;
                        }
                        values[i] = value;
                    }
                    Instance instance = new DenseInstance(values, classValue);
                    handler.add(instance);
                }
            }
            if (line.equalsIgnoreCase("@data")) {
                dataMode = true;
            }
        }
        Dataset dataset = handler.getDataSet();
        assertNotNull(dataset);
        assertTrue(dataset.size() > 0);
    }
}