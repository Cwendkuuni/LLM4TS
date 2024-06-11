import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFFileWithValidData() throws IOException {
 File file = new File("testData/validARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);

 // Check if the dataset is not null and has at least one instance
 Assert.assertNotNull(dataset);
 Assert.assertFalse(dataset.isEmpty());

 // Check if the first instance has the correct attributes and class value
 Instance firstInstance = dataset.instance(0);
 double[] attributes = firstInstance.toArray();
 String classValue = firstInstance.classValue().toString();

 Assert.assertEquals(5, attributes.length); // Replace with the actual number of attributes
 Assert.assertEquals("attribute1", dataset.attribute(0).name());
 Assert.assertEquals("attribute2", dataset.attribute(1).name());
 // Add other assertions for other attributes and class value

 // Check if the class index is set correctly
 Dataset datasetWithClassIndex = ARFFHandler.loadARFF(file, 2);
 for (Instance instance : datasetWithClassIndex) {
 Assert.assertEquals(instance.classValue(), instance.value(2));
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFFileWithInvalidFile() throws IOException {
 File file = new File("testData/invalidFile.txt"); // Replace with a non-existent file
 Dataset dataset = ARFFHandler.loadARFF(file);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFFileWithInvalidValue() throws IOException {
 File file = new File("testData/invalidValueARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFFileWithEmptyDataMode() throws IOException {
 File file = new File("testData/emptyDataModeARFFFile.arff"); // Replace with a file that starts with @data keyword without any data
 Dataset dataset = ARFFHandler.loadARFF(file);

 // Check if the dataset is empty
 Assert.assertTrue(dataset.isEmpty());
 }
}