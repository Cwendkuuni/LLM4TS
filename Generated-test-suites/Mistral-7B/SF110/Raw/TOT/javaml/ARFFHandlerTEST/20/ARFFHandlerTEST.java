import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws IOException {
        File file = new File("testData.arff"); // valid ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        Instance instance = dataset.instance(0);
        assertEquals(5.0, instance.value(0), 0.001);
        assertEquals("positive", instance.classValue());

        file = new File("testDataEmpty.arff"); // empty ARFF file
        dataset = ARFFHandler.loadARFF(file);
        assertTrue(dataset.numInstances() == 0);

        file = new File("testDataInvalidFormat.arff"); // ARFF file with invalid format
        try {
            dataset = ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // expected
        }

        file = new File("testDataClassIndexOutOfBounds.arff"); // ARFF file with class index out of bounds
        try {
            ARFFHandler.loadARFF(file, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}