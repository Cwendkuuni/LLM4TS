import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

 // Test case 1: Load ARFF file with valid data and default class index
 @Test
 public void testLoadARFFValidData() throws FileNotFoundException {
 File file = new File("testData.arff"); // Replace with the actual path of your ARFF file
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }

 // Test case 2: Load ARFF file with valid data and specified class index
 @Test
 public void testLoadARFFSpecifiedClassIndex() throws FileNotFoundException {
 File file = new File("testData.arff"); // Replace with the actual path of your ARFF file
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
 assertEquals(expectedDataset, actualDataset);
 }

 // Test case 3: Load ARFF file with missing data and check for correct handling of NaN values
 @Test
 public void testLoadARFFMissingData() throws FileNotFoundException {
 File file = new File("testDataMissingData.arff"); // Replace with the actual path of your ARFF file with missing data
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, Double.NaN, 3.0}, "Class1"));
 expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0, Double.NaN}, "Class2"));
 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset, actualDataset);
 }

 // Test case 4: Load ARFF file with incorrect format
 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFIncorrectFormat() throws FileNotFoundException {
 File file = new File("testDataIncorrectFormat.txt"); // Replace with the actual path of your incorrect format file
 ARFFHandler.loadARFF(file);
 }
}