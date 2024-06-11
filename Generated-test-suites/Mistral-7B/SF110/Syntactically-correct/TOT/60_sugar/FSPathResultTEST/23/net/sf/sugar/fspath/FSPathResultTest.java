import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileConstructor() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);
 assertEquals("path/to/existing/file", result.getFile().getAbsolutePath());
 }

 @Test
 public void testStringConstructor() {
 String string = "This is a test string";
 FSPathResult result = new FSPathResult(string);
 assertEquals(string, result.getString());
 }

 @Test
 public void testDoubleConstructor() {
 Double doubleValue = new Double(42.0);
 FSPathResult result = new FSPathResult(doubleValue);
 assertEquals(doubleValue, result.getDouble());
 }

 @Test
 public void testDateConstructor() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 assertEquals(date.toString(), result.getDate().toString());
 }

 @Test
 public void testURIConstructor() {
 URI uri = new URI("http://example.com");
 FSPathResult result = new FSPathResult(uri);
 assertEquals(uri.toString(), result.getURI().toString());
 }

 @Test
 public void testBooleanConstructor() {
 Boolean booleanValue = Boolean.TRUE;
 FSPathResult result = new FSPathResult(booleanValue);
 assertEquals(booleanValue.toString(), result.getBoolean().toString());
 }

 @Test
 public void testGetters() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);
 String string = "This is a test string";
 Double doubleValue = new Double(42.0);
 Date date = new Date();
 URI uri = new URI("http://example.com");
 Boolean booleanValue = Boolean.FALSE;

 assertEquals(file, result.getFile());
 assertEquals(string, result.getString());
 assertEquals(doubleValue, result.getDouble());
 assertEquals(date, result.getDate());
 assertEquals(uri, result.getURI());
 assertEquals(booleanValue, result.getBoolean());
 }

 @Test(expected = NullPointerException.class)
 public void testNullConstructor() {
 FSPathResult result = new FSPathResult(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testToStringFile() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);
 assertEquals("path/to/existing/file", result.toString());
 }

 @Test
 public void testToStringString() {
 String string = "This is a test string";
 FSPathResult result = new FSPathResult(string);
 assertEquals(string, result.toString());
 }

 @Test
 public void testToStringDouble() {
 Double doubleValue = new Double(42.0);
 FSPathResult result = new FSPathResult(doubleValue);
 assertEquals(doubleValue.toString(), result.toString());
 }

 @Test
 public void testToStringBoolean() {
 Boolean booleanValue = Boolean.TRUE;
 FSPathResult result = new FSPathResult(booleanValue);
 assertEquals(booleanValue.toString(), result.toString());
 }

 @Test
 public void testToStringDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testToStringURI() {
 URI uri = new URI("http://example.com");
 FSPathResult result = new FSPathResult(uri);
 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testToStringEmpty() {
 FSPathResult result = new FSPathResult();
 assertEquals("FSPathResult : empty", result.toString());
 }
}