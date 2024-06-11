package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.io.CharStreams;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(5, fileText.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructionWithUnsupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = null;
        try {
            fileText = new FileText(file, "XYZ");
        } catch (IllegalStateException e) {
            fileText = e.getCause();
        }
        assertEquals("Unsupported charset: XYZ", fileText.getMessage());
    }

    @Test
    public void testFileTextConstructionWithFileTextObject() {
        File file = new File("testFile.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(new FileText(file, charset.getName()));
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertArrayEquals(fileText.lines, fileText.lines);
        assertSame(fileText.charset, fileText.charset);
    }

    @Test
    public void testFileTextConstructionWithLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Test line 1", "Test line 2", "Test line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(fileText.lines, lines.toArray(new String[lines.size()]));
        assertNull(fileText.lineBreaks);
    }

    @Test
    public void testSizeMethod() {
        FileText fileText = getFileTextInstance();
        assertEquals(5, fileText.size());
    }

    @Test
    public void testGetMethod() {
        FileText fileText = getFileTextInstance();
        assertEquals("Test line 1", fileText.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetMethodWithNegativeIndex() {
        FileText fileText = getFileTextInstance();
        fileText.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetMethodWithIndexGreaterThanSize() {
        FileText fileText = getFileTextInstance();
        fileText.get(5);
    }

    @Test
    public void testFindLineBreaksMethod() {
        FileText fileText = getFileTextInstance();
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[]{0, 11, 21, 33, 43, 46}, lineBreaks);
    }

    @Test
    public void testLineColumnMethod() {
        FileText fileText = getFileTextInstance();
        LineColumn lineColumn = fileText.lineColumn(44);
        assertEquals(3, lineColumn.lineNumber);
        assertEquals(13, lineColumn.columnNumber);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnMethodWithNegativeIndex() {
        FileText fileText = getFileTextInstance();
        fileText.lineColumn(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnMethodWithIndexGreaterThanSize() {
        FileText fileText = getFileTextInstance();
        fileText.lineColumn(6);
    }

    private FileText getFileTextInstance() throws IOException {
        File file = new File("testFile.txt");
        Charset charset = Charset.forName("UTF-8");
        return new FileText(file, charset.getName());
    }
}