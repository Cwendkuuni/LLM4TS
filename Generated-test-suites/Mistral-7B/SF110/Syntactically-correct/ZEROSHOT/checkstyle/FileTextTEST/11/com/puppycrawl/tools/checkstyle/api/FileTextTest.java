package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithValidFileAndCharset() throws Exception {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        assertEquals("Test content", fileText.get(0));
        assertEquals(3, fileText.size());

        Charset charset = fileText.getCharset();
        assertEquals("UTF-8", charset.name());

        String expectedFileContent = readFile("test.txt", "UTF-8");
        assertEquals(expectedFileContent, fileText.getFullText());
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructionWithUnsupportedCharset() throws Exception {
        File file = new File("test.txt");
        new FileText(file, "XXX");
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, Lists.newArrayList("Test content 1", "Test content 2", "Test content 3"));

        assertEquals("Test content 1", fileText.get(0));
        assertEquals("Test content 3", fileText.get(2));
        assertEquals(3, fileText.size());
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        LineColumn column = fileText.lineColumn(5);
        assertEquals(2, column.getLineNumber());
        assertEquals(11, column.getColumnNumber());
    }

    @Test
    public void testGetLine() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        assertEquals("Test content 1", fileText.get(0));
    }

    @Test
    public void testSize() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");

        assertEquals(3, fileText.size());
    }

    private String readFile(String fileName, String charsetName) throws IOException {
        File file = new File(fileName);
        Charset charset = Charset.forName(charsetName);
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        }

        return fileContent.toString();
    }

    static {
        // JUnit 4 does not support JUnit 5's TestInfo and TestReporter, so we can't use
        // StreamTest.assertLinesMatching() and StreamTest.assertLinesMatching(lineMatcher)
        // from org.junit.jupiter.api.junit4.StreamTest
    }
}