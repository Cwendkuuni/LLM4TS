package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.ImmutableList;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.name());

        assertEquals("Expected file path", "test.txt", fileText.getFile().getPath());
        assertEquals("Expected charset", charset, fileText.getCharset());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText;

        try {
            fileText = new FileText(file, "XYZ");
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Unexpected exception type", e);
        }

        fail("Expected IllegalStateException");
    }

    @Test
    public void testFileTextCopyConstructor() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        FileText copiedFileText = new FileText(fileText);

        assertEquals("Copied object's lines should be equal", fileText.size(), copiedFileText.size());
        for (int i = 0; i < fileText.size(); i++) {
            assertEquals("Copied object's lines should be equal", fileText.get(i), copiedFileText.get(i));
        }
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File file = new File("test.txt");
        List<String> lines = ImmutableList.of("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals("Expected file path", file.getPath(), fileText.getFile().getPath());
        assertEquals("Expected lines size", lines.size(), fileText.size());
        for (int i = 0; i < lines.size(); i++) {
            assertEquals("Expected lines to be equal", lines.get(i), fileText.get(i));
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithNonExistentFile() throws IOException {
        File file = new File("non-existent-file.txt");
        new FileText(file, "UTF-8");

        fail("Expected FileNotFoundException");
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();

        int expectedLineBreakCount = 3; // assuming there are 3 line breaks in the file
        assertEquals("Expected number of line breaks", expectedLineBreakCount, lineBreaks.length);
        for (int i = 0; i < expectedLineBreakCount; i++) {
            int expectedLineBreakPosition = i * READ_BUFFER_SIZE + i;
            assertEquals("Expected line break positions", expectedLineBreakPosition, lineBreaks[i]);
        }
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        LineColumn lineColumn1 = fileText.lineColumn(0);
        LineColumn lineColumn2 = fileText.lineColumn(5);
        LineColumn lineColumn3 = fileText.lineColumn(10);

        assertEquals("Expected line number", 1, lineColumn1.getLineNumber());
        assertEquals("Expected column number", 1, lineColumn1.getColumnNumber());

        assertEquals("Expected line number", 1, lineColumn2.getLineNumber());
        assertEquals("Expected column number", 6, lineColumn2.getColumnNumber());

        assertEquals("Expected line number", -1, lineColumn3.getLineNumber());
        assertEquals("Expected column number", 1, lineColumn3.getColumnNumber());
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");

        String expectedLine1 = "Line 1";
        String expectedLine2 = "Line 2";
        String expectedLine3 = "Line 3";

        assertEquals("Expected line 1", expectedLine1, fileText.get(0));
        assertEquals("Expected line 2", expectedLine2, fileText.get(1));
        assertEquals("Expected line 3", expectedLine3, fileText.get(2));
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        int expectedSize = 3;

        assertEquals("Expected size", expectedSize, fileText.size());
    }
}