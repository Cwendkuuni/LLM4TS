import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("test.txt");
 FSPathResult result = new FSPathResult(file);
 File actualResult = result.getFile();
 Assert.assertEquals("test.txt", actualResult.getAbsolutePath());
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "Test String";
 FSPathResult result = new FSPathResult(string);
 String actualResult = result.getString();
 Assert.assertEquals("Test String", actualResult);
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);
 Double actualResult = result.getDouble();
 Assert.assertEquals(3.14, actualResult.doubleValue(), 0.01);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 Date actualResult = result.getDate();
 Assert.assertEquals(date.toString(), actualResult.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http:// www.google.com");
 FSPathResult result = new FSPathResult(uri);
 URI actualResult = result.getURI();
 Assert.assertEquals(uri.toString(), actualResult.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 Boolean actualResult = result.getBoolean();
 Assert.assertEquals(booleanValue, actualResult);
 }

 @Test
 public void testFSPathResultToStringWithFile() {
 File file = new File("test.txt");
 FSPathResult result = new FSPathResult(file);
 String actualResult = result.toString();
 Assert.assertEquals("test.txt", actualResult.substring(0, actualResult.lastIndexOf(File.separator)));
 }

 @Test
 public void testFSPathResultToStringWithString() {
 String string = "Test String";
 FSPathResult result = new FSPathResult(string);
 String actualResult = result.toString();
 Assert.assertEquals("Test String", actualResult);
 }

 @Test
 public void testFSPathResultToStringWithDouble() {
 Double doubleValue = 3.14;
 FSPathResult result = new FSPathResult(doubleValue);
 String actualResult = result.toString();
 Assert.assertEquals("3.14", actualResult.substring(0, actualResult.indexOf(".")).trim());
 }

 @Test
 public void testFSPathResultToStringWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 String actualResult = result.toString();
 Assert.assertEquals("FSPathResult : true", actualResult);
 }

 @Test(expected = IllegalStateException.class)
 public void testFSPathResultWithoutValue() {
 FSPathResult result = new FSPathResult();
 }
}