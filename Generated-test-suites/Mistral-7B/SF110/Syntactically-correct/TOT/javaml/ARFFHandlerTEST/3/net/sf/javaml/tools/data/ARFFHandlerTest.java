import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws FileNotFoundException {
 // Test loading a valid ARFF file with no class attribute
 File fileNoClass = new File("testData/testNoClass.arff");
 DefaultDataset expectedNoClass = new DefaultDataset();
 expectedNoClass.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null));
 expectedNoClass.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, null));
 DefaultDataset actualNoClass = ARFFHandler.loadARFF(fileNoClass);
 assertEquals(expectedNoClass, actualNoClass);

 // Test loading a valid ARFF file with a class attribute
 File fileWithClass = new File("testData/testWithClass.arff");
 DefaultDataset expectedWithClass = new DefaultDataset();
 expectedWithClass.add(new DenseInstance(new double[]{1.0, 2.0, 3.0, "A"}, null));
 expectedWithClass.add(new DenseInstance(new double[]{4.0, 5.0, 6.0, "B"}, null));
 expectedWithClass.add(new DenseInstance(new double[]{7.0, 8.0, 9.0, "A"}, null));
 DefaultDataset actualWithClass = ARFFHandler.loadARFF(fileWithClass, 3);
 assertEquals(expectedWithClass, actualWithClass);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() {
 File invalidFile = new File("testData/invalidFile.arff");
 ARFFHandler.loadARFF(invalidFile);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidNumber() throws FileNotFoundException {
 File fileWithInvalidNumber = new File("testData/testWithInvalidNumber.arff");
 ARFFHandler.loadARFF(fileWithInvalidNumber, -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLoadARFFWithInvalidClassIndex() throws FileNotFoundException {
 File fileWithInvalidClassIndex = new File("testData/testWithInvalidClassIndex.arff");
 ARFFHandler.loadARFF(fileWithInvalidClassIndex, 5);
 }
}