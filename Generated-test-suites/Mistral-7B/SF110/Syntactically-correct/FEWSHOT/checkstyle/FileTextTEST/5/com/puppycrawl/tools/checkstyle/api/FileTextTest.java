package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.name());
        assertEquals("test content", fileText.get(0));
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithInvalidFile() throws IOException {
        File file = new File("nonExistentFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testGetFile() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        assertEquals(new File("testFile.txt"), fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        String fullText = fileText.getFullText();
        assertNotNull(fullText);
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        String[] lines = fileText.toLinesArray();
        assertNotNull(lines);
        assertEquals(5, lines.length);
    }

    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);
        String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals(5, lineBreaks.length);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(1, lineColumn.getLineNumber());
        assertEquals(6, lineColumn.getColumnNumber());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        fileText.get(50);
    }
}