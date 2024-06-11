package com.example.junit4;

import static org.junit.Assert.*;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.ImmutableMap.copyOf;
import static com.puppycrawl.tools.checkstyle.api.FileContents.MATCH_SINGLELINE_COMMENT;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a single line comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertNull(fileContents.getText()); // text is not initialized in this constructor
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "testFile.java";
        String fileContent = "public class TestClass {\n  // this is a single line comment\n}";
        FileText fileText = new FileText(new File("src/test/resources/" + fileName), fileContent);
        FileContents fileContents = new FileContents(fileText);
        assertEquals("testFile.java", fileContents.getFileName());
        assertEquals(Arrays.asList(fileContent.split("\n")), fileContents.getLines());
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a single line comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        fileContents.reportSingleLineComment("", startLineNo, startColNo);
        TextBlock expectedComment = new TextBlock("this is a single line comment");
        TextBlock actualComment = fileContents.cppComments.get(startLineNo);
        assertEquals(expectedComment, actualComment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportSingleLineCommentWithInvalidLineNumber() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a single line comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 0;
        int startColNo = 1;
        fileContents.reportSingleLineComment("", startLineNo, startColNo);
    }

    @Test
    public void testGetCppComments() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a single line comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        fileContents.reportCppComment(startLineNo, startColNo);
        ImmutableMap<Integer, TextBlock> expectedComments = copyOf(fileContents.cppComments);
        ImmutableMap<Integer, TextBlock> actualComments = fileContents.getCppComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testReportCComment() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"/** This is a Javadoc comment", " *", " * @param param1 Description of param1", " */", "public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 1;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        TextBlock expectedComment = new Comment(new String[]{"This is a Javadoc comment", "*", "* @param param1 Description of param1"}, startColNo, endLineNo, endColNo - startColNo);
        TextBlock actualComment = fileContents.clangComments.get(startLineNo).get(0);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetCComments() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"/** This is a Javadoc comment", " *", " * @param param1 Description of param1", " */", "public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 1;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        ImmutableMap<Integer, List<TextBlock>> expectedComments = copyOf(fileContents.clangComments);
        ImmutableMap<Integer, List<TextBlock>> actualComments = fileContents.getCComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testExtractCComment() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"/** This is a Javadoc comment", " *", " * @param param1 Description of param1", " */", "public class TestClass {", "  // this is a C comment", "}", "// this is another single line comment"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 2;
        int startColNo = 1;
        int endLineNo = 5;
        int endColNo = 1;
        String[] expectedComments = new String[]{"/** This is a Javadoc comment", "*", "* @param param1 Description of param1", "*/"};
        String[] actualComments = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertArrayEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"/** This is a Javadoc comment", " *", " * @param param1 Description of param1", " */", "public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int lineNoBefore = 2;
        TextBlock expectedComment = new TextBlock("/** This is a Javadoc comment");
        TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {\r\n}", "    ", "\r\n"};
        FileContents fileContents = new FileContents(fileName, lines);
        int lineNo = 1;
        boolean expectedResult = true;
        boolean actualResult = fileContents.lineIsBlank(lineNo);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLineIsComment() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"// this is a single line comment", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int lineNo = 0;
        boolean expectedResult = true;
        boolean actualResult = fileContents.lineIsComment(lineNo);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"/** This is a Javadoc comment", " *", " * @param param1 Description of param1", " */", "public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 1;
        boolean expectedResult = true;
        boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";
        FileContents fileContents = new FileContents(fileName, Collections.emptyList());
        boolean expectedResult = true;
        boolean actualResult = fileContents.inPackageInfo();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHasIntersectionWithCCommentWithInvalidStartLineNumber() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 0;
        int startColNo = 1;
        int endLineNo = 1;
        int endColNo = 1;
        boolean expectedResult = false;
        boolean actualResult = fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testHasIntersectionWithCCommentWithInvalidEndLineNumber() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 0;
        int endColNo = 1;
        boolean expectedResult = false;
        boolean actualResult = fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasIntersectionWithCCommentWithInvalidStartColNumber() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 1;
        fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasIntersectionWithCCommentWithInvalidEndColNumber() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "  // this is a C comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 1;
        int endColNo = 0;
        fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo);
    }
}