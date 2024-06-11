package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {
    
    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().toString());

        String expectedContent = "This is a test file.";
        String line;
        for (int i = 0; i < fileText.size(); i++) {
            line = fileText.get(i);
            if (i == 0) {
                assertEquals(expectedContent, line);
            } else {
                assertNull(line);
            }
        }
    }

    @Test
    public void testFileTextCopyConstruction() {
        File file = new File("testFile.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copyFileText = new FileText(originalFileText);

        assertNotNull(copyFileText);
        assertNotSame(originalFileText, copyFileText);
        assertEquals(originalFileText.getFile(), copyFileText.getFile());
        assertEquals(originalFileText.getCharset(), copyFileText.getCharset());
        assertEquals(originalFileText.lines.clone(), copyFileText.lines.clone());
        assertNull(originalFileText.lineBreaks);
        assertNull(copyFileText.lineBreaks);
    }

    @Test
    public void testFileTextFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("This is line 1.", "This is line 2.");
        FileText fileText = FileText.fromLines(file, lines);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines.clone());
        assertNull(fileText.lineBreaks);
    }

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        assertEquals(charsetName, fileText.getCharset().toString());
    }

    @Test
    public void testGetFullText() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        String expectedContent = "This is a test file.";
        FileText fileText = new FileText(file, charsetName);

        CharSequence fullText = fileText.getFullText();
        assertEquals(expectedContent, fullText);
    }

    @Test
    public void testToLinesArray() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        String[] expectedLines = {"This is a test file.", ""};
        FileText fileText = new FileText(file, charsetName);

        String[] lines = fileText.toLinesArray();
        assertArrayEquals(expectedLines, lines);
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        String content = "This is a test file.\nThis is the second line.";
        FileText fileText = new FileText(file, charsetName, content.split("\n"));
        int[] lineBreakPositions = fileText.findLineBreaks();

        int[] expectedLineBreakPositions = {0, content.length()};
        assertArrayEquals(expectedLineBreakPositions, lineBreakPositions);
    }

    @Test
    public void testLineColumn() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        String content = "This is line 1.\nThis is line 2.";
        FileText fileText = new FileText(file, charsetName, content.split("\n"));
        int pos = 16;
        LineColumn lineColumn = fileText.lineColumn(pos);

        LineColumn expectedLineColumn = new LineColumn(2, 6);
        assertEquals(expectedLineColumn, lineColumn);
    }

    @Test
    public void testGet() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        String[] lines = {"This is line 1.", "This is line 2."};
        FileText fileText = new FileText(file, charsetName, lines);

        String expectedLine1 = "This is line 1.";
        String expectedLine2 = null;

        String line1 = fileText.get(0);
        String line2 = fileText.get(1);

        assertEquals(expectedLine1, line1);
        assertEquals(expectedLine2, line2);
    }

    @Test
    public void testSize() {
        File file = new File("testFile.txt");
        String charsetName = "UTF-8";
        String[] lines = {"This is line 1.", "This is line 2."};
        FileText fileText = new FileText(file, charsetName, lines);

        int expectedSize = 2;
        int actualSize = fileText.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testStaticFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("This is line 1.", "This is line 2.");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines.clone());
    }

    @Test
    public void testPrivateConstructor() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("This is line 1.", "This is line 2.");
        FileText fileText = new FileText(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines.clone());
        assertNull(fileText.lineBreaks);
    }

    @Test
    public void testConstructionWithInvalidCharset() {
        File file = new File("testFile.txt");
        String invalidCharsetName = "invalidCharset";

        try {
            new FileText(file, invalidCharsetName);
            fail("Expected IllegalStateException, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}