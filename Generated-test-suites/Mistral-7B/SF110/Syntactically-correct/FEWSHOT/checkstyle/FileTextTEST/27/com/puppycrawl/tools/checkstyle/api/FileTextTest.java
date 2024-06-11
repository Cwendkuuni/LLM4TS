package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8);
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());

        FileText fileTextFromLines = FileText.fromLines(file, lines);
        assertEquals(fileText, fileTextFromLines);
    }

    @Test
    public void testFindLineBreaks() {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        FileText fileText = new FileText(new File("testFile.txt"), fileContent.getBytes(StandardCharsets.UTF_8));
        int[] lineBreaks = fileText.findLineBreaks();

        int[] expectedLineBreakPositions = new int[]{0, 7, 14, 19, 22};
        assertArrayEquals(expectedLineBreakPositions, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        String fileContent = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(new File("testFile.txt"), fileContent.getBytes(StandardCharsets.UTF_8));

        LineColumn lineColumn1 = fileText.lineColumn(1);
        LineColumn lineColumn2 = fileText.lineColumn(4);
        LineColumn lineColumn3 = fileText.lineColumn(11);

        assertEquals(1, lineColumn1.lineNo);
        assertEquals(1, lineColumn1.columnNo);
        assertEquals(4, lineColumn2.lineNo);
        assertEquals(3, lineColumn2.columnNo);
        assertNull(lineColumn3);
    }

    @Test
    public void testGet() {
        String fileContent = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(new File("testFile.txt"), fileContent.getBytes(StandardCharsets.UTF_8));

        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        String fileContent = "Line 1\nLine 2\nLine 3";
        FileText fileText = new FileText(new File("testFile.txt"), fileContent.getBytes(StandardCharsets.UTF_8));

        assertEquals(3, fileText.size());
    }
}