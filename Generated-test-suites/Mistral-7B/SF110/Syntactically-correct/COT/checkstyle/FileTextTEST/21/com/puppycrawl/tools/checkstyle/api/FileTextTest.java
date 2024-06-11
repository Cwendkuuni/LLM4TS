package com.example.junit;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals("Expected number of lines", 5, fileText.size());
        // Add more assertions based on the expected content of the file
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() {
        File file = new File("src/test/resources/nonExistentFile.txt");
        try {
            new FileText(file, StandardCharsets.UTF_8.name());
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Expected exception
        }
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("src/test/resources/testFile.txt");
        FileText originalFileText = new FileText(file, StandardCharsets.UTF_8.name());
        FileText copyFileText = new FileText(originalFileText);
        assertEquals("Lines of original and copy FileText should be same", Arrays.toString(originalFileText.toLinesArray()), Arrays.toString(copyFileText.toLinesArray()));
    }

    @Test
    public void testFileTextBuilderConstructor() throws IOException {
        File file = new File("src/test/resources/testFile.txt");
        List<String> lines = Arrays.asList("Line1", "Line2", "Line3");
        FileText fileText = new FileText(file, lines);
        assertEquals("Number of lines in FileText built from list should be the same as the list size", 3, fileText.size());
        // Add more assertions based on the expected content of the file
    }

    @Test
    public void testGetFile() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals("Expected file of FileText object to be the same as the one passed to the constructor", file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals("Expected charset of FileText object to be the one passed to the constructor", StandardCharsets.UTF_8, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        String expectedFullText = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        assertEquals("Expected full text of FileText object to be the same as the file content", expectedFullText, fileText.getFullText());
    }

    @Test
    public void testToLinesArray() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        String[] expectedLines = new String[]{"Line1", "Line2", "Line3"};
        assertArrayEquals("Expected lines array of FileText object to be the same as the file lines", expectedLines, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(Arrays.asList("Line1", "Line2\nLine3", "Line4").toArray(new String[0]), null);
        int[] expectedLineBreaks = new int[]{4, 10, 15};
        int[] actualLineBreaks = fileText.findLineBreaks();
        assertArrayEquals("Expected line breaks of FileText object to be the same as the calculated one", expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(Arrays.asList("Line1", "Line2\nLine3", "Line4").toArray(new String[0]), null);
        LineColumn expectedLineColumn1 = new LineColumn(1, 1);
        LineColumn expectedLineColumn2 = new LineColumn(2, 11);
        LineColumn expectedLineColumn3 = new LineColumn(3, 1);
        LineColumn lineColumn1 = fileText.lineColumn(1);
        LineColumn lineColumn2 = fileText.lineColumn(11);
        LineColumn lineColumn3 = fileText.lineColumn(3);
        assertEquals("Expected line columns of FileText object to be the same as the calculated ones", expectedLineColumn1, lineColumn1);
        assertEquals("Expected line columns of FileText object to be the same as the calculated ones", expectedLineColumn2, lineColumn2);
        assertEquals("Expected line columns of FileText object to be the same as the calculated ones", expectedLineColumn3, lineColumn3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnIndexOutOfBoundsException() {
        FileText fileText = new FileText(Arrays.asList("Line1", "Line2\nLine3", "Line4").toArray(new String[0]), null);
        fileText.lineColumn(5);
        fail("Expected IndexOutOfBoundsException");
    }

    @Test
    public void testGet() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        String expectedLine1 = "Line1";
        String expectedLine2 = "Line2\nLine3";
        String expectedLine3 = "Line4";
        String line1 = fileText.get(0);
        String line2 = fileText.get(1);
        String line3 = fileText.get(2);
        assertEquals("Expected first line of FileText object to be the same as the first line in the file", expectedLine1, line1);
        assertEquals("Expected second line of FileText object to be the same as the second and third lines in the file concatenated", expectedLine2, line2);
        assertEquals("Expected third line of FileText object to be the same as the third line in the file", expectedLine3, line3);
    }

    @Test
    public void testSize() {
        File file = new File("src/test/resources/testFile.txt");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals("Expected size of FileText object to be the same as the number of lines in the file", 4, fileText.size());
    }
}