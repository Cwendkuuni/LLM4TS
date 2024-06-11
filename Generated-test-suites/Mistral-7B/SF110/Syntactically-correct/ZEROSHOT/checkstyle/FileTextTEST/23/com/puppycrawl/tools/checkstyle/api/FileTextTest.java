package com.example.junittest;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        List<String> expectedLines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, "UTF-8");
        Charset charset = Charset.forName("UTF-8");
        assertEquals(file, fileText.getFile());
        assertEquals(charset, fileText.getCharset());
        assertArrayEquals(expectedLines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFileAndCharset() throws IOException {
        File file = new File("nonExistentFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextConstructionWithFileTextObject() {
        File file = new File("testFile.txt");
        List<String> expectedLines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(new FileText(file, "UTF-8"));
        assertEquals(file, fileText.getFile());
        assertArrayEquals(expectedLines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionWithFileAndLines() {
        File file = new File("testFile.txt");
        List<String> expectedLines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, expectedLines);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(expectedLines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testLineColumn() {
        File file = new File("testFile.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(1, lineColumn.getLineNumber());
        assertEquals(6, lineColumn.getColumnNumber());
    }

    @Test
    public void testSize() {
        File file = new File("testFile.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(3, fileText.size());
    }

    @Test
    public void testGet() {
        File file = new File("testFile.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals("Line 2", fileText.get(1));
    }

}