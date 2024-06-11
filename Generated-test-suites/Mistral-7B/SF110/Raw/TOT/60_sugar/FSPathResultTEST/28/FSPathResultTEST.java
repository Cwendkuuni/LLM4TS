import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("testFile.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 assertEquals("testFile.txt", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultString() {
 String string = "Hello World!";
 FSPathResult fsPathResult = new FSPathResult(string);
 assertEquals("Hello World!", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultDouble() {
 Double doubleValue = 3.14;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 assertEquals("3.14", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 assertEquals(date.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://example.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 assertEquals(uri.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultBooleanTrue() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 assertEquals("true", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultBooleanFalse() {
 Boolean booleanValue = false;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 assertEquals("false", fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullFile() {
 FSPathResult fsPathResult = new FSPathResult(null);
 fail("Expected NullPointerException, but got: " + fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullString() {
 FSPathResult fsPathResult = new FSPathResult(null);
 fail("Expected NullPointerException, but got: " + fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullDouble() {
 FSPathResult fsPathResult = new FSPathResult(null);
 fail("Expected NullPointerException, but got: " + fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullDate() {
 FSPathResult fsPathResult = new FSPathResult(null);
 fail("Expected NullPointerException, but got: " + fsPathResult.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultInvalidURI() {
 FSPathResult fsPathResult;
 try {
 fsPathResult = new FSPathResult(new URI("javascript:alert('Hello')"));
 } catch (IllegalArgumentException e) {
 // Expected
 }
 fail("Expected IllegalArgumentException, but got: " + fsPathResult.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultNullURI() {
 FSPathResult fsPathResult = new FSPathResult(null);
 fail("Expected IllegalArgumentException, but got: " + fsPathResult.toString());
 }
}