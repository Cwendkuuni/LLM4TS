package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.*;

@RunWith(Parameterized.class)
public class FileContentsTest {

    private FileContents fileContents;
    private String fileName;
    private String[] lines;

    public FileContentsTest(String fileName, String[] lines) {
        this.fileName = fileName;
        this.lines = lines;
    }

    @Parameters
    public static List<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                { "testFile.java", new String[] { "package test;", "public class TestClass {", "    // This is a comment", "}" } },
                { "testFile.cpp", new String[] { "// This is a comment in a C++ file", "int main() {", "}" } },
                { "testFile.java", new String[0] },
                { null, new String[0] },
                { "", new String[0] }
        });
    }

    @Test
    public void testConstructorWithFileNameAndLines() {
        fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
    }

    @Test
    public void testConstructorWithFileText() {
        File textFile = new File("testFile.txt");
        FileText text = new FileText(textFile);
        fileContents = new FileContents(text);
        assertEquals(textFile.toString(), fileContents.getFileName());
        assertEquals(text.toLinesArray(), fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        fileContents = new FileContents(fileName, lines);
        int startLineNo = 0;
        int startColNo = 10;
        fileContents.reportSingleLineComment("", startLineNo, startColNo);
        TextBlock comment = fileContents.cppComments.get(startLineNo);
        assertEquals(lines[0].substring(startColNo), comment.getText());
    }

    @Test(expected = IllegalStateException.class)
    public void testReportSingleLineCommentWithNullFileName() {
        new FileContents(null, lines);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportSingleLineCommentWithNullLines() {
        new FileContents(fileName, null);
    }

    @Test
    public void testGetCppComments() {
        fileContents = new FileContents(fileName, lines);
        int startLineNo = 0;
        int startColNo = 10;
        fileContents.reportCppComment(startLineNo, startColNo);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock comment = cppComments.get(startLineNo);
        assertEquals(lines[0].substring(startColNo), comment.getText());
    }

    @Test
    public void testReportCComment() {
        fileContents = new FileContents(fileName, lines);
        int startLineNo = 0;
        int startColNo = 10;
        int endLineNo = 2;
        int endColNo = 20;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> commentList = cComments.get(startLineNo);
        TextBlock comment = commentList.get(0);
        assertEquals(lines[0].substring(startColNo), comment.getText());
    }

    @Test
    public void testExtractCComment() {
        fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 20;
        String[] cComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(2, cComment.length);
        assertEquals(lines[1], cComment[0]);
        assertEquals(lines[2], cComment[1]);
    }

    @Test
    public void testGetJavadocBefore() {
        fileContents = new FileContents(fileName, lines);
        int lineNoBefore = 0;
        TextBlock javadoc = fileContents.getJavadocBefore(lineNoBefore);
        assertNull(javadoc);

        lines = new String[] { "/**", " * This is a javadoc comment", " * @author John Doe", " */", "", "public class TestClass {", "}" };
        fileContents = new FileContents(fileName, lines);
        lineNoBefore = 1;
        javadoc = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals(" * This is a javadoc comment", javadoc.getText());
    }

    @Test
    public void testGetFileName() {
        fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFilename());
    }

    @Test
    public void testLineIsBlank() {
        fileContents = new FileContents(fileName, lines);
        int lineNo = 0;
        assertTrue(fileContents.lineIsBlank(lineNo));

        lines = new String[] { "", "public class TestClass {", "}" };
        fileContents = new FileContents(fileName, lines);
        lineNo = 1;
        assertFalse(fileContents.lineIsBlank(lineNo));
    }

    @Test
    public void testLineIsComment() {
        fileContents = new FileContents(fileName, lines);
        int lineNo = 0;
        assertTrue(fileContents.lineIsComment(lineNo));

        lines = new String[] { "public class TestClass {", "    // This is a comment in a Java file", "}" };
        fileContents = new FileContents(fileName, lines);
        lineNo = 1;
        assertTrue(fileContents.lineIsComment(lineNo));

        lines = new String[] { "public class TestClass {", "}" };
        fileContents = new FileContents(fileName, lines);
        lineNo = 1;
        assertFalse(fileContents.lineIsComment(lineNo));
    }

    @Test
    public void testHasIntersectionWithComment() {
        fileContents = new FileContents(fileName, lines);
        int startLineNo = 0;
        int startColNo = 10;
        int endLineNo = 2;
        int endColNo = 20;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        lines = new String[] { "public class TestClass {", "    // This is a comment in a Java file", "    // This comment intersects with the test area", "}" };
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        lines = new String[] { "public class TestClass {", "    // This is a comment in a Java file that does not intersect with the test area", "    // Another comment that does not intersect with the test area", "}", "    /** This is a javadoc comment */" };
        fileContents = new FileContents(fileName, lines);
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        lines = new String[] { "public class TestClass {", "    // This is a comment in a Java file that intersects with the test area at the start", "    // This comment intersects with the test area", "    /** This is a javadoc comment */", "}" };
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        lines = new String[] { "public class TestClass {", "    // This comment intersects with the test area at the end", "    /** This is a javadoc comment */", "}" };
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        lines = new String[] { "public class TestClass {", "    // This comment intersects with the test area", "    /** This is a javadoc comment */", "}" };
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        lines = new String[] { "public class TestClass {", "/** This is a javadoc comment that intersects with the test area at the start */", "    // This comment intersects with the test area", "}" };
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        fileContents = new FileContents(fileName, lines);
        assertFalse(fileContents.inPackageInfo());

        fileName = "testFile/package-info.java";
        lines = new String[0];
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.inPackageInfo());

        fileName = "testFile/package-info.java";
        lines = new String[] { "// This is a comment in a package-info.java file" };
        fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.inPackageInfo());
    }
}