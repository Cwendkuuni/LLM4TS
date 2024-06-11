package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        assertEquals(file.getAbsolutePath(), fileText.getFile().getAbsolutePath());
        assertEquals(charset, fileText.getCharset());
        String fullText = fileText.getFullText();
        // Add more assertions to check the contents of 'fullText' and 'lines'
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFileAndCharset() throws IOException {
        File file = new File("non-existent-file.txt");
        Charset charset = Charset.forName("UTF-8");
        new FileText(file, charset.getName());
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText originalFileText = new FileText(file, charset.getName());
        FileText copyFileText = new FileText(originalFileText);
        assertEquals(originalFileText.getFile(), copyFileText.getFile());
        assertEquals(originalFileText.getCharset(), copyFileText.getCharset());
        // Add more assertions to check the equality of 'originalFileText' and 'copyFileText'
    }

    @Test
    public void testFileTextConstructionWithFileAndLines() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals(file.getAbsolutePath(), fileText.getFile().getAbsolutePath());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
        // Add more assertions to check the contents of 'fullText'
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("test.txt");
        List<String> lines = Arrays.asList("Line 1\n", "Line 2\nLine 3", "Line 4");
        FileText fileText = new FileText(file, lines);
        int[] lineBreaks = fileText.findLineBreaks();
        int lineNo = 0;
        for (int i = 0; i < lineBreaks.length; ++i) {
            int pos = lineBreaks[i];
            LineColumn lineColumn = fileText.lineColumn(pos);
            assertEquals(lineNo + 1, lineColumn.getLineNumber());
            assertEquals(pos - lineColumn.getColumnNumber() + 1, lineColumn.getColumnNumber());
            ++lineNo;
        }
    }

    @Test
    public void testLineColumn() {
        FileText fileText = fromLines("test.txt", Arrays.asList("Line 1", "Line 2", "Line 3"));
        int pos = 10;
        LineColumn lineColumn = fileText.lineColumn(pos);
        fail("Expected FileNotFoundException");
    }

    private static FileText fromLines(String fileName, List<String> lines) {
        File file = new File(fileName);
        return FileText.fromLines(file, lines);
    }
}