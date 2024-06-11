package com.puppycrawl.tools.checkstyle.api;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFileNameAndLines() {
        String fileName = "TestFile.java";
        String[] lines = { "package com.example.test;", "public class TestClass {" };
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertNotNull(fileContents.getText());
        // Add more assertions as needed
    }
}