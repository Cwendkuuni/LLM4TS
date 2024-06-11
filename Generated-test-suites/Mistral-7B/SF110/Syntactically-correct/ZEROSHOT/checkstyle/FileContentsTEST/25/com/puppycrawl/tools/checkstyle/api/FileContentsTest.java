package com.example.junit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineNumberUtil;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.Utilities;

public class FileContentsTest {

    @Test
    public void testFileContentsWithLines() {
        String fileName = "FileUnderTest.java";
        String[] lines = new String[] {
                "/**",
                " * Some Javadoc comment",
                "",
                "public class FileUnderTest {",
                "}",
                "/**",
                " * Some C comment",
                " */"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        FileText expectedText = new FileText(Arrays.asList(
                "/**",
                " * Some Javadoc comment",
                "",
                "public class FileUnderTest {",
                "}",
                "/**",
                " * Some C comment",
                " */"
        ));

        FileText actualText = fileContents.getText();
        assertEquals("Texts should be equal", expectedText, actualText);

        Map<Integer, TextBlock> expectedCppComments = Collections.singletonMap(2, new TextBlock("Some C comment"));
        Map<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
        assertEquals("Cpp comments should be equal", expectedCppComments, actualCppComments);

        List<TextBlock> expectedClangComments = Collections.singletonList(new TextBlock("Some C comment"));
        List<TextBlock> actualClangComments = fileContents.getCComments().get(2);
        assertEquals("Clang comments should be equal", expectedClangComments, actualClangComments);

        TextBlock expectedJavadocBefore = new TextBlock("Some Javadoc comment");
        TextBlock actualJavadocBefore = fileContents.getJavadocBefore(3);
        assertEquals("Javadoc comment before line 3 should be equal", expectedJavadocBefore, actualJavadocBefore);
    }

    @Test
    public void testFileContentsWithFileText() {
        String fileName = "FileUnderTest.java";
        FileText fileText = new FileText(new File(fileName));
        FileContents fileContents = new FileContents(fileText);

        FileText expectedText = fileText;

        FileText actualText = fileContents.getText();
        assertEquals("Texts should be equal", expectedText, actualText);

        Map<Integer, TextBlock> expectedCppComments = Collections.emptyMap();
        Map<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
        assertEquals("Cpp comments should be empty", expectedCppComments, actualCppComments);

        List<TextBlock> expectedClangComments = Collections.emptyList();
        List<TextBlock> actualClangComments = fileContents.getCComments().get(0);
        assertEquals("Clang comments should be empty", expectedClangComments, actualClangComments);
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "FileUnderTest.java";
        String[] lines = new String[] {
                "/**",
                " * Some Javadoc comment",
                "",
                "public class FileUnderTest {",
                "}",
                "/**",
                " * Some C comment",
                " */",
                "// Single line C comment"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 4;
        int startColNo = 0;
        int endLineNo = 4;
        int endColNo = 0;
        assertTrue("Should have intersection with C comment", fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startColNo = 1;
        assertTrue("Should have intersection with C comment", fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 5;
        startColNo = 0;
        endLineNo = 5;
        endColNo = 0;
        assertFalse("Should not have intersection with C comment", fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 6;
        startColNo = 0;
        endLineNo = 6;
        endColNo = 0;
        assertTrue("Should have intersection with single line C comment", fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 5;
        startColNo = 10;
        endLineNo = 6;
        endColNo = 10;
        assertTrue("Should have intersection with single line C comment", fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents(new File("src/test/java/com/example/junit/package-info.java"));
        assertTrue("File in package info should be considered as package info", fileContents.inPackageInfo());
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents(new File("src/test/java/com/example/junit/FileUnderTest.java"));
        String[] lines = fileContents.getLines();
        assertEquals("First line should not be blank", false, fileContents.lineIsBlank(0));
        assertEquals("Last line should be blank", true, fileContents.lineIsBlank(lines.length - 1));
        assertEquals("Middle line should not be blank", false, fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents(new File("src/test/java/com/example/junit/FileUnderTest.java"));
        String[] lines = fileContents.getLines();
        assertEquals("First line should not be a comment", false, fileContents.lineIsComment(0));
        String singleLineComment = "// Single line comment";
        assertEquals("Single line comment should be considered as comment", true, fileContents.lineIsComment(Arrays.asList(singleLineComment).indexOf(lines[1])));
        assertEquals("Middle line should not be a comment", false, fileContents.lineIsComment(2));
    }
}