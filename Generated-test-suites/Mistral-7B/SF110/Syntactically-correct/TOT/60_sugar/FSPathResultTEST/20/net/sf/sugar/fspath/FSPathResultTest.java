import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFileResult() {
 File testFile = new File("testFile.txt");
 FSPathResult result = new FSPathResult(testFile);
 assertEquals("testFile.txt", result.toString());
 }

 @Test
 public void testStringResult() {
 String testString = "Hello World!";
 FSPathResult result = new FSPathResult(testString);
 assertEquals(testString, result.getString());
 }

 @Test
 public void testDoubleResult() {
 Double testDouble = 3.14;
 FSPathResult result = new FSPathResult(testDouble);
 assertEquals(testDouble.toString(), result.toString());
 }

 @Test
 public void testDateResult() {
 Date testDate = new Date();
 FSPathResult result = new FSPathResult(testDate);
 assertEquals(testDate.toString(), result.toString());
 }

 @Test
 public void testURIResult() {
 URI testURI = new URI("http://www.google.com");
 FSPathResult result = new FSPathResult(testURI);
 assertEquals(testURI.toString(), result.toString());
 }

 @Test
 public void testBooleanResult() {
 Boolean testBoolean = true;
 FSPathResult result = new FSPathResult(testBoolean);
 assertEquals(testBoolean.toString(), result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 FSPathResult result = new FSPathResult(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testToStringEmptyResult() {
 FSPathResult result = new FSPathResult(null);
 assertEquals("FSPathResult : empty", result.toString());
 }
}