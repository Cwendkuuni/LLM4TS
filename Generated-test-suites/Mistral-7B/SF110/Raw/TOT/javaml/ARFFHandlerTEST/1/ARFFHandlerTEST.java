import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
 // Test loading a valid ARFF file with no class attribute
 File file = new File("testData/validDataNoClass.arff");
 Dataset expected = createDatasetFromFile("testData/validDataNoClassExpected.arff");
 Dataset actual = ARFFHandler.loadARFF(file);
 assertEquals(expected.numInstances(), actual.numInstances());
 for (int i = 0; i < expected.numInstances(); i++) {
 Instance expectedInstance = expected.instance(i);
 Instance actualInstance = actual.instance(i);
 assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
 }

 // Test loading a valid ARFF file with a class attribute
 file = new File("testData/validDataWithClass.arff");
 expected = createDatasetFromFile("testData/validDataWithClassExpected.arff");
 classIndex = 3; // Class index is the 4th attribute
 actual = ARFFHandler.loadARFF(file, classIndex);
 assertEquals(expected.numInstances(), actual.numInstances());
 for (int i = 0; i < expected.numInstances(); i++) {
 Instance expectedInstance = expected.instance(i);
 Instance actualInstance = actual.instance(i);
 assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
 String expectedClassValue = (String) expectedInstance.value(classIndex);
 String actualClassValue = (String) actualInstance.value(classIndex);
 assertEquals(expectedClassValue, actualClassValue);
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFilePath() throws FileNotFoundException {
 File file = new File("testData/invalidFilePath.arff");
 ARFFHandler.loadARFF(file);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidData() throws IOException, NumberFormatException {
 File file = new File("testData/invalidData.arff");
 ARFFHandler.loadARFF(file);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadARFFWithNegativeClassIndex() throws IOException {
 File file = new File("testData/validDataWithClass.arff");
 int classIndex = -1;
 ARFFHandler.loadARFF(file, classIndex);
 }

 private Dataset createDatasetFromFile(String fileName) throws IOException {
 File file = new File(fileName);
 Dataset dataset = ARFFHandler.loadARFF(file);
 return dataset;
 }
}