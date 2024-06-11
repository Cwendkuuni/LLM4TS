package com.puppycrawl.tools.checkstyle.api;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionWithValidInput() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a Javadoc comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertNotNull(fileContents.getText());
        assertEquals(3, fileContents.getLines().length);
        // Add more assertions for javadocComments, cppComments, and clangComments if needed
    }
}