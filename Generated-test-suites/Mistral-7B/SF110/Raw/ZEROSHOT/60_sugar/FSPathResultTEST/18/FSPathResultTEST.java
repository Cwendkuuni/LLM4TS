import net.sf.sugar.fspath.FSPathResult;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FSPathResultTest {

 @Test
 public void testNewInstanceWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
 File expectedFile = new File("testFile.txt");
 File actualFile = fsPathResult.getFile();

 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testNewInstanceWithString() {
 FSPathResult fsPathResult = new FSPathResult("testString");
 String expectedString = "testString";
 String actualString = fsPathResult.getString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testNewInstanceWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 Double expectedDouble = 3.14;
 Double actualDouble = fsPathResult.getDouble();

 assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testNewInstanceWithDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 Date expectedDate = new Date();
 Date actualDate = fsPathResult.getDate();

 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testNewInstanceWithURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://www.google.com"));
 URI expectedURI = new URI("http://www.google.com");
 URI actualURI = fsPathResult.getURI();

 assertEquals(expectedURI, actualURI);
 }

 @Test
 public void testNewInstanceWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 Boolean expectedBoolean = true;
 Boolean actualBoolean = fsPathResult.getBoolean();

 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testGetFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
 File expectedFile = new File("testFile.txt");
 File actualFile = fsPathResult.getFile();

 assertSame(expectedFile, actualFile);
 }

 @Test
 public void testGetString() {
 FSPathResult fsPathResult = new FSPathResult("testString");
 String expectedString = "testString";
 String actualString = fsPathResult.getString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 Double expectedDouble = 3.14;
 Double actualDouble = fsPathResult.getDouble();

 assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testGetDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 Date expectedDate = new Date();
 Date actualDate = fsPathResult.getDate();

 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testGetURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://www.google.com"));
 URI expectedURI = new URI("http://www.google.com");
 URI actualURI = fsPathResult.getURI();

 assertEquals(expectedURI, actualURI);
 }

 @Test
 public void testGetBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 Boolean expectedBoolean = true;
 Boolean actualBoolean = fsPathResult.getBoolean();

 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testToStringWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
 String expectedString = "testFile.txt";
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithString() {
 FSPathResult fsPathResult = new FSPathResult("testString");
 String expectedString = "testString";
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 String expectedString = "3.14";
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 String expectedString = ""; // TODO: Implement a good toString() for Date
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://www.google.com"));
 String expectedString = "http://www.google.com";
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 String expectedString = "true";
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testToStringWithEmptyResult() {
 FSPathResult fsPathResult = new FSPathResult();
 String expectedString = "FSPathResult : empty";
 String actualString = fsPathResult.toString();

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testIsNullWhenEmptyResult() {
 FSPathResult fsPathResult = new FSPathResult();
 assertNull(fsPathResult.getFile());
 assertNull(fsPathResult.getString());
 assertNull(fsPathResult.getDouble());
 assertNull(fsPathResult.getDate());
 assertNull(fsPathResult.getURI());
 assertNull(fsPathResult.getBoolean());
 }

 @Test
 public void testIsFalseWhenBooleanIsFalse() {
 FSPathResult fsPathResult = new FSPathResult(false);
 assertFalse(fsPathResult.getBoolean());
 }

 @Test
 public void testIsTrueWhenBooleanIsTrue() {
 FSPathResult fsPathResult = new FSPathResult(true);
 assertTrue(fsPathResult.getBoolean());
 }
}