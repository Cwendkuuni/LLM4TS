package com.example.junit4;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextCreationFromFile() throws IOException {
        File file = new File("testfile.txt");
        FileText fileText = new FileText(file, "UTF-8");

        assertEquals(file, fileText.getFile());
        assertEquals("UTF-8", fileText.getCharset().name());

        String expectedText = "This is a test file.";
        String actualText = fileText.getFullText();
        assertEquals(expectedText, actualText);

        String[] expectedLines = new String[] { "This is a test file.", "" };
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testFileTextCreationFromFileWithInvalidCharset() {
        File file = new File("testfile.txt");
        try {
            new FileText(file, "InvalidCharset");
            fail("Should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testFileTextCreationFromLines() {
        File file = new File("testfile.txt");
        List<String> lines = Arrays.asList("This is a test file.", "");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());

        String actualText = fileText.getFullText();
        String expectedText = "This is a test file.\n";
        assertEquals(expectedText, actualText);

        String[] expectedLinesArray = expectedText.split("\n");
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLinesArray, actualLines);

        int[] expectedLineBreakPositions = new int[] { 0, expectedText.length() };
        int[] actualLineBreakPositions = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreakPositions, actualLineBreakPositions);

        LineColumn expectedColumn = new LineColumn(1, 1);
        LineColumn actualColumn = fileText.lineColumn(0);
        assertEquals(expectedColumn, actualColumn);
    }

    @Test
    public void testFileTextCreationFromLinesWithEmptyList() {
        File file = new File("testfile.txt");
        List<String> lines = Collections.emptyList();
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());

        String actualText = fileText.getFullText();
        assertEquals("", actualText);

        String[] expectedLinesArray = new String[0];
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLinesArray, actualLines);

        int[] expectedLineBreakPositions = new int[1];
        expectedLineBreakPositions[0] = 0;
        int[] actualLineBreakPositions = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreakPositions, actualLineBreakPositions);

        LineColumn expectedColumn = new LineColumn(1, 0);
        LineColumn actualColumn = fileText.lineColumn(0);
        assertEquals(expectedColumn, actualColumn);
    }
}