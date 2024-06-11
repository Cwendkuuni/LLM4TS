package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(expectedLines, Arrays.asList(fileText.toLinesArray()));
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFile() throws IOException {
        File file = new File("nonExistentFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextConstructionWithFileAndLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(lines, Arrays.asList(fileText.toLinesArray()));
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(new FileText(file, charsetName), charsetName);
        assertEquals(lines, Arrays.asList(fileText.toLinesArray()));
    }

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        String expectedFullText = String.join("\n", lines) + "\n";
        assertEquals(expectedFullText, fileText.getFullText());
    }

    @Test
    public void testToString() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        String expectedToString = "[FileText: lines=[Line 1, Line 2, Line 3], file=testFile.txt, charset=UTF-8]";
        assertEquals(expectedToString, fileText.toString());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1\n", "Line 2\nLine 3", "Line 4");
        FileText fileText = new FileText(file, "UTF-8", lines.toArray(new String[lines.size()]));
        int[] expectedLineBreaks = {0, 7, 13, 22};
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1\n", "Line 2\nLine 3", "Line 4");
        FileText fileText = new FileText(file, "UTF-8", lines.toArray(new String[lines.size()]));
        LineColumn lineColumn1 = fileText.lineColumn(3);
        LineColumn lineColumn2 = fileText.lineColumn(8);
        LineColumn lineColumn3 = fileText.lineColumn(16);
        LineColumn lineColumn4 = fileText.lineColumn(23);
        LineColumn lineColumn5 = fileText.lineColumn(30);
        assertEquals(1, lineColumn1.getLineNumber());
        assertEquals(1, lineColumn1.getColumnNumber());
        assertEquals(2, lineColumn2.getLineNumber());
        assertEquals(2, lineColumn2.getColumnNumber());
        assertEquals(3, lineColumn3.getLineNumber());
        assertEquals(5, lineColumn3.getColumnNumber());
        assertEquals(4, lineColumn4.getLineNumber());
        assertEquals(5, lineColumn4.getColumnNumber());
        assertEquals(-1, lineColumn5.getLineNumber());
        assertEquals(-1, lineColumn5.getColumnNumber());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testLineColumnNegativeIndex() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1\n", "Line 2\nLine 3", "Line 4");
        FileText fileText = new FileText(file, "UTF-8", lines.toArray(new String[lines.size()]));
        fileText.lineColumn(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnIndexOutOfBounds() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1\n", "Line 2\nLine 3", "Line 4");
        FileText fileText = new FileText(file, "UTF-8", lines.toArray(new String[lines.size()]));
        fileText.lineColumn(6);
    }
}