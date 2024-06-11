package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;

public class FileContentsTest {

    @Test
    public void testFileContents() {
        String[] lines = {"/** This is a Javadoc comment */", "", "public class MyClass {", "    // This is a C-style comment", "", "    int myVariable = 0;", "}", ""};
        FileContents fileContents = new FileContents("MyClass.java", lines);
        assertEquals("MyClass.java", fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        assertEquals("/** This is a Javadoc comment */", fileContents.getLines()[0]);
        assertEquals("", fileContents.getLines()[1]);
        assertNull(fileContents.getLines()[2]);
        assertEquals("public class MyClass {", fileContents.getLines()[3]);
        assertEquals("    // This is a C-style comment", fileContents.getLines()[4]);
        assertEquals("    int myVariable = 0;", fileContents.getLines()[5]);
        assertEquals("}", fileContents.getLines()[6]);
        assertEquals(6, fileContents.getText().size());
        assertEquals("MyClass.java", fileContents.getText().getFile().toString());
        assertEquals("/** This is a Javadoc comment */", fileContents.getText().get(0));
        assertEquals("", fileContents.getText().get(1));
        assertNull(fileContents.getText().get(2));
        assertEquals("public class MyClass {", fileContents.getText().get(3));
        assertEquals("    // This is a C-style comment", fileContents.getText().get(4));
        assertEquals("    int myVariable = 0;", fileContents.getText().get(5));
        assertEquals("}", fileContents.getText().get(6));
        assertNull(fileContents.getLines()[7]);
        assertNull(fileContents.getText().get(7));
    }

    @Test
    public void testFileContentsFromFileText() {
        FileContents fileContents = new FileContents(new FileText("MyClass.java"));
        assertEquals("MyClass.java", fileContents.getFileName());
        assertEquals(0, fileContents.getText().size());
    }

    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"/**", " * This is a Javadoc comment", " * @author John Doe", " */", "", "public class MyClass {", "    // This is a C-style comment", "}", ""});
        String[] expected = {"/**", "* This is a Javadoc comment", "* @author John Doe", "*/"};
        assertEquals(expected.length, fileContents.extractCComment(0, 0, 0, 0).length);
        assertArrayEquals(expected, fileContents.extractCComment(0, 0, 0, 0));
    }

    @Test
    public void testGetJavadocBefore() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"/** This is a Javadoc comment */", "", "public class MyClass {", "    // This is a C-style comment", "", "    int myVariable = 0;", "}", ""});
        Comment javadocBefore = fileContents.getJavadocBefore(3);
        assertNotNull(javadocBefore);
        assertEquals("This is a Javadoc comment", javadocBefore.getText());
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"// This is a C-style comment", "", "public class MyClass {", "", "    int myVariable = 0;", "}", ""});
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals(1, cppComments.get(2).getText().split("\\r\\n").length);
    }

    @Test
    public void testGetCComments() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"/** This is a Javadoc comment */", "", "/* This is a C-style comment */", "", "public class MyClass {", "    // This is a C-style comment", "}", ""});
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(2, cComments.size());
        assertEquals(2, cComments.get(2).size());
        assertEquals(1, cComments.get(3).get(0).getText().split("\\r\\n").length);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"/** This is a Javadoc comment */", "", "public class MyClass {", "    // This is a C-style comment", "", "    int myVariable = 0;", "}", ""});
        boolean hasIntersectionWithCComment = fileContents.hasIntersectionWithComment(2, 0, 3, 0);
        assertTrue(hasIntersectionWithCComment);
        boolean hasIntersectionWithCppComment = fileContents.hasIntersectionWithComment(2, 0, 3, 0);
        assertTrue(hasIntersectionWithCComment);
        boolean hasIntersectionWithJavadocComment = fileContents.hasIntersectionWithComment(2, 0, 3, 0);
        assertFalse(hasIntersectionWithJavadocComment);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", new String[0]);
        assertTrue(fileContents.inPackageInfo());
        FileContents fileContents2 = new FileContents("MyClass.java", new String[0]);
        assertFalse(fileContents2.inPackageInfo());
    }

    @Test
    public void testLineIsBlank() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"", "", "public class MyClass {", "    // This is a C-style comment", "", "    int myVariable = 0;", "}", ""});
        boolean lineIsBlank = fileContents.lineIsBlank(1);
        assertFalse(lineIsBlank);
        lineIsBlank = fileContents.lineIsBlank(2);
        assertTrue(lineIsBlank);
    }

    @Test
    public void testLineIsComment() {
        FileContents fileContents = new FileContents("MyClass.java", new String[]{"", "", "public class MyClass {", "    // This is a C-style comment", "", "    int myVariable = 0;", "}", ""});
        boolean lineIsComment = fileContents.lineIsComment(1);
        assertFalse(lineIsComment);
        lineIsComment = fileContents.lineIsComment(3);
        assertTrue(lineIsComment);
    }
}