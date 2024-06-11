import static org.junit.Assert.assertNotNull;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff"); // Replace with a valid ARFF file path
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff"); // Replace with a valid ARFF file path
        Dataset dataset = ARFFHandler.loadARFF(file, 0); // Assuming class index is 0
        assertNotNull(dataset);
    }
}