package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class FSPathResultTest {

	@Test
	public void testFSPathResultWithFile() {
		File file = new File("testFile.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals("testFile.txt", result.toString());
		assertSame(file, result.getFile());
	}

	@Test
	public void testFSPathResultWithString() {
		FSPathResult result = new FSPathResult("testString");
		assertEquals("testString", result.toString());
		assertNull(result.getFile());
		assertEquals("testString", result.getString());
	}

	@Test
	public void testFSPathResultWithDouble() {
		FSPathResult result = new FSPathResult(123.45);
		assertEquals("123.45", result.toString());
		assertNull(result.getFile());
		assertEquals(123.45, result.getDouble().doubleValue());
	}

	@Test
	public void testFSPathResultWithDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals("Sun Jan 22 15:36:47 IST 2023", result.toString());
		assertNull(result.getFile());
		assertEquals(date, result.getDate());
	}

	@Test
	public void testFSPathResultWithURI() {
		URI uri = new URI("http://www.google.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals("http://www.google.com", result.toString());
		assertNull(result.getFile());
		assertEquals(uri, result.getURI());
	}

	@Test
	public void testFSPathResultWithBoolean() {
		FSPathResult result = new FSPathResult(true);
		assertEquals("true", result.toString());
		assertNull(result.getFile());
		assertEquals(true, result.getBoolean().booleanValue());
	}

	@Test
	public void testToString_empty() {
		FSPathResult result = new FSPathResult();
		assertEquals("FSPathResult : empty", result.toString());
		assertNull(result.getFile());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFSPathResultWithNull() {
		FSPathResult result = new FSPathResult(null);
	}
}