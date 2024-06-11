package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileContentsTest {

    @Test
    public void testFileContentsWithLines() {
        String fileName = "testFile.java";
        String[] lines = new String[] {
            "/**",
            " * This is a test file.",
            " */",
            "public class TestFile {",
            "",
            "}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);

        FileText text = fileContents.getText();
        String expectedFileContent = "/**\n" +
                " * This is a test file.\n" +
                " */\n" +
                "public class TestFile {\n" +
                "\n" +
                "}\n";
        String actualFileContent = new String(text.getByteArrayForReading());
        assertTrue("FileText content does not match expected content.", actualFileContent.equals(expectedFileContent));

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue("Expected empty CPP comments map.", cppComments.isEmpty());

        List<TextBlock> cComments = fileContents.getCComments().get(0);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        TextBlock expectedJavadocComment = new TextBlock(" * This is a test file.", 0, 24);
        assertTrue("Javadoc comment does not match expected comment.", javadocComment.getText().equals(expectedJavadocComment.getText()));
        assertTrue("Javadoc comment start line number does not match expected start line number.", javadocComment.getStartLineNumber() == 2);
        assertTrue("Javadoc comment start column number does not match expected start column number.", javadocComment.getStartColumnNumber() == 0);
        assertTrue("Javadoc comment end line number does not match expected end line number.", javadocComment.getEndLineNumber() == 2);
        assertTrue("Javadoc comment end column number does not match expected end column number.", javadocComment.getEndColumnNumber() == 24);
        assertTrue("C comments list size does not match expected size.", cComments.size() == 1);
        TextBlock cComment = cComments.get(0);
        TextBlock expectedCComment = new TextBlock("/**\n" +
                " * This is a test file.\n" +
                " */", 1, 3);
        assertTrue("C comment does not match expected comment.", cComment.getText().equals(expectedCComment.getText()));
        assertTrue("C comment start line number does not match expected start line number.", cComment.getStartLineNumber() == 1);
        assertTrue("C comment start column number does not match expected start column number.", cComment.getStartColumnNumber() == 0);
        assertTrue("C comment end line number does not match expected end line number.", cComment.getEndLineNumber() == 3);
        assertTrue("C comment end column number does not match expected end column number.", cComment.getEndColumnNumber() == 32);
    }

    @Test
    public void testFileContentsWithFileText() {
        String fileName = "testFile.java";
        String fileContent = "/**\n" +
                " * This is a test file.\n" +
                " */\n" +
                "public class TestFile {\n" +
                "\n" +
                "}";
        FileText fileText = new FileText(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
        FileContents fileContents = new FileContents(fileName, fileText);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue("Expected empty CPP comments map.", cppComments.isEmpty());

        List<TextBlock> cComments = fileContents.getCComments().get(0);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        TextBlock expectedJavadocComment = new TextBlock(" * This is a test file.", 0, 24);
        assertTrue("Javadoc comment does not match expected comment.", javadocComment.getText().equals(expectedJavadocComment.getText()));
        assertTrue("Javadoc comment start line number does not match expected start line number.", javadocComment.getStartLineNumber() == 2);
        assertTrue("Javadoc comment start column number does not match expected start column number.", javadocComment.getStartColumnNumber() == 0);
        assertTrue("Javadoc comment end line number does not match expected end line number.", javadocComment.getEndLineNumber() == 2);
        assertTrue("Javadoc comment end column number does not match expected end column number.", javadocComment.getEndColumnNumber() == 24);
        assertTrue("C comments list size does not match expected size.", cComments.size() == 1);
        TextBlock cComment = cComments.get(0);
        TextBlock expectedCComment = new TextBlock("/**\n" +
                " * This is a test file.\n" +
                " */", 1, 3);
        assertTrue("C comment does not match expected comment.", cComment.getText().equals(expectedCComment.getText()));
        assertTrue("C comment start line number does not match expected start line number.", cComment.getStartLineNumber() == 1);
        assertTrue("C comment start column number does not match expected start column number.", cComment.getStartColumnNumber() == 0);
        assertTrue("C comment end line number does not match expected end line number.", cComment.getEndLineNumber() == 3);
        assertTrue("C comment end column number does not match expected end column number.", cComment.getEndColumnNumber() == 32);
    }

    @Test
    public void testFileContentsWithoutComments() {
        String fileName = "testFileWithoutComments.java";
        String[] lines = new String[] {
            "public class TestFile {",
            "}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue("Expected empty CPP comments map.", cppComments.isEmpty());

        List<TextBlock> cComments = fileContents.getCComments().get(0);
        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        assertNull("Expected null Javadoc comment.", javadocComment);
        assertTrue("C comments list size does not match expected size.", cComments.isEmpty());
    }

    @Test
    public void testFileContentsWithIntersection() {
        String fileName = "testFileWithIntersection.java";
        String[] lines = new String[] {
            "/**",
            " * This is a test file.",
            " */",
            "public class TestFile {",
            "    // This is a test comment.",
            "    int testVariable;",
            "}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertTrue("Expected empty CPP comments map.", cppComments.isEmpty());

        List<TextBlock> cComments = fileContents.getCComments().get(0);
        TextBlock javadocComment = fileContents.getJavadocBefore(4);
        TextBlock expectedJavadocComment = new TextBlock(" * This is a test file.", 2, 24);
        assertTrue("Javadoc comment does not match expected comment.", javadocComment.getText().equals(expectedJavadocComment.getText()));
        assertTrue("Javadoc comment start line number does not match expected start line number.", javadocComment.getStartLineNumber() == 2);
        assertTrue("Javadoc comment start column number does not match expected start column number.", javadocComment.getStartColumnNumber() == 0);
        assertTrue("Javadoc comment end line number does not match expected end line number.", javadocComment.getEndLineNumber() == 2);
        assertTrue("Javadoc comment end column number does not match expected end column number.", javadocComment.getEndColumnNumber() == 24);
        TextBlock cComment = cComments.get(0);
        TextBlock expectedCComment = new TextBlock("// This is a test comment.", 5, 17);
        assertTrue("C comment does not match expected comment.", cComment.getText().equals(expectedCComment.getText()));
        assertTrue("C comment start line number does not match expected start line number.", cComment.getStartLineNumber() == 5);
        assertTrue("C comment start column number does not match expected start column number.", cComment.getStartColumnNumber() == 17);
        assertTrue("C comment end line number does not match expected end line number.", cComment.getEndLineNumber() == 5);
        assertTrue("C comment end column number does not match expected end column number.", cComment.getEndColumnNumber() == 31);
        assertTrue("File contents should intersect with comment at (4, 0) and (5, 17).", fileContents.hasIntersectionWithComment(4, 0, 5, 17));
    }

    @Test
    public void testFileContentsWithoutIntersection() {
        String fileName = "testFileWithoutIntersection.java";
        String[] lines = new String[] {
            "/**",
            " * This is a test file.",
            " */",
            "public class TestFile {",
            "    int testVariable;",
            "}",
            "public static void main(String[] args) {",
            "}"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue("File contents should not intersect with comment at (4, 0) and (6, 0).", !fileContents.hasIntersectionWithComment(4, 0, 6, 0));
    }

    @Test
    public void testFileContentsInPackageInfo() {
        String fileName = "testFileInPackageInfo.java";
        String[] lines = new String[] {
            "/**",
            " * This is a test file in package-info.java.",
            " */",
            "@Version 1.0.0",
            "package com.example.junit;",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue("Expected file to be in package-info.", fileContents.inPackageInfo());
    }

    @Test
    public void testFileContentsNotInPackageInfo() {
        String fileName = "testFileNotInPackageInfo.java";
        String[] lines = new String[] {
            "/**",
            " * This is a test file not in package-info.java.",
            " */",
            "public class TestFile {",
            "    int testVariable;",
            "}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertFalse("Expected file not to be in package-info.", fileContents.inPackageInfo());
    }
}