package com.example.junit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;
import com.google.common.collect.Maps;

public class FileContentsTest {

    private FileContents fileContents;
    private static final String FILE_CONTENTS_TEST_FILE = "FileContentsTest.java";
    private static final String[] LINES = new String[]{"// This is a single line comment.",
                                                      "/**",
                                                      " * This is a Javadoc comment.",
                                                      " * @param param1 This is a Javadoc parameter comment.",
                                                      " * @return The result of the method.",
                                                      " * @throws Exception If an exception occurs.",
                                                      " */",
                                                      "public int testMethod() throws Exception {",
                                                      "// This is another single line comment.",
                                                      "}",
                                                      "/**",
                                                      " * This is another Javadoc comment.",
                                                      " */"};

    @Before
    public void setUp() {
        File file = new File(FILE_CONTENTS_TEST_FILE);
        fileContents = new FileContents(file.getAbsolutePath(), LINES);
    }

    @Test
    public void testGetFileName() {
        assertEquals(FILE_CONTENTS_TEST_FILE, fileContents.getFileName());
    }

    @Test
    public void testGetText() {
        FileText expectedText = FileText.fromLines(new File(FILE_CONTENTS_TEST_FILE), Arrays.asList(LINES));
        FileText actualText = fileContents.getText();
        assertEquals(expectedText.toString(), actualText.toString());
    }

    @Test
    public void testGetLines() {
        String[] expectedLines = LINES;
        String[] actualLines = fileContents.getLines();
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testGetLine() {
        String expectedLine = LINES[0];
        String actualLine = fileContents.getLine(0);
        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 0;
        int startColNo = 0;
        fileContents.reportSingleLineComment("", startLineNo, startColNo);
        TextBlock expectedComment = new Comment(new String[]{"// This is a single line comment."}, 0, startLineNo, 14);
        TextBlock actualComment = fileContents.cppComments.get(startLineNo);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testReportBlockComment() {
        String[] cComment = new String[]{"/**",
                                         " * This is another Javadoc comment.",
                                         " */"};
        int startLineNo = 3;
        int startColNo = 0;
        int endLineNo = 5;
        int endColNo = 14;
        fileContents.reportBlockComment("", "", startLineNo, startColNo, endLineNo, endColNo);
        List<TextBlock> expectedComments = Arrays.asList(new TextBlock[] { new Comment(cComment, 0, endLineNo, endColNo),
                                                                         new Comment(new String[]{" * This is another Javadoc comment."}, 0, 3, 17) });
        List<TextBlock> actualComments = fileContents.clangComments.get(startLineNo);
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> expectedComments = ImmutableMap.of(0, new Comment(new String[]{"// This is a single line comment."}, 0, 0, 14),
                                                                              3, new Comment(cComment, 0, 3, 17));
        ImmutableMap<Integer, TextBlock> actualComments = fileContents.getCppComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> expectedComments = ImmutableMap.of(3, Arrays.asList(new TextBlock[] { new Comment(cComment, 0, 3, 17) }));
        ImmutableMap<Integer, List<TextBlock>> actualComments = fileContents.getCComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 2;
        TextBlock expectedComment = new Comment(new String[]{" * This is a Javadoc comment.",
                                                              " * @param param1 This is a Javadoc parameter comment.",
                                                              " * @return The result of the method.",
                                                              " * @throws Exception If an exception occurs."},
                                                          0, 3, 17);
        TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 0;
        int startColNo = 0;
        int endLineNo = 3;
        int endColNo = 18;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }
}