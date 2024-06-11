import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);
 String expected = file.getAbsolutePath();
 String actual = result.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testStringResult() {
 String string = "Hello World";
 FSPathResult result = new FSPathResult(string);
 String expected = string;
 String actual = result.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);
 String expected = doubleValue.toString();
 String actual = result.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 String expected = date.toString();
 String actual = result.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.example.com/test");
 FSPathResult result = new FSPathResult(uri);
 String expected = uri.toString();
 String actual = result.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 String expected = booleanValue.toString();
 String actual = result.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testNullResult() {
 // test for null result
 FSPathResult result = new FSPathResult(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidResult() {
 // test for invalid result (not of supported types)
 Object invalidResult = new Object();
 FSPathResult result = new FSPathResult(invalidResult);
 }
}