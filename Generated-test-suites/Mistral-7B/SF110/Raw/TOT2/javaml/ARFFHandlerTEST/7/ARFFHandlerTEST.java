import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithValidFile() throws IOException {
        File file = new File("path/to/valid/arff/file.arff");
        Dataset expected = new DefaultDataset();
        // Add instances to expected dataset for validation
        Dataset actual = ARFFHandler.loadARFF(file);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLoadARFFWithValidFileAndClassIndex() throws IOException {
        File file = new File("path/to/valid/arff/file.arff");
        Dataset expected = new DefaultDataset();
        // Add instances to expected dataset for validation
        Dataset actual = ARFFHandler.loadARFF(file, 0); // Assuming first column is the class
        Assert.assertEquals(expected, actual);
    }
}