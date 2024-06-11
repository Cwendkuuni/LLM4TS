import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("path/to/existing/file.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("path/to/existing/file.txt", result.toString());
 }

 @Test
 public void testFSPathResultString() {
 String string = "Hello World!";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Hello World!", result.toString());
 }

 @Test
 public void testFSPathResultDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals("3.14", result.toString());
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullConstructor() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringNullResult() {
 FSPathResult result = null;
 assertNull(result.toString());
 }
}