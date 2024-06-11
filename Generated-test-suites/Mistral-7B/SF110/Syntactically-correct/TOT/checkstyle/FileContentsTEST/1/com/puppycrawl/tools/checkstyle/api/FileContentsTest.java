package com.example.junit;

import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.api.FileContents.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionFromFile() {
        String fileName = "TestFile.java";
        String[] lines = new String[] {
            "public class TestFile {",
            "    // This is a single line comment",
            "}",
        };

        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals("TestFile.java", fileContents.getFileName());
        assertEquals( Arrays.asList(lines), fileContents.getLines() );

        // Test getText() method
        FileText text = fileContents.getText();
        assertEquals(lines.length, text.size());
        for (int i = 0; i < lines.length; i++) {
            assertEquals(lines[i], text.get(i));
        }

        // Test hasIntersectionWithComment() method
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 1;
        int endColNo = 20;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        // Test hasIntersectionWithCComment() method
        List<TextBlock> expectedBlockComments = Arrays.asList(
            new TextBlock("// This is a single line comment")
        );
        Map<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        List<TextBlock> actualBlockComments = clangComments.get(1);
        assertEquals(expectedBlockComments, actualBlockComments);

        // Test getJavadocBefore() method
        int lineNoBefore = 0;
        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
        assertNull(javadocBefore);

        // Test inPackageInfo() method
        fileName = "package-info.java";
        FileContents packageInfo = new FileContents(fileName, lines);
        assertTrue(packageInfo.inPackageInfo());

        // Test reportSingleLineComment() method
        fileName = "TestFileWithSingleLineComments.java";
        String[] linesWithSingleLineComments = new String[] {
            "// This is a single line comment",
            "public class TestFileWithSingleLineComments {",
            "}",
        };
        FileContents fileWithSingleLineComments = new FileContents(fileName, linesWithSingleLineComments);
        String type = "singleline";
        int startLineNoWithSingleLineComment = 1;
        int startColNoWithSingleLineComment = 1;
        fileWithSingleLineComments.reportSingleLineComment(type, startLineNoWithSingleLineComment, startColNoWithSingleLineComment);
        TextBlock singleLineComment = fileWithSingleLineComments.cppComments.get(startLineNoWithSingleLineComment);
        assertEquals("// This is a single line comment", singleLineComment.getText());

        // Test reportBlockComment() method
        fileName = "TestFileWithBlockComments.java";
        String[] linesWithBlockComments = new String[] {
            "/**",
            " * This is a Javadoc comment",
            " */",
            "public class TestFileWithBlockComments {",
            "}",
        };
        FileContents fileWithBlockComments = new FileContents(fileName, linesWithBlockComments);
        String typeBlockComment = "block";
        int startLineNoWithBlockComment = 1;
        int startColNoWithBlockComment = 1;
        int endLineNoWithBlockComment = 2;
        int endColNoWithBlockComment = 1;
        fileWithBlockComments.reportBlockComment(typeBlockComment, startLineNoWithBlockComment, startColNoWithBlockComment, endLineNoWithBlockComment, endColNoWithBlockComment);
        TextBlock blockComment = fileWithBlockComments.clangComments.get(startLineNoWithBlockComment).get(0);
        assertEquals(" * This is a Javadoc comment", blockComment.getText());
    }

    @Test
    public void testFileContentsConstructionFromFileText() {
        String fileName = "TestFile.java";
        String contents = "public class TestFile { }";
        FileText fileText = new FileText(fileName, contents);
        FileContents fileContents = new FileContents(fileText);
        assertEquals("TestFile.java", fileContents.getFileName());
        assertEquals(contents, fileContents.getText().get(0));
    }
}