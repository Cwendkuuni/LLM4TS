package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.LineNumberLocation;

public class FileContentsTest {

    @Test
    public void testEmptyFile() {
        final String filename = "EmptyFile.java";
        final FileContents fileContents = new FileContents(filename);
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertNull(fileContents.getJavadocBefore(0));
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
    }

    @Test
    public void testSingleLineComment() {
        final String filename = "SingleLineComment.java";
        final String[] lines = { "// This is a single line comment", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals(1, fileContents.getText().size());
        assertNull(fileContents.getJavadocBefore(0));
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
        assertEquals(ImmutableMap.of("// This is a single line comment", new Comment(new String[] { "// This is a single line comment" }, 0, 0, 0, 0)), fileContents.getCppComments());
    }

    @Test
    public void testBlockComment() {
        final String filename = "BlockComment.java";
        final String[] lines = { "/** This is a block comment */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals(3, fileContents.getText().size());
        assertEquals(1, fileContents.javadocComments.size());
        assertEquals(new Comment(new String[] { "This is a block comment" }, 0, 0, 0, 0), fileContents.javadocComments.get(0));
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
    }

    @Test
    public void testBlockCommentWithMultipleLines() {
        final String filename = "BlockCommentWithMultipleLines.java";
        final String[] lines = { "/** This is a block comment with", " * multiple lines */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals(4, fileContents.getText().size());
        assertEquals(1, fileContents.javadocComments.size());
        assertEquals(new Comment(new String[] { "This is a block comment with", " * multiple lines" }, 0, 0, 0, 0), fileContents.javadocComments.get(0));
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
    }

    @Test
    public void testCComment() {
        final String filename = "CComment.java";
        final String[] lines = { "/* This is a C comment */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals(3, fileContents.getText().size());
        assertTrue(fileContents.hasIntersectionWithCComment(1, 0, 2, 0));
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
        assertEquals(ImmutableMap.of("/* This is a C comment */", new Comment(new String[] { "This is a C comment" }, 0, 0, 0, 0)), fileContents.getCppComments());
    }

    @Test
    public void testCppComment() {
        final String filename = "CppComment.java";
        final String[] lines = { "// This is a C++ comment", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals(3, fileContents.getText().size());
        assertFalse(fileContents.hasIntersectionWithCComment(1, 0, 2, 0));
        assertEquals(ImmutableMap.of("// This is a C++ comment", new Comment(new String[] { "// This is a C++ comment" }, 0, 0, 0, 0)), fileContents.getCppComments());
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
    }

    @Test
    public void testIntersectionWithCComment() {
        final String filename = "IntersectionWithCComment.java";
        final String[] lines = { "/* This is a comment that intersects with a C comment */", "// This is a C comment", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.hasIntersectionWithCComment(1, 0, 2, 0));
        assertEquals(ImmutableMap.of("/* This is a comment that intersects with a C comment */", new Comment(new String[] { "This is a comment that intersects with a C comment" }, 0, 0, 0, 0), "// This is a C comment", new Comment(new String[] { "This is a C comment" }, 0, 0, 0, 0)), fileContents.getCppComments());
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
    }

    @Test
    public void testIntersectionWithCppComment() {
        final String filename = "IntersectionWithCppComment.java";
        final String[] lines = { "// This is a comment that intersects with a C++ comment", "/** This is a C++ comment */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.hasIntersectionWithCppComment(1, 0, 2, 0));
        assertEquals(ImmutableMap.of("This is a comment that intersects with a C++ comment", new Comment(new String[] { "This is a comment that intersects with a C++ comment" }, 0, 0, 0, 0), "/** This is a C++ comment */", new Comment(new String[] { "This is a C++ comment" }, 0, 0, 0, 0)), fileContents.getCppComments());
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
    }

    @Test
    public void testGetJavadocBefore() {
        final String filename = "GetJavadocBefore.java";
        final String[] lines = { "/** This is a Javadoc comment before a method */", "public int method() {", "}", "/** This is a Javadoc comment after a method */" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals(new Comment(new String[] { "This is a Javadoc comment before a method" }, 0, 1, 0, 0), fileContents.getJavadocBefore(2));
        assertNull(fileContents.getJavadocBefore(0));
        assertNull(fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetLines() {
        final String filename = "GetLines.java";
        final String[] lines = { "public class Test {", "    int i = 1;", "    int j = 2;", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        List<String> expectedLines = Lists.newArrayList("public class Test {", "    int i = 1;", "    int j = 2;", "}");
        List<String> actualLines = fileContents.getLines();
        assertEquals(expectedLines, actualLines);
    }

    @Test
    public void testGetLine() {
        final String filename = "GetLine.java";
        final String[] lines = { "public class Test {", "    int i = 1;", "    int j = 2;", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertEquals("public class Test {", fileContents.getLine(0));
        assertEquals("    int i = 1;", fileContents.getLine(1));
        assertEquals("}", fileContents.getLine(2));
        assertNull(fileContents.getLine(3));
    }

    @Test
    public void testGetFileName() {
        final String filename = "GetFileName.java";
        final FileContents fileContents = new FileContents(filename);
        assertEquals(filename, fileContents.getFileName());
    }

    @Test
    public void testDeprecatedGetFilename() {
        final String filename = "GetFileName.java";
        final FileContents fileContents = new FileContents(filename);
        assertEquals(filename, fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        final String filename = "LineIsBlank.java";
        final String[] lines = { "", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        final String filename = "LineIsComment.java";
        final String[] lines = { "// This is a comment line", "", "public class Test {", "}" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        final String filename = "HasIntersectionWithComment.java";
        final String[] lines = { "/** This is a Javadoc comment that intersects with a C comment */", "/* This is a C comment */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 2, 0));
        assertTrue(fileContents.hasIntersectionWithComment(1, 0, 1, 0));
        assertFalse(fileContents.hasIntersectionWithComment(1, 0, 1, 1));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 0));
    }

    @Test
    public void testInPackageInfo() {
        final String filename = "InPackageInfo.java";
        final FileContents fileContents = new FileContents(filename);
        assertTrue(fileContents.inPackageInfo());
        assertFalse(fileContents.inPackageInfo());
        final String packageFilename = "package-info.java";
        final FileContents packageFileContents = new FileContents(packageFilename);
        assertTrue(packageFileContents.inPackageInfo());
        assertFalse(new FileContents("Test.java").inPackageInfo());
    }

    @Test
    public void testExtractCComment() throws IOException {
        final String filename = "ExtractCComment.java";
        final String[] lines = { "/* This is a comment that spans multiple lines */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        String[] expectedComment = new String[] { "This is a comment that spans multiple lines" };
        String[] actualComment = fileContents.extractCComment(1, 0, 2, 0);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testReportCppComment() {
        final String filename = "ReportCppComment.java";
        final String[] lines = { "// This is a C++ comment", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        fileContents.reportCppComment(1, 0);
        assertEquals(1, fileContents.cppComments.size());
        assertEquals(new Comment(new String[] { "This is a C++ comment" }, 0, 0, 0, 0), fileContents.cppComments.get(1));
    }

    @Test
    public void testReportBlockComment() {
        final String filename = "ReportBlockComment.java";
        final String[] lines = { "/** This is a block comment */", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        fileContents.reportBlockComment("", 1, 0, 2, 0);
        assertEquals(1, fileContents.clangComments.size());
        List<TextBlock> expectedComment = Lists.newArrayList(new Comment("This is a block comment", new LineNumberLocation(1, 0), new LineNumberLocation(2, 0)));
        List<TextBlock> actualComment = fileContents.clangComments.get(1);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testReportSingleLineComment() {
        final String filename = "ReportSingleLineComment.java";
        final String[] lines = { "// This is a single line comment", "public class Test {", "}", "" };
        final FileContents fileContents = new FileContents(filename, lines);
        fileContents.reportSingleLineComment("", 1, 0);
        assertEquals(1, fileContents.cppComments.size());
        assertEquals(new Comment("This is a single line comment", new LineNumberLocation(1, 0), new LineNumberLocation(1, 0)), fileContents.cppComments.get(1));
    }

    @Test
    public void testImmutableMapOf() {
        Builder<Integer, TextBlock> builder = ImmutableMap.builder();
        builder.put(1, new Comment(new String[] { "This is a comment" }, 0, 0, 0, 0));
        ImmutableMap<Integer, TextBlock> map = builder.build();
        assertEquals(1, map.size());
        assertEquals(new Comment(new String[] { "This is a comment" }, 0, 0, 0, 0), map.get(1));
    }

    @Test
    public void testImmutableMapCopyOf() {
        Map<Integer, TextBlock> map = Collections.singletonMap(1, new Comment(new String[] { "This is a comment" }, 0, 0, 0, 0));
        ImmutableMap<Integer, TextBlock> immutableMap = ImmutableMap.copyOf(map);
        assertEquals(1, immutableMap.size());
        assertEquals(new Comment(new String[] { "This is a comment" }, 0, 0, 0, 0), immutableMap.get(1));
    }

    @Test
    public void testFileTextFromLines() {
        final String filename = "FileTextFromLines.java";
        final String[] lines = { "public class Test {", "    int i = 1;", "    int j = 2;", "}", "" };
        final InputStream inputStream = new ByteArrayInputStream(("Test File".getBytes()));
        final FileText expectedText = new FileText(lines, inputStream, new LineNumberLocation(1, 0), new LineNumberLocation(4, 0));
        final FileText actualText = FileText.fromLines(new File(filename), Arrays.asList(lines));
        assertEquals(expectedText.toString(), actualText.toString());
    }

}