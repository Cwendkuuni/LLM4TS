import static org.junit.Assert.*;
import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import org.junit.Test;

public class FileContentsTest {
    @Test
    public void testFileContentsConstructionWithFilenameAndLines() {
        String fileName = "SampleFile.java";
        String[] lines = {"public class SampleFile", "{\n", "    // This is a comment\n", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        
        // Test comment parsing
        Comment expectedCppComment = new Comment(new String[]{"// This is a comment"}, 0, 1, 0);
        Comment actualCppComment = fileContents.getCppComments().get(1);
        assertEquals(expectedCppComment.getText(), actualCppComment.getText());
        assertEquals(expectedCppComment.getStartLineNo(), actualCppComment.getStartLineNo());
        assertEquals(expectedCppComment.getStartColNo(), actualCppComment.getStartColNo());
        assertEquals(expectedCppComment.getEndLineNo(), actualCppComment.getEndLineNo());
        assertEquals(expectedCppComment.getEndColNo(), actualCppComment.getEndColNo());

        // Test Javadoc parsing
        Comment expectedJavadocComment = new Comment(null, 0, 0, 0);
        Comment actualJavadocComment = fileContents.getJavadocBefore(1);
        assertNull(actualJavadocComment);

        // Test empty lines
        String emptyLine = "";
        String[] emptyLines = { emptyLine, emptyLine };
        FileContents emptyFileContents = new FileContents("EmptyFile.java", emptyLines);
        assertTrue(emptyFileContents.lineIsBlank(0));
        assertTrue(emptyFileContents.lineIsBlank(1));

        // Test comment intersection
        String fileWithComments = "package com.test;\n" +
                "/**\n" +
                " * This is a Javadoc comment\n" +
                " */\n" +
                "public class SampleFile {\n" +
                "    // This is a C-style comment\n" +
                "    // This is another C-style comment\n" +
                "}";
        String[] linesWithComments = fileWithComments.split("\n");
        FileContents fileWithCommentsContents = new FileContents(new FileText(linesWithComments));
        int startLineNo = 3;
        int startColNo = 1;
        int endLineNo = 5;
        int endColNo = 11;
        assertTrue(fileWithCommentsContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileWithCommentsContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileWithCommentsContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));
    }
}