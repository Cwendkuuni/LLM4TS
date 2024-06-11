package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1.", "Line 2.", "Line 3.");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8);
        FileText fileTextFromLines = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertArrayEquals(fileText.toLinesArray(), lines.toArray(new String[0]));

        FileText fileTextCopy = new FileText(fileText);
        assertEquals(fileText, fileTextCopy);

        FileText fileTextFromString = new FileText(file, Arrays.asList(fileText.getFullText().split(System.lineSeparator())));
        assertEquals(fileText, fileTextFromString);
    }

    @Test
    public void testFileTextReading() throws IOException {
        File file = new File("testFile.txt");
        Files.write(file.toPath(), Arrays.asList("Line 1.", "Line 2.", "Line 3."), StandardCharsets.UTF_8);
        FileText fileText = new FileText(file, StandardCharsets.UTF_8);

        assertEquals(file.getAbsolutePath(), fileText.getFile().getAbsolutePath());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertArrayEquals(fileText.toLinesArray(), new String[]{"Line 1.", "Line 2.", "Line 3."});
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextReadingNotFound() throws IOException {
        File file = new File("nonExistentFile.txt");
        new FileText(file, StandardCharsets.UTF_8);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1.", "Line 2.", "Line 3."));

        assertEquals(1, fileText.lineColumn(0).getLineNumber());
        assertEquals(1, fileText.lineColumn(6).getLineNumber());
        assertEquals(1, fileText.lineColumn(12).getColumnNumber());
        assertEquals(3, fileText.lineColumn(16).getLineNumber());
        assertEquals(1, fileText.lineColumn(18).getColumnNumber());

        try {
            fileText.lineColumn(-1);
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            fileText.lineColumn(31);
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}