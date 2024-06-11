
package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import static org.junit.Assert.*;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        // Test case for the constructor that takes a File and a charsetName
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        // Test content of the first line
        assertEquals("Expected content", "Actual content", fileText.get(0));

        // Test that the second line is not null
        assertNotNull(fileText.get(1));

        // Test file not found exception
        File fileNotExist = new File("does_not_exist.txt");
        try {
            new FileText(fileNotExist, charsetName);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected exception
        }

        // Test unsupported charset exception
        String charsetNameInvalid = "InvalidCharset";
        try {
            new FileText(file, charsetNameInvalid);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        // Test case for the copy constructor
        File file = new File("test.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);

        FileText fileTextCopy = new FileText(fileText);

        // Test size
        assertEquals(fileText.size(), fileTextCopy.size());

        // Test lines equality
        for (int i = 0; i < fileText.size(); i++) {
            assertEquals(fileText.get(i), fileTextCopy.get(i));
        }

        // Test lineBreaks equality
        assertArrayEquals(fileText.lineBreaks, fileTextCopy.lineBreaks);

        // Test that modifying original instance does not affect copy
        fileText.set(0, "Modified content");
        assertNotEquals(fileText.get(0), fileTextCopy.get(0));
    }

    @Test
    public void testFileTextFromLines() {
        // Test case for the private constructor that takes a File and a List<String>
        File file = new File("test.txt");
        List<String> lines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );
        FileText fileText = FileText.fromLines(file, lines);

        // Test size
        assertEquals(fileText.size(), 3);

        // Test lines equality
        for (int i = 0; i < fileText.size(); i++) {
            assertEquals(fileText.get(i), lines.get(i));
        }

        // Test lineBreaks equality
        assertArrayEquals(new int[] {1, 2, 3}, fileText.lineBreaks);
    }
}