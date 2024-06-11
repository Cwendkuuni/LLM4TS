package com.example.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithValidFileAndCharset() throws Exception {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTextConstructionWithInvalidFile() throws Exception {
        File file = new File("test.abcd");
        Charset charset = Charset.forName("UTF-8");
        new FileText(file, charset.getName());
    }

    @Test
    public void testFileTextConstructionWithValidFileAndLinesList() {
        File file = new File("test.txt");
        List<String> lines = Lists.newArrayList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testGetLine() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        int lineNo = 1;
        int columnNo = 5;
        LineColumn lineColumn = fileText.lineColumn(lineNo * 10 + columnNo);
        assertEquals(lineNo, lineColumn.getLineNumber());
        assertEquals(columnNo, lineColumn.getColumnNumber());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        int index = fileText.size() + 1;
        fileText.get(index);
    }

    @Test
    public void testClone() {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        FileText fileText = new FileText(file, charset.getName());
        FileText clone = fileText.clone();
        assertNotSame(fileText, clone);
        assertEquals(fileText.getFile(), clone.getFile());
        assertEquals(fileText.getCharset(), clone.getCharset());
        assertArrayEquals(fileText.toLinesArray(), clone.toLinesArray());
    }

    @Test
    public void testFromLines() {
        File file = new File("test.txt");
        List<String> lines = Lists.newArrayList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = FileText.fromLines(file, lines);
        FileText clone = fileText.clone();
        assertNotSame(fileText, clone);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testReadFile() throws Exception {
        File file = new File("test.txt");
        Charset charset = Charset.forName("UTF-8");
        String content = "Line 1\nLine 2\nLine 3";
        String actualContent = FileText.readFile(file, charset.newDecoder());
        assertEquals(content, actualContent);
    }
}