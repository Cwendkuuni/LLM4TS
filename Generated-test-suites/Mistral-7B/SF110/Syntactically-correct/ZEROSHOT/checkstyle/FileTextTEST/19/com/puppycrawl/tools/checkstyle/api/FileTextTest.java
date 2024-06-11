package com.example.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws Exception {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        // Test file properties
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);

        // Test file content
        String expectedContent = "Line 1\nLine 2\nLine 3";
        assertEquals(expectedContent, fileText.getFullText());
        String[] expectedLines = expectedContent.split("\n");
        assertArrayEquals(expectedLines, fileText.toLinesArray());

        // Test lineBreaks array
        int[] lineBreaks = fileText.findLineBreaks();
        int expectedLength = expectedContent.length();
        int[] expectedLineBreaks = {0, 2, 5, expectedLength};
        assertArrayEquals(expectedLineBreaks, lineBreaks);

        // Test lineColumn method
        int pos = 4;
        LineColumn expectedLineColumn = new LineColumn(2, 2);
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(expectedLineColumn, lineColumn);

        // Test get method
        String line = fileText.get(1);
        assertEquals("Line 2", line);

        // Test size method
        int size = fileText.size();
        assertEquals(3, size);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFile() throws Exception {
        File file = new File("nonExistentFile.txt");
        String charsetName = "UTF-8";
        new FileText(file, charsetName);
    }

    @Test
    public void testFileTextConstructionWithFileAndLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        // Test file properties
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);

        // Test file content
        String expectedContent = String.join("\n", lines);
        assertEquals(expectedContent, fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());

        // Test lineBreaks array
        int[] lineBreaks = fileText.findLineBreaks();
        int expectedLength = expectedContent.length();
        int[] expectedLineBreaks = {0, 2, 5, expectedLength};
        assertArrayEquals(expectedLineBreaks, lineBreaks);

        // Test lineColumn method
        int pos = 4;
        LineColumn expectedLineColumn = new LineColumn(2, 2);
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(expectedLineColumn, lineColumn);

        // Test get method
        String line = fileText.get(1);
        assertEquals("Line 2", line);

        // Test size method
        int size = fileText.size();
        assertEquals(3, size);
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(new FileText(file, "UTF-8"));

        // Test file properties
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);

        // Test file content
        String expectedContent = "Line 1\nLine 2\nLine 3";
        assertEquals(expectedContent, fileText.getFullText());
        String[] expectedLines = expectedContent.split("\n");
        assertArrayEquals(expectedLines, fileText.toLinesArray());

        // Test lineBreaks array
        int[] lineBreaks = fileText.findLineBreaks();
        int expectedLength = expectedContent.length();
        int[] expectedLineBreaks = {0, 2, 5, expectedLength};
        assertArrayEquals(expectedLineBreaks, lineBreaks);

        // Test lineColumn method
        int pos = 4;
        LineColumn expectedLineColumn = new LineColumn(2, 2);
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(expectedLineColumn, lineColumn);

        // Test get method
        String line = fileText.get(1);
        assertEquals("Line 2", line);

        // Test size method
        int size = fileText.size();
        assertEquals(3, size);
    }
}