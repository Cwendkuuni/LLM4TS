import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

 private FSPathResult fileResult;
 private FSPathResult stringResult;
 private FSPathResult doubleResult;
 private FSPathResult dateResult;
 private FSPathResult uriResult;
 private FSPathResult booleanResult;

 @Before
 public void setUp() {
 File testFile = new File("test.txt");
 this.fileResult = new FSPathResult(testFile);
 this.stringResult = new FSPathResult("Hello World");
 this.doubleResult = new FSPathResult(3.14);
 this.dateResult = new FSPathResult(new Date());
 this.uriResult = new FSPathResult(new URI("http://www.example.com"));
 this.booleanResult = new FSPathResult(Boolean.TRUE);
 }

 @Test
 public void testGetFile() {
 assertEquals("Expected file", testFile, fileResult.getFile());
 }

 @Test
 public void testGetString() {
 assertEquals("Expected string", "Hello World", stringResult.getString());
 }

 @Test
 public void testGetDouble() {
 assertEquals("Expected double", 3.14, doubleResult.getDouble(), 0.01);
 }

 @Test
 public void testGetDate() {
 Date expectedDate = new Date();
 assertEquals("Expected date", expectedDate, dateResult.getDate());
 }

 @Test
 public void testGetURI() {
 assertEquals("Expected URI", "http://www.example.com", uriResult.getURI().toString());
 }

 @Test
 public void testGetBoolean() {
 assertTrue("Expected true", booleanResult.getBoolean());
 }

 @Test
 public void testToStringFile() {
 assertEquals("Expected file string", "/path/to/test.txt", fileResult.toString());
 }

 @Test
 public void testToStringString() {
 assertEquals("Expected string", "Hello World", stringResult.toString());
 }

 @Test
 public void testToStringDouble() {
 assertEquals("Expected double string", "3.14", doubleResult.toString());
 }

 @Test
 public void testToStringDate() {
 Date expectedDate = new Date();
 String expectedDateString = String.format("%tF %<T<p %<T<a %<T<H:%<T<m %<T<s %<T<z", expectedDate);
 assertEquals("Expected date string", expectedDateString, dateResult.toString());
 }

 @Test
 public void testToStringURI() {
 assertEquals("Expected URI string", "http://www.example.com", uriResult.toString());
 }

 @Test
 public void testToStringBoolean() {
 assertEquals("Expected boolean string", "true", booleanResult.toString());
 }

 @Test
 public void testToStringEmpty() {
 assertEquals("Expected empty string", "FSPathResult : empty", new FSPathResult().toString());
 }
}