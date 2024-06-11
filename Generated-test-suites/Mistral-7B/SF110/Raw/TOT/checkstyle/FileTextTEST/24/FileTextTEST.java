package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextCreation() throws IOException {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        assertEquals(readLinesFromFile(file, StandardCharsets.UTF_8), fileText.toLinesArray());
        assertEquals("UTF-8", fileText.getCharset().name());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedCharset() throws IOException {
        final File file = new File("src/test/resources/FileTextTest.java");
        new FileText(file, "XYZ-1234");
    }

    @Test
    public void testFileTextCreationFromLines() throws IOException {
        final File file = new File("src/test/resources/FileTextTest.java");
        final List<String> lines = readLinesFromFile(file, StandardCharsets.UTF_8);
        final FileText fileText = FileText.fromLines(file, lines);
        assertEquals(readLinesFromFile(file, StandardCharsets.UTF_8), fileText.toLinesArray());
        assertEquals("UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFile() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        assertEquals("UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        String expectedFullText = new String(readAllBytes(file), StandardCharsets.UTF_8);
        assertEquals(expectedFullText, fileText.getFullText());
    }

    @Test
    public void testSize() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        int expectedSize = (int) file.length();
        assertEquals(expectedSize, fileText.size());
    }

    @Test
    public void testGetLineColumn() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        final int pos = 50;
        final LineColumn lineColumn = fileText.lineColumn(pos);
        int expectedLineNo = 3; // Approximate line number for the test source
        int expectedColumnNo = 51; // Approximate column number for the test source
        assertEquals(expectedLineNo, lineColumn.lineNumber);
        assertEquals(expectedColumnNo, lineColumn.columnNumber);
    }

    @Test
    public void testGetLine() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, "UTF-8");
        final String expectedLine = "public class FileTextTest {";
        assertEquals(expectedLine, fileText.get(0));
    }

    private List<String> readLinesFromFile(File file, Charset charset) throws IOException {
        return new ArrayList<>(Files.readLines(file, charset));
    }

}