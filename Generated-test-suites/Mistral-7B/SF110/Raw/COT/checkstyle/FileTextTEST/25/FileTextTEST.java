package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.io.CharStreams;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

@RunWith(Parameterized.class)
public class FileTextTest {

    private File file;
    private String charsetName;
    private FileText fileText;

    public FileTextTest(File file, String charsetName) {
        this.file = file;
        this.charsetName = charsetName;
    }

    @Parameters
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[] { new File("testFile.txt"), "UTF-8" });
        data.add(new Object[] { new File("doesNotExist.txt"), "UTF-16" });
        data.add(new Object[] { new File("testFile.txt"), "ISO-8859-1" });
        data.add(new Object[] { new File("testFile.txt"), "UnknownCharset" });
        return data;
    }

    @Test
    public void testFileTextConstruction() throws IOException {
        fileText = new FileText(file, charsetName);
        assertTrue(fileText instanceof FileText);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() throws IOException {
        new FileText(new File("doesNotExist.txt"), charsetName);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedCharsetException() throws IOException {
        new FileText(new File("testFile.txt"), "UnknownCharset");
    }

    @Test
    public void testGetFile() {
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String fullText = fileText.getFullText();
        assertNotNull(fullText);
        assertFalse(fullText.isEmpty());
    }

    @Test
    public void testToLinesArray() {
        String[] lines = fileText.toLinesArray();
        assertNotNull(lines);
        assertFalse(Arrays.isEmpty(lines));
    }

    @Test
    public void testLineColumn() {
        LineColumn lineColumn = fileText.lineColumn(10);
        assertNotNull(lineColumn);
        assertFalse(lineColumn.getColumnNo() <= 0);
    }

    @Test
    public void testGet() {
        String expectedLine = "This is a test line.";
        String actualLine = fileText.get(0);
        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testSize() {
        int expectedSize = 5;
        int actualSize = fileText.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testFindLineBreaks() {
        int[] lineBreakPositions = fileText.findLineBreaks();
        assertTrue(lineBreakPositions.length > 0);
    }

    @Test
    public void testLineColumnWithValidPosition() {
        LineColumn lineColumn = fileText.lineColumn(15);
        int lineNo = lineColumn.getLineNumber();
        int columnNo = lineColumn.getColumnNumber();
        String fileTextContent = CharStreams.toString(fileText.getCharset().newDecoder().wrap(fileText.getFullText().getBytes()));
        String expectedLine = fileTextContent.substring(lineBreakPositions[lineNo], lineBreakPositions[lineNo + 1]);
        int columnPosition = lineBreakPositions[lineNo] + expectedLine.lastIndexOf('\n') + 1;
        int expectedColumnNo = columnPosition - lineBreakPositions[lineNo];
        assertEquals(lineNo + 1, lineColumn.getLineNumber());
        assertEquals(columnNo, lineColumn.getColumnNumber());
        assertEquals(expectedLine, fileText.get(lineNo));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnWithInvalidPosition() {
        fileText.lineColumn(-1);
    }

    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("This is a test line 1.", "This is a test line 2.", "This is a test line 3.");
        FileText fileTextFromLines = FileText.fromLines(file, lines);
        String[] linesArray = fileTextFromLines.toLinesArray();
        assertArrayEquals(lines.toArray(new String[0]), linesArray);
    }

    @Test
    public void testStaticReadFileMethod() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        StringBuilder expectedFullText = new StringBuilder();
        try (Reader reader = new FileReader(file)) {
            expectedFullText.append(CharStreams.read(reader, charset));
        }
        String actualFullText = FileText.readFile(file, charset.newDecoder());
        assertEquals(expectedFullText.toString(), actualFullText);
    }
}