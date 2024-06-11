package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.LineColumnTestSupport;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFile() throws Exception {
        final File inputFile = new File("input.txt");
        final List<String> expectedLines = Lists.newArrayList(
            "Line 1",
            "Line 2",
            "Line 3"
        );
        final FileText fileText = new FileText(inputFile, "UTF-8");
        final String[] actualLines = fileText.toLinesArray();
        assertEquals("Unexpected number of lines", expectedLines.size(), actualLines.length);
        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals("Unexpected line at index " + i, expectedLines.get(i), actualLines[i]);
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionFromNonExistentFile() throws Exception {
        final File inputFile = new File("nonExistentFile.txt");
        new FileText(inputFile, "UTF-8");
    }

    @Test
    public void testFileTextConstructionFromFileText() {
        final File inputFile = new File("input.txt");
        final List<String> expectedLines = Lists.newArrayList(
            "Line 1",
            "Line 2",
            "Line 3"
        );
        final FileText fileText = new FileText(new FileText(inputFile, "UTF-8"));
        final String[] actualLines = fileText.toLinesArray();
        assertEquals("Unexpected number of lines", expectedLines.size(), actualLines.length);
        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals("Unexpected line at index " + i, expectedLines.get(i), actualLines[i]);
        }
    }

    @Test
    public void testFileTextConstructionFromLines() {
        final File inputFile = new File("input.txt");
        final List<String> expectedLines = Lists.newArrayList(
            "Line 1",
            "Line 2",
            "Line 3"
        );
        final FileText fileText = FileText.fromLines(inputFile, expectedLines);
        final String[] actualLines = fileText.toLinesArray();
        assertEquals("Unexpected number of lines", expectedLines.size(), actualLines.length);
        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals("Unexpected line at index " + i, expectedLines.get(i), actualLines[i]);
        }
    }

    @Test
    public void testGetFile() {
        final File inputFile = new File("input.txt");
        final FileText fileText = new FileText(inputFile, "UTF-8");
        assertEquals("Unexpected file", inputFile, fileText.getFile());
    }

    @Test
    public void testGetCharset() {
        final File inputFile = new File("input.txt");
        final FileText fileText = new FileText(inputFile, "UTF-8");
        assertEquals("Unexpected charset", "UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        final File inputFile = new File("input.txt");
        final List<String> expectedLines = Lists.newArrayList(
            "Line 1",
            "Line 2",
            "Line 3"
        );
        final FileText fileText = new FileText(inputFile, "UTF-8");
        final String actualFullText = fileText.getFullText();
        final LineColumnTestSupport lineColumnTestSupport = new LineColumnTestSupport(expectedLines);
        for (LineColumn column : lineColumnTestSupport.getColumns()) {
            final int lineNo = column.getLineNumber();
            final String line = fileText.get(lineNo - 1);
            assertEquals("Unexpected line at line " + lineNo, expectedLines.get(lineNo - 1), line);
        }
        assertEquals("Unexpected full text", Arrays.asList(expectedLines).toString(), actualFullText.substring(0, actualFullText.length() - 1));
    }

    @Test
    public void testLineColumn() throws Exception {
        final File inputFile = new File("input.txt");
        final List<String> expectedLines = Lists.newArrayList(
            "Line 1",
            "Line 2",
            "Line 3"
        );
        final FileText fileText = new FileText(inputFile, "UTF-8");
        for (int i = 0; i < expectedLines.size(); i++) {
            final String line = fileText.get(i);
            final LineColumn column = fileText.lineColumn(line.length());
            final int expectedLineNo = i + 1;
            final int expectedColumnNo = line.length() - line.lastIndexOf("\n").length() + 1;
            assertEquals("Unexpected column for line " + expectedLineNo + " and line \"" + line + "\": lineNumber=" + column.getLineNumber() + ", columnNumber=" + column.getColumnNumber(), expectedLineNo, column.getLineNumber());
            assertEquals("Unexpected column for line " + expectedLineNo + " and line \"" + line + "\": lineNumber=" + column.getLineNumber() + ", columnNumber=" + column.getColumnNumber(), expectedColumnNo, column.getColumnNumber());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnWithInvalidPosition() throws Exception {
        final File inputFile = new File("input.txt");
        final List<String> expectedLines = Lists.newArrayList(
            "Line 1",
            "Line 2",
            "Line 3"
        );
        final FileText fileText = new FileText(inputFile, "UTF-8");
        fileText.lineColumn(0);
    }

}