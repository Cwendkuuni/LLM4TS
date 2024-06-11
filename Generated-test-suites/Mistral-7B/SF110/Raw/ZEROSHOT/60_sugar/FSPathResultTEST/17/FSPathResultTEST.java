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
	public void testFSPathResultFileConstructor() {
		File file = new File("testFile.txt");
		FSPathResult fsPathResult = new FSPathResult(file);
		assertSame(file, fsPathResult.getFile());
	}

	@Test
	public void testFSPathResultStringConstructor() {
		FSPathResult fsPathResult = new FSPathResult("testString");
		assertEquals("testString", fsPathResult.getString());
	}

	@Test
	public void testFSPathResultDoubleConstructor() {
		FSPathResult fsPathResult = new FSPathResult(123.45);
		assertEquals(123.45, fsPathResult.getDouble().doubleValue(), 0.0);
	}

	@Test
	public void testFSPathResultDateConstructor() {
		Date date = new Date();
		FSPathResult fsPathResult = new FSPathResult(date);
		assertSame(date, fsPathResult.getDate());
	}

	@Test
	public void testFSPathResultURIConstructor() {
		URI uri = new URI("http://test.com");
		FSPathResult fsPathResult = new FSPathResult(uri);
		assertSame(uri, fsPathResult.getURI());
	}

	@Test
	public void testFSPathResultBooleanConstructor() {
		FSPathResult fsPathResult = new FSPathResult(Boolean.TRUE);
		assertTrue(fsPathResult.getBoolean());
	}

	@Test
	public void testFSPathResultGetters() {
		File file = new File("testFile.txt");
		FSPathResult fsPathResult = new FSPathResult(file);

		assertSame(file, fsPathResult.getFile());
		assertNull(fsPathResult.getString());
		assertNull(fsPathResult.getDouble());
		assertNull(fsPathResult.getDate());
		assertNull(fsPathResult.getURI());
		assertNull(fsPathResult.getBoolean());
	}

	@Test
	public void testFSPathResultToStringFile() {
		File file = new File("testFile.txt");
		FSPathResult fsPathResult = new FSPathResult(file);
		assertEquals(file.getAbsolutePath(), fsPathResult.toString());
	}

	@Test
	public void testFSPathResultToStringString() {
		FSPathResult fsPathResult = new FSPathResult("testString");
		assertEquals("testString", fsPathResult.toString());
	}

	@Test
	public void testFSPathResultToStringDouble() {
		FSPathResult fsPathResult = new FSPathResult(123.45);
		assertEquals("123.45", fsPathResult.toString());
	}

	@Test
	public void testFSPathResultToStringDate() {
		Date date = new Date();
		FSPathResult fsPathResult = new FSPathResult(date);
		assertEquals(date.toString(), fsPathResult.toString());
	}

	@Test
	public void testFSPathResultToStringURI() {
		URI uri = new URI("http://test.com");
		FSPathResult fsPathResult = new FSPathResult(uri);
		assertEquals(uri.toString(), fsPathResult.toString());
	}

	@Test
	public void testFSPathResultToStringBoolean() {
		FSPathResult fsPathResult = new FSPathResult(Boolean.TRUE);
		assertEquals("true", fsPathResult.toString());
	}

	@Test
	public void testFSPathResultToStringEmpty() {
		FSPathResult fsPathResult = new FSPathResult();
		assertEquals("FSPathResult : empty", fsPathResult.toString());
	}
}