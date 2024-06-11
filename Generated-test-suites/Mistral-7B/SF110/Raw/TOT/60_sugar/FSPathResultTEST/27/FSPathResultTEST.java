import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 File file = new File("test.txt");
 FSPathResult fsPathResult = new FSPathResult(file);

 Assert.assertEquals("test.txt", fsPathResult.toString());
 Assert.assertEquals(file, fsPathResult.getFile());
 }

 @Test
 public void testFSPathResultWithString() {
 String string = "Hello World!";
 FSPathResult fsPathResult = new FSPathResult(string);

 Assert.assertEquals("Hello World!", fsPathResult.toString());
 Assert.assertEquals(string, fsPathResult.getString());
 }

 @Test
 public void testFSPathResultWithDouble() {
 double doubleValue = 3.14;
 FSPathResult fsPathResult = new FSPathResult(doubleValue);

 Assert.assertEquals("3.14", fsPathResult.toString());
 Assert.assertEquals(doubleValue, fsPathResult.getDouble(), 0.001);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);

 Assert.assertEquals(date.toString(), fsPathResult.toString());
 Assert.assertEquals(date, fsPathResult.getDate());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult fsPathResult = new FSPathResult(uri);

 Assert.assertEquals(uri.toString(), fsPathResult.toString());
 Assert.assertEquals(uri, fsPathResult.getURI());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult fsPathResult = new FSPathResult(booleanValue);

 Assert.assertEquals(booleanValue.toString(), fsPathResult.toString());
 Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultWithNullFile() {
 File nullFile = null;
 FSPathResult fsPathResult = new FSPathResult(nullFile);
 }

 @Test(expected = NullPointerException.class)
 public void testFSPathResultWithNullString() {
 String nullString = null;
 FSPathResult fsPathResult = new FSPathResult(nullString);
 }

 @Test(expected = NumberFormatException.class)
 public void testFSPathResultWithInvalidDouble() {
 FSPathResult fsPathResult = new FSPathResult("abc");
 }

 @Test
 public void testFSPathResultWithEmptyString() {
 FSPathResult fsPathResult = new FSPathResult("");
 Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
 }
}