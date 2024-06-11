import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 *
 * @author <your_name>
 * @version 1.0
 */

public class FSPathResultTest {

	@Test
	public void testFSPathResultWithFile() {
		File file = new File("test.txt");
		FSPathResult result = new FSPathResult(file);
		assertEquals(file.getAbsolutePath(), result.toString());
	}

	@Test
	public void testFSPathResultWithString() {
		FSPathResult result = new FSPathResult("Hello World");
		assertEquals("Hello World", result.toString());
	}

	@Test
	public void testFSPathResultWithDouble() {
		FSPathResult result = new FSPathResult(3.14);
		assertEquals("3.14", result.toString());
	}

	@Test
	public void testFSPathResultWithDate() {
		Date date = new Date();
		FSPathResult result = new FSPathResult(date);
		assertEquals(date.toString(), result.toString());
	}

	@Test
	public void testFSPathResultWithURI() {
		URI uri = new URI("http://www.google.com");
		FSPathResult result = new FSPathResult(uri);
		assertEquals(uri.toString(), result.toString());
	}

	@Test
	public void testFSPathResultWithBoolean() {
		FSPathResult result = new FSPathResult(true);
		assertEquals("true", result.toString());
	}
}