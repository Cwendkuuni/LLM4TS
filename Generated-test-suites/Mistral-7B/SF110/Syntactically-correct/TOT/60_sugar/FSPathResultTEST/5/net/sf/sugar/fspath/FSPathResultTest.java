import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);

 assertEquals("testFile.txt", result.getFile().getAbsolutePath());
 assertEquals("testFile.txt", result.toString());
 }

 @Test
 public void testFSPathResultString() {
 String str = "Test String";
 FSPathResult result = new FSPathResult(str);

 assertEquals(str, result.getString());
 assertEquals(str, result.toString());
 }

 @Test
 public void testFSPathResultDouble() {
 Double d = 3.14;
 FSPathResult result = new FSPathResult(d);

 assertEquals(d.toString(), result.getDouble().toString());
 assertEquals(d.toString(), result.toString());
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);

 assertEquals(date.toString(), result.getDate().toString());
 assertEquals(date.toString(), result.toString());
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);

 assertEquals(uri.toString(), result.getURI().toString());
 assertEquals(uri.toString(), result.toString());
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean bool = true;
 FSPathResult result = new FSPathResult(bool);

 assertEquals(bool.toString(), result.getBoolean().toString());
 assertEquals(bool.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultNullConstructor() {
 FSPathResult result = new FSPathResult(null);
 }
}