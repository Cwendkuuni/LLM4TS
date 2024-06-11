import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
 // Test loading a valid ARFF file
 File file = new File("testData/validARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Check if the data has been loaded correctly
 assertEquals(4, dataset.numInstances());
 assertEquals(5, dataset.numAttributes());
 Instance instance = dataset.instance(0);
 Assert.assertEquals(1.0, instance.value(0), 0.001);
 Assert.assertEquals("A", instance.value(1));
 Assert.assertEquals(2.0, instance.value(2), 0.001);
 Assert.assertEquals("B", instance.value(3));
 Assert.assertEquals(3.0, instance.value(4), 0.001);
 // Test loading a ARFF file with class index
 dataset = ARFFHandler.loadARFF(file, 1);
 instance = dataset.instance(0);
 Assert.assertEquals("A", instance.value(0));
 Assert.assertEquals(1.0, instance.value(1), 0.001);
 Assert.assertEquals(2.0, instance.value(2), 0.001);
 Assert.assertEquals("B", instance.value(3));
 Assert.assertEquals(3.0, instance.value(4), 0.001);
 Assert.assertEquals("B", instance.classValue());
 }

 @Test
 public void testLoadARFFWithNaN() throws IOException {
 // Test loading a ARFF file with NaN value
 File file = new File("testData/NaNARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 // Check if the data has been loaded correctly
 Instance instance = dataset.instance(0);
 Assert.assertEquals(1.0, instance.value(0), 0.001);
 Assert.assertEquals("A", instance.value(1));
 Assert.assertEquals(Double.NaN, instance.value(2));
 Assert.assertEquals("B", instance.value(3));
 Assert.assertEquals(3.0, instance.value(4), 0.001);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
 // Test loading a invalid ARFF file
 File file = new File("testData/invalidARFF.arff");
 ARFFHandler.loadARFF(file);
 }
}