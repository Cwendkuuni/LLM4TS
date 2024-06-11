import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("testFile.txt", result.getFile().getAbsolutePath());
 assertEquals("testFile.txt", result.toString());
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "Hello World";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Hello World", result.getString());
 assertEquals("FSPathResult : Hello World", result.toString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals(3.14, result.getDouble().doubleValue(), 0.0);
 assertEquals("3.14", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullDouble() {
 Double nullDouble = null;
 FSPathResult result = new FSPathResult(nullDouble);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.getDate().toString());
 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.getURI().toString());
 assertEquals("http://www.example.com", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullURI() {
 URI nullUri = null;
 FSPathResult result = new FSPathResult(nullUri);
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), result.getBoolean().toString());
 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullBoolean() {
 Boolean nullBoolean = null;
 FSPathResult result = new FSPathResult(nullBoolean);
 }
}