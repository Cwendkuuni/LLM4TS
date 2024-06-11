package com.example.junit;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFile() throws IOException {
        File file = new File("test_file.txt");
        List<String> expectedLines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(expectedLines.toArray(new String[expectedLines.size()]), actualLines);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionFromNonExistentFile() throws IOException {
        File file = new File("test_non_existent_file.txt");
        new FileText(file, StandardCharsets.UTF_8.name());
    }

    @Test
    public void testFileTextCopyConstructor() throws IOException {
        File file = new File("test_file.txt");
        List<String> expectedLines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText originalFileText = new FileText(file, StandardCharsets.UTF_8.name());
        FileText copyFileText = new FileText(originalFileText);
        String[] actualLines = copyFileText.toLinesArray();
        assertArrayEquals(expectedLines.toArray(new String[expectedLines.size()]), actualLines);
    }

    @Test
    public void testFileTextFromLines() throws IOException {
        File file = new File("test_file.txt");
        List<String> lines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = FileText.fromLines(file, lines);
        String[] actualLines = fileText.toLinesArray();
        assertArrayEquals(lines.toArray(new String[lines.size()]), actualLines);
    }

    @Test
    public void testGetFile() {
        File file = new File("test_file.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        Charset charset = StandardCharsets.UTF_8;
        File file = new File("test_file.txt");
        FileText fileText = new FileText(file, charset.name());
        assertEquals(charset, fileText.getCharset());
    }

    @Test
    public void testGetFullText() throws IOException {
        File file = new File("test_file.txt");
        List<String> expectedLines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        String actualFullText = fileText.getFullText();
        String expectedFullText = String.join("\n", expectedLines);
        assertEquals(expectedFullText, actualFullText);
    }

    @Test
    public void testFindLineBreaks() throws IOException {
        File file = new File("test_file.txt");
        List<String> expectedLines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        int[] actualLineBreaks = fileText.findLineBreaks();
        int[] expectedLineBreaks = new int[expectedLines.size() + 1];
        for (int i = 0; i < expectedLines.size(); ++i) {
            expectedLineBreaks[i] = expectedLines.get(i).length() + i;
        }
        expectedLineBreaks[expectedLines.size()] = expectedLines.get(expectedLines.size() - 1).length() + expectedLines.size();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File("test_file.txt");
        List<String> lines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name(), lines);
        LineColumn expectedLineColumn1 = new LineColumn(1, 1);
        LineColumn expectedLineColumn2 = new LineColumn(2, 15);
        LineColumn expectedLineColumn3 = new LineColumn(3, 1);
        LineColumn expectedLineColumn4 = new LineColumn(4, 21);
        LineColumn actualLineColumn1 = fileText.lineColumn(0);
        LineColumn actualLineColumn2 = fileText.lineColumn(14);
        LineColumn actualLineColumn3 = fileText.lineColumn(2);
        LineColumn actualLineColumn4 = fileText.lineColumn(39);
        assertEquals(expectedLineColumn1, actualLineColumn1);
        assertEquals(expectedLineColumn2, actualLineColumn2);
        assertEquals(expectedLineColumn3, actualLineColumn3);
        assertEquals(expectedLineColumn4, actualLineColumn4);
    }

    @Test
    public void testGet() throws IOException {
        File file = new File("test_file.txt");
        List<String> expectedLines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name(), expectedLines);
        for (int i = 0; i < expectedLines.size(); ++i) {
            assertEquals(expectedLines.get(i), fileText.get(i));
        }
    }

    @Test
    public void testSize() throws IOException {
        File file = new File("test_file.txt");
        List<String> expectedLines = Arrays.asList(
                "This is line 1.",
                "This is line 2.",
                "This is line 3.",
                "This is line 4."
        );
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name(), expectedLines);
        assertEquals(expectedLines.size(), fileText.size());
    }
}