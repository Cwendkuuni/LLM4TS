package com.example.junit4.test;

import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.test.CheckstyleTestSupport;

public class FileContentsTest {

    @Test
    public void testFileContentsWithValidLines() {
        String fileName = "SampleFile.java";
        String[] lines = {
            "/**",
            " * This is a Javadoc comment",
            " */",
            "public class SampleFile {",
            "}",
            "",
            "// This is a single-line comment",
            "/*",
            " * This is a multi-line comment",
            " */"
        };

        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();

        List<TextBlock> expectedCppComments = Arrays.asList(
            new TextBlock("// This is a single-line comment", 7, 1, 7, 30),
            new TextBlock("/* This is a multi-line comment */", 9, 1, 11, 54)
        );
        assertEquals(expectedCppComments, cppComments.values());

        List<List<TextBlock>> expectedClangComments = Arrays.asList(
            Collections.singletonList(
                Arrays.asList(
                    new TextBlock("/**", 1, 1, 1, 3),
                    new TextBlock(" * This is a Javadoc comment", 1, 4, 1, 39),
                    new TextBlock(" */", 1, 40, 1, 44),
                    new TextBlock("public", 3, 1, 3, 5),
                    new TextBlock("class", 3, 6, 3, 10),
                    new TextBlock("SampleFile", 3, 12, 3, 21),
                    new TextBlock("{", 5, 1, 5, 1),
                    new TextBlock("}", 6, 1, 6, 1)
                )
            ),
            Collections.singletonList(
                Arrays.asList(
                    new TextBlock("/**", 9, 1, 9, 3),
                    new TextBlock(" * This is a multi-line comment", 9, 4, 9, 52),
                    new TextBlock(" */", 11, 1, 11, 4)
                )
            )
        );
        assertEquals(expectedClangComments, clangComments.values());
    }

    @Test
    public void testFileContentsWithEmptyLines() {
        String fileName = "SampleFile.java";
        String[] lines = {
            "/**",
            " * This is a Javadoc comment",
            " */",
            "",
            "",
            "// This is a single-line comment",
            "/*",
            " * This is a multi-line comment",
            " */"
        };

        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();

        List<TextBlock> expectedCppComments = Arrays.asList(
            new TextBlock("// This is a single-line comment", 6, 1, 6, 30),
            new TextBlock("/* This is a multi-line comment */", 8, 1, 10, 54)
        );
        assertEquals(expectedCppComments, cppComments.values());

        List<List<TextBlock>> expectedClangComments = Arrays.asList(
            Collections.singletonList(
                Arrays.asList(
                    new TextBlock("/**", 1, 1, 1, 3),
                    new TextBlock(" * This is a Javadoc comment", 1, 4, 1, 39),
                    new TextBlock(" */", 1, 40, 1, 44),
                    new TextBlock("public", 3, 1, 3, 5),
                    new TextBlock("class", 3, 6, 3, 10),
                    new TextBlock("SampleFile", 3, 12, 3, 21),
                    new TextBlock("{", 5, 1, 5, 1),
                    new TextBlock("}", 6, 1, 6, 1),
                    new TextBlock("", 7, 1, 7, 0),
                    new TextBlock("", 8, 1, 8, 0)
                )
            ),
            Collections.singletonList(
                Arrays.asList(
                    new TextBlock("/**", 9, 1, 9, 3),
                    new TextBlock(" * This is a multi-line comment", 9, 4, 9, 52),
                    new TextBlock(" */", 11, 1, 11, 4)
                )
            )
        );
        assertEquals(expectedClangComments, clangComments.values());
    }

    @Test
    public void testFileContentsWithMissingJavadoc() {
        String fileName = "SampleFile.java";
        String[] lines = {
            "",
            "public class SampleFile {",
            "}",
            "// This is a single-line comment",
            "/*",
            " * This is a multi-line comment",
            " */"
        };

        try {
            FileContents fileContents = new FileContents(fileName, lines);
            ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
            ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
            fail("Expected FileContentsException but got FileContents object.");
        } catch (FileContentsException e) {
            // Expected
        }
    }

    @Test
    public void testFileContentsWithInvalidLines() {
        String fileName = "SampleFile.java";
        String[] lines = {
            "/**",
            " * This is a Javadoc comment",
            " */",
            "public class SampleFile {",
            "}",
            "// This is a single-line comment",
            "/*",
            " * This is a multi-line comment",
            " */",
            "//",
            "int main() {"
        };

        try {
            FileContents fileContents = new FileContents(fileName, lines);
            ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
            ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
            fail("Expected FileContentsException but got FileContents object.");
        } catch (FileContentsException e) {
            // Expected
            CheckstyleTestSupport.assertContains("Line", 11, e.getMessage());
            CheckstyleTestSupport.assertContains("Invalid character", e.getMessage());
        }
    }

    @Test(expected = FileContentsException.class)
    public void testFileContentsWithInvalidFile() {
        String fileName = "invalidFile.txt";
        String[] lines = {};

        try {
            FileContents fileContents = new FileContents(fileName, lines);
            ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
            ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
            fail("Expected FileContentsException but got FileContents object.");
        } catch (FileContentsException e) {
            // Expected
        }
    }

    @Test
    public void testFileContentsWithInvalidFileType() {
        String fileName = "SampleFile.xml";
        String[] lines = {};

        try {
            FileContents fileContents = new FileContents(fileName, lines);
            ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
            ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
            fail("Expected FileContentsException but got FileContents object.");
        } catch (FileContentsException e) {
            CheckstyleTestSupport.assertContains("File must be a Java file", e.getMessage());
        }
    }
}