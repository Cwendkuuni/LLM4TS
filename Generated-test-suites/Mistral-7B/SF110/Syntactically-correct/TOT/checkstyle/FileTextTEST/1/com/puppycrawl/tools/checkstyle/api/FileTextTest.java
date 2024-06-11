package com.example.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {
    @Test
    public void testFileTextCreation() throws IOException {
        // given
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        String expectedContent = "Expected content of the file";

        // when
        FileText fileText = new FileText(file, charsetName);

        // then
        assertEquals(expectedContent, fileText.getFullText());
        String[] lines = fileText.toLinesArray();
        for (int i = 0; i < lines.length; i++) {
            assertEquals("Line " + i, lines[i].trim(), expectedContent.split("\n")[i].trim());
        }
    }

    @Test
    public void testLineColumn() {
        // given
        String fileContent = "Line 1: This is the first line.\nLine 2: This is the second line.\nLine 3: This is the third line.";
        FileText fileText = new FileText(new File("test.txt"), fileContent);

        // when
        int position = 30; // inside the second line
        LineColumn lineColumn = fileText.lineColumn(position);

        // then
        assertEquals(2, lineColumn.getLineNumber());
        assertEquals(10, lineColumn.getColumnNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedCharset() throws IOException {
        // given
        File file = new File("test.txt");
        String unsupportedCharsetName = "ABC123";

        // when
        try {
            new FileText(file, unsupportedCharsetName);
            fail("An IllegalStateException should have been thrown");
        } catch (IllegalStateException ex) {
            // then
            assertEquals("Unsupported charset: ABC123", ex.getMessage());
        }
    }
}