package com.example.junit; // replace with your test package if needed

import static org.junit.Assert.*;
import com.google.common.io.Files;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @org.junit.Test
    public void testFileTextConstructionFromFile() throws IOException {
        String filePath = "path/to/your/test/file.txt";
        File file = new File(filePath);
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals(Files.readLines(file, StandardCharsets.UTF_8), fileText.toLinesArray());
    }

    @org.junit.Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionFromNonExistingFile() throws IOException {
        String filePath = "path/to/non-existent/file.txt";
        File file = new File(filePath);
        new FileText(file, StandardCharsets.UTF_8.name());
    }

    @org.junit.Test
    public void testFileTextConstructionFromFileText() {
        FileText fileText = new FileText(Arrays.asList("Line 1", "Line 2", "Line 3"), StandardCharsets.UTF_8);
        String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @org.junit.Test
    public void testGetFile() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        assertEquals(new File(filePath), fileText.getFile());
    }

    @org.junit.Test
    public void testGetCharset() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
    }

    @org.junit.Test
    public void testGetFullText() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        assertEquals(Files.readString(new File(filePath).toPath(), StandardCharsets.UTF_8), fileText.getFullText());
    }

    @org.junit.Test
    public void testToLinesArray() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        String[] expectedLines = Files.readLines(new File(filePath).toPath(), StandardCharsets.UTF_8).toArray(new String[0]);
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @org.junit.Test
    public void testFindLineBreaks() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        int[] expectedLineBreaks = Files.readAllLines(new File(filePath).toPath(), StandardCharsets.UTF_8).stream()
                .mapToInt(s -> s.length() + s.lastIndexOf('\n') + 1)
                .toArray();
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @org.junit.Test
    public void testLineColumn() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        String content = Files.readString(new File(filePath).toPath(), StandardCharsets.UTF_8);
        LineColumn lineColumn = fileText.lineColumn(content.indexOf("Line 2"));
        assertEquals(2, lineColumn.lineNumber);
        assertEquals(content.substring(content.lastIndexOf("\n", content.lastIndexOf("\n")), content.lastIndexOf("\n")).length() + 1, lineColumn.columnNumber);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnInvalidIndex() {
        String filePath = "path/to/your/test/file.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8.name());
        LineColumn lineColumn = fileText.lineColumn(Integer.MAX_VALUE);
    }
}