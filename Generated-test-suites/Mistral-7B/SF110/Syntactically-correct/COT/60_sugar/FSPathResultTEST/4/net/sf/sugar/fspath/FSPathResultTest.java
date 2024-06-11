import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

public class FSPathResultTest {

 private FSPathResult fsPathResult;

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("testFile.txt");
 fsPathResult = new FSPathResult(file);
 Assert.assertEquals("testFile.txt", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithString() {
 fsPathResult = new FSPathResult("Hello World");
 Assert.assertEquals("Hello World", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 fsPathResult = new FSPathResult(123.45);
 Assert.assertEquals("123.45", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 fsPathResult = new FSPathResult(date);
 Assert.assertEquals(date.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.example.com");
 fsPathResult = new FSPathResult(uri);
 Assert.assertEquals(uri.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 fsPathResult = new FSPathResult(true);
 Assert.assertEquals("true", fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultWithNullObject() {
 // This test case is for checking the NullPointerException when an object is passed as null
 fsPathResult = new FSPathResult(null);
 }

 @Test
 public void testToStringMethodWithEmptyFSPathResult() {
 fsPathResult = new FSPathResult(null);
 Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultWithInvalidFile() {
 File invalidFile = new File("//test/testFile.txt");
 fsPathResult = new FSPathResult(invalidFile);
 }

 @Test
 public void testToStringMethodWithInvalidDateFormat() {
 try {
 Date invalidDate = new Date("2022-02-31");
 } catch (ParseException e) {
 fsPathResult = new FSPathResult(invalidDate);
 }
 Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
 }
}