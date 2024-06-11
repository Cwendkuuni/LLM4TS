package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.io.CharStreams;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file text", fileText.get(0), readFile("testLine1.txt", "UTF-8").toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        try {
            new FileText(file, "ABCDEFG");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: ABCDEFG", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertEquals(fileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test
    public void testFileTextFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextGetFile() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testFileTextGetCharset() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
    }

    @Test
    public void testFileTextGetFullText() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(readFile("testFile.txt", "UTF-8").toString(), fileText.getFullText().toString());
    }

    @Test
    public void testFileTextToLinesArray() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String[] expectedLines = new String[]{"Line 1", "Line 2", "Line 3"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFileTextFindLineBreaks() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        int[] expectedLineBreaks = new int[]{0, 5, 14, 24, fileText.size()};
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testFileTextLineColumn() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        LineColumn expectedLineColumn = new LineColumn(1, 5);
        LineColumn actualLineColumn = fileText.lineColumn(5);
        assertEquals(expectedLineColumn, actualLineColumn);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFileTextGetOutOfBounds() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        fileText.get(fileText.size());
    }

    private static String readFile(String fileName, String charsetName) throws IOException {
        File file = new File(fileName);
        Charset charset = Charset.forName(charsetName);
        CharStreams.Reader reader = CharStreams.newReader(new FileReader(file), charset);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            CharStreams.copy(reader, stringBuilder);
        } finally {
            reader.close();
        }
        return stringBuilder.toString();
    }
}