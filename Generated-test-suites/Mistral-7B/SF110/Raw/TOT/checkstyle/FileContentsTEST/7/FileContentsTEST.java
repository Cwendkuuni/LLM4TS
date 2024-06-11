package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.CommentListener;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;

public class FileContentsTest {

    @Test
    public void testFileContentsSingleLineComment() {
        String fileName = "SingleLineComment.java";
        String[] lines = new String[] {
            "// This is a single line comment",
            "public class SingleLineComment {}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);
        Comment comment = fileContents.getCppComments().get(1);
        assertEquals("// This is a single line comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
    }

    @Test
    public void testFileContentsBlockComment() {
        String fileName = "BlockComment.java";
        String[] lines = new String[] {
            "/**",
            " * This is a block comment",
            " */",
            "public class BlockComment {}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);
        List<TextBlock> clangComments = fileContents.getCComments().get(1);
        Comment comment = clangComments.get(0);
        assertEquals("* This is a block comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(2, comment.getEndLineNo());
        assertEquals(3, comment.getEndColNo());
    }

    @Test
    public void testFileContentsNoComments() {
        String fileName = "NoComments.java";
        String[] lines = new String[] {
            "public class NoComments {}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);
        assertNull(fileContents.getCppComments());
        assertNull(fileContents.getCComments());
    }

    @Test
    public void testFileContentsMixedComments() {
        String fileName = "MixedComments.java";
        String[] lines = new String[] {
            "// Single line comment",
            "/**",
            " * Block comment",
            " */",
            "public class MixedComments {}",
            ""
        };
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock singleLineComment = cppComments.get(1);
        assertEquals("// Single line comment", singleLineComment.getText());
        assertEquals(1, singleLineComment.getStartLineNo());
        assertEquals(0, singleLineComment.getStartColNo());

        List<TextBlock> clangComments = fileContents.getCComments().get(1);
        TextBlock blockComment = clangComments.get(0);
        assertEquals("* Block comment", blockComment.getText());
        assertEquals(2, blockComment.getStartLineNo());
        assertEquals(0, blockComment.getStartColNo());
        assertEquals(3, blockComment.getEndLineNo());
        assertEquals(3, blockComment.getEndColNo());
    }

    @Test
    public void testFileContentsEmptyFile() {
        String fileName = "EmptyFile.java";
        FileContents fileContents = new FileContents(new FileText(""));
        assertNull(fileContents.getCppComments());
        assertNull(fileContents.getCComments());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFileContentsInvalidLineNumber() {
        String[] lines = new String[] {
            "public class InvalidLineTest {}",
            "",
            "public void test() {}",
            ""
        };
        FileContents fileContents = new FileContents("InvalidLineTest.java", lines);
        fileContents.getJavadocBefore(10);
    }
}