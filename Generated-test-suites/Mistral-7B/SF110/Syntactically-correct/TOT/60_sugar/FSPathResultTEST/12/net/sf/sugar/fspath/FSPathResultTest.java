import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);
 assertEquals("testFile.txt", result.getFile().getAbsolutePath());
 assertEquals("testFile.txt", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullFile() {
 FSPathResult result = new FSPathResult(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testFSPathResultString() {
 String string = "Hello World";
 FSPathResult result = new FSPathResult(string);
 assertEquals("Hello World", result.getString());
 assertEquals("Hello World", result.toString());
 }

 @Test
 public void testFSPathResultDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);
 assertEquals(doubleValue, result.getDouble());
 assertEquals("3.14", result.toString());
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 assertEquals(date, result.getDate());
 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);
 assertEquals(uri, result.getURI());
 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 assertEquals(booleanValue, result.getBoolean());
 assertEquals(booleanValue.toString(), result.toString());
 }
}