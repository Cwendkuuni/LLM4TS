import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("path/to/existing/file.txt");
 FSPathResult fsPathResult = new FSPathResult(file);

 assertEquals("path/to/existing/file.txt", fsPathResult.getFile().getAbsolutePath());
 assertEquals("path/to/existing/file.txt", fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultWithNullFile() {
 FSPathResult fsPathResult = new FSPathResult(null);
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "this is a test string";
 FSPathResult fsPathResult = new FSPathResult(string);

 assertEquals("this is a test string", fsPathResult.getString());
 assertEquals("FSPathResult : this is a test string", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);

 assertEquals(123.45, fsPathResult.getDouble().doubleValue(), 0.0);
 assertEquals("123.45", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);

 assertEquals(date.toString(), fsPathResult.getDate().toString());
 assertEquals("FSPathResult : " + date.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.example.com/test");
 FSPathResult fsPathResult = new FSPathResult(uri);

 assertEquals(uri.toString(), fsPathResult.getURI().toString());
 assertEquals("FSPathResult : http://www.example.com/test", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);

 assertEquals(booleanValue.toString(), fsPathResult.getBoolean().toString());
 assertEquals("FSPathResult : true", fsPathResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultWithNullBoolean() {
 FSPathResult fsPathResult = new FSPathResult(null);
 }
}