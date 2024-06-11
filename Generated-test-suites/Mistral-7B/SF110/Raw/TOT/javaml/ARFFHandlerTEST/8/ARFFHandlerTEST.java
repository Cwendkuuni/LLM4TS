import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.*;
import java.io.*;

public class ARFFHandlerTest {

 // Test case for loadARFF method with valid ARFF file
 @Test
 public void testLoadARFFValidFile() throws FileNotFoundException {
 File file = new File("testData/validARFFFile.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1, 3.2}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1, 6.2}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }

 // Test case for loadARFF method with invalid file extension
 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFInvalidFileExtension() throws FileNotFoundException {
 File file = new File("testData/invalidFile.txt");
 ARFFHandler.loadARFF(file);
 }

 // Test case for loadARFF method with missing "@data" keyword
 @Test(expected = IllegalArgumentException.class)
 public void testLoadARFFMissingDataKeyword() throws FileNotFoundException {
 File file = new File("testData/missingDataKeyword.arff");
 ARFFHandler.loadARFF(file);
 }

 // Test case for loadARFF method with incorrect class index
 @Test(expected = IllegalArgumentException.class)
 public void testLoadARFFIncorrectClassIndex() throws FileNotFoundException {
 File file = new File("testData/validARFFFile.arff");
 ARFFHandler.loadARFF(file, 5);
 }

 // Test case for loadARFF method with NaN values
 @Test
 public void testLoadARFFNaNValues() throws FileNotFoundException {
 File file = new File("testData/NaNValues.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, Double.NaN, 3.2}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1, Double.NaN}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }

 // Test case for loadARFF method with commas in class values
 @Test
 public void testLoadARFFCommasInClassValues() throws FileNotFoundException {
 File file = new File("testData/commasInClassValues.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1, 3.2}, "Class1,Class2"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1, 6.2}, "Class3,Class4"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }
}