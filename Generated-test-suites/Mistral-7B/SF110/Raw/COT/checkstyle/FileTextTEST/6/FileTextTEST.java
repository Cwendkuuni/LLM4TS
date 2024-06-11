package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.puppycrawl.tools.checkstyle.api.FileText;

@RunWith(JUnit4.class)
public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(22, fileText.size()); // Approximate number of lines in the file
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithInvalidFile() throws IOException {
        File file = new File("invalidfile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextConstructorWithFileAndLines() {
        File file = new File("src/test/resources/FileTextTest.java");
        List<String> lines = Files.readLines(file.toPath(), Charsets.UTF_8);
        FileText fileText = new FileText(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
        assertEquals(lines.size(), fileText.size());
    }

    @Test
    public void testFromLinesConstructorWithFileAndLines() {
        File file = new File("src/test/resources/FileTextTest.java");
        List<String> lines = Files.readLines(file.toPath(), Charsets.UTF_8);
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
        assertEquals(lines.size(), fileText.size());
    }

    @Test
    public void testGetFile() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        String[] expectedLines = fileText.toLinesArray();
        String[] actualLines = Files.readAllLines(file.toPath(), Charsets.UTF_8);
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        int[] lineBreakPositions = fileText.findLineBreaks();
        int lastIndex = lineBreakPositions[lineBreakPositions.length - 1];
        assertEquals(lastIndex, fileText.getFullText().length());

        int lineNo = 1;
        for (int i = 0; i < lineBreakPositions.length - 1; i++) {
            int lineBreakPosition = lineBreakPositions[i];
            assertEquals(lineNo++, getLineNumber(fileText.getFullText(), lineBreakPosition));
        }
    }

    @Test
    public void testLineColumn() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        int pos = 30;
        LineColumn lineColumn = fileText.lineColumn(pos);
        int expectedLineNo = getLineNumber(fileText.getFullText(), pos) + 1;
        int expectedColumnNo = pos - getLineStartPosition(fileText.getFullText(), pos);
        assertEquals(expectedLineNo, lineColumn.getLineNumber());
        assertEquals(expectedColumnNo, lineColumn.getColumnNumber());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        int size = fileText.size();
        int index = size + 1;
        fileText.get(index);
    }

    private int getLineNumber(CharSequence cs, int pos) {
        int lineNo = 1;
        int lineStart = 0;
        int lineEnd = 0;
        while ((lineEnd = cs.indexOf("\n", lineStart)) > 0) {
            if (pos >= lineStart && pos < lineEnd) {
                return lineNo;
            }
            lineStart = lineEnd + 1;
            lineNo++;
        }
        return lineNo;
    }

    private int getLineStartPosition(CharSequence cs, int pos) {
        int lineStart = 0;
        int lineEnd = 0;
        while ((lineEnd = cs.indexOf("\n", lineStart)) > 0) {
            if (pos >= lineStart && pos < lineEnd) {
                return pos - lineStart;
            }
            lineStart = lineEnd + 1;
        }
        return pos;
    }

}