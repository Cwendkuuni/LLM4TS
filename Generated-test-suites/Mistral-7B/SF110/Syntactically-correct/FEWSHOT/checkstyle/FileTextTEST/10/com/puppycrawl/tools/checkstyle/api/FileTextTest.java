package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() {
        String filePath = "testFile.txt";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        assertSame(new File(filePath), fileText.getFile());
        assertSame(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals(lines.size(), fileText.size());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());

        FileText fileTextFromLines = FileText.fromLines(new File(filePath), lines);
        assertSame(fileText, fileTextFromLines);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextThrowsFileNotFoundException() throws IOException {
        String filePath = "nonExistentFile.txt";
        new FileText(new File(filePath), "UTF-8");
    }

    @Test
    public void testFileTextGetFullText() throws IOException {
        String filePath = "testFile.txt";
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");

        FileText fileText = new FileText(new File(filePath), "UTF-8");
        String fullText = fileText.getFullText();
        String expectedFullText = String.join("\n", lines) + "\n";
        assertEquals(expectedFullText, fullText);
    }

    @Test
    public void testFindLineBreaks() {
        String testText = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        FileText fileText = new FileText(Arrays.asList(testText.split("\n")), null);
        int[] lineBreaks = fileText.findLineBreaks();

        int[] expectedLineBreaks = {0, 1, 3, 5, testText.length()};
        assertArrayEquals(expectedLineBreaks, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        String testText = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        FileText fileText = new FileText(Arrays.asList(testText.split("\n")), null);

        LineColumn lineColumn1 = fileText.lineColumn(5);
        LineColumn lineColumn2 = fileText.lineColumn(10);

        assertEquals(3, lineColumn1.getLineNumber());
        assertEquals(6, lineColumn1.getColumnNumber());
        assertEquals(5, lineColumn2.getLineNumber());
        assertEquals(1, lineColumn2.getColumnNumber());
    }
}