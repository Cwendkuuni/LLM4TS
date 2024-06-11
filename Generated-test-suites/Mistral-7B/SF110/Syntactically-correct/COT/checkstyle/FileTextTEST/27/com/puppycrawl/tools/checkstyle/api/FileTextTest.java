package com.example.junit;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file path", file.getPath(), fileText.getFile().getPath());
        assertEquals("Expected charset", StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "XYZ");
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        FileText fileTextCopy = new FileText(fileText);
        assertEquals("Expected files to be the same", fileText.getFile(), fileTextCopy.getFile());
        assertEquals("Expected charsets to be the same", fileText.getCharset(), fileTextCopy.getCharset());
        assertArrayEquals("Expected lines to be the same", fileText.toLinesArray(), fileTextCopy.toLinesArray());
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals("Expected file path", file.getPath(), fileText.getFile().getPath());
        assertEquals("Expected lines size", 3, fileText.size());
        assertArrayEquals("Expected lines", lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file", file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected charset", StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String fullText = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, fullText);
        assertEquals("Expected full text", fullText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String[] lines = {"Line 1", "Line 2", "Line 3"};
        FileText fileText = new FileText(file, lines);
        assertArrayEquals("Expected lines", lines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String fullText = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, fullText);
        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals("Expected number of line breaks", 3, lineBreaks.length);
        int expectedLineBreakPositions[] = {0, 5, 12};
        assertArrayEquals("Expected line break positions", expectedLineBreakPositions, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String fullText = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, fullText);
        LineColumn lineColumn = fileText.lineColumn(12);
        assertEquals("Expected line number", 2, lineColumn.getLineNumber());
        assertEquals("Expected column number", 2, lineColumn.getColumnNumber());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        String[] lines = {"Line 1", "Line 2", "Line 3"};
        FileText fileText = new FileText(file, lines);
        assertEquals("Expected line content", "Line 2", fileText.get(1));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        String[] lines = {"Line 1", "Line 2", "Line 3"};
        FileText fileText = new FileText(file, lines);
        assertEquals("Expected size", 3, fileText.size());
    }
}