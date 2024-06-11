import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;

public class FileContentsTest {
    @Test
    public void testFileContents() {
        String fileName = "example.java";
        String[] lines = {"package com.test;", "/** This is a test file */", "public class TestClass {", "    // Single line comment", "    int x = 5;", "}", ""};
        FileContents fileContents = new FileContents(fileName, lines);

        // Test getFileName method
        assertEquals(fileName, fileContents.getFileName());

        // Test getLines method
        String[] expectedLines = {"package com.test;", "/** This is a test file */", "public class TestClass {", "    // Single line comment", "    int x = 5;", "}", ""};
        assertArrayEquals(expectedLines, fileContents.getLines());

        // Test getLine method
        assertEquals("package com.test;", fileContents.getLine(0));

        // Test hasIntersectionWithComment method with C comment
        int startLineNo = 2;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 15;
        assertTrue(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));

        // Test hasIntersectionWithComment method with C++ comment
        startLineNo = 3;
        startColNo = 1;
        endLineNo = 6;
        endColNo = 14;
        assertTrue(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));

        // Test reportCppComment method
        startLineNo = 4;
        startColNo = 5;
        fileContents.reportCppComment(startLineNo, startColNo);
        TextBlock comment = fileContents.cppComments.get(startLineNo);
        assertEquals("// Single line comment", comment.getText());

        // Test getCppComments method
        ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(2, new TextBlock("// Single line comment", 1, 2, 14));
        ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
        assertEquals(expectedCppComments, actualCppComments);
    }
}