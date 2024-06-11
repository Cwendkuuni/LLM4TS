package com.example.junit;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @org.junit.Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file text", "Actual file text", fileText.getFullText());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("testFile.txt");
        new FileText(file, "X-Unknown-Charset");
    }

    @org.junit.Test
    public void testFileTextCopyConstructor() {
        File file = new File("testFile.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copyFileText = new FileText(originalFileText);
        assertEquals("Original and copy files should be equal", originalFileText.getFullText(), copyFileText.getFullText());
    }

    @org.junit.Test
    public void testFileTextConstructorWithFileAndList() {
        File file = new File("testFile.txt");
        List<String> lines = Lists.newArrayList("Test line 1", "Test line 2");
        FileText fileText = new FileText(file, lines);
        assertEquals("Expected file text", "Test line 1\nTest line 2", fileText.getFullText());
        assertArrayEquals("Expected lines array", new String[]{"Test line 1", "Test line 2"}, fileText.toLinesArray());
    }

    @org.junit.Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line1\nLine2\nLine3"));
        int[] lineBreakPositions = fileText.findLineBreaks();
        assertEquals("Expected array size", 3, lineBreakPositions.length);
        assertEquals("Expected line break position for line 1", 7, lineBreakPositions[1]);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testLineColumnWithNullList() {
        new FileText(null, null).lineColumn(0);
    }

    @org.junit.Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line1\nLine2\nLine3"));
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals("Expected line number", 2, lineColumn.getLineNumber());
        assertEquals("Expected column number", 1, lineColumn.getColumnNumber());
    }

    @org.junit.Test
    public void testGet() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line1\nLine2\nLine3"));
        assertEquals("Expected line content", "Line2", fileText.get(1));
    }

    @org.junit.Test
    public void testSize() {
        FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line1\nLine2\nLine3"));
        assertEquals("Expected file size", 3, fileText.size());
    }
}