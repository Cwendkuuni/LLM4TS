import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 private File testFile;
 private String testString;
 private Double testDouble;
 private Date testDate;
 private URI testURI;
 private Boolean testBoolean;

 @Test
 public void testFSPathResultWithFile() {
 FSPathResult result = new FSPathResult(new File("testFilePath"));
 File expected = new File("testFilePath");
 File actual = result.getFile();
 assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
 }

 @Test
 public void testFSPathResultWithString() {
 FSPathResult result = new FSPathResult("testString");
 String expected = "testString";
 String actual = result.getString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFSPathResultWithDouble() {
 FSPathResult result = new FSPathResult(123.45);
 Double expected = 123.45;
 Double actual = result.getDouble();
 assertEquals(expected, actual);
 }

 @Test
 public void testFSPathResultWithDate() {
 FSPathResult result = new FSPathResult(new Date(System.currentTimeMillis()));
 Date expected = new Date(System.currentTimeMillis());
 Date actual = result.getDate();
 assertEquals(expected, actual);
 }

 @Test
 public void testFSPathResultWithURI() {
 FSPathResult result = new FSPathResult(new URI("http://test.com"));
 URI expected = new URI("http://test.com");
 URI actual = result.getURI();
 assertEquals(expected, actual);
 }

 @Test
 public void testFSPathResultWithBoolean() {
 FSPathResult result = new FSPathResult(true);
 Boolean expected = true;
 Boolean actual = result.getBoolean();
 assertEquals(expected, actual);
 }

 @Test (expected = NullPointerException.class)
 public void testFSPathResultWithNullFile() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test (expected = NullPointerException.class)
 public void testFSPathResultWithNullString() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test (expected = NumberFormatException.class)
 public void testFSPathResultWithInvalidDouble() {
 FSPathResult result = new FSPathResult("invalidDouble");
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullDate() {
 FSPathResult result = new FSPathResult(null);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFSPathResultWithNullURI() {
 FSPathResult result = new FSPathResult(null);
 }

}