package net.sf.sugar.fspath.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 *
 * Test cases for FSPathResult class
 *
 * @author your_name_here
 */
public class FSPathResultTest {

	/**
	 * Test constructor with File argument
	 */
	@Test
	public void testFSPathResultWithFile() {
		File file = new File("test.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals("test.txt", result.getFile().getAbsolutePath());
	}

	/**
	 * Test constructor with String argument
	 */
	@Test
	public void testFSPathResultWithString() {
		String string = "Test string";
		FSPathResult result = new FSPathResult(string);
		assertEquals(string, result.getString());
	}

	/**
	 * Test constructor with Double argument
	 */
	@Test
	public void testFSPathResultWithDouble() {
		Double doubleValue = 12.3;
		FSPathResult result = new FSPathResult(doubleValue);
		assertEquals(doubleValue.toString(), result.getDouble().toString());
	}

	/**
	 * Test constructor with Date argument
	 */
	@Test
	public void testFSPathResultWithDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date.toString(), result.getDate().toString());
	}

	/**
	 * Test constructor with URI argument
	 */
	@Test
	public void testFSPathResultWithURI() {
		URI uri = new URI("http://example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri.toString(), result.getURI().toString());
	}

	/**
	 * Test constructor with Boolean argument
	 */
	@Test
	public void testFSPathResultWithBoolean() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);
		assertEquals(booleanValue.toString(), result.getBoolean().toString());
	}

	/**
	 * Test getFile method
	 */
	@Test
	public void testGetFile() {
		File file = new File("test.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals(file, result.getFile());
	}

	/**
	 * Test getString method
	 */
	@Test
	public void testGetString() {
		String string = "Test string";
		FSPathResult result = new FSPathResult(string);
		assertEquals(string, result.getString());
	}

	/**
	 * Test getDouble method
	 */
	@Test
	public void testGetDouble() {
		Double doubleValue = 12.3;
		FSPathResult result = new FSPathResult(doubleValue);
		assertEquals(doubleValue, result.getDouble());
	}

	/**
	 * Test getDate method
	 */
	@Test
	public void testGetDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date, result.getDate());
	}

	/**
	 * Test getURI method
	 */
	@Test
	public void testGetURI() {
		URI uri = new URI("http://example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri, result.getURI());
	}

	/**
	 * Test getBoolean method
	 */
	@Test
	public void testGetBoolean() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);
		assertEquals(booleanValue, result.getBoolean());
	}

	/**
	 * Test toString method with File object
	 */
	@Test
	public void testToStringWithFile() {
		File file = new File("test.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals("test.txt", result.toString());
	}

	/**
	 * Test toString method with String object
	 */
	@Test
	public void testToStringWithString() {
		String string = "Test string";
		FSPathResult result = new FSPathResult(string);
		assertEquals(string, result.toString());
	}

	/**
	 * Test toString method with Double object
	 */
	@Test
	public void testToStringWithDouble() {
		Double doubleValue = 12.3;
		FSPathResult result = new FSPathResult(doubleValue);
		assertEquals(doubleValue.toString(), result.toString());
	}

	/**
	 * Test toString method with Boolean object
	 */
	@Test
	public void testToStringWithBoolean() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);
		assertEquals(booleanValue.toString(), result.toString());
	}

	/**
	 * Test toString method with Date object
	 */
	@Test
	public void testToStringWithDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date.toString(), result.toString());
	}

	/**
	 * Test toString method with URI object
	 */
	@Test
	public void testToStringWithURI() {
		URI uri = new URI("http://example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri.toString(), result.toString());
	}

	/**
	 * Test toString method when underlying object is empty
	 */
	@Test
	public void testToStringWithEmptyObject() {
		FSPathResult result = new FSPathResult();
		assertEquals("FSPathResult : empty", result.toString());
	}
}