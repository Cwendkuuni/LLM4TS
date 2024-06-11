package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws Exception {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertEquals("UTF-8", fileText.getCharset().name());
        assertEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());

        FileText clone = new FileText(fileText);
        assertEquals(fileText, clone);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNotFoundFile() throws Exception {
        File file = new File("notFoundFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextFromLines() throws Exception {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testGetLine() {
        FileText fileText = getFileTextFromTestFile("testFile.txt");
        assertEquals("Line 1", fileText.get(0));
    }

    @Test
    public void testSize() {
        FileText fileText = getFileTextFromTestFile("testFile.txt");
        assertEquals(3, fileText.size());
    }

    @Test
    public void testLineColumn() {
        FileText fileText = getFileTextFromTestFile("testFile.txt");
        LineColumn lineColumn = fileText.lineColumn(10);
        assertEquals(2, lineColumn.getLine());
        assertEquals(7, lineColumn.getColumn());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        FileText fileText = getFileTextFromTestFile("testFile.txt");
        fileText.get(5);
    }

    private FileText getFileTextFromTestFile(String fileName) throws IOException {
        File file = new File(fileName);
        return new FileText(file, "UTF-8");
    }

}