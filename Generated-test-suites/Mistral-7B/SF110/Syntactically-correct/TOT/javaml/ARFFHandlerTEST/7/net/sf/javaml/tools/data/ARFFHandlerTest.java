import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.*;
import java.io.*;

public class ARFFHandlerTest {

 // Test case for loadARFF method with valid ARFF file
 @Test
 public void testLoadARFFValidFile() throws Exception {
 File file = new File("path/to/valid/arff/file.arff");
 Dataset expectedDataset = new DefaultDataset();
 // Add instances to expectedDataset for testing
 // ...
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
 for (int i = 0; i < expectedDataset.numInstances(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.0);
 }
 }

 // Test case for loadARFF method with classIndex and valid ARFF file
 @Test
 public void testLoadARFFValidFileWithClassIndex() throws Exception {
 File file = new File("path/to/valid/arff/file.arff");
 Dataset expectedDataset = new DefaultDataset();
 // Add instances to expectedDataset for testing
 // ...
 Dataset actualDataset = ARFFHandler.loadARFF(file, 1); // classIndex is 1
 assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
 for (int i = 0; i < expectedDataset.numInstances(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 // Exclude class value from comparison
 double[] expectedValues = (double[]) expectedInstance.toArray();
 double[] actualValues = (double[]) actualInstance.toArray();
 Arrays.setAll(Arrays.copyOfRange(expectedValues, 0, expectedValues.length - 1), (d) -> Double.NaN);
 Arrays.sort(expectedValues);
 Arrays.sort(actualValues);
 assertArrayEquals(expectedValues, actualValues);
 }
 }

 // Test case for loadARFF method with empty ARFF file
 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFEmptyFile() throws Exception {
 File file = new File("path/to/empty/arff/file.arff");
 ARFFHandler.loadARFF(file);
 }

 // Test case for loadARFF method with non-existent file
 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFNonExistentFile() throws Exception {
 File file = new File("path/to/nonexistent/file.arff");
 ARFFHandler.loadARFF(file);
 }

 // Test case for loadARFF method with malformed ARFF file
 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFMalformedFile() throws Exception {
 File file = new File("path/to/malformed/arff/file.arff");
 ARFFHandler.loadARFF(file);
 }
}