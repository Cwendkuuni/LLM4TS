package com.example.junit;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.LineColumnTest;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        assertEquals(354, fileText.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedCharset() throws IOException {
        String filePath = "src/test/resources/FileTextTest.java";
        new FileText(new File(filePath), "XYZ-123"); // Unsupported charset
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText originalFileText = new FileText(new File(filePath), "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), copiedFileText.getFile());
        assertEquals(originalFileText.getCharset(), copiedFileText.getCharset());
        assertArrayEquals(originalFileText.lines, copiedFileText.lines);
    }

    @Test
    public void testFileTextConstructionWithFileAndLines() throws IOException {
        String filePath = "src/test/resources/FileTextTest.java";
        List<String> lines = readLines(new File(filePath), Charsets.UTF_8);
        FileText fileText = new FileText(new File(filePath), lines);
        assertEquals(lines.size(), fileText.size());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        assertEquals(new File(filePath), fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        assertEquals("UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        String expectedText = new String(readAllBytes(new File(filePath)), Charset.forName("UTF-8"));
        assertEquals(expectedText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        String[] expectedLines = readAllLines(new File(filePath), Charsets.UTF_8);
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() throws IOException {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        int[] expectedLineBreaks = new int[355];
        for (int i = 0; i < fileText.size(); ++i) {
            expectedLineBreaks[i] = i < 354 ? i + 1 : fileText.size();
        }
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() throws IOException {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        LineColumnTest lineColumnTest = new LineColumnTest();
        for (int lineNo = 1; lineNo <= fileText.size(); ++lineNo) {
            LineColumn expectedLineColumn = lineColumnTest.lineColumn(lineNo);
            LineColumn actualLineColumn = fileText.lineColumn(lineNo - 1); // JUnit uses zero-based indexing
            assertEquals(expectedLineColumn.line, actualLineColumn.line);
            assertEquals(expectedLineColumn.column, actualLineColumn.column);
        }
    }

    @Test
    public void testGet() {
        String filePath = "src/test/resources/FileTextTest.java";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        for (int i = 0; i < fileText.size(); ++i) {
            assertEquals(readLine(new File(filePath), Charsets.UTF_8, i), fileText.get(i));
        }
    }
}