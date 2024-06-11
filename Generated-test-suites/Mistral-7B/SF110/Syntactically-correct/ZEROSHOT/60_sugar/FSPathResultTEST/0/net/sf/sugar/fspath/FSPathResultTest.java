import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("path/to/file.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 File expectedFile = new File("path/to/file.txt");

 assertEquals(expectedFile, fsPathResult.getFile());
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "Test String";
 FSPathResult fsPathResult = new FSPathResult(string);
 String expectedString = "Test String";

 assertEquals(expectedString, fsPathResult.getString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 Double expectedDouble = 123.45;

 assertEquals(expectedDouble, fsPathResult.getDouble());
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 Date expectedDate = date;

 assertEquals(expectedDate, fsPathResult.getDate());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://example.com/");
 FSPathResult fsPathResult = new FSPathResult(uri);
 URI expectedURI = uri;

 assertEquals(expectedURI, fsPathResult.getURI());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 Boolean expectedBoolean = booleanValue;

 assertEquals(expectedBoolean, fsPathResult.getBoolean());
 }

 @Test
 public void testFSPathResultToStringWithFile() {
 File file = new File("path/to/file.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 String expectedString = file.getAbsolutePath();

 assertEquals(expectedString, fsPathResult.toString());
 }

 @Test
 public void testFSPathResultToStringWithString() {
 String string = "Test String";
 FSPathResult fsPathResult = new FSPathResult(string);
 String expectedString = string;

 assertEquals(expectedString, fsPathResult.toString());
 }

 @Test
 public void testFSPathResultToStringWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 String expectedString = doubleValue.toString();

 assertEquals(expectedString, fsPathResult.toString());
 }

 @Test
 public void testFSPathResultToStringWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 String expectedString = date.toString();

 assertEquals(expectedString, fsPathResult.toString());
 }

 @Test
 public void testFSPathResultToStringWithURI() {
 URI uri = new URI("http://example.com/");
 FSPathResult fsPathResult = new FSPathResult(uri);
 String expectedString = uri.toString();

 assertEquals(expectedString, fsPathResult.toString());
 }

 @Test
 public void testFSPathResultToStringWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 String expectedString = booleanValue.toString();

 assertEquals(expectedString, fsPathResult.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullFile() {
 File file = null;
 FSPathResult fsPathResult = new FSPathResult(file);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullString() {
 String string = null;
 FSPathResult fsPathResult = new FSPathResult(string);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullDouble() {
 Double doubleValue = null;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullDate() {
 Date date = null;
 FSPathResult fsPathResult = new FSPathResult(date);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullURI() {
 URI uri = null;
 FSPathResult fsPathResult = new FSPathResult(uri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullBoolean() {
 Boolean booleanValue = null;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 }
}