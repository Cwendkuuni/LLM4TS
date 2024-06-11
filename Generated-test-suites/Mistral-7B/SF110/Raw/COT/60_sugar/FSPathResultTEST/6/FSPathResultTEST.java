import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

public class FSPathResultTest {

 private FSPathResult fsPathResult;

 @Test
 public void testConstructorWithFileArgument() {
 File file = new File("testFile.txt");
 fsPathResult = new FSPathResult(file);
 Assert.assertSame(file, fsPathResult.getFile());
 }

 @Test
 public void testConstructorWithStringArgument() {
 fsPathResult = new FSPathResult("Test String");
 Assert.assertEquals("Test String", fsPathResult.getString());
 }

 @Test
 public void testConstructorWithDoubleArgument() {
 fsPathResult = new FSPathResult(123.45);
 Assert.assertEquals(123.45, fsPathResult.getDouble().doubleValue(), 0.0);
 }

 @Test
 public void testConstructorWithDateArgument() throws ParseException {
 Date date = new Date(0L);
 fsPathResult = new FSPathResult(date);
 Assert.assertEquals(date, fsPathResult.getDate());
 }

 @Test
 public void testConstructorWithURIArgument() {
 URI uri = new URI("http://www.example.com/test");
 fsPathResult = new FSPathResult(uri);
 Assert.assertEquals(uri, fsPathResult.getURI());
 }

 @Test
 public void testConstructorWithBooleanArgument() {
 fsPathResult = new FSPathResult(true);
 Assert.assertEquals(true, fsPathResult.getBoolean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullArguments() {
 try {
 fsPathResult = new FSPathResult(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testToStringWhenResultFileIsPresent() {
 File file = new File("testFile.txt");
 fsPathResult = new FSPathResult(file);
 Assert.assertEquals(file.getAbsolutePath(), fsPathResult.toString());
 }

 @Test
 public void testToStringWhenResultStringIsPresent() {
 fsPathResult = new FSPathResult("Test String");
 Assert.assertEquals("Test String", fsPathResult.toString());
 }

 @Test
 public void testToStringWhenResultDoubleIsPresent() {
 fsPathResult = new FSPathResult(123.45);
 Assert.assertEquals("123.45", fsPathResult.toString());
 }

 @Test
 public void testToStringWhenResultBooleanIsPresent() {
 fsPathResult = new FSPathResult(true);
 Assert.assertEquals("true", fsPathResult.toString());
 }

 @Test
 public void testToStringWhenResultDateIsPresent() throws ParseException {
 Date date = new Date(0L);
 fsPathResult = new FSPathResult(date);
 Assert.assertEquals(date.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringWhenResultURIIsPresent() {
 URI uri = new URI("http://www.example.com/test");
 fsPathResult = new FSPathResult(uri);
 Assert.assertEquals(uri.toString(), fsPathResult.toString());
 }

 @Test
 public void testToStringWhenResultIsEmpty() {
 fsPathResult = new FSPathResult();
 Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
 }

}