import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

import org.junit.Test;

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
 String string = "Hello World";
 FSPathResult result = new FSPathResult(string);

 assertEquals("Hello World", result.getString());
 assertEquals("Hello World", result.toString());
 }

 @Test
 public void testDoubleResult() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals(3.14, result.getDouble().doubleValue(), 0.01);
 assertEquals("3.14", result.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.getDate().toString());
 assertEquals("Mon Aug 24 15:12:34 EDT 2021", result.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.getURI().toString());
 assertEquals("http://www.example.com", result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals(booleanValue, result.getBoolean());
 assertEquals("true", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullResult() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidResult() {
 Object invalidResult = new Object();
 FSPathResult result = new FSPathResult(invalidResult);
 }
}