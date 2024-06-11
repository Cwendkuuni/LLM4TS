import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("testFile.txt", result.toString());
 }

 @Test
 public void testFSPathResultString() {
 String string = "testString";
 FSPathResult result = new FSPathResult(string);

 assertEquals("testString", result.toString());
 }

 @Test
 public void testFSPathResultDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);

 assertEquals("3.14", result.toString());
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals("Sat Aug 27 11:25:17 IST 2023", result.toString());
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals("http://www.example.com", result.toString());
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);

 assertEquals("true", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullConstruction() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullGetMethod() {
 FSPathResult result = new FSPathResult(new File("testFile.txt"));
 assertNull(result.getBoolean());
 }
}