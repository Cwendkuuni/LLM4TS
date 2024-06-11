package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileContentsTest {

    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("test.cpp", "// This is a single line comment",
                "-- This is a multi-line C comment\n" +
                "-- This is another multi-line C comment\n" +
                "int main() { return 0; }");
        String[] expectedCComment = new String[] { "-- This is a multi-line C comment\n",
                "-- This is another multi-line C comment\n"};
        assertEquals(Arrays.asList(expectedCComment), fileContents.extractCComment(3, 0, 5, 0));
    }

    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("test.cpp", null,
                "-- This is a multi-line C comment\n" +
                "-- This is another multi-line C comment\n" +
                "int main() { return 0; }");
        List<TextBlock> expectedCComments = Arrays.asList(new Comment(new String[] {"-- This is a multi-line C comment"}, 0, 0, 0),
                new Comment(new String[] {"-- This is another multi-line C comment"}, 0, 0, 0));
        ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
        assertEquals(expectedCComments, actualCComments.get(0));
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("test.java", null,
                "/**\n" +
                " * This is a Javadoc comment before a method\n" +
                " * @param x The parameter x\n" +
                " * @param y The parameter y\n" +
                " */\n" +
                "public void myMethod(int x, int y) {}\n" +
                "/**\n" +
                " * This is a Javadoc comment after a method\n" +
                " */");
        Comment expectedJavadoc = new Comment(new String[] {" * This is a Javadoc comment before a method\n" +
                " * @param x The parameter x\n" +
                " * @param y The parameter y\n", 4, 2, 21}, 6, 2, 30);
        assertEquals(expectedJavadoc, fileContents.getJavadocBefore(6));
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("test.java", null,
                "/**\n" +
                " * This is a Javadoc comment before a method\n" +
                " * @param x The parameter x\n" +
                " * @param y The parameter y\n" +
                " */\n" +
                "public void myMethod(int x, int y) {}\n" +
                "/**\n" +
                " * This is a Javadoc comment after a method\n" +
                " */");
        int startLineNo = 5;
        int startColNo = 5;
        int endLineNo = 5;
        int endColNo = 20;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 1;
        startColNo = 1;
        endLineNo = 1;
        endColNo = 1;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 3;
        startColNo = 1;
        endLineNo = 3;
        endColNo = 20;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 4;
        startColNo = 5;
        endLineNo = 5;
        endColNo = 20;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 6;
        startColNo = 5;
        endLineNo = 6;
        endColNo = 20;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 7;
        startColNo = 5;
        endLineNo = 7;
        endColNo = 20;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 8;
        startColNo = 5;
        endLineNo = 8;
        endColNo = 20;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("test/package-info.java", null);
        assertTrue(fileContents.inPackageInfo());

        fileContents = new FileContents("test/Test.java", null);
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testReportCppComment() {
        FileContents fileContents = new FileContents("test.cpp", null);
        fileContents.reportCppComment(1, 5);
        Comment expectedComment = new Comment(new String[] {"// This is a single line comment"}, 0, 0, 4);
        assertEquals(expectedComment, fileContents.cppComments.get(1));
    }

    @Test
    public void testReportBlockComment() {
        FileContents fileContents = new FileContents("test.java", null);
        fileContents.reportBlockComment("JavaDoc", 1, 5, 3, 10);
        Comment expectedComment = new Comment(new String[] {"/**\n" +
                " * JavaDoc comment\n" +
                " */"}, 0, 0, 12);
        assertEquals(expectedComment, fileContents.clangComments.get(1).get(0));
    }

    @Test
    public void testReportSingleLineComment() {
        FileContents fileContents = new FileContents("test.java", null);
        fileContents.reportSingleLineComment("JavaDoc", 1, 5);
        Comment expectedComment = new Comment(new String[] {"// JavaDoc comment"}, 0, 0, 5);
        assertEquals(expectedComment, fileContents.cppComments.get(1));
    }

    @Test
    public void testToString() {
        FileContents fileContents = new FileContents("test.java", null, "/**\n" +
                " * This is a Javadoc comment\n" +
                " */");
        String expectedString = "FileContents[fileName=test.java, javadocComments={}, cppComments={}, clangComments={}]";
        assertEquals(expectedString, fileContents.toString());
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("test.java", null,
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */\n" +
                "");
        assertTrue(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("test.java", null,
                "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */\n");
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testGetText() {
        FileContents fileContents = new FileContents("test.java", null,
                "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */\n");
        FileText expectedText = new FileText("test.java", "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */");
        assertEquals(expectedText, fileContents.getText());
    }

    @Test
    public void testGetLines() {
        FileContents fileContents = new FileContents("test.java", null,
                "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */");
        String[] expectedLines = new String[] {"// This is a single line comment",
                "/**",
                " * This is a Javadoc comment",
                " */"};
        assertEquals(Arrays.asList(expectedLines), Arrays.asList(fileContents.getLines()));
    }

    @Test
    public void testGetLine() {
        FileContents fileContents = new FileContents("test.java", null,
                "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */");
        String expectedLine = "// This is a single line comment";
        assertEquals(expectedLine, fileContents.getLine(0));
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("test.java", null,
                "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */");
        String expectedFileName = "test.java";
        assertEquals(expectedFileName, fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        FileContents fileContents = new FileContents("test.java", null,
                "// This is a single line comment\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */");
        String expectedFileName = "test.java";
        assertEquals(expectedFileName, fileContents.getFileName());
    }
}