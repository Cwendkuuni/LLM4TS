import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File file = new File("test.txt");
 FSPathResult result = new FSPathResult(file);
 assertEquals("test.txt", result.getFile().getAbsolutePath());
 assertEquals("test.txt", result.toString());
 }

 @Test
 public void testStringResult() {
 String str = "test string";
 FSPathResult result = new FSPathResult(str);
 assertEquals("test string", result.getString());
 assertEquals("test string", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double d = 3.14;
 FSPathResult result = new FSPathResult(d);
 assertEquals("3.14", result.getDouble().toString());
 assertEquals("3.14", result.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 assertEquals(date.toString(), result.getDate().toString());
 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);
 assertEquals(uri.toString(), result.getURI().toString());
 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean bool = true;
 FSPathResult result = new FSPathResult(bool);
 assertEquals("true", result.getBoolean().toString());
 assertEquals("true", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullResult() {
 FSPathResult result = new FSPathResult(null);
 fail("Should throw NullPointerException");
 }
}