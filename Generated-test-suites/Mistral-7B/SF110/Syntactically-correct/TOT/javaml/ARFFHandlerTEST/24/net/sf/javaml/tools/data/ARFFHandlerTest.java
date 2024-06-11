import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.IOException;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
 // Test loading a valid ARFF file with no class index
 File file = new File("testdata/validARFFfile.arff");
 Dataset expected = new DefaultDataset();
 expected.add(new DenseInstance(new double[]{1.0, 2.1, 3.2}, "Class1"));
 expected.add(new DenseInstance(new double[]{4.0, 5.1, 6.2}, "Class2"));
 Dataset result = ARFFHandler.loadARFF(file);
 assertEquals(expected, result);

 // Test loading a valid ARFF file with a class index
 file = new File("testdata/validARFFfile.arff");
 result = ARFFHandler.loadARFF(file, 1);
 Instance instance = result.instance(0);
 assertEquals("Class1", instance.classValue());

 // Test loading a file that does not exist
 file = new File("nonexistentfile.arff");
 try {
 ARFFHandler.loadARFF(file);
 fail("Expected FileNotFoundException");
 } catch (FileNotFoundException e) {
 // Expected
 }
 }

 @Test
 public void testLoadARFFWithInvalidFormat() throws IOException {
 // Test loading a file with an invalid format
 File file = new File("testdata/invalidARFFfile.txt"); // Not an ARFF file
 Dataset result = ARFFHandler.loadARFF(file);
 assertNull(result);
 }

 @Test
 public void testLoadARFFWithMissingDataMode() throws IOException {
 // Test loading a file without the "@data" identifier
 File file = new File("testdata/missingDataModeARFFfile.arff");
 try {
 ARFFHandler.loadARFF(file);
 fail("Expected FileFormatException");
 } catch (IOException e) {
 Assert.assertInstanceOf(NoSuchElementException.class, e.getCause());
 }
 }
}