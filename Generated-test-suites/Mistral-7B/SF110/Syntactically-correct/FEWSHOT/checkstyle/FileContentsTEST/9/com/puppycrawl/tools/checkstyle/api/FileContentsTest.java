package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import com.google.common.collect.*;
import java.io.*;
import java.util.*;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.FileContents.*;

public class FileContentsTest {

    @Test
    public void testFileContents() throws IOException {
        String fileName = "TestFile.java";
        String[] lines = {"package com.example;", "", "public class TestClass {}", "", "/** This is a test comment */", ""};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertEquals("", fileContents.getLine(0));
        assertEquals("package com.example;", fileContents.getLine(1));
        assertEquals("public class TestClass ", fileContents.getLine(3));
        assertEquals("/** This is a test comment */", fileContents.getLine(5));

        FileContents fileTextFileContents = new FileContents(new FileText(new ByteArrayInputStream(new byte[]{})));
        assertEquals("", fileTextFileContents.getFileName());
        assertEquals(Collections.emptyList(), fileTextFileContents.getLines());

        String fileName2 = "TestFile2.java";
        String[] lines2 = {"package com.example;", "", "/** This is a test comment */", "public class TestClass {}", ""};
        FileContents fileContents2 = new FileContents(fileName2, lines2);
        assertEquals(fileName2, fileContents2.getFileName());
        assertEquals(Arrays.asList(lines2), fileContents2.getLines());
        assertEquals("package com.example;", fileContents2.getLine(1));
        assertEquals("/** This is a test comment */", fileContents2.getLine(2));
        assertEquals("public class TestClass ", fileContents2.getLine(4));
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "TestFile.java";
        String[] lines = {"package com.example;", "/** This is a test comment */", "public class TestClass "};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 0;
        fileContents.reportSingleLineComment("Javadoc", startLineNo, startColNo);
        TextBlock comment = fileContents.getJavadocBefore(startLineNo - 1);
        assertEquals("This is a test comment ", comment.getText());
        assertEquals(startLineNo - 1, comment.getStartLineNo());
        assertEquals(startLineNo - 1, comment.getEndLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(0, comment.getEndColNo());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "TestFile.java";
        String[] lines = {"package com.example;", "/**", " * This is a test comment", " */", "public class TestClass "};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 3;
        int endColNo = 25;
        fileContents.reportBlockComment("Javadoc", startLineNo, startColNo, endLineNo, endColNo);
        TextBlock comment = fileContents.getJavadocBefore(startLineNo - 1);
        List<TextBlock> entries = fileContents.getCComments().get(startLineNo);
        assertEquals("This is a test comment ", comment.getText());
        assertEquals(startLineNo - 1, comment.getStartLineNo());
        assertEquals(startLineNo - 1, comment.getEndLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(0, comment.getEndColNo());
        assertEquals(1, entries.size());
        TextBlock entry = entries.get(0);
        assertEquals(" * This is a test comment", entry.getText());
        assertEquals(startLineNo, entry.getStartLineNo());
        assertEquals(endLineNo, entry.getEndLineNo());
        assertEquals(startColNo, entry.getStartColNo());
        assertEquals(endColNo, entry.getEndColNo());
    }

    @Test
    public void testGetCComments() {
        String fileName = "TestFile.java";
        String[] lines = {"package com.example;", "/**", " * This is a test comment", " */", "/**", " * Another test comment", " * This is yet another test comment", " */", "public class TestClass "};
        FileContents fileContents = new FileContents(fileName, lines);
        List<TextBlock> comments = fileContents.getCppComments().get(1);
        List<TextBlock> comments2 = fileContents.getCComments().get(3);
        assertEquals(1, comments.size());
        assertEquals(" * This is a test comment", comments.get(0).getText());
        assertEquals(1, comments2.size());
        assertEquals(" * Another test comment", comments2.get(0).getText());
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String[] lines = {"package com.example;", "/**", " * This is a test comment", " */", "public class TestClass "};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 3;
        int endColNo = 25;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo - 1, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo - 1, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo - 1));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", FileContents.stripLeadingHyphens("-f"));
        assertEquals("foo", FileContents.stripLeadingHyphens("--foo"));
        assertEquals("-foo", FileContents.stripLeadingHyphens("---foo"));
        assertNull(FileContents.stripLeadingHyphens(null));
    }
}