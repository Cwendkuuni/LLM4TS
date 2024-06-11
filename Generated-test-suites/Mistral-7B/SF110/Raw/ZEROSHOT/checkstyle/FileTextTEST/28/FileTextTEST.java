package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.io.Files;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.util.StringUtil;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("testFile.txt");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        Files.write(file, fileContent.getBytes(), Charsets.UTF_8);

        FileText fileText = new FileText(file, "UTF-8");

        assertEquals("testFile.txt", fileText.getFile().getPath());
        assertEquals("UTF-8", fileText.getCharset().name());
        assertEquals(fileContent, fileText.getFullText());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3", "Line 4"}, fileText.toLinesArray());
        assertEquals(4, fileText.size());

        Files.delete(file.toPath());
    }

    @Test
    public void testFileTextConstructionFromFileText() throws IOException {
        File file = new File("testFile.txt");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        Files.write(file, fileContent.getBytes(), Charsets.UTF_8);

        FileText originalFileText = new FileText(file, "UTF-8");
        FileText newFileText = new FileText(originalFileText);

        assertEquals(originalFileText.getFile(), newFileText.getFile());
        assertEquals(originalFileText.getCharset(), newFileText.getCharset());
        assertEquals(originalFileText.getFullText(), newFileText.getFullText());
        assertArrayEquals(originalFileText.toLinesArray(), newFileText.toLinesArray());
        assertEquals(originalFileText.size(), newFileText.size());

        Files.delete(file.toPath());
    }

    @Test
    public void testFileTextConstructionFromLines() throws IOException {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        List<String> lines = Arrays.asList(fileContent.split("\n"));
        FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);

        assertEquals("testFile.txt", fileText.getFile().getPath());
        assertNull(fileText.getCharset());
        assertEquals(fileContent, fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(4, fileText.size());

        File file = new File("testFile.txt");
        Files.write(file, fileContent.getBytes(), Charsets.UTF_8);
        FileText fileTextFromFile = new FileText(file, "UTF-8");
        Files.delete(file.toPath());

        assertNotSame(fileText, fileTextFromFile);
        assertEquals(fileTextFromFile.getFile(), fileText.getFile());
        assertEquals(fileTextFromFile.getCharset(), fileText.getCharset());
        assertEquals(fileTextFromFile.getFullText(), fileText.getFullText());
        assertArrayEquals(fileTextFromFile.toLinesArray(), fileText.toLinesArray());
        assertEquals(fileTextFromFile.size(), fileText.size());
    }

    @Test
    public void testGetFile() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        assertEquals("testFile.txt", fileText.getFile().getPath());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        assertEquals("UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        assertEquals(fileContent, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        String[] expectedLines = fileContent.split("\n");

        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";

        assertEquals(4, fileText.size());
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";

        LineColumn lineColumn1 = fileText.lineColumn(5);
        LineColumn lineColumn2 = fileText.lineColumn(10);
        LineColumn lineColumn3 = fileText.lineColumn(15);

        LineColumn expectedLineColumn1 = new LineColumn(1, 5);
        LineColumn expectedLineColumn2 = new LineColumn(2, 1);
        LineColumn expectedLineColumn3 = null;

        assertEquals(expectedLineColumn1.getLineNumber(), lineColumn1.getLineNumber());
        assertEquals(expectedLineColumn1.getColumnNumber(), lineColumn1.getColumnNumber());

        assertEquals(expectedLineColumn2.getLineNumber(), lineColumn2.getLineNumber());
        assertEquals(expectedLineColumn2.getColumnNumber(), lineColumn2.getColumnNumber());

        assertNull(lineColumn3);
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText("testFile.txt", "UTF-8");
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";

        assertEquals("Line 2", fileText.get(1));
    }
}