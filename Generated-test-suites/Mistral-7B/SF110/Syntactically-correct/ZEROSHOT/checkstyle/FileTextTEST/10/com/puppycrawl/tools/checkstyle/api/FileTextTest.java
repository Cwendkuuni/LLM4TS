package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Charsets;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    private File file;
    private FileText fileText;
    private Charset charset;

    @Before
    public void setUp() throws IOException {
        file = new File("test.txt");
        charset = Charsets.UTF_8;
        fileText = new FileText(file, charset.name());
    }

    @Test
    public void testFileTextConstructionFromFile() throws IOException {
        // Test constructor that takes a File and charset name
        assertEquals(file, fileText.getFile());
        assertEquals(charset, fileText.getCharset());
        assertEquals(readFileContent(file, charset), fileText.getFullText());
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        // Test constructor that takes a FileText
        FileText fileTextFromFileText = new FileText(fileText);
        assertEquals(file, fileTextFromFileText.getFile());
        assertEquals(charset, fileTextFromFileText.getCharset());
        assertEquals(fileText.getFullText(), fileTextFromFileText.getFullText());
        assertArrayEquals(fileText.lines, fileTextFromFileText.lines);
        assertNull(fileTextFromFileText.lineBreaks);
    }

    @Test
    public void testFileTextConstructionFromLines() {
        // Test constructor that takes a File and List<String>
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileTextFromLines = FileText.fromLines(file, lines);
        assertEquals(file, fileTextFromLines.getFile());
        assertNull(fileTextFromLines.getCharset());
        assertEquals(lines.toString(), fileTextFromLines.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileTextFromLines.toLinesArray());
        assertNull(fileTextFromLines.lineBreaks);
    }

    @Test
    public void testFindLineBreaks() {
        int[] lineBreaks = fileText.findLineBreaks();
        int lastLineBreakPosition = 0;
        for (int i = 0; i < fileText.size(); i++) {
            int lineBreakPosition = lineBreaks[i];
            assertEquals(lastLineBreakPosition + fileText.get(i).length() + 1, lineBreakPosition);
            lastLineBreakPosition = lineBreakPosition;
        }
    }

    @Test
    public void testLineColumn() {
        int[] lineBreaks = fileText.findLineBreaks();
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(1, lineColumn.getLineNumber());
        assertEquals(6, lineColumn.getColumnNumber());
        lineColumn = fileText.lineColumn(lineBreaks[1] + 1);
        assertEquals(2, lineColumn.getLineNumber());
        assertEquals(1, lineColumn.getColumnNumber());
    }

    @Test
    public void testGetLine() {
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, fileText.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() throws IOException {
        File nonExistentFile = new File("non-existent-file.txt");
        assertThrows(FileNotFoundException.class, () -> new FileText(nonExistentFile, charset.name()));
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testUnsupportedCharsetException() {
        assertThrows(UnsupportedCharsetException.class, () -> new FileText(file, "ABC-123"));
    }

    private static String readFileContent(File file, Charset charset) throws IOException {
        String content = "";
        try (Reader reader = new InputStreamReader(new FileInputStream(file), charset)) {
            StringBuilder buffer = new StringBuilder();
            int cp;
            while ((cp = reader.read()) != -1) {
                buffer.append((char) cp);
            }
            content = buffer.toString();
        }
        return content;
    }
}