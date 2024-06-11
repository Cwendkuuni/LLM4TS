package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.io.Charsets;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(fileText.toLinesArray(), readFileToArray("testFile.txt"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithInvalidFile() throws IOException {
        File file = new File("nonExistentFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertEquals(fileText, copiedFileText);
    }

    @Test
    public void testFileTextFromLines() {
        String fileContent = "Line 1\nLine 2\nLine 3";
        List<String> lines = Arrays.asList(fileContent.split("\n"));
        FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);
        assertEquals(fileText.size(), 3);
        assertEquals(fileText.get(0), "Line 1");
        assertEquals(fileText.get(1), "Line 2");
        assertEquals(fileText.get(2), "Line 3");
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int[] lineBreakPositions = fileText.findLineBreaks();
        assertEquals(lineBreakPositions.length, fileText.size() + 1);
        int lastLineBreakPosition = lineBreakPositions[lineBreakPositions.length - 1];
        assertEquals(lastLineBreakPosition, fileText.getFullText().length());
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int pos = fileText.getFullText().indexOf("Line 2");
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(lineColumn.getLineNo(), 2);
        assertEquals(lineColumn.getColumnNo(), fileText.getFullText().valueAt(pos).length());
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        assertEquals(fileText.size(), readFileToLines("testFile.txt").size());
    }

    private String[] readFileToArray(String filePath) throws IOException {
        File file = new File(filePath);
        FileText fileText = new FileText(file, "UTF-8");
        return fileText.toLinesArray();
    }

    private List<String> readFileToLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        BufferedReader reader = Files.newBufferedReader(file.toPath(), Charsets.UTF_8);
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }
}