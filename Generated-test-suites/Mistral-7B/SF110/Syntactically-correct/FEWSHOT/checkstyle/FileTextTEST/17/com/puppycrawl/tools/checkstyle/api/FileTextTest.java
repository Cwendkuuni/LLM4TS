package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        // Add tests for other methods in the constructor here

        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
        assertEquals(fileText.size(), (int) Files.size(file));
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        // Add tests for other methods in the constructor here

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileTextAndCharset() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        Charset charset = StandardCharsets.UTF_8;
        FileText fileText = new FileText(file, lines, charset);
        // Add tests for other methods in the constructor here

        assertEquals(file, fileText.getFile());
        assertEquals(charset, fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"), null);
        int[] lineBreaks = fileText.findLineBreaks();

        assertArrayEquals(new int[]{0, 7, 14, 24, 30}, lineBreaks);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindLineBreaksWithNullFullText() {
        new FileText(new File("testFile.txt"), null, null).findLineBreaks();
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"), null);
        LineColumn lineColumn = fileText.lineColumn(11);

        assertEquals(2, lineColumn.getLineNumber());
        assertEquals(6, lineColumn.getColumnNumber());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnWithInvalidIndex() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"), null);
        fileText.lineColumn(-1);
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"), null);
        String line = fileText.get(1);

        assertEquals("Line 2", line);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"), null);
        fileText.get(-1);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"), null);
        assertEquals(3, fileText.size());
    }
}