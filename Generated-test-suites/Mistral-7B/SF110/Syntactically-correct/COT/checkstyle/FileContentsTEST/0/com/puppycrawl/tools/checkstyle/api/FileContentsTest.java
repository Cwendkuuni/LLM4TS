package com.example.junit.test.filecontents;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileTextMock;

    @Before
    public void setup() {
        fileTextMock = Mockito.mock(FileText.class);
        fileContents = new FileContents("testFile.java", fileTextMock);
    }

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};

        // When
        fileContents = new FileContents("testFile.java", lines);

        // Then
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        // Given
        String fileName = "testFile.java";
        String fileContent = "package com.example.test;\n" +
                "/**\n" +
                "* This is a test file.\n" +
                "* @author John Doe\n" +
                "*/\n" +
                "public class TestClass {\n";
        fileTextMock = Mockito.spy(FileText.fromString(fileContent));

        // When
        fileContents = new FileContents(fileTextMock);

        // Then
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileContent, fileContents.getText().toString());
    }

    @Test
    public void testReportSingleLineComment() {
        // Given
        int startLineNo = 1;
        int startColNo = 5;

        // When
        fileContents.reportCppComment(startLineNo, startColNo);

        // Then
        TextBlock comment = fileContents.cppComments.get(startLineNo);
        assertNotNull(comment);
        assertEquals(startLineNo, comment.getBeginLine());
        assertEquals(startColNo, comment.getBeginColumn());
    }

    @Test
    public void testGetCppComments() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);
        int startLineNo = 1;
        int startColNo = 5;
        fileContents.reportCppComment(startLineNo, startColNo);

        // When
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        // Then
        TextBlock comment = cppComments.get(startLineNo);
        assertNotNull(comment);
        assertEquals(startLineNo, comment.getBeginLine());
        assertEquals(startColNo, comment.getBeginColumn());
    }

    @Test
    public void testReportBlockComment() {
        // Given
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        // When
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        List<TextBlock> clangComments = fileContents.clangComments.get(startLineNo);
        assertNotNull(clangComments);
        assertEquals(1, clangComments.size());
        TextBlock comment = clangComments.get(0);
        assertNotNull(comment);
        assertEquals(startLineNo, comment.getBeginLine());
        assertEquals(startColNo, comment.getBeginColumn());
        assertEquals(endLineNo, comment.getEndLine());
        assertEquals(endColNo, comment.getEndColumn());
    }

    @Test
    public void testGetCComments() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        // When
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        // Then
        List<TextBlock> comment = cComments.get(startLineNo);
        assertNotNull(comment);
        assertEquals(1, comment.size());
        TextBlock cComment = comment.get(0);
        assertNotNull(cComment);
        assertEquals(startLineNo, cComment.getBeginLine());
        assertEquals(startColNo, cComment.getBeginColumn());
        assertEquals(endLineNo, cComment.getEndLine());
        assertEquals(endColNo, cComment.getEndColumn());
    }

    @Test
    public void testExtractCComment() {
        // Given
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        // When
        String[] extractedComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        assertEquals(3, extractedComment.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetJavadocBeforeWithInvalidLineNo() {
        // Given
        int lineNoBefore = -1;

        // When & Then
        fileContents.getJavadocBefore(lineNoBefore);
    }

    @Test
    public void testGetJavadocBeforeWithValidLineNo() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);
        int lineNoBefore = 3;

        // When
        TextBlock javadoc = fileContents.getJavadocBefore(lineNoBefore);

        // Then
        assertEquals(1, javadoc.getEndLine());
        assertEquals("* This is a test file.", javadoc.getText().toString());
    }

    @Test
    public void testGetText() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);

        // When
        FileText text = fileContents.getText();

        // Then
        String expected = "package com.example.test;\n" +
                "/**\n" +
                "* This is a test file.\n" +
                "* @author John Doe\n" +
                "*/\n" +
                "public class TestClass {\n";
        assertEquals(expected, text.toString());
    }

    @Test
    public void testGetLines() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);

        // When
        String[] actual = fileContents.getLines();

        // Then
        assertArrayEquals(lines, actual);
    }

    @Test
    public void testGetLine() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);

        // When
        String actual = fileContents.getLine(1);

        // Then
        assertEquals("/**", actual);
    }

    @Test
    public void testLineIsBlank() {
        // Given
        String[] lines = new String[] {"",
                "package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);

        // When
        boolean actual = fileContents.lineIsBlank(0);

        // Then
        assertTrue(actual);
    }

    @Test
    public void testLineIsComment() {
        // Given
        String[] lines = new String[] {"",
                "package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);

        // When
        boolean actual = fileContents.lineIsComment(2);

        // Then
        assertTrue(actual);
    }

    @Test
    public void testHasIntersectionWithComment_cppComment() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;
        fileContents.reportCppComment(startLineNo, startColNo);

        // When
        boolean actual = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        assertTrue(actual);
    }

    @Test
    public void testHasIntersectionWithComment_clangComment() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        // When
        boolean actual = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        assertTrue(actual);
    }

    @Test
    public void testHasIntersectionWithComment_noIntersection() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        // When
        boolean actual = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);

        // Then
        assertFalse(actual);
    }

    @Test
    public void testInPackageInfo() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/"};
        fileContents = new FileContents("package-info.java", lines);

        // When
        boolean actual = fileContents.inPackageInfo();

        // Then
        assertTrue(actual);
    }

    @Test
    public void testInPackageInfo_notPackageInfo() {
        // Given
        String[] lines = new String[] {"package com.example.test;",
                "/**",
                "* This is a test file.",
                "* @author John Doe",
                "*/",
                "public class TestClass {"};
        fileContents = new FileContents("testFile.java", lines);

        // When
        boolean actual = fileContents.inPackageInfo();

        // Then
        assertFalse(actual);
    }
}