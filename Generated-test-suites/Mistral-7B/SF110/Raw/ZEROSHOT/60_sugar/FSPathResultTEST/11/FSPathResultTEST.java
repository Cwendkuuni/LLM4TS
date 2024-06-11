import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 *
 * Created on 08 April 2023, 16:21
 */

public class FSPathResultTest {

 /**
 * Test constructor with File argument.
 */
 @Test
 public void testFSPathResultWithFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);
 File expectedFile = file;
 assertEquals("Expected File object", expectedFile, result.getFile());
 }

 /**
 * Test constructor with String argument.
 */
 @Test
 public void testFSPathResultWithString() {
 String string = "testString";
 FSPathResult result = new FSPathResult(string);
 String expectedString = string;
 assertEquals("Expected String object", expectedString, result.getString());
 }

 /**
 * Test constructor with Double argument.
 */
 @Test
 public void testFSPathResultWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult result = new FSPathResult(doubleValue);
 Double expectedDouble = doubleValue;
 assertEquals("Expected Double object", expectedDouble, result.getDouble());
 }

 /**
 * Test constructor with Date argument.
 */
 @Test
 public void testFSPathResultWithDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 Date expectedDate = date;
 assertEquals("Expected Date object", expectedDate, result.getDate());
 }

 /**
 * Test constructor with URI argument.
 */
 @Test
 public void testFSPathResultWithURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);
 URI expectedURI = uri;
 assertEquals("Expected URI object", expectedURI, result.getURI());
 }

 /**
 * Test constructor with Boolean argument.
 */
 @Test
 public void testFSPathResultWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 Boolean expectedBoolean = booleanValue;
 assertEquals("Expected Boolean object", expectedBoolean, result.getBoolean());
 }

 /**
 * Test toString method with File object.
 */
 @Test
 public void testToStringWithFile() {
 File file = new File("testFile.txt");
 FSPathResult result = new FSPathResult(file);
 String expectedString = file.getAbsolutePath();
 String resultString = result.toString();
 assertEquals("Expected String representation of File object", expectedString, resultString);
 }

 /**
 * Test toString method with String object.
 */
 @Test
 public void testToStringWithString() {
 String string = "testString";
 FSPathResult result = new FSPathResult(string);
 String expectedString = string;
 String resultString = result.toString();
 assertEquals("Expected String representation of String object", expectedString, resultString);
 }

 /**
 * Test toString method with Double object.
 */
 @Test
 public void testToStringWithDouble() {
 Double doubleValue = 123.45;
 FSPathResult result = new FSPathResult(doubleValue);
 String expectedString = doubleValue.toString();
 String resultString = result.toString();
 assertEquals("Expected String representation of Double object", expectedString, resultString);
 }

 /**
 * Test toString method with Date object.
 */
 @Test
 public void testToStringWithDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 String expectedString = date.toString();
 String resultString = result.toString();
 assertEquals("Expected String representation of Date object", expectedString, resultString);
 }

 /**
 * Test toString method with URI object.
 */
 @Test
 public void testToStringWithURI() {
 URI uri = new URI("http://www.example.com");
 FSPathResult result = new FSPathResult(uri);
 String expectedString = uri.toString();
 String resultString = result.toString();
 assertEquals("Expected String representation of URI object", expectedString, resultString);
 }

 /**
 * Test toString method with Boolean object.
 */
 @Test
 public void testToStringWithBoolean() {
 Boolean booleanValue = true;
 FSPathResult result = new FSPathResult(booleanValue);
 String expectedString = booleanValue.toString();
 String resultString = result.toString();
 assertEquals("Expected String representation of Boolean object", expectedString, resultString);
 }
}