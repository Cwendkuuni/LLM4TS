import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("testFile.txt", result.toString());
 }

 @Test
 public void testStringResult() {
 String string = "Test String";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Test String", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 123.45;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals("123.45", result.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.google.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullResult() {
 FSPathResult result = new FSPathResult(null);
 }
}