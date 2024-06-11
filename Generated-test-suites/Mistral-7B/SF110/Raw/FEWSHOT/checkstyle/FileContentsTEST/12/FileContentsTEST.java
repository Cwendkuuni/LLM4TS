package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testNewInstanceFromLines() {
        String fileName = "TestFile.java";
        String[] lines = {
                "/**",
                " * This is a test Javadoc comment",
                "",
                "public class TestFile {",
                "}",
                ""
        };

        FileContents fileContents = new FileContents(fileName, lines);
        String expectedFileName = "TestFile.java";
        String expectedJavadoc = " * This is a test Javadoc comment";
        List<TextBlock> expectedCppComments = Collections.emptyList();
        List<List<TextBlock>> expectedClangComments = Collections.emptyList();
        String expectedLine1 = "/**";
        String expectedLine2 = " * This is a test Javadoc comment";
        String expectedLine3 = "public class TestFile {";

        assertSame(expectedFileName, fileContents.getFileName());
        assertEquals(expectedJavadoc, fileContents.getJavadocBefore(2).getText());
        assertEquals(expectedCppComments, fileContents.getCComments().values());
        assertEquals(expectedClangComments, fileContents.getCComments().values());
        assertEquals(expectedLine1, fileContents.getLine(0));
        assertEquals(expectedLine2, fileContents.getLine(1));
        assertEquals(expectedLine3, fileContents.getLine(3));
    }

    @Test
    public void testNewInstanceFromFileText() {
        String fileName = "TestFile.java";
        String line1 = "/**";
        String line2 = " * This is a test Javadoc comment";
        String line3 = "public class TestFile {";
        String line4 = "";
        String line5 = "";
        String[] lines = { line1, line2, line3, line4, line5 };
        FileText fileText = new FileText(Arrays.asList(lines));

        FileContents fileContents = new FileContents(fileName, fileText);
        String expectedFileName = "TestFile.java";
        String expectedJavadoc = " * This is a test Javadoc comment";
        List<TextBlock> expectedCppComments = Collections.emptyList();
        List<List<TextBlock>> expectedClangComments = Collections.emptyList();
        String expectedLine1 = line1;
        String expectedLine2 = line2;
        String expectedLine3 = line3;

        assertSame(expectedFileName, fileContents.getFileName());
        assertEquals(expectedJavadoc, fileContents.getJavadocBefore(2).getText());
        assertEquals(expectedCppComments, fileContents.getCComments().values());
        assertEquals(expectedClangComments, fileContents.getCComments().values());
        assertEquals(expectedLine1, fileContents.getLine(0));
        assertEquals(expectedLine2, fileContents.getLine(1));
        assertEquals(expectedLine3, fileContents.getLine(3));
    }

    @Test
    public void testExtractCComment() {
        String line1 = "/** This is a multiline Javadoc comment";
        String line2 = " * @param param1 This is a param Javadoc comment";
        String line3 = " * @return This is a return Javadoc comment";
        String line4 = "int myMethod();";

        int startLineNo = 2;
        int startColNo = 0;
        int endLineNo = 4;
        int endColNo = 21;

        String[] expected = { line2, line3 };
        String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testExtractCComment_SingleLine() {
        String line1 = "// This is a single line C comment";
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 19;

        String[] expected = { line1 };
        String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testExtractCComment_Empty() {
        String line1 = "/**";
        String line2 = " *";
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 0;

        String[] expected = new String[0];
        String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testExtractCComment_OutsideComment() {
        String line1 = "int myMethod();";
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 1;
        int endColNo = 11;

        String[] expected = new String[0];
        String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testHasIntersectionWithComment_NoComment() {
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 19;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_CPPComment() {
        String line1 = "// This is a C comment for line 1";
        String line2 = "int myMethod();";
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 11;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_CComment() {
        String line1 = "/* This is a Javadoc comment for line 1 */";
        String line2 = "int myMethod();";
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 11;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_JavadocComment() {
        String line1 = "/** This is a multiline Javadoc comment";
        String line2 = " * @param param1 This is a param Javadoc comment";
        String line3 = " * @return This is a return Javadoc comment";
        String line4 = "int myMethod();";
        int startLineNo = 2;
        int startColNo = 0;
        int endLineNo = 4;
        int endColNo = 17;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_JavadocComment_Outside() {
        String line1 = "/** This is a multiline Javadoc comment";
        String line2 = " * @param param1 This is a param Javadoc comment";
        String line3 = " * @return This is a return Javadoc comment";
        String line4 = "int myMethod();";
        int startLineNo = 3;
        int startColNo = 0;
        int endLineNo = 3;
        int endColNo = 17;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testGetJavadocBefore() {
        String line1 = "/** This is a multiline Javadoc comment";
        String line2 = " * @param param1 This is a param Javadoc comment";
        String line3 = " * @return This is a return Javadoc comment";
        String line4 = "int myMethod();";
        int startLineNo = 2;

        TextBlock expected = new TextBlock(" * @param param1 This is a param Javadoc comment", startLineNo, 0, startLineNo, 55);
        TextBlock actual = fileContents.getJavadocBefore(startLineNo);

        assertSame(expected, actual);
    }

    @Test
    public void testGetJavadocBefore_BeforeFirstLine() {
        String line1 = "";
        String line2 = "/** This is a multiline Javadoc comment";
        String line3 = " * @param param1 This is a param Javadoc comment";
        String line4 = " * @return This is a return Javadoc comment";
        String line5 = "int myMethod();";
        int startLineNo = -1;

        assertNull(fileContents.getJavadocBefore(startLineNo));
    }

    @Test
    public void testGetJavadocBefore_AfterLastLine() {
        String line1 = "int myMethod();";
        String line2 = "/** This is a multiline Javadoc comment";
        String line3 = " * @param param1 This is a param Javadoc comment";
        String line4 = " * @return This is a return Javadoc comment";
        int startLineNo = 5;

        assertNull(fileContents.getJavadocBefore(startLineNo));
    }

    @Test
    public void testGetJavadocBefore_BlankLine() {
        String line1 = "";
        String line2 = "/** This is a multiline Javadoc comment";
        String line3 = " * @param param1 This is a param Javadoc comment";
        String line4 = " * @return This is a return Javadoc comment";
        String line5 = "int myMethod();";
        int startLineNo = 3;

        assertNull(fileContents.getJavadocBefore(startLineNo));
    }

    @Test
    public void testGetJavadocBefore_CommentLine() {
        String line1 = "";
        String line2 = "// This is a comment for line 3";
        String line3 = "/** This is a multiline Javadoc comment";
        String line4 = " * @param param1 This is a param Javadoc comment";
        String line5 = " * @return This is a return Javadoc comment";
        String line6 = "int myMethod();";
        int startLineNo = 3;

        assertNull(fileContents.getJavadocBefore(startLineNo));
    }
}