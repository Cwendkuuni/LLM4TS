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

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullFile() {
 File file = null;
 FSPathResult result = new FSPathResult(file);
 }

 @Test
 public void testFSPathResultString() {
 String string = "Hello World";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Hello World", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullString() {
 String string = null;
 FSPathResult result = new FSPathResult(string);
 }

 @Test
 public void testFSPathResultDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals("3.14", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullDouble() {
 Double doubleValue = null;
 FSPathResult result = new FSPathResult(doubleValue);
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullDate() {
 Date date = null;
 FSPathResult result = new FSPathResult(date);
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullURI() {
 URI uri = null;
 FSPathResult result = new FSPathResult(uri);
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullBoolean() {
 Boolean booleanValue = null;
 FSPathResult result = new FSPathResult(booleanValue);
 }
}