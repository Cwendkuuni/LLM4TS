import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("src/test/resources/testFile.txt");
 FSPathResult fsPathResult = new FSPathResult(file);

 Assert.assertNotNull(fsPathResult.getFile());
 Assert.assertEquals("src/test/resources/testFile.txt", fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithString() {
 String resultString = "Hello World";
 FSPathResult fsPathResult = new FSPathResult(resultString);

 Assert.assertNull(fsPathResult.getFile());
 Assert.assertEquals(resultString, fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double resultDouble = 123.45;
 FSPathResult fsPathResult = new FSPathResult(resultDouble);

 Assert.assertNull(fsPathResult.getFile());
 Assert.assertEquals(resultDouble.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);

 Assert.assertNull(fsPathResult.getFile());
 Assert.assertEquals(date.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult fsPathResult = new FSPathResult(uri);

 Assert.assertNull(fsPathResult.getFile());
 Assert.assertEquals(uri.toString(), fsPathResult.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean resultBoolean = true;
 FSPathResult fsPathResult = new FSPathResult(resultBoolean);

 Assert.assertNull(fsPathResult.getFile());
 Assert.assertEquals(resultBoolean.toString(), fsPathResult.toString());
 }
}