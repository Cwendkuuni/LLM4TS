package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws Exception {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
        assertEquals(10, fileText.size());
        assertEquals("This is a test file.", fileText.get(0));
        assertEquals("This is line 2.", fileText.get(1));
        // Add more assertions as needed
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFile() throws Exception {
        String filePath = "src/test/resources/nonexistentfile.txt";
        new FileText(new File(filePath), StandardCharsets.UTF_8.name());
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText1 = new FileText(new File(filePath), StandardCharsets.UTF_8);
        FileText fileText2 = new FileText(fileText1);

        assertEquals(fileText1.size(), fileText2.size());
        for (int i = 0; i < fileText1.size(); i++) {
            assertEquals(fileText1.get(i), fileText2.get(i));
        }
    }

    @Test
    public void testFileTextFromLines() {
        String filePath = "src/test/resources/testFile.txt";
        List<String> lines = Files.readLines(new File(filePath), StandardCharsets.UTF_8);
        FileText fileText = FileText.fromLines(new File(filePath), lines);

        assertEquals(fileText1.size(), fileText.size());
        for (int i = 0; i < fileText.size(); i++) {
            assertEquals(fileText1.get(i), fileText.get(i));
        }
    }

    @Test
    public void testGetFile() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8);
        assertEquals(new File(filePath), fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8);
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8);
        String fullText = fileText.getFullText();
        // Add assertions to check if the fullText is correct
    }

    @Test
    public void testToLinesArray() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8);
        String[] lines = fileText.toLinesArray();
        // Add assertions to check if the lines array is correct
    }

    @Test
    public void testFindLineBreaks() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8);
        int[] lineBreaks = fileText.findLineBreaks();
        // Add assertions to check if the lineBreaks array is correct
    }

    @Test
    public void testLineColumn() {
        String filePath = "src/test/resources/testFile.txt";
        FileText fileText = new FileText(new File(filePath), StandardCharsets.UTF_8);
        LineColumn column = fileText.lineColumn(10);
        // Add assertions to check if the LineColumn object is correct
    }
}