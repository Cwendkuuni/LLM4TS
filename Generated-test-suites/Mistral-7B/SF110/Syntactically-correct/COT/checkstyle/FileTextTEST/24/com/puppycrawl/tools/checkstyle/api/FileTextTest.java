package com.example.junit;

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
    public void testFileTextConstructorWithValidFileAndCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(fileText.size(), 10); // Assuming the file contains 10 lines
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithInvalidCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "ABCDE");
    }

    @Test
    public void testFileTextConstructorWithFileTextObject() {
        FileText fileText = new FileText("Hello World".split("\n"));
        File file = new File("test.txt");
        FileText fileText2 = new FileText(fileText);
        assertEquals(fileText, fileText2);
    }

    @Test
    public void testFromLinesConstructor() {
        List<String> lines = Lists.newArrayList("Line1", "Line2", "Line3");
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(fileText.size(), 3);
    }

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(fileText.getFile(), file);
    }

    @Test
    public void testGetCharset() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(fileText.getCharset(), Charset.forName("UTF-8"));
    }

    @Test
    public void testGetFullText() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String expectedFullText = "Line1\nLine2\nLine3\nLine4\nLine5\nLine6\nLine7\nLine8\nLine9\nLine10";
        assertEquals(fileText.getFullText(), expectedFullText);
    }

    @Test
    public void testToLinesArray() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String[] expectedLines = {"Line1", "Line2", "Line3", "Line4", "Line5", "Line6", "Line7", "Line8", "Line9", "Line10"};
        assertArrayEquals(expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaksAndLineColumn() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        int[] lineBreaks = fileText.findLineBreaks();
        LineColumn lineColumn = fileText.lineColumn(20);

        assertEquals(lineBreaks.length, 11);
        assertEquals(lineBreaks[2], 22); // Assuming the second line break is at position 22
        assertEquals(lineColumn.lineNo, 2);
        assertEquals(lineColumn.columnNo, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFindLineColumnWithInvalidIndex() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        fileText.lineColumn(100);
    }

    @Test
    public void testGetAtInvalidIndex() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String expected = "Line11"; // Assuming there is a line 11 in the file
        try {
            assertEquals(fileText.get(10), expected);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}