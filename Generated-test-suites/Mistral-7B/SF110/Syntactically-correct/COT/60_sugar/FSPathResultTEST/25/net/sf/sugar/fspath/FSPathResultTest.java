import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

public class FSPathResultTest {

 private FSPathResult result;

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("testFile.txt");
 result = new FSPathResult(file);
 File expectedFile = new File("testFile.txt");
 File actualFile = result.getFile();
 Assert.assertEquals("Expected and actual file should be same", expectedFile, actualFile);
 }

 @Test
 public void testFSPathResultWithString() {
 String testString = "testString";
 result = new FSPathResult(testString);
 String expectedString = testString;
 String actualString = result.getString();
 Assert.assertEquals("Expected and actual strings should be same", expectedString, actualString);
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double testDouble = 3.14;
 result = new FSPathResult(testDouble);
 Double expectedDouble = testDouble;
 Double actualDouble = result.getDouble();
 Assert.assertEquals("Expected and actual doubles should be same", expectedDouble, actualDouble);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date testDate = new Date();
 result = new FSPathResult(testDate);
 Date expectedDate = testDate;
 Date actualDate = result.getDate();
 Assert.assertEquals("Expected and actual dates should be same", expectedDate, actualDate);
 }

 @Test
 public void testFSPathResultWithURI() {
 URI testURI = new URI("http://www.example.com");
 result = new FSPathResult(testURI);
 URI expectedURI = testURI;
 URI actualURI = result.getURI();
 Assert.assertEquals("Expected and actual URIs should be same", expectedURI, actualURI);
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean testBoolean = true;
 result = new FSPathResult(testBoolean);
 Boolean expectedBoolean = testBoolean;
 Boolean actualBoolean = result.getBoolean();
 Assert.assertEquals("Expected and actual booleans should be same", expectedBoolean, actualBoolean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullValue() {
 result = new FSPathResult(null);
 }

 @Test
 public void testToStringWithFile() {
 File file = new File("testFile.txt");
 result = new FSPathResult(file);
 String expected = file.getAbsolutePath();
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test
 public void testToStringWithString() {
 String testString = "testString";
 result = new FSPathResult(testString);
 String expected = testString;
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test
 public void testToStringWithDouble() {
 Double testDouble = 3.14;
 result = new FSPathResult(testDouble);
 String expected = testDouble.toString();
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test
 public void testToStringWithBoolean() {
 Boolean testBoolean = true;
 result = new FSPathResult(testBoolean);
 String expected = testBoolean.toString();
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test
 public void testToStringWithDate() {
 Date testDate = new Date();
 result = new FSPathResult(testDate);
 String expected = testDate.toString();
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test
 public void testToStringWithURI() {
 URI testURI = new URI("http://www.example.com");
 result = new FSPathResult(testURI);
 String expected = testURI.toString();
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test
 public void testToStringWithEmptyResult() {
 result = new FSPathResult(null);
 String expected = "FSPathResult : empty";
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullResult() {
 result = null;
 String actual = result.toString();
 }

 @Test
 public void testToStringWithDateFormatException() {
 Date testDate = null;
 try {
 testDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365);
 } catch (Exception e) {
 // Do nothing
 }
 result = new FSPathResult(testDate);
 String expected = "FSPathResult : empty";
 String actual = result.toString();
 Assert.assertEquals("Expected and actual strings should be same", expected, actual);
 }
}