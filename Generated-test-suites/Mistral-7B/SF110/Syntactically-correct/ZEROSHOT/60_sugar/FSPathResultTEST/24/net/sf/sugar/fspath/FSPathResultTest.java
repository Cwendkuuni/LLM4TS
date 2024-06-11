import net.sf.sugar.fspath.FSPathResult;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("test.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 File resultFile = fsPathResult.getFile();
 assertEquals("test.txt", resultFile.getAbsolutePath());
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "Test String";
 FSPathResult fsPathResult = new FSPathResult(string);
 String resultString = fsPathResult.getString();
 assertEquals("Test String", resultString);
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 Double resultDouble = fsPathResult.getDouble();
 assertEquals(123.45, resultDouble.doubleValue(), 0.01);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 Date resultDate = fsPathResult.getDate();
 assertEquals(date.toString(), resultDate.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 URI resultURI = fsPathResult.getURI();
 assertEquals(uri.toString(), resultURI.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 Boolean resultBoolean = fsPathResult.getBoolean();
 assertEquals(booleanValue, resultBoolean);
 }

 @Test
 public void testFSPathResultToStringWithFile() {
 File file = new File("test.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 String result = fsPathResult.toString();
 assertEquals("test.txt", result.split("/")[result.split("/").length - 1]);
 }

 @Test
 public void testFSPathResultToStringWithString() {
 String string = "Test String";
 FSPathResult fsPathResult = new FSPathResult(string);
 String result = fsPathResult.toString();
 assertEquals("Test String", result);
 }

 @Test
 public void testFSPathResultToStringWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 String result = fsPathResult.toString();
 assertEquals(doubleValue.toString(), result);
 }

 @Test
 public void testFSPathResultToStringWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 String result = fsPathResult.toString();
 assertEquals(booleanValue.toString(), result);
 }

 @Test
 public void testFSPathResultToStringWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 String result = fsPathResult.toString();
 assertEquals(date.toString(), result);
 }

 @Test
 public void testFSPathResultToStringWithURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 String result = fsPathResult.toString();
 assertEquals(uri.toString(), result);
 }
}