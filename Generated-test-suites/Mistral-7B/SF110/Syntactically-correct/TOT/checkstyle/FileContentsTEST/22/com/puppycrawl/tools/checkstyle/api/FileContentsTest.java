package com.example.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {
    // Test constructor with filename and lines

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "TestFile.java";
        String[] lines = new String[] { "public class TestClass {}", "/** This is a Javadoc comment */" };
        FileContents fileContents = new FileContents(fileName, lines);
        assertSame(fileName, fileContents.getFileName());
        List<String> expectedLines = Arrays.asList(lines);
        assertSame(expectedLines, fileContents.getLines());
    }

    // Test constructor with FileText

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "TestFile.java";
        String text = "public class TestClass {\n" +
                      "/** This is a Javadoc comment */\n" +
                      "}";
        FileContents fileContents = new FileContents(new FileText(text));
        assertSame(fileName, fileContents.getFileName());
        List<String> expectedLines = Arrays.asList(text.split("\n"));
        assertSame(expectedLines, fileContents.getLines());
    }

    // Test getCppComments

    @Test
    public void testGetCppComments() {
        String fileName = "TestFile.java";
        String text = "// This is a single line C comment\n" +
                      "/**\n" +
                      " * This is a Javadoc comment\n" +
                      " * with multiple lines\n" +
                      " */\n" +
                      "// Another single line C comment";
        FileContents fileContents = new FileContents(new FileText(text));
        List<Comment> expectedCppComments = Arrays.asList(
                new Comment(new String[] { "This is a single line C comment" }, 0, 0, text.length()),
                new Comment(new String[] { "This is a Javadoc comment",
                                           "with multiple lines" }, 1, 0, 4)
        );
        ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
        List<Comment> actualCppCommentsList = Lists.newArrayList(actualCppComments.values());
        assertSame(expectedCppComments, actualCppCommentsList);
    }

    // Test getCComments

    @Test
    public void testGetCComments() {
        String fileName = "TestFile.java";
        String text = "/**\n" +
                      " * This is a Javadoc comment\n" +
                      " * with multiple lines\n" +
                      " */\n" +
                      "// This is a single line C comment\n" +
                      "/*\n" +
                      " * This is a multi-line C comment\n" +
                      " */";
        FileContents fileContents = new FileContents(new FileText(text));
        List<List<Comment>> expectedCComments = Arrays.asList(
                Collections.singletonList(new Comment(new String[] { "This is a single line C comment" }, 0, 0, text.length()))
        );
        ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
        List<List<Comment>> actualCCommentsList = Lists.newArrayList(actualCComments.values());
        assertSame(expectedCComments, actualCCommentsList);
    }

    // Test getJavadocBefore

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        String text = "/**\n" +
                      " * This is a Javadoc comment\n" +
                      " */\n" +
                      "public class TestClass {\n" +
                      "}";
        FileContents fileContents = new FileText(text);
        Comment expectedJavadoc = new Comment(new String[] { "This is a Javadoc comment" }, 0, 0, 8);
        Comment actualJavadoc = fileContents.getJavadocBefore(1);
        assertSame(expectedJavadoc, actualJavadoc);
    }

    // Test lineIsBlank

    @Test
    public void testLineIsBlank() {
        String fileName = "TestFile.java";
        String text = "public class TestClass {\n" +
                      "}\n" +
                      "\n" +
                      "";
        FileContents fileContents = new FileText(text);
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(0));
    }

    // Test lineIsComment

    @Test
    public void testLineIsComment() {
        String fileName = "TestFile.java";
        String text = "public class TestClass {\n" +
                      "// This is a comment\n" +
                      "}\n" +
                      "\n" +
                      "// Another comment";
        FileContents fileContents = new FileText(text);
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(3));
        assertFalse(fileContents.lineIsComment(0));
    }

    // Test hasIntersectionWithComment

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String text = "public class TestClass {\n" +
                      "// This is a comment that intersects with the target region\n" +
                      "int variable = 0;\n" +
                      "/**\n" +
                      " * This is a Javadoc comment that intersects with the target region\n" +
                      " */\n" +
                      "// Another comment that does not intersect with the target region\n" +
                      "}";
        FileContents fileContents = new FileText(text);
        int startLineNo = 2;
        int startColNo = 3;
        int endLineNo = 3;
        int endColNo = 11;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        int startLineNo2 = 2;
        int startColNo2 = 3;
        int endLineNo2 = 3;
        int endColNo2 = 20;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo2, startColNo2, endLineNo2, endColNo2));
    }

    // Test inPackageInfo

    @Test
    public void testInPackageInfo() {
        String fileName = "TestFile.java";
        String text = "/**\n" +
                      " * This is a Javadoc comment\n" +
                      " */\n" +
                      "package com.example.test;\n" +
                      "public class TestClass {}";
        FileContents fileContents = new FileText(text);
        assertTrue(fileContents.inPackageInfo());

        String fileName2 = "TestFile.java";
        String text2 = "public class TestClass {}";
        FileContents fileContents2 = new FileText(text2);
        assertFalse(fileContents2.inPackageInfo());
    }
}