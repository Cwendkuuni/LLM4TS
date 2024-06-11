import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);

 assertEquals("path/to/existing/file", result.toString());
 }

 @Test
 public void testStringResult() {
 String string = "test string";
 FSPathResult result = new FSPathResult(string);

 assertEquals("test string", result.getString());
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals("3.14", result.getDouble().toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.getDate().toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.getURI().toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), result.getBoolean().toString());
 }

 @Test
 public void testEmptyResult() {
 FSPathResult result = new FSPathResult();

 assertEquals("FSPathResult : empty", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullResult() {
 FSPathResult result = new FSPathResult(null);
 }
}