package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.grammars.JavaGrammar;
import com.puppycrawl.tools.checkstyle.grammars.CPlusPlusGrammar;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "TestFile.java";
        String[] lines = new String[] { "public class TestFile {", "    int a = 1;", "}" };

        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(3, fileContents.getLines().length);
        assertEquals("public class TestFile {", fileContents.getLines()[0]);
        assertEquals("    int a = 1;", fileContents.getLines()[1]);
        assertEquals("}", fileContents.getLines()[2]);
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "TestFile.java";
        String fileContentsText = "public class TestFile {\n    int a = 1;\n}";

        FileText fileText = mock(FileText.class);
        when(fileText.get(0)).thenReturn(fileContentsText);
        when(fileText.getFile()).thenReturn(new File(fileName));

        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(1, fileContents.getLines().length);
        assertEquals(fileContentsText, fileContents.getLines()[0]);
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "TestFile.cpp";
        int startLineNo = 5;
        int startColNo = 10;

        FileContents fileContents = new FileContents(fileName, new Object() {});
        fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);

        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        TextBlock comment = cppComments.get(startLineNo);
        assertEquals("//", comment.getPrefix());
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
        assertEquals(startLineNo, comment.getEndLineNo());
        assertEquals(startColNo, comment.getEndColNo());
        assertEquals("", comment.getText());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "TestFile.cpp";
        int startLineNo = 5;
        int startColNo = 10;
        int endLineNo = 7;
        int endColNo = 20;

        FileContents fileContents = new FileContents(fileName, new Object() {});
        fileContents.reportBlockComment("", startLineNo, startColNo, endLineNo, endColNo);

        Map<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        assertEquals(1, clangComments.size());
        List<TextBlock> entries = clangComments.get(startLineNo);
        assertEquals(1, entries.size());
        TextBlock comment = entries.get(0);
        assertEquals("/*", comment.getPrefix());
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
        assertEquals(endLineNo, comment.getEndLineNo());
        assertEquals(endColNo, comment.getEndColNo());
        assertEquals("", comment.getText());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        int lineNoBefore = 2;

        FileContents fileContents = new FileContents(fileName, new Object() {});
        fileContents.getText().set(lineNoBefore - 1, "/** This is a javadoc comment */");
        fileContents.getText().set(lineNoBefore, "public class TestFile {");

        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals("/** This is a javadoc comment */", javadocBefore.getText());
        assertEquals(lineNoBefore - 1, javadocBefore.getStartLineNo());
        assertEquals(0, javadocBefore.getStartColNo());
        assertEquals(lineNoBefore - 1, javadocBefore.getEndLineNo());
        assertEquals(javadocBefore.getText().length(), javadocBefore.getEndColNo());
    }

    @Test
    public void testExtractCComment() {
        String fileName = "TestFile.cpp";
        int startLineNo = 5;
        int startColNo = 10;
        int endLineNo = 7;
        int endColNo = 20;

        FileContents fileContents = new FileContents(fileName, new Object() {});
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        String[] result = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(3, result.length);
        assertEquals("TestFile.cpp", result[0]);
        assertEquals("", result[1]);
        assertEquals("", result[2]);

        when(fileContents.line(startLineNo - 1)).thenReturn("/** This is a javadoc comment */");
        result = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(3, result.length);
        assertEquals("/** This is a javadoc comment */", result[0]);
        assertEquals("", result[1]);
        assertEquals("", result[2]);
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        int startLineNo = 5;
        int startColNo = 10;
        int endLineNo = 7;
        int endColNo = 20;

        FileContents fileContents = new FileContents(fileName, new Object() {});

        fileContents.reportCppComment(startLineNo, startColNo);
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents.text = new FileText(Arrays.asList(new String[] { "public class TestFile {", "/** This is a javadoc comment */", "int a = 1;", "}" }));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents.javadocComments.put(endLineNo - 1, mock(Comment.class));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents.cppComments.put(startLineNo, mock(Comment.class));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents.clangComments.put(startLineNo, Collections.singletonList(mock(Comment.class)));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents.clangComments.put(startLineNo - 1, Collections.singletonList(mock(Comment.class)));
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "TestFile/package-info.java";
        assertTrue(new FileContents(fileName, new Object()).inPackageInfo());

        String fileName2 = "TestFile.java";
        assertFalse(new FileContents(fileName2, new Object()).inPackageInfo());
    }

}