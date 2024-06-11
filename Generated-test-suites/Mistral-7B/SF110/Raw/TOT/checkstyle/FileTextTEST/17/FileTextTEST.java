package com.example.junittest;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.LineColumnTest;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(23, fileText.size());
        String expectedContent = new String(readAllBytes(file), StandardCharsets.UTF_8);
        assertEquals(expectedContent, fileText.getFullText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTextInvalidCharset() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        new FileText(file, "InvalidCharset");
    }

    @Test
    public void testFileTextFromFileAndLines() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(23, fileText.size());
        String expectedContent = String.join("\n", lines);
        assertEquals(expectedContent, fileText.getFullText());
    }

    @Test
    public void testFileTextFromFileAndEmptyLinesList() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        List<String> emptyLines = Arrays.asList();
        FileText fileText = FileText.fromLines(file, emptyLines);
        assertEquals(0, fileText.size());
    }

    @Test
    public void testLineColumn() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        LineColumnTest lineColumnTest = new LineColumnTest();
        LineColumn lineColumn;
        for (int i = 0; i < fileText.size(); ++i) {
            lineColumn = fileText.lineColumn(i);
            assertEquals(i + 1, lineColumn.getLineNumber());
            assertEquals(i % 4 + 1, lineColumn.getColumnNumber());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnOutOfBounds() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(-1);
    }
}