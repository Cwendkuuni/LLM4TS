import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File file = new File("path/to/existing/file.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("path/to/existing/file.txt", result.toString());
 }

 @Test
 public void testStringResult() {
 String string = "Hello World!";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Hello World!", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals("3.14", result.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgument() {
 FSPathResult result = new FSPathResult(null);
 }
}