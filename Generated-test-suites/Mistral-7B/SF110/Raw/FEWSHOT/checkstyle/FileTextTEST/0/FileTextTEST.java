package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private static final int READ_BUFFER_SIZE = 1024;
    private static final Pattern LINE_TERMINATOR;

    static {
        LINE_TERMINATOR = Pattern.compile("\\n|\\r\\n?");
    }

    private File file;
    private Charset charset;
    private FileText fileText;
    private FileText fileTextFromLines;
    private CharsetDecoder decoder;

    @Before
    public void setUp() throws IOException {
        file = new File("src/test/resources/testFile.txt");
        charset = Charset.forName("UTF-8");
        decoder = charset.newDecoder();
    }

    @Test
    public void testFileTextConstructionWithFileAndCharset() throws IOException {
        fileText = new FileText(file, charset.getName());
        assertSame(file, fileText.getFile());
        assertSame(charset, fileText.getCharset());
        assertEquals(readFileContent(file, decoder), fileText.getFullText());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFileTextSizeIsReadonly() {
        fileText = new FileText(file, charset.getName());
        fileText.add("test");
    }

    @Test
    public void testFileTextConstructionWithFileText() {
        fileTextFromLines = new FileText(Arrays.asList("Hello world\n", "Line 2\n", "Line 3"));
        assertSame(fileTextFromLines.getFile(), fileText.getFile());
        assertSame(fileTextFromLines.getCharset(), fileText.getCharset());
        assertEquals(fileTextFromLines.getFullText(), fileText.getFullText());
        assertArrayEquals(fileTextFromLines.toLinesArray(), fileText.toLinesArray());
    }

    @Test
    public void testFileTextConstructionWithFileAndListOfStrings() {
        List<String> lines = Arrays.asList("Hello world\n", "Line 2\n", "Line 3");
        fileText = new FileText(file, lines);
        assertSame(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertEquals(String.join("\n", lines), fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks() {
        fileText = new FileText(file, charset.getName());
        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals(11, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
        assertEquals(1, lineBreaks[1]);
        assertEquals(53, lineBreaks[5]);
        assertEquals(121, lineBreaks[10]);
    }

    @Test
    public void testLineColumn() {
        fileText = new FileText(file, charset.getName());
        int pos = 53;
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(1, lineColumn.lineNumber);
        assertEquals(12, lineColumn.columnNumber);
    }

    @Test
    public void testGetCharAt() {
        fileText = new FileText(file, charset.getName());
        char charAt = fileText.get(0).charAt(5);
        assertEquals('w', charAt);
    }

    @Test
    public void testToString() {
        fileText = new FileText(file, charset.getName());
        String toString = fileText.toString();
        assertEquals(readFileContent(file, decoder), toString);
    }

    @Test
    public void testFromLines() {
        List<String> lines = Arrays.asList("Line 1\n", "Line 2\n", "Line 3\n");
        FileText fileTextFromLines = FileText.fromLines(file, lines);
        assertSame(file, fileTextFromLines.getFile());
        assertNull(fileTextFromLines.getCharset());
        assertEquals(String.join("\n", lines), fileTextFromLines.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileTextFromLines.toLinesArray());
    }

    @Test
    public void testFromLinesWithNullList() {
        List<String> nullList = null;
        assertNull(FileText.fromLines(file, nullList));
    }

    @Test
    public void testFromLinesWithEmptyList() {
        List<String> emptyList = Collections.emptyList();
        FileText fileTextFromLines = FileText.fromLines(file, emptyList);
        assertSame(file, fileTextFromLines.getFile());
        assertNull(fileTextFromLines.getCharset());
        assertEquals("", fileTextFromLines.getFullText());
        assertArrayEquals(new String[0], fileTextFromLines.toLinesArray());
    }

    @Test
    public void testFromLinesWithFileText() {
        FileText fileTextToConvert = new FileText(file, charset.getName());
        FileText fileTextFromLines = FileText.fromLines(fileTextToConvert);
        assertSame(fileTextToConvert.getFile(), fileTextFromLines.getFile());
        assertSame(fileTextToConvert.getCharset(), fileTextFromLines.getCharset());
        assertEquals(fileTextToConvert.getFullText(), fileTextFromLines.getFullText());
        assertArrayEquals(fileTextToConvert.toLinesArray(), fileTextFromLines.toLinesArray());
    }

    private CharSequence readFileContent(File file, CharsetDecoder decoder) throws IOException {
        FileText fileText = new FileText(file, decoder);
        return fileText.getFullText();
    }

    @Test
    public void testConstructorWithNullFile() {
        assertNull(new FileText(null, charset.getName()));
    }

    @Test
    public void testConstructorWithEmptyFile() {
        assertNull(new FileText(new File(""), charset.getName()));
    }

    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithFileNotFound() throws IOException {
        new FileText(new File("src/test/resources/nonexistentFile.txt"), charset.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCharset() {
        new FileText(file, null);
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testConstructorWithUnsupportedCharset() {
        new FileText(file, "XYZ-1234");
    }

    @Test
    public void testGetFile() {
        fileText = new FileText(file, charset.getName());
        assertSame(file, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        fileText = new FileText(file, charset.getName());
        assertSame(charset, fileText.getCharset());
    }

    @Test
    public void testGetFullText() {
        fileText = new FileText(file, charset.getName());
        String fullText = fileText.getFullText();
        assertThat(fullText, is(notNullValue()));
        assertThat(fullText, isInstanceOf(CharSequence.class));
    }

    @Test
    public void testToLinesArray() {
        fileText = new FileText(file, charset.getName());
        String[] lines = fileText.toLinesArray();
        assertThat(lines, is(notNullValue()));
        assertThat(lines, isInstanceOf(String[].class));
    }

    @Test
    public void testLineColumnNull() {
        fileText = new FileText(file, charset.getName());
        LineColumn lineColumn = fileText.lineColumn(null);
        assertNull(lineColumn);
    }

    @Test
    public void testLineColumnNegative() {
        fileText = new FileText(file, charset.getName());
        LineColumn lineColumn = fileText.lineColumn(-1);
        assertNull(lineColumn);
    }

    @Test
    public void testLineColumnGreaterThanSize() {
        fileText = new FileText(file, charset.getName());
        LineColumn lineColumn = fileText.lineColumn(fileText.size() + 1);
        assertNull(lineColumn);
    }
}