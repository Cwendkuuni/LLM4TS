import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File file = new File("path/to/existing/file.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("path/to/existing/file.txt", result.getFile().getAbsolutePath());
 assertEquals("path/to/existing/file.txt", result.toString());
 }

 @Test
 public void testStringResult() {
 String string = "This is a test string.";
 FSPathResult result = new FSPathResult(string);

 assertEquals("This is a test string.", result.getString());
 assertEquals("FSPathResult : This is a test string.", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double result = 3.141592653589793;
 FSPathResult resultObject = new FSPathResult(result);

 assertEquals(result, resultObject.getDouble());
 assertEquals("3.141592653589793", resultObject.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date, result.getDate());
 assertEquals("Sat Jan 15 12:13:14 EST 2022", result.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://google.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri, result.getURI());
 assertEquals("http://google.com", result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue, result.getBoolean());
 assertEquals("true", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullResult() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullFileResult() {
 File file = null;
 FSPathResult result = new FSPathResult(file);
 }
}