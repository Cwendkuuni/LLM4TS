import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

 private FSPathResult fileResult;
 private FSPathResult stringResult;
 private FSPathResult doubleResult;
 private FSPathResult dateResult;
 private FSPathResult uriResult;
 private FSPathResult booleanResult;

 @Test
 public void testFileResult() {
 File file = new File("testFile.txt");
 fileResult = new FSPathResult(file);

 assertEquals("testFile.txt", fileResult.toString());
 }

 @Test
 public void testStringResult() {
 stringResult = new FSPathResult("Hello World!");

 assertEquals("Hello World!", stringResult.toString());
 }

 @Test
 public void testDoubleResult() {
 doubleResult = new FSPathResult(3.14159265358979323846);

 assertEquals("3.14159265358979323846", doubleResult.toString());
 }

 @Test
 public void testDateResult() {
 Date date = new Date();
 dateResult = new FSPathResult(date);

 assertEquals("Sun Jan 23 15:54:21 EST 2022", dateResult.toString());
 }

 @Test
 public void testURIResult() {
 URI uri = new URI("http://www.example.com");
 uriResult = new FSPathResult(uri);

 assertEquals("http://www.example.com", uriResult.toString());
 }

 @Test
 public void testBooleanResult() {
 booleanResult = new FSPathResult(true);

 assertEquals("true", booleanResult.toString());
 }

 @Test
 public void testEmptyResult() {
 FSPathResult emptyResult = new FSPathResult(null);

 assertEquals("FSPathResult : empty", emptyResult.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullResult() {
 FSPathResult nullResult = new FSPathResult(null);
 }
}