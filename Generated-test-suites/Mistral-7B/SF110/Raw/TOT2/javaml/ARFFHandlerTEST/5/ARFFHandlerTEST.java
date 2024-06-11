import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFFile() throws FileNotFoundException {
        File testFile = new File("testData.arff"); // Replace with the path to your ARFF file
        Dataset expectedDataset = new DefaultDataset(); // Replace with the expected Dataset
        Dataset actualDataset = ARFFHandler.loadARFF(testFile);
        assertEquals(expectedDataset, actualDataset);
    }
}