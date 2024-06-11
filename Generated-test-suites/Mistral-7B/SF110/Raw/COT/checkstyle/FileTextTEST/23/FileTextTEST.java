package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(fileText.size(), 0); // initial list is empty
    }

    @Test(expected = IOException.class)
    public void testFileTextConstructorWithInvalidCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "ABCDEFGH");
    }

    @Test
    public void testFileTextCloneConstructor() {
        File file = new File("test.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText clonedFileText = new FileText(originalFileText);
        assertEquals(originalFileText.file, clonedFileText.file);
        assertEquals(originalFileText.charset, clonedFileText.charset);
        assertEquals(originalFileText.fullText, clonedFileText.fullText);
        assertArrayEquals(originalFileText.lines, clonedFileText.lines);
        assertArrayEquals(originalFileText.lineBreaks, clonedFileText.lineBreaks);
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(fileText.size(), 3);
        assertEquals(fileText.lines[0], "Line 1");
        assertEquals(fileText.lines[1], "Line 2");
        assertEquals(fileText.lines[2], "Line 3");
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(fileText.getFile(), file);
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(fileText.getCharset(), Charset.forName("UTF-8"));
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(fileText.getFullText(), content);
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, "UTF-8");
        String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, "UTF-8");
        int[] expectedLineBreaks = {0, 2, 5};
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, "UTF-8");
        LineColumn expectedLineColumn = new LineColumn(1, 5);
        LineColumn actualLineColumn = fileText.lineColumn(5);
        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        File file = new File("test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, "UTF-8");
        fileText.get(5);
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(fileText.size(), 3);
    }

}