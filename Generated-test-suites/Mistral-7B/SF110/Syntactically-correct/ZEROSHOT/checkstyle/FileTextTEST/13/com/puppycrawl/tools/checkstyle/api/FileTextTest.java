package com.example.junit4;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextCreation() throws IOException {
        File file = new File("testFile.txt");
        String content = "Line 1\nLine 2\nLine 3";
        write(file, content, Charsets.UTF_8);

        FileText fileText = new FileText(file, "UTF-8");

        assertEquals(file, fileText.getFile());
        assertEquals("UTF-8", fileText.getCharset().name());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFileTextCreationFromLines() throws IOException {
        File file = new File("testFile.txt");
        String content = "Line 1\nLine 2\nLine 3";
        write(file, content, Charsets.UTF_8);

        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFileTextCreationFromFileText() {
        File file = new File("testFile.txt");
        String content = "Line 1\nLine 2\nLine 3";
        write(file, content, Charsets.UTF_8);

        FileText fileText = new FileText(new FileText(file, "UTF-8"));

        assertEquals(file, fileText.getFile());
        assertEquals("UTF-8", fileText.getCharset().name());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText("testFileContent.txt".getBytes(), 0, new String[]{"Line 1\nLine 2\nLine 3"});
        int[] lineBreaks = fileText.findLineBreaks();

        int[] expectedLineBreaks = {0, 5, 12, 15, fileText.size()};
        assertArrayEquals(expectedLineBreaks, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText("testFileContent.txt".getBytes(), 0, new String[]{"Line 1\nLine 2\nLine 3"});

        LineColumn lineColumn1 = fileText.lineColumn(7);
        LineColumn lineColumn2 = fileText.lineColumn(12);

        assertEquals(1, lineColumn1.getLineNumber());
        assertEquals(7, lineColumn1.getColumnNumber());

        assertEquals(2, lineColumn2.getLineNumber());
        assertEquals(1, lineColumn2.getColumnNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharset() {
        File file = new File("testFile.txt");
        new FileText(file, "invalidCharset");
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() throws IOException {
        new FileText(new File("testFileNotExists.txt"), "UTF-8");
    }

    private void write(File file, String content, Charset charset) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset))) {
            writer.write(content);
        }
    }
}