package com.example.junit;

import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertEquals("UTF-8", fileText.getCharset().name());
        String expectedFullText = "Hello, world!\n";
        String expectedLine0 = "Hello, world!";
        String[] expectedLines = new String[]{"Hello, world!"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
        assertEquals(expectedFullText, fileText.getFullText());
        // Add more assertions to test the lineBreaks, findLineBreaks(), and lineColumn() methods if needed
    }

    @Test
    public void testCopyConstructor() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset);
        FileText copy = new FileText(fileText);
        assertEquals(fileText.getFile(), copy.getFile());
        assertEquals(fileText.getCharset(), copy.getCharset());
        assertArrayEquals(fileText.lines, copy.lines);
        // Add more assertions to test the fullText and lineBreaks properties if needed
    }

    @Test
    public void testReadFile() throws IOException, UnsupportedCharsetException {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        String expectedContent = "Hello, world!";
        String actualContent = FileText.readFile(file, charset);
        assertEquals(expectedContent, actualContent);

        try {
            FileText.readFile(file, "InvalidCharset");
            fail("Expected UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            // Expected
        }
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Hello, world!", "");
        FileText expectedFileText = new FileText(file, lines);
        FileText actualFileText = FileText.fromLines(file, lines);
        assertEquals(expectedFileText, actualFileText);
    }
}