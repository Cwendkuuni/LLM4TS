package com.puppycrawl.tools.checkstyle.api;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String filename = "testFile.java";
        String[] lines = new String[]{"public class TestClass {", "// This is a comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getText().size());
    }
}