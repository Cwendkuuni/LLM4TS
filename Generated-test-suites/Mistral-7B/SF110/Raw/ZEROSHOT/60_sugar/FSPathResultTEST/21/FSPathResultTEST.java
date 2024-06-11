import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

	@Test
	public void testFSPathResultFile() {
		File file = new File("testFile.txt");
		FSPathResult fsPathResult = new FSPathResult(file);
		File resultFile = fsPathResult.getFile();
		assertEquals(file, resultFile);
	}

	@Test
	public void testFSPathResultString() {
		String string = "Test String";
		FSPathResult fsPathResult = new FSPathResult(string);
		String resultString = fsPathResult.getString();
		assertEquals(string, resultString);
	}

	@Test
	public void testFSPathResultDouble() {
		Double doubleValue = 12.34;
		FSPathResult fsPathResult = new FSPathResult(doubleValue);
		Double resultDouble = fsPathResult.getDouble();
		assertEquals(doubleValue, resultDouble);
	}

	@Test
	public void testFSPathResultDate() {
		Date date = new Date();
		FSPathResult fsPathResult = new FSPathResult(date);
		Date resultDate = fsPathResult.getDate();
		assertEquals(date, resultDate);
	}

	@Test
	public void testFSPathResultURI() {
		URI uri = new URI("http://www.example.com/test.html");
		FSPathResult fsPathResult = new FSPathResult(uri);
		URI resultURI = fsPathResult.getURI();
		assertEquals(uri, resultURI);
	}

	@Test
	public void testFSPathResultBoolean() {
		Boolean booleanValue = true;
		FSPathResult fsPathResult = new FSPathResult(booleanValue);
		Boolean resultBoolean = fsPathResult.getBoolean();
		assertEquals(booleanValue, resultBoolean);
	}

	@Test
	public void testFSPathResultToStringFile() {
		File file = new File("testFile.txt");
		FSPathResult fsPathResult = new FSPathResult(file);
		String resultString = fsPathResult.toString();
		assertTrue(resultString.contains(file.getAbsolutePath()));
	}

	@Test
	public void testFSPathResultToStringString() {
		String string = "Test String";
		FSPathResult fsPathResult = new FSPathResult(string);
		String resultString = fsPathResult.toString();
		assertEquals(string, resultString);
	}

	@Test
	public void testFSPathResultToStringDouble() {
		Double doubleValue = 12.34;
		FSPathResult fsPathResult = new FSPathResult(doubleValue);
		String resultString = fsPathResult.toString();
		assertTrue(resultString.contains(doubleValue.toString()));
	}

	@Test
	public void testFSPathResultToStringDate() {
		Date date = new Date();
		FSPathResult fsPathResult = new FSPathResult(date);
		String resultString = fsPathResult.toString();
		assertTrue(resultString.contains(date.toString()));
	}

	@Test
	public void testFSPathResultToStringURI() {
		URI uri = new URI("http://www.example.com/test.html");
		FSPathResult fsPathResult = new FSPathResult(uri);
		String resultString = fsPathResult.toString();
		assertTrue(resultString.contains(uri.toString()));
	}

	@Test
	public void testFSPathResultToStringBoolean() {
		Boolean booleanValue = true;
		FSPathResult fsPathResult = new FSPathResult(booleanValue);
		String resultString = fsPathResult.toString();
		assertTrue(resultString.contains(booleanValue.toString()));
	}

	@Test
	public void testFSPathResultToStringEmpty() {
		FSPathResult fsPathResult = new FSPathResult(null);
		String resultString = fsPathResult.toString();
		assertEquals("FSPathResult : empty", resultString);
	}
}