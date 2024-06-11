package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionWithFilenameAndLines() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "/**",
                " * This is a test file.",
                " */",
                "public class TestClass {",
                "}",
                "" };

        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        assertEquals(Arrays.toString(lines), Arrays.toString(fileContents.getLines()));
        assertNull(fileContents.getText());
    }

    @Test
    public void testFileContentsConstructionWithFileText() {
        String fileName = "TestFile.java";
        String fileTextContent = "package test;\n\n"
                + "/**\n"
                + " * This is a test file.\n"
                + " */\n"
                + "public class TestClass {\n"
                + "}";

        FileText fileText = new FileText(Collections.singletonList(fileTextContent));
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(1, fileContents.getLines().length);
        assertEquals(fileTextContent, fileContents.getLines()[0]);
        assertSame(fileText, fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "// This is a single line comment.",
                "/**",
                " * This is a Javadoc comment.",
                " */",
                "public class TestClass {\n"
                + "}",
                "" };

        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 1;

        fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);

        Comment cppComment = fileContents.cppComments.get(startLineNo);

        assertEquals(startLineNo, cppComment.getStartLineNumber());
        assertEquals(startColNo, cppComment.getStartColumn());
        assertEquals(startColNo + cppComment.getText().length(), cppComment.getEndColumn());
        assertEquals("// This is a single line comment.", cppComment.getText());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "" };

        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 37;

        fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);

        List<TextBlock> cComment = fileContents.clangComments.get(startLineNo);

        assertEquals(1, cComment.size());
        assertEquals(startLineNo, cComment.get(0).getStartLineNumber());
        assertEquals(startColNo, cComment.get(0).getStartColumn());
        assertEquals(endLineNo, cComment.get(0).getEndLineNumber());
        assertEquals(endColNo, cComment.get(0).getEndColumn());
        assertEquals("/**\n * This is the Javadoc for the TestClass.\n */", cComment.get(0).getText());
    }

    @Test
    public void testGetCppComments() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "// This is a single line CPP comment.",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "" };

        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertEquals(1, cppComments.size());
        assertEquals(1, cppComments.get(1).getStartLineNumber());
        assertEquals(1, cppComments.get(1).getStartColumn());
        assertEquals(1, cppComments.get(1).getEndLineNumber());
        assertEquals(23, cppComments.get(1).getEndColumn());
        assertEquals("// This is a single line CPP comment.", cppComments.get(1).getText());
    }

    @Test
    public void testGetCComments() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "/**\n"
                + " * This is a C comment block.\n"
                + " */" };

        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertEquals(1, cComments.size());
        assertEquals(1, cComments.get(1).size());
        TextBlock cComment = cComments.get(1).get(0);

        assertEquals(1, cComment.getStartLineNumber());
        assertEquals(1, cComment.getStartColumn());
        assertEquals(2, cComment.getEndLineNumber());
        assertEquals(44, cComment.getEndColumn());
        assertEquals("/**\n * This is a C comment block.\n */", cComment.getText());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "/**\n"
                + " * This is the Javadoc for the method testMethod().\n"
                + " */",
                "public void testMethod() {\n"
                + "}" };

        FileContents fileContents = new FileContents(fileName, lines);

        int lineNoBefore = 4;

        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);

        assertEquals(3, javadocBefore.getStartLineNumber());
        assertEquals(1, javadocBefore.getStartColumn());
        assertEquals(3, javadocBefore.getEndLineNumber());
        assertEquals(42, javadocBefore.getEndColumn());
        assertEquals(" * This is the Javadoc for the TestClass.", javadocBefore.getText());
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "// This is a single line CPP comment.",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "/**\n"
                + " * This is a C comment block.\n"
                + " */",
                "public int testMethod(int param) {\n"
                + "// This is a single line CPP comment.\n"
                + "// This is another single line CPP comment.\n"
                + "}",
                "" };

        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 2;
        int startColNo = 1;
        int endLineNo = 5;
        int endColNo = 14;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 3;
        startColNo = 1;
        endLineNo = 6;
        endColNo = 42;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 4;
        startColNo = 1;
        endLineNo = 5;
        endColNo = 14;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 5;
        startColNo = 1;
        endLineNo = 6;
        endColNo = 42;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";

        FileContents fileContents = new FileContents(fileName);

        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "public int testMethod(int param) {\n"
                + "}" };

        FileContents fileContents = new FileContents(fileName, lines);

        int lineNo1 = 1;
        int lineNo2 = 3;
        int lineNo3 = 5;

        assertTrue(fileContents.lineIsBlank(lineNo1));
        assertFalse(fileContents.lineIsBlank(lineNo2));
        assertFalse(fileContents.lineIsBlank(lineNo3));
    }

    @Test
    public void testLineIsComment() {
        String fileName = "TestFile.java";
        String[] lines = { "package test;",
                "// This is a single line comment.",
                "/**",
                " * This is the Javadoc for the TestClass.",
                " */",
                "public class TestClass {\n"
                + "}",
                "public int testMethod(int param) {\n"
                + "// This is a single line comment.\n"
                + "}" };

        FileContents fileContents = new FileContents(fileName, lines);

        int lineNo1 = 1;
        int lineNo2 = 2;
        int lineNo3 = 5;

        assertTrue(fileContents.lineIsComment(lineNo1));
        assertFalse(fileContents.lineIsComment(lineNo2));
        assertTrue(fileContents.lineIsComment(lineNo3));
    }
}