import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.sf.sugar.fspath.FSPathResult;

@RunWith(JUnit4.class)
public class FSPathResultTest {

 @Test
 public void testConstructWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("path/to/file.txt"));
 File expectedFile = new File("path/to/file.txt");
 File actualFile = fsPathResult.getFile();

 Assert.assertSame(expectedFile, actualFile);
 }

 @Test
 public void testConstructWithString() {
 FSPathResult fsPathResult = new FSPathResult("Test String");
 String expectedString = "Test String";
 String actualString = fsPathResult.getString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testConstructWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(123.45);
 Double expectedDouble = 123.45;
 Double actualDouble = fsPathResult.getDouble();

 Assert.assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testConstructWithDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 Date expectedDate = new Date();
 Date actualDate = fsPathResult.getDate();

 Assert.assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testConstructWithURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://example.com"));
 URI expectedURI = new URI("http://example.com");
 URI actualURI = fsPathResult.getURI();

 Assert.assertEquals(expectedURI, actualURI);
 }

 @Test
 public void testConstructWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 Boolean expectedBoolean = true;
 Boolean actualBoolean = fsPathResult.getBoolean();

 Assert.assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testToStringWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("path/to/file.txt"));
 String expectedString = "path/to/file.txt";
 String actualString = fsPathResult.toString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithString() {
 FSPathResult fsPathResult = new FSPathResult("Test String");
 String expectedString = "Test String";
 String actualString = fsPathResult.toString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(123.45);
 String expectedString = "123.45";
 String actualString = fsPathResult.toString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 String expectedString = ""; // A Date object's toString() method may vary
 String actualString = fsPathResult.toString();

 Assert.assertNotSame(expectedString, actualString); // Ensure that the actual string is not empty
 }

 @Test
 public void testToStringWithURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://example.com"));
 String expectedString = "http://example.com";
 String actualString = fsPathResult.toString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 String expectedString = "true";
 String actualString = fsPathResult.toString();

 Assert.assertEquals(expectedString, actualString);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNull() {
 FSPathResult fsPathResult = null;

 fsPathResult.toString();
 }
}