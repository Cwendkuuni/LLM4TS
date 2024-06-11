package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        // ...
    }

    // Test file not found
    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorFileNotFound() throws IOException {
        // ...
    }

    // Test unsupported charset
    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorUnsupportedCharset() throws IOException {
        // ...
    }

    // Test empty lines
    @Test
    public void testFileTextFromEmptyLines() {
        FileText fileText = FileText.fromLines(new File("testEmptyLines.txt"), new ArrayList<>());
        assertNull(fileText.getCharset());
        assertArrayEquals(new String[]{}, fileText.toLinesArray());
        assertEquals(0, fileText.size());
    }

    // Test null lines
    @Test
    public void testFileTextFromNullLines() {
        assertThrows(NullPointerException.class, () -> new FileText(new File("testNullLines.txt"), null));
    }

    // Test invalid line number
    @Test
    public void testGetInvalidLineNumber() {
        FileText fileText = new FileText(new File("testLines.txt"), "UTF-8");
        assertThrows(IndexOutOfBoundsException.class, () -> fileText.get(-1));
    }

    // Test findLineBreaks() with empty List<String>
    @Test
    public void testFindLineBreaksEmpty() {
        FileText fileText = new FileText(new File("testLines.txt"), "UTF-8");
        FileText emptyFileText = new FileText(new ArrayList<>());
        int[] expectedLineBreaksEmpty = new int[0];
        assertArrayEquals(expectedLineBreaksEmpty, emptyFileText.findLineBreaks());
    }

    // Test findLineBreaks() with null lines
    @Test
    public void testFindLineBreaksNull() {
        assertThrows(NullPointerException.class, () -> new FileText(null));
    }

    // Test lineColumn(int pos) with invalid position
    @Test
    public void testLineColumnInvalidPosition() {
        FileText fileText = new FileText(new File("testLines.txt"), "UTF-8");
        assertThrows(IndexOutOfBoundsException.class, () -> fileText.lineColumn(5));
    }

}