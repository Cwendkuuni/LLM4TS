package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.puppycrawl.tools.checkstyle.api.*;
import java.io.*;
import java.nio.charset.*;
import static java.util.Arrays.*;

@RunWith(JUnit4.class)
public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        // Test successful construction
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("test.txt", fileText.getFile().getPath());
        assertEquals("UTF-8", fileText.getCharset().name());
        assertArrayEquals(new String[]{"Test line 1.", "Test line 2."}, fileText.toLinesArray());

        // Test construction with unsupported charset
        assertThrows(IllegalStateException.class, () -> new FileText(file, "XYZ"));

        // Test construction with non-existent file
        assertNull(new FileText(new File("non-existent.txt"), "UTF-8"));
    }

    @Test
    public void testCopyConstructor() {
        // Test shallow copy
        FileText fileText = new FileText(new File("test.txt"), "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertEquals(fileText.getFile(), copiedFileText.getFile());
        assertEquals(fileText.getCharset(), copiedFileText.getCharset());
        assertArrayEquals(fileText.toLinesArray(), copiedFileText.toLinesArray());

        // Test deep copy
        fileText.lineBreaks = new int[10];
        copiedFileText = new FileText(fileText);
        assertArrayNotSame(fileText.lineBreaks, copiedFileText.lineBreaks);
    }

    @Test
    public void testReadFile() throws IOException {
        File file = new File("test.txt");
        String expected = "Test line 1.\nTest line 2.\n";
        String actual = FileText.readFile(file, Charsets.UTF_8);
        assertEquals(expected, actual);
    }
}