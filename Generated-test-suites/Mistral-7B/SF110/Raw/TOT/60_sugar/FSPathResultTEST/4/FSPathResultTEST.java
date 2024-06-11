import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);

assertEquals("path/to/existing/file", result.getFile().getAbsolutePath());
assertEquals("path/to/existing/file", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullFile() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test
 public void testFSPathResultString() {
 String string = "test string";
 FSPathResult result = new FSPathResult(string);

assertEquals(string, result.getString());
assertEquals(string, result.toString());
 }

 @Test
 public void testFSPathResultDouble() {
 Double doubleValue = 123.45;
 FSPathResult result = new FSPathResult(doubleValue);

assertEquals(doubleValue.toString(), result.getDouble().toString());
assertEquals(doubleValue.toString(), result.toString());
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

assertEquals(date.toString(), result.getDate().toString());
assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

assertEquals(uri.toString(), result.getURI().toString());
assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

assertEquals(booleanValue.toString(), result.getBoolean().toString());
assertEquals(booleanValue.toString(), result.toString());
 }

 @Test
 public void testFSPathResultToString() {
 File file = new File("path/to/existing/file");
 FSPathResult resultFile = new FSPathResult(file);
 String string = "test string";
 FSPathResult resultString = new FSPathResult(string);
 Double doubleValue = 123.45;
 FSPathResult resultDouble = new FSPathResult(doubleValue);
 Date date = new Date();
 FSPathResult resultDate = new FSPathResult(date);
 URI uri = new URI("http://www.example.com");
 FSPathResult resultURI = new FSPathResult(uri);
 Boolean booleanValue = false;
 FSPathResult resultBoolean = new FSPathResult(booleanValue);

 assertEquals(file.getAbsolutePath(), resultFile.toString());
 assertEquals(string, resultString.toString());
 assertEquals(doubleValue.toString(), resultDouble.toString());
 assertEquals(date.toString(), resultDate.toString());
 assertEquals(uri.toString(), resultURI.toString());
 assertEquals(booleanValue.toString(), resultBoolean.toString());
 }
}