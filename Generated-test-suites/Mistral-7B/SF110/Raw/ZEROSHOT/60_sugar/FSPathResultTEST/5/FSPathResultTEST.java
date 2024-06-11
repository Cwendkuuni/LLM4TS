import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 FSPathResult result = new FSPathResult(new File("testFile.txt"));
 File expectedFile = new File("testFile.txt");
 File actualFile = result.getFile();
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testFSPathResultString() {
 FSPathResult result = new FSPathResult("testString");
 String expectedString = "testString";
 String actualString = result.getString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultDouble() {
 FSPathResult result = new FSPathResult(3.14);
 Double expectedDouble = 3.14;
 Double actualDouble = result.getDouble();
 assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 Date expectedDate = date;
 Date actualDate = result.getDate();
 assertEquals(expectedDate, actualDate);
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult result = new FSPathResult(uri);
 URI expectedURI = uri;
 URI actualURI = result.getURI();
 assertEquals(expectedURI, actualURI);
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean booleanResult = true;
 FSPathResult result = new FSPathResult(booleanResult);
 Boolean expectedBoolean = booleanResult;
 Boolean actualBoolean = result.getBoolean();
 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testFSPathResultToStringFile() {
 FSPathResult result = new FSPathResult(new File("testFile.txt"));
 String expectedString = "testFile.txt";
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultToStringString() {
 FSPathResult result = new FSPathResult("testString");
 String expectedString = "testString";
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultToStringDouble() {
 FSPathResult result = new FSPathResult(3.14);
 String expectedString = "3.14";
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultToStringDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 String expectedString = date.toString();
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultToStringURI() {
 URI uri = new URI("http://www.google.com");
 FSPathResult result = new FSPathResult(uri);
 String expectedString = uri.toString();
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultToStringBoolean() {
 Boolean booleanResult = true;
 FSPathResult result = new FSPathResult(booleanResult);
 String expectedString = booleanResult.toString();
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultToStringEmpty() {
 FSPathResult result = new FSPathResult();
 String expectedString = "FSPathResult : empty";
 String actualString = result.toString();
 assertEquals(expectedString, actualString);
 }
}