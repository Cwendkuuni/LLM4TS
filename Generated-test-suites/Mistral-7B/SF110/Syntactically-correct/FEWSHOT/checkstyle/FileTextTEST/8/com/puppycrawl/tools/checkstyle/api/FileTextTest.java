package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(1, fileText.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionFromNonExistentFileAndCharset() throws IOException {
        File file = new File("nonExistentFile.txt");
        String charsetName = "UTF-8";
        new FileText(file, charsetName);
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(new FileText(file, charsetName));
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals(fileText.fullText, fileText.getFile().getCanonicalPath());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFileTextConstructionFromFileAndLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFileTextConstructionFromStringAndCharset() {
        Charset charset = Charset.forName("UTF-8");
        String text = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(null, text, charset);
        assertNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertEquals(text, fileText.getFullText());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        int[] lineBreakPositions = fileText.findLineBreaks();
        assertEquals(4, lineBreakPositions.length);
        assertEquals(0, lineBreakPositions[0]);
        int lastLineBreakPosition = 0;
        for (int i = 1; i < lineBreakPositions.length; i++) {
            assertEquals(lastLineBreakPosition + fileText.lines[i - 1].length() + 1, lineBreakPositions[i]);
            lastLineBreakPosition = lineBreakPositions[i];
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructionFromUnsupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "CP1252-TEST";
        new FileText(file, charsetName);
    }

    @Test
    public void testLineColumn() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        LineColumn lineColumn = fileText.lineColumn(5);
        assertEquals(1, lineColumn.lineNumber);
        assertEquals(5, lineColumn.columnNumber);
    }

    @Test
    public void testLineColumnOutOfBounds() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        LineColumn lineColumn = fileText.lineColumn(-1);
        assertNull(lineColumn);
    }
}