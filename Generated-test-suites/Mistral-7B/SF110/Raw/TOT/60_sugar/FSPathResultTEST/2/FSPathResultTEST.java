import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() throws IOException {
 File file = new File("path/to/existing/file.txt");
 FSPathResult result = new FSPathResult(file);
 assertEquals("path/to/existing/file.txt", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullFileResult() {
 FSPathResult result = new FSPathResult(null);
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
 public void testDateResult() throws ParseException {
 Date date = new Date(1234567890123L);
 FSPathResult result = new FSPathResult(date);
 assertEquals("Mon Jan 01 10:10:10 GMT+01:00 1970", result.toString());
 }

 @Test
 public void testUriResult() throws MalformedURLException {
 URI uri = new URI("http://www.example.com/path/to/resource.html");
 FSPathResult result = new FSPathResult(uri);
 assertEquals("http://www.example.com/path/to/resource.html", result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 assertEquals("true", result.toString());
 }

 @Test
 public void testEmptyResult() {
 FSPathResult result = new FSPathResult();
 assertEquals("FSPathResult : empty", result.toString());
 }
}