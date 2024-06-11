import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 private File file;
 private String string;
 private Double doubleValue;
 private Date date;
 private URI uri;
 private Boolean booleanValue;

 @Test
 public void testFSPathResultFileConstructor() {
 FSPathResult fsPathResult = new FSPathResult(file = new File("testFile.txt"));
 Assert.assertEquals(file, fsPathResult.getFile());
 }

 @Test
 public void testFSPathResultStringConstructor() {
 FSPathResult fsPathResult = new FSPathResult(string = "Test String");
 Assert.assertEquals(string, fsPathResult.getString());
 }

 @Test
 public void testFSPathResultDoubleConstructor() {
 FSPathResult fsPathResult = new FSPathResult(doubleValue = 123.45);
 Assert.assertEquals(doubleValue, fsPathResult.getDouble(), 0.0);
 }

 @Test
 public void testFSPathResultDateConstructor() {
 FSPathResult fsPathResult = new FSPathResult(date = new Date());
 Assert.assertEquals(date, fsPathResult.getDate());
 }

 @Test
 public void testFSPathResultURIConstructor() {
 FSPathResult fsPathResult = new FSPathResult(uri = new URI("http://localhost:8080"));
 Assert.assertEquals(uri, fsPathResult.getURI());
 }

 @Test
 public void testFSPathResultBooleanConstructor() {
 FSPathResult fsPathResult = new FSPathResult(booleanValue = true);
 Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFSPathResultNullConstructor() {
 FSPathResult fsPathResult = new FSPathResult(null);
 }

 @Test
 public void testToStringFile() {
 FSPathResult fsPathResult = new FSPathResult(file = new File("testFile.txt"));
 Assert.assertEquals(file.getAbsolutePath(), fsPathResult.toString());
 }

 @Test
 public void testToStringString() {
 FSPathResult fsPathResult = new FSPathResult(string = "Test String");
 Assert.assertEquals(string, fsPathResult.toString());
 }

 @Test
 public void testToStringDouble() {
 FSPathResult fsPathResult = new FSPathResult(doubleValue = 123.45);
 Assert.assertEquals(doubleValue.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringBoolean() {
 FSPathResult fsPathResult = new FSPathResult(booleanValue = true);
 Assert.assertEquals(booleanValue.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringDate() {
 FSPathResult fsPathResult = new FSPathResult(date = new Date());
 Assert.assertEquals(date.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringURI() {
 FSPathResult fsPathResult = new FSPathResult(uri = new URI("http://localhost:8080"));
 Assert.assertEquals(uri.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringEmpty() {
 FSPathResult fsPathResult = new FSPathResult();
 Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
 }
}