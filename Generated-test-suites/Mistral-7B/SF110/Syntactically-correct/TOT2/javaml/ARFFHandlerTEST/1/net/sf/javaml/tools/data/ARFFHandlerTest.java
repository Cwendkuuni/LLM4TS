import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import java.io.File;
import java.io.IOException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    // All the tests from Experts 1 and 2
    // ...

    @Test
    public void testLoadARFFWithNullFile() {
        File file = null;
        assertThrows(NullPointerException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFWithMissingClassIndex() {
        File file = new File("testData/validARFFfile.arff");
        assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file, 5)); // Assume class index is out of bounds
    }

    @Test
    public void testLoadARFFWithMalformedLine() throws FileNotFoundException {
        File file = new File("testData/ARFFfileWithMalformedLine.arff");
        assertThrows(IllegalArgumentException.class, () -> ARFFHandler.loadARFF(file));
    }
}