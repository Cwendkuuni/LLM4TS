package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class, FileText.class})
public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        File testFile = PowerMockito.spy(new File("testFile.txt"));
        FileText fileText = new FileText(testFile, "UTF-8");
        assertEquals(testFile, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(readFileContent("testFile.txt"), fileText.getFullText());
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        FileText originalFileText = createFileText("testFile.txt");
        FileText cloneFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), cloneFileText.getFile());
        assertEquals(originalFileText.getCharset(), cloneFileText.getCharset());
        assertEquals(originalFileText.getFullText(), cloneFileText.getFullText());
        assertArrayEquals(originalFileText.lines, cloneFileText.lines);
    }

    @Test
    public void testFileTextConstructionWithFileAndLines() {
        File testFile = PowerMockito.spy(new File("testFile.txt"));
        List<String> lines = Arrays.asList(new String[]{"Line1", "Line2", "Line3"});
        FileText fileText = new FileText(testFile, lines);
        assertEquals(testFile, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals(lines.toArray(new String[lines.size()]), fileText.lines);
    }

    @Test
    public void testFromLines() throws IOException {
        List<String> lines = Arrays.asList(new String[]{"Line1", "Line2", "Line3"});
        File testFile = PowerMockito.spy(new File("testFile.txt"));
        FileText fileText = FileText.fromLines(testFile, lines);
        assertEquals(testFile, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
    }

    @Test
    public void testGetFile() {
        FileText fileText = createFileText("testFile.txt");
        assertEquals(new File("testFile.txt"), fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = createFileText("testFile.txt");
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = createFileText("testFile.txt");
        assertEquals(readFileContent("testFile.txt"), fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = createFileText("testFile.txt");
        String[] expectedLines = fileText.toLinesArray();
        String[] actualLines = readFileContentLines("testFile.txt");
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = createFileText("testFile.txt");
        int[] expectedLineBreaks = findLineBreaks("testFile.txt");
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = createFileText("testFile.txt");
        LineColumn expectedLineColumn = new LineColumn(1, 5);
        LineColumn actualLineColumn = fileText.lineColumn(5);
        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithInvalidFile() throws IOException {
        File invalidFile = new File("NonExistentFile.txt");
        new FileText(invalidFile, "UTF-8");
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testConstructorWithUnsupportedCharset() throws IOException {
        File testFile = PowerMockito.spy(new File("testFile.txt"));
        new FileText(testFile, "X-123456");
    }

    private static String readFileContent(String fileName) throws IOException {
        return Files.toString(new File(fileName), StandardCharsets.UTF_8);
    }

    private static String[] readFileContentLines(String fileName) throws IOException {
        return Files.readAllLines(new File(fileName), StandardCharsets.UTF_8).toArray(new String[0]);
    }

    private static int[] findLineBreaks(String fileName) throws IOException {
        FileText fileText = createFileText(fileName);
        int[] lineBreaks = fileText.findLineBreaks();
        return lineBreaks;
    }

    private static FileText createFileText(String fileName) throws IOException {
        File testFile = new File(fileName);
        return new FileText(testFile, StandardCharsets.UTF_8);
    }
}