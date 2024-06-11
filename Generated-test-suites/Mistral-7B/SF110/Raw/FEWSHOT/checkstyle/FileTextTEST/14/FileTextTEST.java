package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFileAndCharset() throws IOException {
        final File inputFile = new File("test.txt");
        final List<String> lines = Lists.newArrayList(
                "This is line 1",
                "This is line 2",
                "This is line 3");
        final FileText fileText = new FileText(inputFile, StandardCharsets.UTF_8.name());
        assertNotNull(fileText.getFile());
        assertTrue(fileText.getFile().exists());
        assertEquals("This is line 1\nThis is line 2\nThis is line 3", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        final File file = new File("test.txt");
        final List<String> lines = Lists.newArrayList(
                "This is line 1",
                "This is line 2",
                "This is line 3");
        final FileText fileText = new FileText(new FileText(file, lines));
        assertNotNull(fileText.getFile());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileAndLines() {
        final File inputFile = new File("test.txt");
        final List<String> lines = Lists.newArrayList(
                "This is line 1",
                "This is line 2",
                "This is line 3");
        final FileText fileText = FileText.fromLines(inputFile, lines);
        assertNotNull(fileText.getFile());
        assertTrue(fileText.getFile().exists());
        assertEquals("This is line 1\nThis is line 2\nThis is line 3", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        final String testString = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
        final FileText fileText = new FileText(new File("tempFile.txt"), testString);
        final int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[]{0, 7, 16, 24}, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        final String testString = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
        final FileText fileText = new FileText(new File("tempFile.txt"), testString);
        final LineColumn lineColumn1 = fileText.lineColumn(7);
        final LineColumn lineColumn2 = fileText.lineColumn(22);
        assertNull(lineColumn1);
        assertNotNull(lineColumn2);
        assertEquals(2, lineColumn2.getLineNumber());
        assertEquals(12, lineColumn2.getColumnNumber());
    }

    @Test
    public void testGet() {
        final String testString = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
        final FileText fileText = new FileText(new File("tempFile.txt"), testString);
        assertEquals("This is line 1", fileText.get(0));
        assertEquals("This is line 2", fileText.get(1));
        assertEquals("This is line 3", fileText.get(2));
        assertEquals("This is line 4", fileText.get(3));
        assertNull(fileText.get(4));
    }

    @Test
    public void testSize() {
        final String testString = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
        final FileText fileText = new FileText(new File("tempFile.txt"), testString);
        assertEquals(4, fileText.size());
    }

    @Test
    public void testGetCharset() {
        final File inputFile = new File("test.txt");
        final List<String> lines = Lists.newArrayList(
                "This is line 1",
                "This is line 2",
                "This is line 3");
        final FileText fileText = new FileText(inputFile, StandardCharsets.UTF_8.name());
        assertNotNull(fileText.getCharset());
        assertEquals(StandardCharsets.UTF_8.name(), fileText.getCharset().name());
    }

    @Test
    public void testGetFile() {
        final File inputFile = new File("test.txt");
        final List<String> lines = Lists.newArrayList(
                "This is line 1",
                "This is line 2",
                "This is line 3");
        final FileText fileText = new FileText(inputFile, StandardCharsets.UTF_8.name());
        assertNotNull(fileText.getFile());
        assertEquals(inputFile, fileText.getFile());
    }

    @Test
    public void testToString() {
        final String testString = "This is line 1\nThis is line 2\nThis is line 3\nThis is line 4";
        final FileText fileText = new FileText(new File("tempFile.txt"), testString);
        assertEquals(testString, fileText.toString());
    }
}