package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        Charset charset = StandardCharsets.UTF_8;
        List<String> expectedLines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = new FileText(file, charset.toString());

        CharSequence fullText = fileText.getFullText();
        String[] lines = fileText.toLinesArray();
        String[] expectedLinesArray = expectedLines.toArray(new String[0]);

        assertSame("The files should be the same", file, fileText.getFile());
        assertSame("The charsets should be the same", charset, fileText.getCharset());
        assertEquals("The full text should match the content of the file", expectedLines.get(0), fullText.subSequence(0, expectedLines.get(0).length()));
        assertArrayEquals("The lines should match the expected lines", expectedLinesArray, lines);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithInvalidFile() throws IOException {
        File file = new File("invalidFile.txt");
        new FileText(file, StandardCharsets.UTF_8.toString());
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText1 = new FileText(file, StandardCharsets.UTF_8.toString());
        FileText fileText2 = new FileText(fileText1);

        assertEquals("The files should be the same", file, fileText1.getFile());
        assertEquals("The files should be the same", file, fileText2.getFile());
        assertArrayEquals("The lines should be the same", lines.toArray(new String[0]), fileText1.toLinesArray());
        assertArrayEquals("The lines should be the same", lines.toArray(new String[0]), fileText2.toLinesArray());
    }

    @Test
    public void testFileTextConstructionWithLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = FileText.fromLines(file, lines);

        CharSequence fullText = fileText.getFullText();
        String[] linesArray = fileText.toLinesArray();

        assertEquals("The files should be the same", file, fileText.getFile());
        assertArrayEquals("The lines should match the provided lines", lines.toArray(new String[0]), linesArray);
        assertEquals("The full text should match the content of the lines", expectedLines.get(0), fullText.subSequence(0, expectedLines.get(0).length()));
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.toString());
        int[] lineBreaks = fileText.findLineBreaks();

        int expectedLineBreaksLength = 5;
        assertArrayEquals("The number of line breaks should match the number of lines plus one", new int[]{0, 6}, lineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.toString());
        LineColumn lineColumn = fileText.lineColumn(10);

        int expectedLineNumber = 1;
        int expectedColumnNumber = 1;
        assertEquals("The line number should be 1", expectedLineNumber, lineColumn.lineNumber);
        assertEquals("The column number should be 1", expectedColumnNumber, lineColumn.columnNumber);
    }

    @Test
    public void testGet() {
        FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.toString());
        String expectedLine1 = "Line 1";

        String line1 = fileText.get(0);

        assertEquals("The first line should match the expected line", expectedLine1, line1);
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.toString());
        int expectedSize = 3;

        int size = fileText.size();

        assertEquals("The size should match the number of lines", expectedSize, size);
    }
}