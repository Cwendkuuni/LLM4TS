package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.AbstractList;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file text", fileText.getFullText(), readFile("test.txt", "UTF-8"));
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedCharsetException() {
        File file = new File("test.txt");
        new FileText(file, "XYZ");
    }

    @Test
    public void testCopyConstructor() {
        File file = new File("test.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(originalFileText);
        assertEquals("Expected copied file text to be equal to original", originalFileText.getFullText(), copiedFileText.getFullText());
    }

    @Test
    public void testConstructorWithFileAndLines() {
        String fileContent = "Line 1\nLine 2\nLine 3";
        File file = new File("test.txt");
        List<String> lines = Arrays.asList(fileContent.split("\n"));
        FileText fileText = new FileText(file, lines);
        assertEquals("Expected file text", fileContent, fileText.getFullText());
    }

    @Test
    public void testStaticMethodFromLines() {
        String fileContent = "Line 1\nLine 2\nLine 3";
        File file = new File("test.txt");
        List<String> lines = Arrays.asList(fileContent.split("\n"));
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals("Expected file text", fileContent, fileText.getFullText());
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file", file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected charset", "UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file text", readFile("test.txt", "UTF-8"), fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String[] expectedLines = readFileLines("test.txt");
        assertArrayEquals("Expected lines array", expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        int[] expectedLineBreaks = readFileLineBreaks("test.txt");
        assertArrayEquals("Expected line breaks array", expectedLineBreaks, fileText.findLineBreaks());
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        int[] expectedLineBreaks = readFileLineBreaks("test.txt");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals("Expected line number", 1, lineColumn.getLineNumber());
        assertEquals("Expected column number", 1, lineColumn.getColumnNumber());
    }

    @Test
    public void testGet() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String expectedLine = readFileLines("test.txt")[0];
        assertEquals("Expected line", expectedLine, fileText.get(0));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        int expectedSize = readFileLines("test.txt").length;
        assertEquals("Expected size", expectedSize, fileText.size());
    }

    private static String readFile(String fileName, String charsetName) throws IOException {
        File file = new File(fileName);
        Charset charset = Charset.forName(charsetName);
        return FileText.readFile(file, charset.newDecoder());
    }

    private static String[] readFileLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[lines.size()]);
    }

    private static int[] readFileLineBreaks(String fileName) throws IOException {
        File file = new File(fileName);
        Charset charset = StandardCharsets.UTF_8;
        FileText fileText = new FileText(file, charset);
        return fileText.findLineBreaks();
    }
}