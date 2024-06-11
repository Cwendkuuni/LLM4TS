package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionFromFile() {
        String filename = "FileWithComments.txt";
        String[] lines = new String[] {
            "@Test",
            "public void testSomething() {",
            "    // This is a single line comment",
            "    int a = 5;",
            "    System.out.println(\"The value of a is: \" + a);",
            "}",
            "/**",
            "* This is a Javadoc comment",
            "*/"
        };

        FileContents fileContents = new FileContents(filename, lines);

        FileText text = fileContents.getText();
        assertEquals(lines.length, text.size());
        for (int i = 0; i < lines.length; ++i) {
            assertEquals(lines[i], text.get(i));
        }

        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        TextBlock cppComment = cppComments.values().iterator().next();
        List<String> commentLines = new ArrayList<>();
        commentLines.add("This is a single line comment");
        Comment expectedCppComment = new Comment(commentLines.toArray(new String[commentLines.size()]), 12, 1);
        assertEquals(expectedCppComment.getText().toString(), cppComment.getText().toString());

        Map<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        assertEquals(0, clangComments.size());

        Map<Integer, TextBlock> javadocComments = fileContents.getJavadocComments();
        assertEquals(1, javadocComments.size());
        TextBlock javadocComment = javadocComments.values().iterator().next();
        List<String> javadocCommentLines = new ArrayList<>();
        javadocCommentLines.add("* This is a Javadoc comment");
        Comment expectedJavadocComment = new Comment(javadocCommentLines.toArray(new String[javadocCommentLines.size()]), 5, 13, 5, 44);
        assertEquals(expectedJavadocComment.getText().toString(), javadocComment.getText().toString());
    }

    @Test
    public void testFileContentsConstructionFromFileText() {
        String filename = "FileWithComments.txt";
        String fileText = "filename=FileWithComments.txt\n" +
                "@Test\n" +
                "public void testSomething() {\n" +
                "    // This is a single line comment\n" +
                "    int a = 5;\n" +
                "    System.out.println(\"The value of a is: \" + a)\n" +
                "}\n" +
                "/**\n" +
                "* This is a Javadoc comment\n" +
                "*/";

        FileText fileTextObj = new FileText(filename, fileText);
        FileContents fileContents = new FileContents(fileTextObj);

        FileText text = fileContents.getText();
        assertEquals(1, text.size());
        assertEquals(fileText, text.get(0));

        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(0, cppComments.size());

        Map<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        assertEquals(0, clangComments.size());

        Map<Integer, TextBlock> javadocComments = fileContents.getJavadocComments();
        assertEquals(1, javadocComments.size());
        TextBlock javadocComment = javadocComments.values().iterator().next();
        List<String> javadocCommentLines = new ArrayList<>();
        javadocCommentLines.add("* This is a Javadoc comment");
        Comment expectedJavadocComment = new Comment(javadocCommentLines.toArray(new String[javadocCommentLines.size()]), 0, 0, 0, 0);
        assertEquals(expectedJavadocComment.getText().toString(), javadocComment.getText().toString());
    }

    @Test
    public void testExtractCComment() {
        String filename = "FileWithComments.txt";
        String[] lines = new String[] {
            "@Test",
            "public void testSomething() {",
            "    // This is a single line comment\n" +
            "    /**\n" +
            "     * This is a Javadoc comment\n" +
            "     */\n" +
            "    int a = 5;\n" +
            "    System.out.println(\"The value of a is: \" + a);\n" +
            "    /**\n" +
            "     * This is a C-style comment\n" +
            "     */\n" +
            "    int b = 10;\n" +
            "}",
            "/**\n" +
            " * This is a Javadoc comment for the file\n" +
            " */"
        };

        FileContents fileContents = new FileContents(filename, lines);

        String[] expectedCComment = fileContents.extractCComment(4, 0, 5, 0);
        String[] actualCComment = new String[] {
            "    /**\n" +
            "     * This is a C-style comment\n" +
            "     */"
        };
        assertArrayEquals(actualCComment, expectedCComment);
    }

    @Test
    public void testGetJavadocBefore() {
        String filename = "FileWithComments.txt";
        String[] lines = new String[] {
            "@Test",
            "public void testSomething() {",
            "    // This is a single line comment\n" +
            "    /**\n" +
            "     * This is a Javadoc comment for the method\n" +
            "     */\n" +
            "    int a = 5;\n" +
            "    System.out.println(\"The value of a is: \" + a);\n" +
            "}",
            "/**\n" +
            " * This is a Javadoc comment for the class\n" +
            " */"
        };

        FileContents fileContents = new FileContents(filename, lines);

        TextBlock expectedJavadocComment = fileContents.getJavadocBefore(2);
        TextBlock actualJavadocComment = fileContents.getJavadocBefore(3);

        assertEquals(expectedJavadocComment, actualJavadocComment);
    }

}