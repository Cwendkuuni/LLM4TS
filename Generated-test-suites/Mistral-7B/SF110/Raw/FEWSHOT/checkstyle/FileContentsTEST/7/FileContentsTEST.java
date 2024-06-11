package com.example.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileUtils;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.utils.StringUtils;
import com.puppycrawl.tools.checkstyle.api.utils.log.LogManager;
import com.puppycrawl.tools.checkstyle.api.utils.log.LogWriter;
import com.puppycrawl.tools.checkstyle.grammars.java.JavaLexer;

@RunWith(PowerMockRunner.class)
public class FileContentsTest {

    @Test
    public void testConstructorWithFilenameAndLines() {
        String filename = "testfile.java";
        String[] lines = {"public class Test", "{\n", "    // This is a comment\n", "}"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertNotNull(fileContents.getText());
        assertEquals(3, fileContents.getLines().length);
        assertEquals("public class Test{\n", fileContents.getLines()[0]);
        assertEquals("    // This is a comment\n", fileContents.getLines()[1]);
        assertEquals("}", fileContents.getLines()[2]);
    }

    @Test
    public void testConstructorWithFileText() {
        String filename = "testfile.java";
        String textContent = "public class Test{\n" +
                "    // This is a comment\n" +
                "}";
        FileText fileText = FileText.fromString(textContent);
        FileContents fileContents = new FileContents(fileText);
        assertEquals(filename, fileContents.getFileName());
        assertNotNull(fileContents.getText());
        assertEquals(1, fileContents.getLines().length);
        assertEquals("public class Test{\n", fileContents.getLines()[0]);
    }

    @Test
    public void testReportSingleLineComment() {
        String filename = "testfile.java";
        String[] lines = {"// This is a single line comment", "public class Test"};
        FileContents fileContents = new FileContents(filename, lines);
        int startLineNo = 0;
        int startColNo = 0;
        fileContents.reportSingleLineComment("commentType", startLineNo, startColNo);
        TextBlock comment = fileContents.cppComments.get(startLineNo);
        assertNotNull(comment);
        assertEquals("// This is a single line comment", comment.getText());
    }

    @Test
    public void testReportBlockComment() {
        String filename = "testfile.java";
        String[] lines = {"/** This is a multiline comment */", "public class Test"};
        FileContents fileContents = new FileContents(filename, lines);
        int startLineNo = 0;
        int startColNo = 0;
        int endLineNo = 0;
        int endColNo = lines[0].length();
        fileContents.reportBlockComment("commentType", startLineNo, startColNo, endLineNo, endColNo);
        TextBlock comment = fileContents.clangComments.get(startLineNo).get(0);
        assertNotNull(comment);
        assertEquals("This is a multiline comment", comment.getText());
    }

    @Test
    public void testGetCppComments() {
        String filename = "testfile.java";
        String[] lines = {"// This is a single line comment", "/** This is a multiline comment */", "public class Test"};
        FileContents fileContents = new FileContents(filename, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(2, cppComments.size());
        assertNotNull(cppComments.get(0));
        assertNotNull(cppComments.get(1));
        assertEquals("// This is a single line comment", cppComments.get(0).getText());
        assertEquals("This is a multiline comment", cppComments.get(1).getText());
    }

    @Test
    public void testGetCComments() {
        String filename = "testfile.java";
        String[] lines = {"/** This is a multiline comment */", "* This is a C-style multiline comment\n* Another line", "public class Test"};
        FileContents fileContents = new FileContents(filename, lines);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(2, cComments.size());
        List<TextBlock> commentList1 = cComments.get(0);
        List<TextBlock> commentList2 = cComments.get(1);
        assertNotNull(commentList1);
        assertNotNull(commentList2);
        assertEquals(1, commentList1.size());
        assertEquals("This is a multiline comment", commentList1.get(0).getText());
        assertEquals(2, commentList2.size());
        assertEquals("Another line", commentList2.get(1).getText());
    }

    @Test
    public void testExtractCComment() {
        String filename = "testfile.java";
        String[] lines = {"/** This is a multiline comment */", "int main() {", "    // Single line comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);
        String[] extractedComment = fileContents.extractCComment(1, 0, 2, lines[1].length());
        assertEquals(3, extractedComment.length);
        assertEquals("/** This is a multiline comment */", extractedComment[0]);
        assertEquals("int main() {\n", extractedComment[1]);
        assertEquals("}", extractedComment[2]);
    }

    @Test
    public void testGetJavadocBefore() {
        String filename = "testfile.java";
        String[] lines = {"/** This is a Javadoc comment */", "public void testMethod()", "{"};
        FileContents fileContents = new FileContents(filename, lines);
        int lineNoBefore = 0;
        TextBlock javadocComment = fileContents.getJavadocBefore(lineNoBefore);
        assertNotNull(javadocComment);
        assertEquals("This is a Javadoc comment", javadocComment.getText());
    }

    @Test
    public void testLineIsBlank() {
        String filename = "testfile.java";
        String[] lines = {"", "public class Test"};
        FileContents fileContents = new FileContents(filename, lines);
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        String filename = "testfile.java";
        String[] lines = {"// This is a comment", "public class Test"};
        FileContents fileContents = new FileContents(filename, lines);
        assertFalse(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsComment(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String filename = "testfile.java";
        String[] lines = {"/** This is a multiline comment */", "public class Test", "// This is a single line comment"};
        FileContents fileContents = new FileContents(filename, lines);
        int startLineNo = 0;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = lines[1].length();
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, startLineNo, startColNo));
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo + 1, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, startLineNo - 1, startColNo));
    }

    @Test
    public void testInPackageInfo() {
        String filename = "testfile.java";
        FileContents fileContents = new FileContents(filename);
        assertFalse(fileContents.inPackageInfo());

        filename = "testfile-package.java";
        fileContents = new FileContents(filename);
        assertTrue(fileContents.inPackageInfo());
    }
}