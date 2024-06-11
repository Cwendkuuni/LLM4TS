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
	public void testFSPathResultFileConstruction() {
		File file = new File("testFile.txt");
		FSPathResult result = new FSPathResult(file);

		assertEquals(file, result.getFile());
	}

	@Test
	public void testFSPathResultStringConstruction() {
		String string = "testString";
		FSPathResult result = new FSPathResult(string);

		assertEquals(string, result.getString());
	}

	@Test
	public void testFSPathResultDoubleConstruction() {
		Double doubleValue = 123.45;
		FSPathResult result = new FSPathResult(doubleValue);

		assertEquals(doubleValue, result.getDouble());
	}

	@Test
	public void testFSPathResultDateConstruction() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);

		assertEquals(date, result.getDate());
	}

	@Test
	public void testFSPathResultURIConstruction() {
		URI uri = new URI("http://test.com");
		FSPathResult result = new FSPathResult(uri);

		assertEquals(uri, result.getURI());
	}

	@Test
	public void testFSPathResultBooleanConstruction() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);

		assertEquals(booleanValue, result.getBoolean());
	}

	@Test
	public void testToStringFile() {
		File file = new File("testFile.txt");
		FSPathResult result = new FSPathResult(file);

		assertEquals(file.getAbsolutePath(), result.toString());
	}

	@Test
	public void testToStringString() {
		String string = "testString";
		FSPathResult result = new FSPathResult(string);

		assertEquals(string, result.toString());
	}

	@Test
	public void testToStringDouble() {
		Double doubleValue = 123.45;
		FSPathResult result = new FSPathResult(doubleValue);

		assertEquals(doubleValue.toString(), result.toString());
	}

	@Test
	public void testToStringBoolean() {
		Boolean booleanValue = true;
		FSPathResult result = new FSPathResult(booleanValue);

		assertEquals(booleanValue.toString(), result.toString());
	}

	@Test
	public void testToStringDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);

		assertEquals(date.toString(), result.toString());
	}

	@Test
	public void testToStringURI() {
		URI uri = new URI("http://test.com");
		FSPathResult result = new FSPathResult(uri);

		assertEquals(uri.toString(), result.toString());
	}

	@Test
	public void testToStringEmpty() {
		FSPathResult result = new FSPathResult(null);

		assertEquals("FSPathResult : empty", result.toString());
	}

	@Test
	public void testGetFileNull() {
		FSPathResult result = new FSPathResult(null);

		assertNull(result.getFile());
	}

	@Test
	public void testGetStringNull() {
		FSPathResult result = new FSPathResult(null);

		assertNull(result.getString());
	}

	@Test
	public void testGetDoubleNull() {
		FSPathResult result = new FSPathResult(null);

		assertNull(result.getDouble());
	}

	@Test
	public void testGetDateNull() {
		FSPathResult result = new FSPathResult(null);

		assertNull(result.getDate());
	}

	@Test
	public void testGetURINull() {
		FSPathResult result = new FSPathResult(null);

		assertNull(result.getURI());
	}

	@Test
	public void testGetBooleanNull() {
		FSPathResult result = new FSPathResult(null);

		assertNull(result.getBoolean());
	}
}