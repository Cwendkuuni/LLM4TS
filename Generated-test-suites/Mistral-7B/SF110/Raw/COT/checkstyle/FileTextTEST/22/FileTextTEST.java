package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertSame(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(3, fileText.size());
    }

    @Test(expected = IOException.class)
    public void testConstructorWithInvalidCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "InvalidCharset");
    }

    @Test
    public void testConstructorWithFileAndLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertSame(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertSame(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(3, fileText.size());
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertSame(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetString() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        assertEquals("Line 1\nLine 2\nLine 3", fileText.getFullText());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        int[] lineBreakPositions = fileText.findLineBreaks();
        assertEquals(4, lineBreakPositions.length);
        assertEquals(0, lineBreakPositions[0]);
        assertEquals(12, lineBreakPositions[1]);
        assertEquals(24, lineBreakPositions[2]);
        assertEquals(33, lineBreakPositions[3]);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(12);
        assertEquals(1, lineColumn.lineNumber);
        assertEquals(2, lineColumn.columnNumber);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        fileText.get(5);
    }

}