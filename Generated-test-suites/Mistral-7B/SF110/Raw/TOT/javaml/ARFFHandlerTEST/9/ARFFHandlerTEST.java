import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {

 // Test case for loadARFF(File file) method
 @Test
 public void testLoadARFF() throws FileNotFoundException {
 final File file = new File("testData/test.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, "class1"}, new String[]{"attribute1", "attribute2", "class"}));
 expectedDataset.add(new DenseInstance(new double[]{3.0, 4.0, "class2"}, new String[]{"attribute1", "attribute2", "class"}));

 Dataset actualDataset = ARFFHandler.loadARFF(file);
 assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
 for (int i = 0; i < expectedDataset.numInstances(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 Instance actualInstance = actualDataset.instance(i);
 assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.01);
 }
 }

 // Test case for loadARFF(File file, int classIndex) method
 @Test
 public void testLoadARFFWithClassIndex() throws FileNotFoundException {
 final File file = new File("testData/test.arff");
 Dataset expectedDataset = new DefaultDataset();
 expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0}, new String[]{"attribute1", "attribute2"}));
 expectedDataset.add(new DenseInstance(new double[]{3.0, 4.0}, new String[]{"attribute1", "attribute2"}));
 expectedDataset.add(new DenseInstance(new String[]{"class1", "class2"}, new String[]{"class"}));

 Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
 assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
 for (int i = 0; i < expectedDataset.numInstances(); i++) {
 Instance expectedInstance = expectedDataset.instance(i);
 if (expectedInstance.classValue() instanceof String) {
 String expectedClass = (String) expectedInstance.classValue();
 String actualClass = ((DenseInstance) actualDataset.instance(i)).classValue();
 assertEquals(expectedClass, actualClass);
 } else {
 Instance expectedNumericInstance = expectedDataset.instance(i);
 Instance actualNumericInstance = actualDataset.instance(i);
 assertArrayEquals(expectedNumericInstance.toArray(), actualNumericInstance.toArray(), 0.01);
 }
 }
 }

 // Test case for handling NaN values
 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithNaN() throws FileNotFoundException {
 final File file = new File("testData/testWithNaN.arff");
 ARFFHandler.loadARFF(file);
 }

 // Test case for handling invalid file
 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFInvalidFile() throws FileNotFoundException {
 ARFFHandler.loadARFF(new File("testData/invalidFile.txt"));
 }

 // Test case for handling empty ARFF file
 @Test
 public void testLoadARFFEmptyFile() throws FileNotFoundException {
 Dataset expectedDataset = new DefaultDataset();
 Dataset actualDataset = ARFFHandler.loadARFF(new File("testData/empty.arff"));
 assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
 }
}