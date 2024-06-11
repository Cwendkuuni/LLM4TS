package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

	@Test
	public void testFSPathResultWithFile() {
		String filePath = "/test/sample.txt";
		File file = new File(filePath);

		FSPathResult result = new FSPathResult(file);
		File expectedResult = file;

		assertEquals(expectedResult, result.getFile());
		assertEquals(filePath, result.toString());
	}

	@Test
	public void testFSPathResultWithString() {
		String testString = "Test String";

		FSPathResult result = new FSPathResult(testString);
		String expectedResult = testString;

		assertEquals(expectedResult, result.getString());
		assertEquals(testString, result.toString());
	}

	@Test
	public void testFSPathResultWithDouble() {
		double testDouble = 3.14159265;

		FSPathResult result = new FSPathResult(testDouble);
		assertEquals(testDouble, result.getDouble(), 0.0);
		assertEquals(String.valueOf(testDouble), result.toString());
	}

	@Test
	public void testFSPathResultWithDate() {
		Date testDate = new Date();

		FSPathResult result = new FSPathResult(testDate);
		assertEquals(testDate, result.getDate());
		assertEquals(testDate.toString(), result.toString());
	}

	@Test
	public void testFSPathResultWithURI() {
		String testURIString = "http://example.com/test";
		URI testURI = new URI(testURIString);

		FSPathResult result = new FSPathResult(testURI);
		assertEquals(testURI, result.getURI());
		assertEquals(testURIString, result.toString());
	}

	@Test
	public void testFSPathResultWithBoolean() {
		boolean testBoolean = true;

		FSPathResult result = new FSPathResult(testBoolean);
		assertEquals(testBoolean, result.getBoolean());
		assertEquals(String.valueOf(testBoolean), result.toString());
	}

	@Test
	public void testEmptyFSPathResult() {
		FSPathResult result = new FSPathResult();
		assertEquals("FSPathResult : empty", result.toString());
		assertNull(result.getFile());
		assertNull(result.getString());
		assertNull(result.getDouble());
		assertNull(result.getDate());
		assertNull(result.getURI());
		assertNull(result.getBoolean());
	}

	@Test
	public void testToStringWithNullFSPathResult() {
		assertNull(new FSPathResult(null).toString());
	}

	@Test
	public void testGetters() {
		String filePath = "/test/sample.txt";
		File file = new File(filePath);
		String testString = "Test String";
		double testDouble = 3.14159265;
		Date testDate = new Date();
		URI testURI = new URI("http://example.com/test");
		Boolean testBoolean = true;

		FSPathResult resultFile = new FSPathResult(file);
		FSPathResult resultString = new FSPathResult(testString);
		FSPathResult resultDouble = new FSPathResult(testDouble);
		FSPathResult resultDate = new FSPathResult(testDate);
		FSPathResult resultURI = new FSPathResult(testURI);
		FSPathResult resultBoolean = new FSPathResult(testBoolean);

		assertSame(file, resultFile.getFile());
		assertSame(testString, resultString.getString());
		assertEquals(testDouble, resultDouble.getDouble(), 0.0);
		assertSame(testDate, resultDate.getDate());
		assertSame(testURI, resultURI.getURI());
		assertEquals(testBoolean, resultBoolean.getBoolean());
	}

	@Test
	public void testGettersWithEmptyFSPathResult() {
		FSPathResult emptyResult = new FSPathResult();
		assertNull(emptyResult.getFile());
		assertNull(emptyResult.getString());
		assertNull(emptyResult.getDouble());
		assertNull(emptyResult.getDate());
		assertNull(emptyResult.getURI());
		assertNull(emptyResult.getBoolean());
	}

	@Test
	public void testToStringWithFileFSPathResult() {
		String filePath = "/test/sample.txt";
		File file = new File(filePath);

		FSPathResult result = new FSPathResult(file);
		assertEquals(filePath, result.toString());
	}

	@Test
	public void testToStringWithStringFSPathResult() {
		String testString = "Test String";

		FSPathResult result = new FSPathResult(testString);
		assertEquals(testString, result.toString());
	}

	@Test
	public void testToStringWithDoubleFSPathResult() {
		double testDouble = 3.14159265;

		FSPathResult result = new FSPathResult(testDouble);
		assertEquals(String.valueOf(testDouble), result.toString());
	}

	@Test
	public void testToStringWithDateFSPathResult() {
		Date testDate = new Date();

		FSPathResult result = new FSPathResult(testDate);
		assertEquals(testDate.toString(), result.toString());
	}

	@Test
	public void testToStringWithURIFSPathResult() {
		String testURIString = "http://example.com/test";
		URI testURI = new URI(testURIString);

		FSPathResult result = new FSPathResult(testURI);
		assertEquals(testURIString, result.toString());
	}

	@Test
	public void testToStringWithBooleanFSPathResult() {
		boolean testBoolean = true;

		FSPathResult result = new FSPathResult(testBoolean);
		assertEquals(String.valueOf(testBoolean), result.toString());
	}
}