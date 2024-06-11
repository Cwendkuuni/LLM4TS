package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFile() throws IOException {
        File file = new File("src/test/resources/testFile.txt");
        Charset charset = StandardCharsets.UTF_8;
        FileText fileText = new FileText(file, charset.name());
        assertEquals(file, fileText.getFile());
        assertArrayEquals(readFileToArray(file, charset), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileAndCharset() throws IOException {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(readFileToArray(file, StandardCharsets.UTF_8), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileTextWithLineBreaks() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines, new int[lines.size() + 1]);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionFromFileTextAndCharset() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines, StandardCharsets.UTF_8);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextClone() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines);
        FileText clonedFileText = new FileText(fileText);
        assertEquals(fileText.getFile(), clonedFileText.getFile());
        assertArrayEquals(fileText.toLinesArray(), clonedFileText.toLinesArray());
    }

    @Test
    public void testFileTextFromLines() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines);
        int[] lineBreaks = fileText.findLineBreaks();
        assertNotNull(lineBreaks);
        assertEquals(lines.size(), lineBreaks.length);
        for (int i = 0; i < lines.size(); i++) {
            int expectedLineBreak = lines.get(i).length() + i * 1024;
            int actualLineBreak = lineBreaks[i];
            assertEquals(expectedLineBreak, actualLineBreak);
        }
    }

    @Test
    public void testGetLineColumn() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines);
        int[] lineBreaks = fileText.findLineBreaks();
        for (int pos = 0; pos < fileText.size() * 1024; pos += 10) {
            LineColumn lineColumn = fileText.lineColumn(pos);
            int expectedLineNo = pos / 1024 + 1;
            int expectedColumnNo = pos % 10;
            assertEquals(expectedLineNo, lineColumn.lineNo);
            assertEquals(expectedColumnNo, lineColumn.columnNo);
        }
        assertNull(fileText.lineColumn(fileText.size() * 1024));
    }

    @Test
    public void testGet() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines);
        for (int i = 0; i < lines.size(); i++) {
            assertEquals(lines.get(i), fileText.get(i));
        }
    }

    @Test
    public void testSize() {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        FileText fileText = new FileText(lines);
        assertEquals(lines.size(), fileText.size());
    }

    private String[] readFileToArray(File file, Charset charset) throws IOException {
        return FileUtils.readLines(file, charset).toArray(new String[0]);
    }

}