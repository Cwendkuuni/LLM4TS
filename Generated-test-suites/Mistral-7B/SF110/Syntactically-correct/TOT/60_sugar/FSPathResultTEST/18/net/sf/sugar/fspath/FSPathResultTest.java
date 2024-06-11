import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

 private FSPathResult fileResult;
 private FSPathResult stringResult;
 private FSPathResult doubleResult;
 private FSPathResult dateResult;
 private FSPathResult uriResult;
 private FSPathResult booleanResult;

 @Test
 public void testFileResult() {
 File file = new File("path/to/existing/file");
 FSPathResult result = new FSPathResult(file);
 fileResult = result;

 assertEquals("path/to/existing/file", result.getFile().getAbsolutePath());
 assertEquals("path/to/existing/file", result.toString());
 }

 @Test
 public void testStringResult() {
 String string = "test string";
 FSPathResult result = new FSPathResult(string);
 stringResult = result;

 assertEquals("test string", result.getString());
 assertEquals("test string", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 123.45;
 FSPathResult result = new FSPathResult(doubleValue);
 doubleResult = result;

 assertEquals(123.45, result.getDouble().doubleValue(), 0.001);
 assertEquals("123.45", result.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 dateResult = result;

 assertEquals(date, result.getDate());
 assertEquals("Mon Jan 01 00:00:00 GMT 1970", result.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);
 uriResult = result;

 assertEquals(uri, result.getURI());
 assertEquals("http://www.example.com", result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 booleanResult = result;

 assertEquals(booleanValue, result.getBoolean());
 assertEquals("true", result.toString());
 }

 @Test
 public void testEmptyResult() {
 FSPathResult result = new FSPathResult();

 assertEquals(null, result.getFile());
 assertEquals(null, result.getString());
 assertEquals(null, result.getDouble());
 assertEquals(null, result.getDate());
 assertEquals(null, result.getURI());
 assertEquals(null, result.getBoolean());
 assertEquals("FSPathResult : empty", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullResults() {
 File nullFile = null;
 FSPathResult result = new FSPathResult(nullFile);

 assertNull(result.getFile());
 assertNull(result.getString());
 assertNull(result.getDouble());
 assertNull(result.getDate());
 assertNull(result.getURI());
 assertNull(result.getBoolean());
 assertNotNull(result.toString());
 }

 @Test(expected = ClassCastException.class)
 public void testIncompatibleTypes() {
 String incompatibleType = "test string";
 File incompatibleFile = new File(incompatibleType);
 FSPathResult result = new FSPathResult(incompatibleFile);

 assertNotNull(result.getFile());
 assertNull(result.getString());
 assertNull(result.getDouble());
 assertNull(result.getDate());
 assertNull(result.getURI());
 assertNull(result.getBoolean());
 assertFalse(result.toString().equals(incompatibleType));
 }
}