package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.util.FileText;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testEmptyFile() {
        String fileName = "emptyFile.java";
        FileContents fileContents = new FileContents(fileName);
        List<String> lines = fileContents.getLines();
        assertEquals(Collections.emptyList(), lines);
        assertEquals(Collections.emptyMap(), fileContents.getCComments());
        assertEquals(Collections.emptyMap(), fileContents.getCppComments());
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testSingleLineComment() {
        String fileName = "singleLineComment.java";
        String expectedComment = "This is a single line comment.";
        String[] lines = new String[]{"//" + expectedComment};
        FileContents fileContents = new FileContents(fileName, lines);
        List<String> linesFromFile = fileContents.getLines();
        assertEquals(Arrays.asList(lines), linesFromFile);
        assertEquals(ImmutableMap.of(1, new Comment(new String[]{expectedComment}, 0, 1)), fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testBlockComment() {
        String fileName = "blockComment.java";
        String expectedComment = "/**\n * This is a block comment.\n */";
        String[] lines = new String[]{"/**", expectedComment, "*/"};
        FileContents fileContents = new FileContents(fileName, lines);
        List<String> linesFromFile = fileContents.getLines();
        assertEquals(Arrays.asList(lines), linesFromFile);
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(1, Lists.newArrayList(new Comment(new String[]{expectedComment}, 0, 1, 2)));
        assertEquals(expectedCComments, fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testMixedComments() {
        String fileName = "mixedComments.java";
        String expectedSingleLineComment = "//This is a single line comment.";
        String expectedBlockComment = "/**\n * This is a block comment.\n */";
        String[] lines = new String[]{expectedSingleLineComment, "", "", expectedBlockComment};
        FileContents fileContents = new FileContents(fileName, lines);
        List<String> linesFromFile = fileContents.getLines();
        assertEquals(Arrays.asList(lines), linesFromFile);
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(3, Lists.newArrayList(new Comment(new String[]{expectedBlockComment}, 0, 3, 3)));
        ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(2, new Comment(new String[]{""}, 0, 2));
        assertEquals(expectedCppComments, fileContents.getCppComments());
        assertEquals(expectedCComments, fileContents.getCComments());
        assertEquals(expectedComment, fileContents.getJavadocBefore(3).getText());
    }

    @Test
    public void testMultipleBlockComments() {
        String fileName = "multipleBlockComments.java";
        String expectedComment1 = "/**\n * This is the first block comment.\n */";
        String expectedComment2 = "/**\n * This is the second block comment.\n */";
        String[] lines = new String[]{"", "", expectedComment1, "", "", expectedComment2};
        FileContents fileContents = new FileContents(fileName, lines);
        List<String> linesFromFile = fileContents.getLines();
        assertEquals(Arrays.asList(lines), linesFromFile);
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(3, Lists.newArrayList(new Comment(new String[]{expectedComment1}, 0, 3, 3)), 5, Lists.newArrayList(new Comment(new String[]{expectedComment2}, 0, 5, 5)));
        assertEquals(expectedCComments, fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "javadocBefore.java";
        String expectedJavadoc = "/**\n * This is a Javadoc comment.\n */";
        String[] lines = new String[]{"/**", expectedJavadoc, "*/"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(expectedJavadoc, fileContents.getJavadocBefore(1).getText());
    }

    @Test
    public void testGetJavadocBeforeWithEmptyLineBefore() {
        String fileName = "javadocBeforeEmptyLine.java";
        String expectedJavadoc = "/**\n * This is a Javadoc comment.\n */";
        String[] lines = new String[]{"", expectedJavadoc, "*/"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(expectedJavadoc, fileContents.getJavadocBefore(1).getText());
    }

    @Test
    public void testGetJavadocBeforeWithNoJavadoc() {
        String fileName = "noJavadoc.java";
        String[] lines = new String[]{"public class MyClass"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testFileTextConstructor() {
        String fileName = "fileTextConstructor.java";
        String[] lines = new String[]{"public class MyClass"};
        FileText fileText = new FileText(Arrays.asList(lines));
        FileContents fileContents = new FileContents(fileName, fileText);
        List<String> linesFromFile = fileContents.getLines();
        assertEquals(Arrays.asList(lines), linesFromFile);
        assertEquals(ImmutableMap.of(), fileContents.getCComments());
        assertEquals(ImmutableMap.of(), fileContents.getCppComments());
        assertNull(fileContents.getJavadocBefore(0));
    }
}