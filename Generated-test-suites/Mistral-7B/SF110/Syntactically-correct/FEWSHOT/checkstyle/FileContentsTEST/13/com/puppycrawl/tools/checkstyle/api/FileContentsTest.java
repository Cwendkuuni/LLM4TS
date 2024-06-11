package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContents_fromFileAndLines() {
        final String fileName = "TestFile.java";
        final String[] lines = new String[] {
                "/**",
                " * This is a comment",
                " */",
                "public class TestFile {",
                "    // Single line comment",
                "    private int x;",
                "}",
                ""
        };

        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        assertSame(lines[0], fileContents.getLines()[0]);
        assertSame(lines[lines.length - 1], fileContents.getLines()[lines.length - 1]);
        assertNull(fileContents.getJavadocBefore(0));
        assertSame(lines[1], fileContents.getLine(1));
        assertTrue(fileContents.lineIsBlank(2));
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsComment(2));
        assertNull(fileContents.javadocComments.get(1));
        assertNull(fileContents.cppComments.get(1));
        assertNull(fileContents.clangComments.get(1));
    }

    @Test
    public void testFileContents_fromFileText() {
        final String fileName = "TestFile.java";
        final String fileText = "/**\n" +
                " * This is a comment\n" +
                " */\n" +
                "public class TestFile {\n" +
                "    // Single line comment\n" +
                "    private int x;\n" +
                "}";
        FileText fileTextObj = new FileText(fileText);
        FileContents fileContents = new FileContents(fileTextObj);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(1, fileContents.getLines().length);
        assertSame(fileText, fileContents.getText().toString());
        assertNull(fileContents.getJavadocBefore(0));
        assertNull(fileContents.cppComments.get(1));
        assertNull(fileContents.clangComments.get(1));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsComment(0));
    }

    @Test
    public void testGetCppComments() {
        final String fileName = "TestFile.java";
        final String[] lines = new String[] {
                "/**",
                " * This is a comment",
                " */",
                "// Single line C++ comment",
                "public class TestFile {",
                "    // Multi line C++ comment\n" +
                "    // started here",
                "    // and ended here",
                "    private int x;\n" +
                "}",
                ""
        };

        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(3, cppComments.size());
        TextBlock cppComment1 = cppComments.get(3);
        assertTrue(cppComment1.getText().contains("Single line C++ comment"));
        TextBlock cppComment2 = cppComments.get(5);
        assertEquals(2, cppComment2.getText().split("\n").length);
    }

    @Test
    public void testGetCComments() {
        final String fileName = "TestFile.java";
        final String[] lines = new String[] {
                "/**",
                " * This is a comment",
                " */",
                "/*\n" +
                " * Multi line C comment\n" +
                " * started here\n" +
                " * and ended here\n" +
                " */",
                "public class TestFile {",
                "    // Single line C comment",
                "    private int x;\n" +
                "}",
                ""
        };

        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        List<TextBlock> clangComments = cComments.get(3);
        assertEquals(2, clangComments.size());
        TextBlock clangComment1 = clangComments.get(0);
        assertTrue(clangComment1.getText().contains("Multi line C comment started here"));
        TextBlock clangComment2 = clangComments.get(1);
        assertTrue(clangComment2.getText().contains("Single line C comment"));
    }

    @Test
    public void testGetJavadocBefore() {
        final String fileName = "TestFile.java";
        final String[] lines = new String[] {
                "/**",
                " * This is a Javadoc comment",
                " */",
                "public class TestFile {",
                "    public void testMethod() {",
                "        // this is a method javadoc",
                "    }",
                "}"
        };

        FileContents fileContents = new FileContents(fileName, lines);

        TextBlock javadocBefore = fileContents.getJavadocBefore(6);
        assertEquals(" * This is a Javadoc comment", javadocBefore.getText());
    }

    @Test
    public void testHasIntersectionWithComment() {
        final String fileName = "TestFile.java";
        final String[] lines = new String[] {
                "/**",
                " * This is a Javadoc comment",
                " */",
                "public class TestFile {",
                "    public void testMethod() {",
                "        // this is a method javadoc",
                "        private int x;\n" +
                "    }",
                "}"
        };

        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 5;
        int startColNo = 0;
        int endLineNo = 5;
        int endColNo = 10;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 6;
        startColNo = 0;
        endLineNo = 6;
        endColNo = 10;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        final String fileName = "TestFile.java";
        final String[] lines = new String[] {
                "@package test.package",
                "",
                "public class TestFile {",
                "}"
        };

        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.inPackageInfo());

        fileName = "TestFile.java";
        lines = new String[] {
                "public class TestFile {",
                "}"
        };

        fileContents = new FileContents(fileName, lines);

        assertFalse(fileContents.inPackageInfo());
    }
}