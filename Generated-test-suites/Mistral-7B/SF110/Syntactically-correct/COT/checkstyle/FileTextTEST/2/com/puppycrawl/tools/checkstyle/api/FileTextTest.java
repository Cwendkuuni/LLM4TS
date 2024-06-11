package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(fileText.size(), getNumberOfLines(textFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTextConstructorWithInvalidCharset() throws IOException {
        File textFile = new File("path/to/your/file.txt");
        new FileText(textFile, "invalid_charset");
    }

    @Test
    public void testFileTextCopyConstructor() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileTextOrigin = new FileText(textFile, "UTF-8");
        FileText fileTextCopy = new FileText(fileTextOrigin);
        assertEquals(fileTextOrigin.getFile(), fileTextCopy.getFile());
        assertEquals(fileTextOrigin.getCharset(), fileTextCopy.getCharset());
        assertArrayEquals(fileTextOrigin.lines, fileTextCopy.lines);
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File textFile = new File("path/to/your/file.txt");
        List<String> lines = Lists.newArrayList(Files.readLines(textFile, Charsets.UTF_8));
        FileText fileText = FileText.fromLines(textFile, lines);
        assertEquals(fileText.getFile(), textFile);
        assertArrayEquals(fileText.lines, lines.toArray(new String[lines.size()]));
    }

    @Test
    public void testGetFile() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        assertEquals(fileText.getFile(), textFile);
    }

    @Test
    public void testGetCharset() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        assertEquals(fileText.getCharset(), Charset.forName("UTF-8"));
    }

    @Test
    public void testGetFullText() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        String expectedFullText = Files.toString(textFile, Charsets.UTF_8);
        assertEquals(fileText.getFullText(), expectedFullText);
    }

    @Test
    public void testToLinesArray() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        String[] expectedLines = Files.readAllLines(textFile, Charsets.UTF_8);
        assertArrayEquals(fileText.toLinesArray(), expectedLines);
    }

    @Test
    public void testFindLineBreaks() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        int[] expectedLineBreaks = findExpectedLineBreaks(textFile);
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        int[] expectedLineBreaks = findExpectedLineBreaks(textFile);
        LineColumn expectedLineColumn = lineColumnFor(expectedLineBreaks, 10);
        LineColumn actualLineColumn = fileText.lineColumn(10);
        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        File textFile = new File("path/to/your/file.txt");
        FileText fileText = new FileText(textFile, "UTF-8");
        int index = fileText.size();
        String expectedErrorMessage = "String index out of range: " + index;
        String actualErrorMessage = null;
        try {
            fileText.get(index);
        } catch (IndexOutOfBoundsException e) {
            actualErrorMessage = e.getMessage();
        }
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    private int getNumberOfLines(File textFile) {
        try {
            return (int) Files.size(textFile, Charsets.UTF_8) / (int) (System.lineSeparator().getBytes(Charsets.UTF_8).length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] findExpectedLineBreaks(File textFile) {
        List<Integer> lineBreakPositions = Lists.newArrayList();
        try (LineNumberReader reader = new LineNumberReader(new FileReader(textFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineBreakPositions.add(reader.getLineNumber() * (int) (System.lineSeparator().getBytes(Charsets.UTF_8).length) + line.length());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineBreakPositions.stream().mapToInt(i -> i).toArray();
    }

    private LineColumn lineColumnFor(int[] lineBreaks, int position) {
        int lineNo = Arrays.binarySearch(lineBreaks, position);
        if (lineNo < 0) {
            lineNo = -lineNo - 2;
        }
        return new LineColumn(lineNo + 1, position - lineBreaks[lineNo]);
    }

}