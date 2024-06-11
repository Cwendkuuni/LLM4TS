package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        String expectedFullText = readFile("src/test/resources/testFile.txt", StandardCharsets.UTF_8);
        assertEquals(expectedFullText, fileText.getFullText());
        String[] expectedLines = expectedFullText.split(System.lineSeparator());
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructorWithFileText() {
        FileText fileText1 = new FileText("Test File Text".split(System.lineSeparator()));
        FileText fileText2 = new FileText(fileText1);
        assertSame(fileText1.getFile(), fileText2.getFile());
        assertSame(fileText1.getCharset(), fileText2.getCharset());
        assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
    }

    @Test
    public void testFileTextConstructorWithFileAndLines() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(new File("src/test/resources/testFile.txt"), lines);
        assertEquals(new File("src/test/resources/testFile.txt"), fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File("src/test/resources/testFile.txt"), lines);
        assertEquals(new File("src/test/resources/testFile.txt"), fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testGetFile() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        FileText fileText = new FileText("Test File Text".split(System.lineSeparator()));
        assertNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        FileText fileText = new FileText("Test File Text".split(System.lineSeparator()));
        assertEquals("Test File Text\n", fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        FileText fileText = new FileText("Test File Text".split(System.lineSeparator()));
        String[] expectedLines = {"Test File Text"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText("Line 1\nLine 2\nLine 3".split(System.lineSeparator()));
        int[] expectedLineBreaks = {0, 2, 5};
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText("Line 1\nLine 2\nLine 3".split(System.lineSeparator()));
        LineColumn expectedColumn = new LineColumn(1, 1);
        LineColumn actualColumn = fileText.lineColumn(1);
        assertEquals(expectedColumn, actualColumn);

        expectedColumn = new LineColumn(2, 5);
        actualColumn = fileText.lineColumn(6);
        assertEquals(expectedColumn, actualColumn);

        LineColumn invalidColumn = fileText.lineColumn(10);
        assertNull(invalidColumn);
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText("Line 1\nLine 2\nLine 3".split(System.lineSeparator()));
        String expectedLine = "Line 2";
        String actualLine = fileText.get(1);
        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText("Line 1\nLine 2\nLine 3".split(System.lineSeparator()));
        int expectedSize = 3;
        int actualSize = fileText.size();
        assertEquals(expectedSize, actualSize);
    }

    private String readFile(String filePath, Charset charset) throws IOException {
        InputStream inputStream = new FileInputStream(new File(filePath));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        StringBuilder stringBuilder = new StringBuilder();
        int c;
        while ((c = inputStreamReader.read()) != -1) {
            stringBuilder.append((char) c);
        }
        return stringBuilder.toString();
    }
}