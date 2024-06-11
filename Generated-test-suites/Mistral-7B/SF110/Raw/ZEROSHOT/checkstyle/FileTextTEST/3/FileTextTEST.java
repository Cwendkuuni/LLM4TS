package com.example.junit;

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
    public void testFileTextConstructionFromFile() throws Exception {
        File file = new File("testFile.txt");
        List<String> expectedLines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, "UTF-8");
        assertArrayEquals(expectedLines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionFromNonExistentFile() throws Exception {
        File file = new File("nonExistentFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText1 = new FileText(file, StandardCharsets.UTF_8);
        FileText fileText2 = new FileText(fileText1);
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals("Line 1\nLine 2\nLine 3", fileText.getFullText());
    }

    @Test
    public void testSize() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(3, fileText.size());
    }

    @Test
    public void testGet() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testLineColumn() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        LineColumn expectedColumn1 = new LineColumn(1, 1);
        LineColumn expectedColumn2 = new LineColumn(1, 5);
        LineColumn expectedColumn3 = new LineColumn(2, 1);

        assertEquals(expectedColumn1, fileText.lineColumn(1));
        assertEquals(expectedColumn2, fileText.lineColumn(6));
        assertEquals(expectedColumn3, fileText.lineColumn(3));
    }
}