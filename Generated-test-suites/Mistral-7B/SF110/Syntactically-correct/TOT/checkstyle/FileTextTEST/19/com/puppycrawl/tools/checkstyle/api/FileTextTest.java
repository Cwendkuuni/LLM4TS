package com.puppycrawl.tools.checkstyle.api;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        String filePath = "test.txt";
        File file = new File(filePath);
        String charsetName = "UTF-8";

        FileText fileText = new FileText(file, charsetName);
        String expectedFileContent = "This is a test file.";
        String actualFileContent = fileText.getFullText();

        assertEquals(expectedFileContent, actualFileContent);
    }

    @Test
    public void testFileTextCopyConstructor() {
        String filePath = "test.txt";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        FileText originalFileText = new FileText(file, charsetName);

        FileText copiedFileText = new FileText(originalFileText);
        String originalContent = originalFileText.getFullText();
        String copiedContent = copiedFileText.getFullText();

        assertEquals(originalContent, copiedContent);
        assertArrayEquals(originalFileText.lines, copiedFileText.lines);
    }

    @Test
    public void testFileTextPrivateConstructor() {
        String filePath = "test.txt";
        File file = new File(filePath);
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");

        FileText fileText = new FileText(file, lines);
        String expectedContent = "Line 1\nLine 2\nLine 3";
        String actualContent = fileText.getFullText();

        assertEquals(expectedContent, actualContent);
        assertArrayEquals(lines.toArray(), fileText.lines);
    }

    @BeforeAll
    public void setup() throws IOException {
        // Set up anything that needs to be set up before running the tests
    }

    @AfterAll
    public void teardown() {
        // Clean up anything that was set up before running the tests
    }
}