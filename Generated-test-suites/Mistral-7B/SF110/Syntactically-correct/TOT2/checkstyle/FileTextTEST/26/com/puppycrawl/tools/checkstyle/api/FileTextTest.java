package com.example.junit;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testGetMethod() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        String expectedLine = "Expected line text";
        String actualLine = fileText.get(0);

        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testErrorScenarios() {
        // Test file does not exist
        assertThrows(FileNotFoundException.class, () -> new FileText(new File("nonExistentFile.txt"), "UTF-8"));

        // Test unsupported charset
        assertThrows(IllegalStateException.class, () -> new FileText(new File("testFile.txt"), "unsupportedCharset"));

        // Test empty file
        String emptyFileContent = "";
        File emptyFile = new File("emptyFile.txt");
        assertThrows(IOException.class, () -> new FileText(emptyFile, "UTF-8"));

        // Test malformed content
        String malformedContent = "malformedContent";
        File malformedFile = new File("malformedFile.txt");
        assertThrows(IOException.class, () -> new FileText(malformedFile, "UTF-8"));
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int lineNumber = 1;
        int columnNumber = 5;
        LineColumn expectedLineColumn = new LineColumn(lineNumber, columnNumber);
        LineColumn actualLineColumn = fileText.lineColumn(10);

        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int expectedSize = 10;
        int actualSize = fileText.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetLine() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        String expectedLine = "Expected line text";
        String actualLine = fileText.get(5);

        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int[] expectedLineBreaks = new int[11];
        int[] actualLineBreaks = fileText.findLineBreaks();

        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }
}