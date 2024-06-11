package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileContents.Comment;
import com.puppycrawl.tools.checkstyle.api.FileContents.TextBlock;
import com.google.common.collect.ImmutableMap;

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileTextMock;
    private String fileName;

    @Before
    public void setup() {
        fileName = "sample.java";
        fileTextMock = mock(FileText.class);
        fileContents = new FileContents(fileName, fileTextMock);
    }

    @Test
    public void testFileContentsCreationWithFile() {
        // Given
        File file = Mockito.mock(File.class);
        when(fileTextMock.getFile()).thenReturn(file);

        // When
        String[] lines = {"package p1;", "public class Sample {}", "", "/**", " * This is a Javadoc comment.", " */"};
        FileContents fileContents = new FileContents(fileName, lines);

        // Then
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
    }

    @Test
    public void testFileContentsCreationWithLines() {
        // Given

        // When
        FileContents fileContents = new FileContents(fileName, fileTextMock);

        // Then
        assertEquals(fileName, fileContents.getFileName());
        assertNull(fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        // Given
        int startLineNo = 1;
        int startColNo = 5;

        // When
        fileContents.reportCppComment(startLineNo, startColNo);

        // Then
        Comment cppComment = fileContents.cppComments.get(startLineNo);
        assertNotNull(cppComment);
    }

    @Test
    public void testGetCppComments() {
        // Given
        String[] lines = {"// This is a single line comment.", "/**", " * This is a Javadoc comment.", " */", "public class Sample {", "}"};
        fileContents = new FileContents(fileName, lines);

        // When
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        // Then
        assertEquals(2, cppComments.size());
        assertEquals(fileContents.cppComments.get(1), cppComments.get(1));
        assertEquals(fileContents.cppComments.get(3), cppComments.get(3));
    }

    @Test
    public void testExtractCComment() {
        // Given
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 5;

        // When
        String[] extractedComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        assertEquals(3, extractedComment.length);
        assertEquals("// This is a single line comment.", extractedComment[0]);
        assertEquals("/**", extractedComment[1]);
        assertEquals(" * This is a Javadoc comment.", extractedComment[2]);
    }

    @Test
    public void testGetJavadocBefore() {
        // Given
        int lineNoBefore = 2;

        // When
        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);

        // Then
        assertEquals("/**", javadocBefore.getText());
    }

    @Test
    public void testHasIntersectionWithComment() {
        // Given
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 5;

        // When
        boolean hasIntersection = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        assertTrue(hasIntersection);
    }

    @Test
    public void testInPackageInfo() {
        // Given

        // When
        boolean isPackageInfo = fileContents.inPackageInfo();

        // Then
        assertFalse(isPackageInfo);
    }
}