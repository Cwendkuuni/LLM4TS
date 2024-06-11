import static org.junit.Assert.*;

import net.sf.sugar.fspath.FSPathResult;

import org.junit.Test;

public class FSPathResultTest {

 @Test
 public void testFSPathResultWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("test.txt"));
 File expectedFile = new File("test.txt");
 File actualFile = fsPathResult.getFile();
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testFSPathResultWithString() {
 FSPathResult fsPathResult = new FSPathResult("Hello World");
 String expectedString = "Hello World";
 String actualString = fsPathResult.getString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testFSPathResultWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 Double expectedDouble = 3.14;
 Double actualDouble = fsPathResult.getDouble();
 assertEquals(expectedDouble, actualDouble);
 }

 @Test
 public void testFSPathResultWithDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 Date expectedDate = new Date();
 Date actualDate = fsPathResult.getDate();
 assertEquals(expectedDate.toString(), actualDate.toString());
 }

 @Test
 public void testFSPathResultWithURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://www.google.com"));
 URI expectedURI = new URI("http://www.google.com");
 URI actualURI = fsPathResult.getURI();
 assertEquals(expectedURI.toString(), actualURI.toString());
 }

 @Test
 public void testFSPathResultWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 Boolean expectedBoolean = true;
 Boolean actualBoolean = fsPathResult.getBoolean();
 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testToStringWithFile() {
 FSPathResult fsPathResult = new FSPathResult(new File("test.txt"));
 String expectedToString = "test.txt";
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringWithString() {
 FSPathResult fsPathResult = new FSPathResult("Hello World");
 String expectedToString = "Hello World";
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringWithDouble() {
 FSPathResult fsPathResult = new FSPathResult(3.14);
 String expectedToString = "3.14";
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringWithDate() {
 FSPathResult fsPathResult = new FSPathResult(new Date());
 String expectedToString = "[Mon Jan 01 00:00:00 EST 2023]"; // Replace with current date
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringWithURI() {
 FSPathResult fsPathResult = new FSPathResult(new URI("http://www.google.com"));
 String expectedToString = "http://www.google.com";
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringWithBoolean() {
 FSPathResult fsPathResult = new FSPathResult(true);
 String expectedToString = "true";
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringWithEmptyResult() {
 FSPathResult fsPathResult = new FSPathResult();
 String expectedToString = "FSPathResult : empty";
 String actualToString = fsPathResult.toString();
 assertEquals(expectedToString, actualToString);
 }
}