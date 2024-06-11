import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("path/to/file"));
 File resultFile = fsPathResult.getFile();
 assertEquals("path/to/file", resultFile.getAbsolutePath());
 }

 @Test
 public void testFSPathResultWithString() {
 FSPathResult fsPathResult = new FSPathResult("result string");
 String resultString = fsPathResult.getString();
 assertEquals("result string", resultString);
 }

 @Test
 public void testFSPathResultWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 Double resultDouble = fsPathResult.getDouble();
 assertEquals(3.14, resultDouble, 0.0);
 }

 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 Date resultDate = fsPathResult.getDate();
 assertEquals(date.toString(), resultDate.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 URI resultURI = fsPathResult.getURI();
 assertEquals(uri.toString(), resultURI.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 Boolean resultBoolean = fsPathResult.getBoolean();
 assertEquals(true, resultBoolean);
 }

 @Test
 public void testFSPathResultToStringWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("path/to/file"));
 String result = fsPathResult.toString();
 assertEquals("path/to/file", result);
 }

 @Test
 public void testFSPathResultToStringWithString() {
 FSPathResult fsPathResult = new FSPathResult("result string");
 String result = fsPathResult.toString();
 assertEquals("result string", result);
 }

 @Test
 public void testFSPathResultToStringWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 String result = fsPathResult.toString();
 assertEquals("3.14", result);
 }

 @Test
 public void testFSPathResultToStringWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 String result = fsPathResult.toString();
 assertEquals("FSPathResult : true", result);
 }

 @Test
 public void testFSPathResultToStringWithDate() {
 Date date = new Date();
 FSPathResult fsPathResult = new FSPathResult(date);
 String result = fsPathResult.toString();
 assertEquals(date.toString(), result);
 }

 @Test
 public void testFSPathResultToStringWithURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult fsPathResult = new FSPathResult(uri);
 String result = fsPathResult.toString();
 assertEquals(uri.toString(), result);
 }
}