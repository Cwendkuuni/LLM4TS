import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFileConstructor() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);
 assertEquals("testFile.txt", result.getFile().getAbsolutePath());
 }

 @Test
 public void testFSPathResultStringConstructor() {
 String string = "Test String";
 FSPathResult result = new FSPathResult(string);
 assertEquals("Test String", result.getString());
 }

 @Test
 public void testFSPathResultDoubleConstructor() {
 Double result = new Double(4.5);
 FSPathResult fsPathResult = new FSPathResult(result);
 assertEquals(4.5, fsPathResult.getDouble().doubleValue(), 0.0);
 }

 @Test
 public void testFSPathResultDateConstructor() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 assertEquals(date.toString(), fsPathResult.getDate().toString());
 }

 @Test
 public void testFSPathResultURIConstructor() {
 URI uri = new URI("http://www.example.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 assertEquals(uri.toString(), fsPathResult.getURI().toString());
 }

 @Test
 public void testFSPathResultBooleanConstructor() {
 Boolean booleanValue = Boolean.TRUE;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 assertTrue(fsPathResult.getBoolean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultInvalidConstructor() {
 Object invalidConstructor = new Object();
 FSPathResult fsPathResult = new FSPathResult(invalidConstructor);
 fail("FSPathResult should not accept an Object as an argument.");
 }

 @Test
 public void testToStringFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);
 assertEquals("testFile.txt", result.toString());
 }

 @Test
 public void testToStringString() {
 String string = "Test String";
 FSPathResult result = new FSPathResult(string);
 assertEquals("Test String", result.toString());
 }

 @Test
 public void testToStringDouble() {
 Double result = new Double(4.5);
 FSPathResult fsPathResult = new FSPathResult(result);
 assertEquals("4.5", result.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 assertEquals(date.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 assertEquals(uri.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringBoolean() {
 Boolean booleanValue = Boolean.TRUE;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 assertEquals("true", fsPathResult.toString());
 }

 @Test
 public void testToStringEmpty() {
 FSPathResult result = new FSPathResult();
 assertEquals("FSPathResult : empty", result.toString());
 }
}