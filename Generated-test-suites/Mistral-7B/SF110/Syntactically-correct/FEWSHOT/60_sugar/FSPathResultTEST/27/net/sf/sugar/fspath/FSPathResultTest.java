package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

	@Test
	public void testFSPathResultWithFile() {
		File file = new File("/test/folder/file.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals(file, result.getFile());
	}

	@Test
	public void testFSPathResultWithString() {
		String string = "test string";
		FSPathResult result = new FSPathResult(string);
		assertEquals(string, result.getString());
	}

	@Test
	public void testFSPathResultWithDouble() {
		Double doubleValue = 3.14;
		FSPathResult result = new FSPathResult(doubleValue);
		assertEquals(doubleValue, result.getDouble());
	}

	@Test
	public void testFSPathResultWithDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date, result.getDate());
	}

	@Test
	public void testFSPathResultWithURI() {
		URI uri = new URI("http://www.example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri, result.getURI());
	}

	@Test
	public void testFSPathResultWithBoolean() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);
		assertEquals(booleanValue, result.getBoolean());
	}

	@Test
	public void testToStringWithFile() {
		File file = new File("/test/folder/file.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals("/test/folder/file.txt", result.toString());
	}

	@Test
	public void testToStringWithString() {
		String string = "test string";
		FSPathResult result = new FSPathResult(string);
		assertEquals("test string", result.toString());
	}

	@Test
	public void testToStringWithDouble() {
		Double doubleValue = 3.14;
		FSPathResult result = new FSPathResult(doubleValue);
		assertEquals("3.14", result.toString());
	}

	@Test
	public void testToStringWithBoolean() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);
		assertEquals("true", result.toString());
	}

	@Test
	public void testToStringWithDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date.toString(), result.toString());
	}

	@Test
	public void testToStringWithURI() {
		URI uri = new URI("http://www.example.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri.toString(), result.toString());
	}

	@Test
	public void testToStringWithEmptyResult() {
		FSPathResult result = new FSPathResult(null);
		assertEquals("FSPathResult : empty", result.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testToStringWithNullResult() {
		FSPathResult result = null;
		result.toString();
	}

	@Test
	public void testGetters() {
		File file = new File("/test/folder/file.txt");
		String string = "test string";
		Double doubleValue = 3.14;
		Date date = new Date();
		URI uri = new URI("http://www.example.com");
		Boolean booleanValue = true;
		FSPathResult resultFile = new FSPathResult(file);
		FSPathResult resultString = new FSPathResult(string);
		FSPathResult resultDouble = new FSPathResult(doubleValue);
		FSPathResult resultDate = new FSPathResult(date);
		FSPathResult resultURI = new FSPathResult(uri);
		FSPathResult resultBoolean = new FSPathResult(booleanValue);
		assertSame(file, resultFile.getFile());
		assertSame(string, resultString.getString());
		assertSame(doubleValue, resultDouble.getDouble());
		assertSame(date, resultDate.getDate());
		assertSame(uri, resultURI.getURI());
		assertSame(booleanValue, resultBoolean.getBoolean());
		assertNull(resultFile.getString());
		assertNull(resultFile.getDouble());
		assertNull(resultFile.getDate());
		assertNull(resultFile.getURI());
		assertNull(resultFile.getBoolean());
		assertNull(resultString.getFile());
		assertNull(resultString.getDouble());
		assertNull(resultString.getDate());
		assertNull(resultString.getURI());
		assertNull(resultString.getBoolean());
		assertNull(resultDouble.getFile());
		assertNull(resultDouble.getString());
		assertNull(resultDouble.getDate());
		assertNull(resultDouble.getURI());
		assertNull(resultDouble.getBoolean());
		assertNull(resultDate.getFile());
		assertNull(resultDate.getString());
		assertNull(resultDate.getDouble());
		assertNull(resultDate.getURI());
		assertNull(resultDate.getBoolean());
		assertNull(resultURI.getFile());
		assertNull(resultURI.getString());
		assertNull(resultURI.getDouble());
		assertNull(resultURI.getDate());
		assertNull(resultURI.getBoolean());
		assertNull(resultBoolean.getFile());
		assertNull(resultBoolean.getString());
		assertNull(resultBoolean.getDouble());
		assertNull(resultBoolean.getDate());
		assertNull(resultBoolean.getURI());
	}
}