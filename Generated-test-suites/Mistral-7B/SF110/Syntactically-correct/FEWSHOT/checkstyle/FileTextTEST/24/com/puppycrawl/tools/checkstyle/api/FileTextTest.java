package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFileWithCharset() throws IOException {
        final File file = new File("test.txt");
        String expectedContent = "Line 1\n Line 2\n Line 3";
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(expectedContent, fileText.getFullText());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructionWithUnsupportedCharset() throws IOException {
        final File file = new File("test.txt");
        String charsetName = "ABCDEF";
        new FileText(file, charsetName);
    }

    @Test
    public void testFileTextConstructionFromFileAndLines() {
        final File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", " Line 2", " Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals(" Line 2", fileText.get(1));
        assertEquals(" Line 3", fileText.get(2));
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        final String text = "Line 1\n Line 2\n Line 3";
        FileText fileText = new FileText(Collections.singletonList(text));
        assertEquals(1, fileText.size());
        assertEquals(text, fileText.get(0));
    }

    @Test
    public void testFileTextConstructionFromFileTextAndCharset() {
        final String text = "Line 1\n Line 2\n Line 3";
        FileText fileText = new FileText(Collections.singletonList(text), StandardCharsets.UTF_8);
        assertEquals(1, fileText.size());
        assertEquals(text, fileText.get(0));
    }

    @Test
    public void testFileTextConstructionFromFileTextAndLines() {
        final String text = "Line 1\n Line 2\n Line 3";
        FileText fileText = new FileText(Collections.singletonList(text), Arrays.asList("Line 1", " Line 2", " Line 3"));
        assertEquals(1, fileText.size());
        assertEquals(text, fileText.get(0));
    }

    @Test
    public void testFileTextConstructionFromFileTextAndCharsetAndFile() throws IOException {
        final String text = "Line 1\n Line 2\n Line 3";
        File file = new File("test.txt");
        FileText fileText = new FileText(Collections.singletonList(text), StandardCharsets.UTF_8, file);
        assertEquals(text, fileText.getFullText());
        assertEquals(1, fileText.size());
        assertEquals(text, fileText.get(0));
    }

    @Test
    public void testFileTextConstructionFromFileTextAndLinesAndFile() throws IOException {
        final String text = "Line 1\n Line 2\n Line 3";
        File file = new File("test.txt");
        FileText fileText = new FileText(Collections.singletonList(text), Arrays.asList("Line 1", " Line 2", " Line 3"), file);
        assertEquals(text, fileText.getFullText());
        assertEquals(1, fileText.size());
        assertEquals(text, fileText.get(0));
    }

    @Test
    public void testFindLineBreaks() {
        final String content = "Line 1\n Line 2\n Line 3\n Line 4";
        FileText fileText = new FileText(Arrays.asList(content), StandardCharsets.UTF_8);
        int[] lineBreakPositions = fileText.findLineBreaks();
        int[] expectedLineBreakPositions = {0, 4, 11, 16};
        assertArrayEquals(expectedLineBreakPositions, lineBreakPositions);
    }

    @Test
    public void testLineColumn() {
        final String content = "Line 1\n Line 2\n Line 3\n Line 4";
        FileText fileText = new FileText(Arrays.asList(content), StandardCharsets.UTF_8);
        LineColumn lineColumn1 = fileText.lineColumn(0);
        LineColumn lineColumn2 = fileText.lineColumn(1);
        LineColumn lineColumn3 = fileText.lineColumn(2);
        LineColumn lineColumn4 = fileText.lineColumn(3);
        LineColumn lineColumn5 = fileText.lineColumn(4);
        LineColumn lineColumn6 = fileText.lineColumn(5);
        LineColumn lineColumn7 = fileText.lineColumn(6);
        LineColumn lineColumn8 = fileText.lineColumn(7);
        LineColumn lineColumn9 = fileText.lineColumn(8);
        LineColumn lineColumn10 = fileText.lineColumn(9);

        assertEquals(1, lineColumn1.getLineNo());
        assertEquals(1, lineColumn1.getColumnNo());
        assertEquals(1, lineColumn2.getLineNo());
        assertEquals(2, lineColumn2.getColumnNo());
        assertEquals(2, lineColumn3.getLineNo());
        assertEquals(3, lineColumn3.getColumnNo());
        assertEquals(3, lineColumn4.getLineNo());
        assertEquals(4, lineColumn4.getColumnNo());
        assertEquals(null, lineColumn5);
        assertEquals(null, lineColumn6);
        assertEquals(null, lineColumn7);
        assertEquals(null, lineColumn8);
        assertEquals(null, lineColumn9);
        assertEquals(null, lineColumn10);
    }

    @Test
    public void testGetFile() {
        final File file = new File("test.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8);
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        final String charsetName = "UTF-8";
        FileText fileText = new FileText(Collections.singletonList("test"), StandardCharsets.UTF_8);
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testToString() {
        final String content = "Line 1\n Line 2\n Line 3\n Line 4";
        FileText fileText = new FileText(Arrays.asList(content), StandardCharsets.UTF_8);
        assertEquals(content, fileText.toString());
    }
}