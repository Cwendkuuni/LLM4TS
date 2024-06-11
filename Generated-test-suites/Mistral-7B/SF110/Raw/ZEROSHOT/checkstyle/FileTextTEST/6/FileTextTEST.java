package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

	@Test
	public void testConstructor() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		assertNotNull(fileText.getFile());
		assertNotNull(fileText.getCharset());
		assertNotNull(fileText.getFullText());
		assertNotNull(fileText.lines);
		assertEquals(fileText.size(), 5); // replace with the number of lines in your test file
	}

	@Test(expected = FileNotFoundException.class)
	public void testFileNotFoundException() throws Exception {
		File file = new File("nonexistentfile.txt");
		new FileText(file, "UTF-8");
	}

	@Test
	public void testCharset() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		Charset charset = fileText.getCharset();
		assertEquals(charset.name(), "UTF-8");
	}

	@Test
	public void testGetFullText() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		String fullText = fileText.getFullText();
		// Replace this with the actual text from your test file
		assertTrue(fullText.contains("This is a test"));
	}

	@Test
	public void testToLinesArray() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		String[] lines = fileText.toLinesArray();
		// Replace this with the actual number of lines in your test file
		assertEquals(lines.length, 5);
		assertEquals(lines[0], "This is line 1"); // replace with the actual line contents
		assertEquals(lines[1], "This is line 2"); // replace with the actual line contents
		// ...
		assertEquals(lines[4], "This is line 5"); // replace with the actual line contents
	}

	@Test
	public void testFromLines() throws Exception {
		File file = new File("test.txt");
		List<String> lines = Lists.newArrayList(
				"This is line 1",
				"This is line 2",
				"This is line 3",
				"This is line 4",
				"This is line 5");
		FileText fileText1 = new FileText(file, "UTF-8");
		FileText fileText2 = FileText.fromLines(file, lines);
		assertEquals(fileText1, fileText2);
	}

	@Test
	public void testLineColumn() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		int pos = 20; // replace with a valid position in your test file
		LineColumn lineColumn = fileText.lineColumn(pos);
		assertEquals(lineColumn.lineNumber, 3); // replace with the line number at the given position
		assertEquals(lineColumn.columnNumber, 1); // replace with the column number at the given position
	}

	@Test
	public void testGet() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		String line = fileText.get(2); // replace with a valid line number
		assertEquals(line, "This is line 3"); // replace with the actual line content
	}

	@Test
	public void testSize() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		assertEquals(fileText.size(), 5); // replace with the number of lines in your test file
	}

	@Test
	public void testClone() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		FileText clonedFileText = fileText.clone();
		assertEquals(fileText, clonedFileText);
		assertNotSame(fileText.lineBreaks, clonedFileText.lineBreaks);
	}

	@Test
	public void testFindLineBreaks() throws Exception {
		File file = new File("test.txt");
		FileText fileText = new FileText(file, "UTF-8");
		int[] lineBreaks = fileText.findLineBreaks();
		// Replace this with the actual line breaks and size of your test file
		assertArrayEquals(new int[] { 0, 10, 20, 30, 40, 50 }, lineBreaks);
	}
}