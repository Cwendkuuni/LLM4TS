package com.example.junitTest;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

	@Test
	public void testFileTextCreation() throws IOException {
		File file = new File("src/test/resources/testFile.txt");
		FileText fileText = new FileText(file, "UTF-8");
		assertEquals(10, fileText.size());
		assertEquals("This is line 1.\nThis is line 2.\nThis is line 3.\nThis is line 4.\nThis is line 5.\nThis is line 6.\nThis is line 7.\nThis is line 8.\nThis is line 9.\nThis is line 10.", fileText.getFullText());
	}

	@Test(expected = IOException.class)
	public void testFileTextCreationWithInvalidCharset() throws IOException {
		File file = new File("src/test/resources/testFile.txt");
		new FileText(file, "invalidCharset");
	}

	@Test
	public void testFileTextCreationFromLines() {
		File file = new File("src/test/resources/testFile.txt");
		List<String> lines = readLines(file, Charsets.UTF_8);
		FileText fileText = FileText.fromLines(file, lines);
		assertEquals(10, fileText.size());
		assertEquals("This is line 1.\nThis is line 2.\nThis is line 3.\nThis is line 4.\nThis is line 5.\nThis is line 6.\nThis is line 7.\nThis is line 8.\nThis is line 9.\nThis is line 10.", fileText.getFullText());
	}

	@Test
	public void testFindLineBreaks() {
		FileText fileText = new FileText(new File("src/test/resources/testFile.txt"), "UTF-8");
		int[] lineBreaks = fileText.findLineBreaks();
		assertEquals(11, lineBreaks.length);
		assertArrayEquals(new int[] { 0, 30, 62, 94, 128, 162, 196, 230, 264, 300, 338 }, lineBreaks);
	}

	@Test
	public void testLineColumn() {
		FileText fileText = new FileText(new File("src/test/resources/testFile.txt"), "UTF-8");
		LineColumn lineColumn = fileText.lineColumn(120);
		assertEquals(6, lineColumn.getLineNo());
		assertEquals(13, lineColumn.getColumnNo());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testLineColumnNegativeIndex() {
		FileText fileText = new FileText(new File("src/test/resources/testFile.txt"), "UTF-8");
		fileText.lineColumn(-1);
	}

	@Test
	public void testGetLine() {
		FileText fileText = new FileText(new File("src/test/resources/testFile.txt"), "UTF-8");
		assertEquals("This is line 1.\n", fileText.get(0));
		assertEquals("This is line 5.\n", fileText.get(4));
		assertEquals("This is line 6.\n", fileText.get(5));
		assertEquals("This is line 7.\n", fileText.get(6));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetLineOutOfBounds() {
		FileText fileText = new FileText(new File("src/test/resources/testFile.txt"), "UTF-8");
		fileText.get(11);
	}
}