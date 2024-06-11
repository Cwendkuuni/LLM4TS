
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    // Existing tests from previous experts

    @Test
    public void testFileTextFromLines() {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("This is a test file", "", "// This is a comment");
        FileText fileText = FileText.fromLines(file, lines);

        String expectedContent = "This is a test file."; // Replace with the actual content of the testFile.txt
        assertEquals(expectedContent, fileText.getFullText());

        String[] expectedLines = new String[]{"This is a test file.", "", "// This is a comment"}; // Add lines from the file if any
        assert ArrayEquals(expectedLines, fileText.toLinesArray());

        int[] expectedLineBreaks = new int[]{6, 13, 14, 23, 24, 30}; // Line breaks positions from the file
        int[] actualLineBreaks = fileText.findLineBreaks();

        assert ArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testFileTextWithInvalidCharset() {
        File file = new File("testFile.txt");
        String charsetName = "InvalidCharset";
        try {
            new FileText(file, charsetName);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Unsupported charset: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testFileTextWithNullFile() {
        String charsetName = "UTF-8";
        try {
            new FileText(null, charsetName);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
