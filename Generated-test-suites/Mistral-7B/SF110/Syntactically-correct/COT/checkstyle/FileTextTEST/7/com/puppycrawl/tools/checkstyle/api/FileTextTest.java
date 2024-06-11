package com.example.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.LineColumnTestHelper;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        String filePath = "test.txt";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(filePath, fileText.getFile().getPath());
        assertEquals(charsetName, fileText.getCharset().name());
        String expectedContent = "Hello World!";
        String fileContent = new String(Files.toByteArray(new File(filePath)), StandardCharsets.UTF_8);
        assertEquals(expectedContent, fileText.getFullText());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistingFileAndCharset() throws IOException {
        String filePath = "non_existing_file.txt";
        FileText fileText = new FileText(new File(filePath), "UTF-8");
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        String filePath = "test.txt";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        FileText fileText = new FileText(new FileText(file, charsetName));
        assertEquals(fileText.getFile(), fileText.getFile());
        assertEquals(fileText.getCharset(), fileText.getCharset());
    }

    @Test
    public void testFileTextConstructionWithLines() {
        String filePath = "test.txt";
        File file = new File(filePath);
        List<String> lines = Arrays.asList("Hello", "World!");
        FileText fileText = new FileText(file, lines);
        assertEquals(file.getPath(), fileText.getFile().getPath());
        assertEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        FileText fileText = new FileText(new File("file.txt"), fileContent);
        int[] lineBreakPositions = fileText.findLineBreaks();
        assertEquals(Arrays.asList(4, 7, 10, 13), Arrays.asList(lineBreakPositions[0], lineBreakPositions[1], lineBreakPositions[2], lineBreakPositions[3]));
    }

    @Test
    public void testGetLineColumn() {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        FileText fileText = new FileText(new File("file.txt"), fileContent);
        LineColumnTestHelper helper = new LineColumnTestHelper(fileText);
        LineColumn lineColumn1 = fileText.lineColumn(0);
        LineColumn lineColumn2 = fileText.lineColumn(1);
        LineColumn lineColumn3 = fileText.lineColumn(2);
        LineColumn lineColumn4 = fileText.lineColumn(3);
        helper.assertLineColumn(lineColumn1, 1, 0);
        helper.assertLineColumn(lineColumn2, 1, 5);
        helper.assertLineColumn(lineColumn3, 1, 11);
        helper.assertLineColumn(lineColumn4, 1, 18);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidLineIndex() {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        FileText fileText = new FileText(new File("file.txt"), fileContent);
        fileText.get(5);
    }

    @Test
    public void testSize() {
        String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        FileText fileText = new FileText(new File("file.txt"), fileContent);
        assertEquals(4, fileText.size());
    }
}