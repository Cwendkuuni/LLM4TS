package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {
    // Test case for constructor with filename and lines

    @Test
    public void testFileContentsWithFilenameAndLines() {
        String fileName = "FileWithComments.java";
        String[] lines = {"public class FileWithComments {", "/**", " * This is a Javadoc comment.", " */", "}", "", "// This is a single line comment"};

        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(7, fileContents.getLines().length);
        assertEquals("FileWithComments.java", fileContents.getFileName());
        assertTrue(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsComment(2));
        assertNull(fileContents.getJavadocBefore(0));
        assertEquals("public class FileWithComments {", fileContents.getLines()[0]);

        // Test with empty file
        String emptyFileName = "EmptyFile.java";
        String[] emptyLines = {};
        FileContents emptyFileContents = new FileContents(emptyFileName, emptyLines);
        assertEquals(0, emptyFileContents.getLines().length);
    }

    // Test case for constructor with FileText

    @Test
    public void testFileContentsWithFileText() {
        String filePath = "src/test/java/com/puppycrawl/tools/checkstyle/api/FileContentsTest.java";
        File file = new File(filePath);
        FileText fileText = new FileText(file);

        FileContents fileContents = new FileContents(fileText);

        assertEquals(filePath, fileContents.getFileName());
        assertEquals(173, fileContents.getLines().length);
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
        assertNull(fileContents.getJavadocBefore(0));
        assertEquals("package com.puppycrawl.tools.checkstyle.api;", fileContents.getLines()[0]);
    }

    // Test case for reportSingleLineComment method

    @Test
    public void testReportSingleLineComment() {
        String fileName = "SingleLineCommentFile.java";
        String[] lines = {"// This is a single line comment"};
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCppComment(1, 0);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock comment = cppComments.get(1);
        assertEquals("This is a single line comment", comment.getText());
        assertEquals(1, cppComments.size());
    }

    // Test case for reportBlockComment method

    @Test
    public void testReportBlockComment() {
        String fileName = "BlockCommentFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment.", " */"};
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(1, 0, 1, lines[0].length());
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> commentList = cComments.get(1);
        TextBlock comment = commentList.get(0);
        assertEquals("This is a Javadoc comment.", comment.getText());
        assertEquals(1, commentList.size());
    }

    // Test case for getJavadocBefore method

    @Test
    public void testGetJavadocBefore() {
        String fileName = "JavadocBeforeFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment.", " */", "", "public class TestClass {", "/**", " * Another Javadoc comment.", " */"};
        FileContents fileContents = new FileContents(fileName, lines);

        TextBlock javadocBefore = fileContents.getJavadocBefore(3);
        assertEquals("This is a Javadoc comment.", javadocBefore.getText());
    }

    // Test case for inPackageInfo method

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";
        String[] lines = {"@javax.annotation.Generated", "/**", " * @author Puppy Crawl", " */"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.inPackageInfo());
    }
}