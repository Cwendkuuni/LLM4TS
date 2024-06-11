import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_NoClassIndex() throws FileNotFoundException {
        File file = new File("testData.arff"); // Replace with the path to your ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(4, dataset.numInstances()); // Replace with the expected number of instances in your test data
    }
}