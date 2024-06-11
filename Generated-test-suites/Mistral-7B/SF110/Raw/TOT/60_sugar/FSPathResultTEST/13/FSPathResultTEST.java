import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("test.txt");
 FSPathResult result = new FSPathResult(file);
 assertEquals("test.txt", result.toString());
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "Hello World!";
 FSPathResult result = new FSPathResult(string);
 assertEquals("Hello World!", result.toString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);
 assertEquals("3.14", result.toString());
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);
 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNull() {
 FSPathResult result = new FSPathResult(null); // should throw an IllegalArgumentException
 fail("IllegalArgumentException not thrown");
 }
}