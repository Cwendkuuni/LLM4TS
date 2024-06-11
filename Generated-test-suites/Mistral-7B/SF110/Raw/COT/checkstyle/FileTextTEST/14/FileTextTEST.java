package com.example.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.ImmutableList;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
        String fullText = fileText.getFullText();
        // Add a test to check the content of fullText here

        String[] expectedLines = new String[]{"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithInvalidFile() throws IOException {
        File invalidFile = new File("invalidFile.txt");
        String charsetName = "UTF-8";
        new FileText(invalidFile, charsetName);
    }

    @Test
    public void testConstructorWithFileText() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        List<String> lines = ImmutableList.of("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset() == null ? null : fileText.getCharset().name());
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(lines.toArray(new String[0]), actualLines);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullFile() {
        File nullFile = null;
        String charsetName = "UTF-8";
        new FileText(nullFile, charsetName);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullLines() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        new FileText(file, null);
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("test.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"));
        int[] lineBreakPositions = fileText.findLineBreaks();
        int size = fileText.size();
        assertArrayEquals(new int[]{0, 6, 13}, lineBreakPositions);
        LineColumn lineColumn = fileText.lineColumn(11);
        assertEquals(1, lineColumn.lineNo);
        assertEquals(6, lineColumn.columnNo);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testLineColumnWithInvalidPosition() {
        FileText fileText = new FileText(new File("test.txt"), Arrays.asList("Line 1", "Line 2"));
        fileText.lineColumn(-1);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("test.txt"), Arrays.asList("Line 1", "Line 2"));
        assertEquals(2, fileText.size());
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("test.txt"), Arrays.asList("Line 1\nLine 2\nLine 3"));
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testStaticFromLinesMethod() throws IOException {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        String charsetName = "UTF-8";
        Charset charset = Charset.forName(charsetName);
        FileText fileTextWithCharset = new FileText(file, charset);
        assertEquals(fileText, FileText.fromLines(fileTextWithCharset, lines));
    }
}