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
 String string = "Hello World";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Hello World", result.getString());
 assertEquals("Hello World", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals(3.14, result.getDouble(), 0.01);
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
 URI uri = new URI("http://example.com/");
 FSPathResult result = new FSPathResult(uri);

 assertEquals("http://example.com/", result.getURI().toString());
 assertEquals("http://example.com/", result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertTrue(result.getBoolean());
 assertEquals("true", result.toString());
 }

 @Test (expected = NullPointerException.class)
 public void testNullInput() {
 // Test for null input in File constructor
 FSPathResult result = new FSPathResult(null);
 }

 @Test (expected = NullPointerException.class)
 public void testNullInput2() {
 // Test for null input in String constructor
 FSPathResult result = new FSPathResult(null);
 }

 @Test (expected = NumberFormatException.class)
 public void testInvalidDoubleInput() {
 // Test for invalid double input in Double constructor
 FSPathResult result = new FSPathResult("invalidDoubleInput");
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidDateInput() {
 // Test for invalid date input in Date constructor
 FSPathResult result = new FSPathResult("invalidDateInput");
 }

 @Test (expected = IllegalArgumentException.class)
 public void testInvalidURIInput() {
 // Test for invalid URI input in URI constructor
 FSPathResult result = new FSPathResult(new URI("invalidURIInput:"));
 }
}