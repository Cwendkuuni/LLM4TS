package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private static final String TEST_FILE = "test.txt";
    private static final List<String> TEST_LINES = Arrays.asList(
            "Line 1",
            "Line 2",
            "Line 3",
            "Line 4",
            "Line 5");

    private File file;
    private FileText fileText;
    private FileText fileTextWithLines;
    private FileText fileTextWithCharset;
    private FileText fileTextCloned;

    @Before
    public void setUp() throws IOException {
        file = new File(TEST_FILE);
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            for (String line : TEST_LINES) {
                writer.println(line);
            }
        }

        fileText = new FileText(file, StandardCharsets.UTF_8);
        fileTextWithLines = new FileText(TEST_LINES);
        fileTextWithCharset = new FileText(file, StandardCharsets.UTF_8.name());
        fileTextCloned = new FileText(fileText);
    }

    @Test
    public void testFileTextConstructor() throws IOException {
        assertEquals(TEST_FILE, fileText.getFile().getAbsolutePath());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertArrayEquals(TEST_LINES.toArray(new String[TEST_LINES.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        assertEquals(TEST_FILE, fileTextWithLines.getFile());
        assertNull(fileTextWithLines.getCharset());
        assertArrayEquals(TEST_LINES.toArray(new String[TEST_LINES.size()]), fileTextWithLines.toLinesArray());
    }

    @Test
    public void testReadFileMethod() throws IOException {
        String expectedText = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expectedText += line + "\n";
            }
        }

        assertEquals(expectedText, fileText.getFullText());
    }

    @Test
    public void testFromLinesStaticMethod() throws IOException {
        FileText expectedFileText = new FileText(TEST_LINES);
        FileText actualFileText = FileText.fromLines(new File(TEST_FILE), TEST_LINES);
        assertSame(expectedFileText, actualFileText);
    }

    @Test
    public void testGetFileMethod() {
        assertSame(file, fileText.getFile());
    }

    @Test
    public void testGetCharsetMethod() {
        assertSame(StandardCharsets.UTF_8, fileTextWithCharset.getCharset());
    }

    @Test
    public void testGetFullTextMethod() {
        assertEquals(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8), fileText.getFullText());
    }

    @Test
    public void testToLinesArrayMethod() {
        assertArrayEquals(TEST_LINES.toArray(new String[TEST_LINES.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaksMethod() {
        int[] expectedLineBreakPositions = new int[TEST_LINES.size() + 1];
        expectedLineBreakPositions[0] = 0;
        for (int i = 0; i < TEST_LINES.size(); ++i) {
            expectedLineBreakPositions[i + 1] = TEST_LINES.get(i).length() + expectedLineBreakPositions[i];
        }
        expectedLineBreakPositions[TEST_LINES.size() + 1] = TEST_LINES.get(TEST_LINES.size() - 1).length() + expectedLineBreakPositions[TEST_LINES.size()];

        int[] actualLineBreakPositions = fileText.findLineBreaks();
        assertArrayEquals(expectedLineBreakPositions, actualLineBreakPositions);
    }

    @Test
    public void testLineColumnMethod() {
        int[] expectedLineBreakPositions = fileText.findLineBreaks();

        LineColumn lineColumn1 = fileText.lineColumn(10);
        LineColumn lineColumn2 = fileText.lineColumn(15);
        LineColumn lineColumn3 = fileText.lineColumn(22);
        LineColumn lineColumn4 = fileText.lineColumn(28);

        assertEquals(1, lineColumn1.getLineNo());
        assertEquals(11, lineColumn1.getColumnNo());
        assertEquals(2, lineColumn2.getLineNo());
        assertEquals(5, lineColumn2.getColumnNo());
        assertEquals(3, lineColumn3.getLineNo());
        assertEquals(3, lineColumn3.getColumnNo());
        assertEquals(4, lineColumn4.getLineNo());
        assertEquals(8, lineColumn4.getColumnNo());

        try {
            fileText.lineColumn(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected
        }
    }

    @Test
    public void testGetMethod() {
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));
        assertEquals("Line 4", fileText.get(3));
        assertEquals("Line 5", fileText.get(4));
    }

    @Test
    public void testSizeMethod() {
        assertEquals(TEST_LINES.size(), fileText.size());
    }

    @Test
    public void testCloneMethod() {
        assertArrayEquals(fileText.toLinesArray(), fileTextCloned.toLinesArray());
    }

    @Test
    public void testCloneWithNullLineBreaks() {
        fileText.lineBreaks = null;
        FileText clonedFileText = new FileText(fileText);
        assertNull(clonedFileText.lineBreaks);
    }

    @Test
    public void testCloneWithNotNullLineBreaks() {
        int[] lineBreakPositions = fileText.findLineBreaks();
        FileText clonedFileText = new FileText(fileText);
        int[] clonedLineBreakPositions = clonedFileText.findLineBreaks();
        assertArrayEquals(lineBreakPositions, clonedLineBreakPositions);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithNonExistentFile() throws IOException {
        new FileText(new File("non-existent-file.txt"), "UTF-8");
    }

    @After
    public void tearDown() throws IOException {
        Files.delete(file.toPath());
    }
}