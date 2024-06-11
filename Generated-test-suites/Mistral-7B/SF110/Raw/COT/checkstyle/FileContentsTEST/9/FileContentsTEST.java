package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "TestFile.java";
        FileContents fileContents = new FileContents(fileName, new String[]{"public class TestClass"});
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(1, fileContents.getLines().length);
        assertEquals("public class TestClass", fileContents.getLines()[0]);
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "TestFile.java";
        String fileContent = "public class TestClass";
        FileText fileText = Mockito.mock(FileText.class);
        when(fileText.get(0)).thenReturn(fileContent);
        FileContents fileContents = new FileContents(fileText);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileContent, fileContents.getLines()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportSingleLineCommentWithInvalidStartLineNo() {
        FileContents fileContents = new FileContents("TestFile.java", Arrays.asList(new FileText("TestContent")));
        fileContents.reportSingleLineComment("JavaDoc", -1, 0);
    }

    @Test
    public void testReportCppComment() {
        String fileName = "TestFile.java";
        String fileContent = "// This is a single line comment\npublic class TestClass{\n// This is a multi line comment\n// */\n}";
        FileText fileText = Mockito.mock(FileText.class);
        when(fileText.get(0)).thenReturn("// This is a single line comment");
        when(fileText.get(1)).thenReturn("public class TestClass");
        when(fileText.get(2)).thenReturn("// This is a multi line comment");
        when(fileText.get(3)).thenReturn("}");
        FileContents fileContents = new FileContents(fileText);
        fileContents.reportCppComment(1, 0);

        Map<Integer, TextBlock> expectedCppComments = new HashMap<>();
        TextBlock singleLineComment = new TextBlock("This is a single line comment", 1, 0, 1, 0);
        TextBlock multiLineComment = new TextBlock("This is a multi line comment", 2, 0, 3, 3);
        expectedCppComments.put(1, singleLineComment);
        expectedCppComments.put(2, multiLineComment);
        verify(fileContents).reportCComment(eq(1), eq(0), eq(2), eq(3), anyList());
        assertEquals(expectedCppComments, fileContents.getCppComments());
    }

    @Test
    public void testReportCComment() {
        String fileName = "TestFile.java";
        String fileContent = "/* This is a multi line comment\n */\npublic class TestClass";
        FileText fileText = Mockito.mock(FileText.class);
        when(fileText.get(0)).thenReturn("/* This is a multi line comment */");
        when(fileText.get(1)).thenReturn("public class TestClass");
        FileContents fileContents = new FileContents(fileText);
        fileContents.reportCComment(1, 0, 1, 0);

        Map<Integer, List<TextBlock>> expectedCComments = new HashMap<>();
        List<TextBlock> cCommentList = Lists.newArrayList();
        TextBlock multiLineComment = new TextBlock("This is a multi line comment", 1, 0, 1, 0);
        cCommentList.add(multiLineComment);
        expectedCComments.put(1, cCommentList);
        verify(fileContents).reportJavadocComment(eq(1), eq(0), anyList());
        assertEquals(expectedCComments, fileContents.getCComments());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        String fileContent = "/** This is a javadoc comment */\npublic class TestClass{\n}";
        FileText fileText = Mockito.mock(FileText.class);
        when(fileText.get(0)).thenReturn("/** This is a javadoc comment */");
        when(fileText.get(1)).thenReturn("public class TestClass{\n}");
        FileContents fileContents = new FileContents(fileText);
        TextBlock javadocComment = new TextBlock("This is a javadoc comment", 0, 0, 0, 0);
        TextBlock blankLine = new TextBlock("", 1, 0, 1, 0);
        TextBlock commentBeforeJavadoc = new TextBlock("", 2, 0, 2, 0);

        verify(fileContents).reportJavadocComment(eq(1), eq(0), anyList());
        assertEquals(javadocComment, fileContents.getJavadocBefore(2));
        assertEquals(commentBeforeJavadoc, fileContents.getJavadocBefore(1));
        assertEquals(blankLine, fileContents.getJavadocBefore(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetJavadocBeforeWithNegativeIndex() {
        FileContents fileContents = new FileContents("TestFile.java", Arrays.asList(new FileText("TestContent")));
        fileContents.getJavadocBefore(-1);
    }

    @Test
    public void testGetLines() {
        String fileName = "TestFile.java";
        String fileContent = "public class TestClass{\n    public void testMethod() {\n        int a = 5;\n        int b = 10;\n        int c = a + b;\n    }\n}";
        FileText fileText = Mockito.mock(FileText.class);
        when(fileText.get(0)).thenReturn("public class TestClass");
        when(fileText.get(1)).thenReturn("{\n    public void testMethod() {\n")
                .thenReturn("        int a = 5;\n")
                .thenReturn("        int b = 10;\n")
                .thenReturn("        int c = a + b;\n")
                .thenReturn("}\n}");
        FileContents fileContents = new FileContents(fileText);
        List<String> lines = Arrays.asList(fileContents.getLines());
        assertEquals(4, lines.size());
        assertEquals("public class TestClass", lines.get(0));
        assertEquals("{\n    public void testMethod() {\n", lines.get(1));
        assertEquals("        int a = 5;\n", lines.get(2));
        assertEquals("        int b = 10;\n", lines.get(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String fileContent = "/* This is a multi line comment\n */\npublic class TestClass{\n// This is a single line comment\n}";
        FileText fileText = Mockito.mock(FileText.class);
        when(fileText.get(0)).thenReturn("/* This is a multi line comment */");
        when(fileText.get(1)).thenReturn("public class TestClass");
        when(fileText.get(2)).thenReturn("// This is a single line comment");
        FileContents fileContents = new FileContents(fileText);
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 1;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 1;
        startColNo = 5;
        endLineNo = 1;
        endColNo = 10;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 1;
        startColNo = 0;
        endLineNo = 2;
        endColNo = 5;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 1;
        startColNo = 0;
        endLineNo = 2;
        endColNo = 10;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 1;
        startColNo = 0;
        endLineNo = 2;
        endColNo = 11;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "TestFile.java";
        FileContents fileContents = new FileContents(fileName, Arrays.asList(new FileText("TestContent")));
        assertFalse(fileContents.inPackageInfo());

        fileName = "TestFile-package.java";
        fileContents = new FileContents(fileName, Arrays.asList(new FileText("TestContent")));
        assertTrue(fileContents.inPackageInfo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasIntersectionWithCommentWithInvalidStartLineNo() {
        FileContents fileContents = new FileContents("TestFile.java", Arrays.asList(new FileText("TestContent")));
        fileContents.hasIntersectionWithComment(-1, 0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasIntersectionWithCommentWithInvalidEndLineNo() {
        FileContents fileContents = new FileContents("TestFile.java", Arrays.asList(new FileText("TestContent")));
        fileContents.hasIntersectionWithComment(1, 0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasIntersectionWithCommentWithInvalidStartColNo() {
        FileContents fileContents = new FileContents("TestFile.java", Arrays.asList(new FileText("TestContent")));
        fileContents.hasIntersectionWithComment(1, -1, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasIntersectionWithCommentWithInvalidEndColNo() {
        FileContents fileContents = new FileContents("TestFile.java", Arrays.asList(new FileText("TestContent")));
        fileContents.hasIntersectionWithComment(1, 0, 1, -1);
    }
}