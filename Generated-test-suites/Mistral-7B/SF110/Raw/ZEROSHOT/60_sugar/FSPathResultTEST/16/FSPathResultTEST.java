import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("test.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 File resultFile = fsPathResult.getFile();
 Assert.assertEquals(resultFile.getAbsolutePath(), "test.txt");
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "testString";
 FSPathResult fsPathResult = new FSPathResult(string);
 String resultString = fsPathResult.getString();
 Assert.assertEquals(resultString, "testString");
 }

 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 12.5;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 Double resultDouble = fsPathResult.getDouble();
 Assert.assertEquals(resultDouble, 12.5);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 Date resultDate = fsPathResult.getDate();
 Assert.assertEquals(resultDate, date);
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://test.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 URI resultURI = fsPathResult.getURI();
 Assert.assertEquals(resultURI, uri);
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 Boolean resultBoolean = fsPathResult.getBoolean();
 Assert.assertEquals(resultBoolean, booleanValue);
 }

 @Test
 public void testFSPathResultToStringWithFile() {
 File file = new File("test.txt");
 FSPathResult fsPathResult = new FSPathResult(file);
 String resultString = fsPathResult.toString();
 Assert.assertEquals(resultString, file.getAbsolutePath());
 }

 @Test
 public void testFSPathResultToStringWithString() {
 String string = "testString";
 FSPathResult fsPathResult = new FSPathResult(string);
 String resultString = fsPathResult.toString();
 Assert.assertEquals(resultString, string);
 }

 @Test
 public void testFSPathResultToStringWithDouble() {
 Double doubleValue = 12.5;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);
 String resultString = fsPathResult.toString();
 Assert.assertEquals(resultString, doubleValue.toString());
 }

 @Test
 public void testFSPathResultToStringWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);
 String resultString = fsPathResult.toString();
 Assert.assertEquals(resultString, booleanValue.toString());
 }

 @Test
 public void testFSPathResultToStringWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 String resultString = fsPathResult.toString();
 Assert.assertEquals(resultString, date.toString());
 }

 @Test
 public void testFSPathResultToStringWithURI() {
 URI uri = new URI("http://test.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 String resultString = fsPathResult.toString();
 Assert.assertEquals(resultString, uri.toString());
 }
}