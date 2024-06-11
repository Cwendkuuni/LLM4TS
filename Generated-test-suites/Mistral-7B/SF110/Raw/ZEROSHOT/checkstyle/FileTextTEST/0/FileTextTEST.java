package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFile() throws Exception {
        File file = new File("testFile.txt");
        String fileContent = "Line 1\nLine 2\nLine 3";
        Files.write(file.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());

        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        String[] lines = fileText.toLinesArray();
        assertEquals(3, lines.length);
        assertEquals("Line 1", lines[0]);
        assertEquals("Line 2", lines[1]);
        assertEquals("Line 3", lines[2]);

        Files.delete(file.toPath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonexistentFile() throws Exception {
        new FileText(new File("nonexistentFile.txt"), StandardCharsets.UTF_8.name());
    }

    @Test
    public void testFileTextConstructionFromLines() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);

        assertEquals("testFile.txt", fileText.getFile().getName());
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
        fileText.add("Line 4");
        fileText.add("Line 5");

        LineColumn lineColumn1 = fileText.lineColumn(10);
        LineColumn lineColumn2 = fileText.lineColumn(20);
        LineColumn lineColumn3 = fileText.lineColumn(35);

        assertEquals(2, lineColumn1.getLineNumber());
        assertEquals(7, lineColumn1.getColumnNumber());
        assertEquals(3, lineColumn2.getLineNumber());
        assertEquals(5, lineColumn2.getColumnNumber());
        assertEquals(-1, lineColumn3.getLineNumber());
        assertEquals(-1, lineColumn3.getColumnNumber());
    }

    @Test
    public void testFindLineBreaks() {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name(),
                Arrays.asList(fileContent.split("\\r?\\n")));

        int[] lineBreakPositions = fileText.findLineBreaks();

        assertEquals(5, lineBreakPositions.length);
        assertEquals(0, lineBreakPositions[0]);
        assertEquals(fileContent.length(), lineBreakPositions[4]);
    }
}