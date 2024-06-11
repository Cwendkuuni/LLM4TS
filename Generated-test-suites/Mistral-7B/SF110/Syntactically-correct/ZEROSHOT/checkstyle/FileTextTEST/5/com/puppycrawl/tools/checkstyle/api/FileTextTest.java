package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws Exception {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.newDecoder());
        FileText expectedFileText = new FileText(file, lines.toArray(new String[lines.size()]));

        assertEquals(expectedFileText.getFile(), fileText.getFile());
        assertEquals(expectedFileText.getCharset(), fileText.getCharset());
        assertEquals(expectedFileText.getFullText(), fileText.getFullText());
        assertArrayEquals(expectedFileText.lines, fileText.lines);
        assertArrayEquals(expectedFileText.lineBreaks, fileText.lineBreaks);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFile() throws Exception {
        String filePath = "nonExistentFile.txt";
        File file = new File(filePath);
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.newDecoder());
    }

    @Test
    public void testFileTextClone() throws Exception {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines.toArray(new String[lines.size()]));
        FileText clonedFileText = new FileText(fileText);

        assertEquals(fileText.getFile(), clonedFileText.getFile());
        assertEquals(fileText.getCharset(), clonedFileText.getCharset());
        assertEquals(fileText.getFullText(), clonedFileText.getFullText());
        assertArrayEquals(fileText.lines, clonedFileText.lines);
        assertArrayEquals(fileText.lineBreaks, clonedFileText.lineBreaks);
    }

    @Test
    public void testFileTextFromLines() throws Exception {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(fileText.getFile(), file);
        assertEquals(fileText.getCharset(), null);
        assertEquals(fileText.getFullText(), lines.toString());
        assertArrayEquals(fileText.lines, lines.toArray(new String[lines.size()]));
        assertNull(fileText.lineBreaks);
    }

    @Test
    public void testGetLineColumn() throws Exception {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines.toArray(new String[lines.size()]));

        LineColumn lineColumn1 = fileText.lineColumn(1);
        LineColumn lineColumn2 = fileText.lineColumn(3);

        assertEquals(1, lineColumn1.getLineNumber());
        assertEquals(1, lineColumn1.getColumnNumber());

        assertEquals(3, lineColumn2.getLineNumber());
        assertEquals(1, lineColumn2.getColumnNumber());
    }

    @Test
    public void testSize() throws Exception {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines.toArray(new String[lines.size()]));

        assertEquals(3, fileText.size());
    }

    @Test
    public void testGet() throws Exception {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines.toArray(new String[lines.size()]));

        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }
}