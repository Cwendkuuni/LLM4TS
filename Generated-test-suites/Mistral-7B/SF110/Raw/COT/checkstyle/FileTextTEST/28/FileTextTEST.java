package com.example.junit4;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

    private File file;
    private String charsetName;
    private FileText fileText;

    @Before
    public void setUp() throws IOException {
        file = new File("path/to/your/file.txt");
        charsetName = "UTF-8";
    }

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        fileText = new FileText(file, charsetName);
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(fileText.size(), getNumberOfLinesFromFile(file));
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFileAndCharset() throws IOException {
        fileText = new FileText(new File("non-existent-file.txt"), charsetName);
    }

    @Test
    public void testFileTextConstructionWithFileTextObject() {
        List<String> lines = Lists.newArrayList(
            "Line 1", "Line 2", "Line 3");
        fileText = new FileText(FileText.fromLines(new File("path/to/your/file.txt"), lines));
        assertArrayEquals(fileText.toLinesArray(), lines.toArray(new String[lines.size()]));
        assertNull(fileText.getCharset());
        assertNull(fileText.getFile());
    }

    @Test
    public void testGetFile() {
        assertEquals(fileText.getFile(), file);
    }

    @Test
    public void testGetCharset() {
        assertNotNull(fileText.getCharset());
        assertEquals(fileText.getCharset().name(), charsetName);
    }

    @Test
    public void testGetFullText() {
        String expectedFullText = readFileAsString(file, StandardCharsets.UTF_8);
        assertEquals(expectedFullText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        String[] expectedLines = getNumberOfLinesFromFile(file).split("\n");
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        int[] lineBreakPositions = fileText.findLineBreaks();
        int lastLineBreakPosition = lineBreakPositions[lineBreakPositions.length - 1];
        assertEquals(lastLineBreakPosition, fileText.getFullText().length());
    }

    @Test
    public void testLineColumn() {
        int pos = 15;
        LineColumn expectedLineColumn = new LineColumn(2, 3);
        LineColumn actualLineColumn = fileText.lineColumn(pos);
        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBound() {
        int indexOutOfBound = size() + 1;
        String result = fileText.get(indexOutOfBound);
    }

    private int getNumberOfLinesFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int numberOfLines = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            numberOfLines++;
        }
        reader.close();
        return numberOfLines;
    }

    private String readFileAsString(File file, Charset charset) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charset);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}