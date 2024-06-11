package com.example.junit;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileContentsTest {

    private FileContents fileContents;

    @Before
    public void setUp() {
        String fileName = "example.java";
        String[] lines = {"public class Example",
                "{\\n",
                " * This is a Javadoc comment. \\n",
                " */",
                " int main(String[] args) {",
                " // This is a C-style comment.",
                " // It starts with two slashes and ends with a star.",
                " // It can span multiple lines.",
                " // This is another C-style comment.",
                " // It starts at the beginning of the line.",
                " // and ends with a star.",
                " }"};
        fileContents = new FileContents(fileName, lines);
    }

    @Test
    public void testGetFileName() {
        assertEquals("example.java", fileContents.getFileName());
    }

    @Test
    public void testGetLines() {
        String[] expectedLines = {"public class Example",
                "{\\n",
                " * This is a Javadoc comment. \\n",
                " */",
                " int main(String[] args) {",
                " // This is a C-style comment.",
                " // It starts with two slashes and ends with a star.",
                " // It can span multiple lines.",
                " // This is another C-style comment.",
                " // It starts at the beginning of the line.",
                " // and ends with a star.",
                " }"};
        assertArrayEquals(expectedLines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        assertEquals("int main(String[] args) {", fileContents.getLine(5));
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 6;
        int startColNo = 1;
        fileContents.reportCppComment(startLineNo, startColNo);
        TextBlock expectedCppComment = new TextBlock("// This is another C-style comment.", startColNo, startLineNo, startColNo + 21);
        assertEquals(expectedCppComment, fileContents.cppComments.get(startLineNo));
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(6, new TextBlock("// This is another C-style comment.", 1, 6, 1 + 21));
        ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
        assertEquals(expectedCppComments, actualCppComments);
    }

    @Test
    public void testExtractCComment() {
        int startLineNo = 3;
        int startColNo = 0;
        int endLineNo = 5;
        int endColNo = 12;
        String[] expectedCComment = new String[]{" * This is a Javadoc comment.", ""};
        String[] actualCComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertArrayEquals(expectedCComment, actualCComment);
    }

    @Test
    public void testReportBlockComment() {
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 2;
        int endColNo = 19;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        List<TextBlock> expectedClangComments = Collections.singletonList(new TextBlock("/* This is a block comment.", 0, 1, 19));
        List<TextBlock> actualClangComments = fileContents.clangComments.get(1);
        assertEquals(expectedClangComments, actualClangComments);
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(1, Collections.singletonList(new TextBlock("/* This is a block comment.", 0, 1, 19)));
        ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
        assertEquals(expectedCComments, actualCComments);
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 2;
        TextBlock expectedJavadocBefore = new TextBlock(" * This is a Javadoc comment. \\n", 0, 1, 19);
        TextBlock actualJavadocBefore = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals(expectedJavadocBefore, actualJavadocBefore);
    }
}