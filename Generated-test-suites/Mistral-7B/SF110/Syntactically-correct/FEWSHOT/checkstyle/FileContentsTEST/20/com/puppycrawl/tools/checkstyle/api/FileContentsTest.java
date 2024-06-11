package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "SampleFile.java";
        String[] lines = {"package com.example.test;", "", "public class SampleClass {", "    // This is a single line comment", "    void method();", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertNull(fileContents.getText());
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "SampleFile.java";
        String fileContent = "package com.example.test;\n\n" +
                "public class SampleClass {\n" +
                "    // This is a single line comment\n" +
                "    void method();\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertSame(fileText, fileContents.getText());
        assertEquals(Arrays.asList(fileText.toLinesArray()), fileContents.getLines());
        assertNull(fileContents.getJavadocBefore(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testGetCppComments() {
        String fileName = "SampleFile.cpp";
        String fileContent = "// This is a C++ single line comment\n" +
                "/*\n" +
                " * This is a C++ block comment\n" +
                " */\n" +
                "int main() {\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        assertEquals(2, cppComments.size());
        TextBlock singleLineComment = cppComments.get(1);
        TextBlock blockComment = cppComments.get(3);

        assertEquals("This is a C++ single line comment", singleLineComment.getText());
        assertEquals(" * This is a C++ block comment", blockComment.getText());
    }

    @Test
    public void testGetCComments() {
        String fileName = "SampleFile.c";
        String fileContent = "/*\n" +
                " * This is a C block comment\n" +
                " */\n" +
                "int main(int argc, char *argv[]) {\n" +
                "    /* This is a C multiline comment */\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        assertEquals(1, cComments.size());
        List<TextBlock> comments = cComments.get(1);

        assertEquals(1, comments.size());
        TextBlock blockComment = comments.get(0);

        assertEquals(" * This is a C block comment", blockComment.getText());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "SampleFile.java";
        String fileContent = "/**\n" +
                " * This is a Javadoc comment before a method\n" +
                " */\n" +
                "public class SampleClass {\n" +
                "    public void method() {\n" +
                "    }\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        TextBlock javadocComment = fileContents.getJavadocBefore(1);

        assertEquals("This is a Javadoc comment before a method", javadocComment.getText());
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "SampleFile.java";
        String fileContent = "/**\n" +
                " * This is a Javadoc comment before a method\n" +
                " */\n" +
                "public class SampleClass {\n" +
                "    public void method() {\n" +
                "        // This is a Javadoc comment inside a method\n" +
                "    }\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        int startLineNo = 2;
        int startColNo = 5;
        int endLineNo = 2;
        int endColNo = 10;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_NoIntersection() {
        String fileName = "SampleFile.java";
        String fileContent = "/**\n" +
                " * This is a Javadoc comment before a method\n" +
                " */\n" +
                "public class SampleClass {\n" +
                "    public void method() {\n" +
                "    }\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        int startLineNo = 2;
        int startColNo = 15;
        int endLineNo = 3;
        int endColNo = 5;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "SampleFile.java";
        String fileContent = "package com.example.test;\n" +
                "\n" +
                "public class SampleClass {\n" +
                "    void method();\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        String fileName = "SampleFile.java";
        String fileContent = "package com.example.test;\n" +
                "// This is a Javadoc single line comment\n" +
                "public class SampleClass {\n" +
                "    void method();\n" +
                "}";

        FileText fileText = new FileText(fileName, fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);

        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "sample-module-info.java";

        FileContents fileContents = new FileContents(fileName);

        assertTrue(fileContents.inPackageInfo());

        fileName = "SampleFile.java";

        fileContents = new FileContents(fileName);

        assertFalse(fileContents.inPackageInfo());
    }
}