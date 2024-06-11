package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testEmptyFile() {
        String filename = "EmptyFile.java";
        FileContents fileContents = new FileContents(filename);
        assertEquals(filename, fileContents.getFileName());
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertNull(fileContents.getJavadocBefore(0));
        assertNull(fileContents.getJavadocBefore(1));
    }

    @Test
    public void testSingleLineComment() {
        String filename = "SingleLineComment.java";
        String[] lines = {"// This is a single line comment"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsComment(0));
        assertEquals("This is a single line comment", fileContents.getCComments().get(0).getContent().toString());
    }

    @Test
    public void testMultiLineComment() {
        String filename = "MultiLineComment.java";
        String[] lines = {"/**", " * This is a multi-line comment", " */"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        List<TextBlock> cComments = fileContents.getCComments().get(0);
        assertEquals(1, cComments.size());
        TextBlock comment = cComments.get(0);
        assertEquals(" * This is a multi-line comment", comment.getContent().toString());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(4, comment.getEndLineNo());
    }

    @Test
    public void testMultiLineCommentWithEmptyLine() {
        String filename = "MultiLineCommentWithEmptyLine.java";
        String[] lines = {"/**", "", " * This is a multi-line comment", " */"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
        List<TextBlock> cComments = fileContents.getCComments().get(0);
        assertEquals(1, cComments.size());
        TextBlock comment = cComments.get(0);
        assertEquals(" * This is a multi-line comment", comment.getContent().toString());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(4, comment.getEndLineNo());
    }

    @Test
    public void testExtractCComment() {
        String filename = "ExtractCComment.java";
        String[] lines = {"/**", " * This is a C comment", " * with multiple lines", " */", "", "int main() {", "}", ""};
        FileContents fileContents = new FileContents(filename, lines);
        String[] expected = {"/**", " * This is a C comment", " * with multiple lines"};
        List<TextBlock> cComments = fileContents.getCComments().get(0);
        String[] actual = new String[cComments.size()];
        for (int i = 0; i < cComments.size(); i++) {
            actual[i] = cComments.get(i).getContent().toString();
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavadocBefore() {
        String filename = "GetJavadocBefore.java";
        String[] lines = {"/**", " * This is a Javadoc comment", " */", "", "public class Main {", "}", ""};
        FileContents fileContents = new FileContents(filename, lines);
        TextBlock expected = new TextBlock(" * This is a Javadoc comment", 5, 0);
        TextBlock actual = fileContents.getJavadocBefore(6);
        assertSame(expected, actual);
    }

    @Test
    public void testGetJavadocBeforeEmpty() {
        String filename = "GetJavadocBeforeEmpty.java";
        String[] lines = {"/**", " */", "", "public class Main {", "}", ""};
        FileContents fileContents = new FileContents(filename, lines);
        assertNull(fileContents.getJavadocBefore(5));
    }

    @Test
    public void testGetJavadocBeforeNoJavadoc() {
        String filename = "GetJavadocBeforeNoJavadoc.java";
        String[] lines = {"public class Main {", "}", ""};
        FileContents fileContents = new FileContents(filename, lines);
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String filename = "HasIntersectionWithComment.java";
        String[] lines = {"/**", " * This is a Javadoc comment", " */", "// This is a single line comment", "int main() {", "}", ""};
        FileContents fileContents = new FileContents(filename, lines);
        int startLineNo = 2;
        int startColNo = 11;
        int endLineNo = 3;
        int endColNo = 16;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
        assertFalse(fileContents.hasIntersectionWithComment(5, 1, 5, 1));
    }

    @Test
    public void testInPackageInfo() {
        String filename = "InPackageInfo.java";
        FileContents fileContents = new FileContents(filename);
        assertTrue(fileContents.inPackageInfo());
        filename = "NotInPackageInfo.java";
        fileContents = new FileContents(filename);
        assertFalse(fileContents.inPackageInfo());
    }
}