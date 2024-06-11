import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFNoClassIndex() throws IOException {
 File file = new File("testData/testARFFFile.arff"); // Replace with valid ARFF file
 Dataset expectedDataSet = new DefaultDataset();
 expectedDataSet.add(new DenseInstance(new double[]{1.0, 2.0, "A"}, new Object[]{"Feature1", "Feature2", "ClassLabel"})); // Replace with valid data
 expectedDataSet.add(new DenseInstance(new double[]{3.0, 4.0, "B"}, new Object[]{"Feature1", "Feature2", "ClassLabel"})); // Replace with valid data

 Dataset actualDataSet = ARFFHandler.loadARFF(file);

 assertEquals(expectedDataSet.size(), actualDataSet.size());

 for (int i = 0; i < expectedDataSet.size(); i++) {
 Instance expectedInstance = expectedDataSet.instance(i);
 Instance actualInstance = actualDataSet.instance(i);

 assertEquals(expectedInstance.classValue().toString(), actualInstance.classValue().toString());

 for (int j = 0; j < expectedInstance.numAttributes(); j++) {
 assertEquals(expectedInstance.value(j), actualInstance.value(j), 0.001); // Allow slight difference due to double precision
 }
 }
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 File file = new File("testData/testARFFFile.arff"); // Replace with valid ARFF file
 Dataset expectedDataSet = new DefaultDataset();
 // Add instances with valid data for the class index
 // ...

 Dataset actualDataSet = ARFFHandler.loadARFF(file, 1); // Set the class index to 1

 assertEquals(expectedDataSet.size(), actualDataSet.size());

 for (int i = 0; i < expectedDataSet.size(); i++) {
 Instance expectedInstance = expectedDataSet.instance(i);
 Instance actualInstance = actualDataSet.instance(i);

 assertEquals(expectedInstance.value(1), actualInstance.value(0), 0.001); // Assuming the class attribute is the first attribute
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFFileNotFound() throws IOException {
 File file = new File("testData/nonExistentFile.arff"); // Replace with non-existent file

 try {
 ARFFHandler.loadARFF(file);
 fail("FileNotFoundException expected");
 } catch (FileNotFoundException e) {
 // Expected
 }
 }

}