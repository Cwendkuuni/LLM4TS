package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionFromFile() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {}",
            "// Another single line comment",
            "/** This is another Javadoc comment */"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertNotNull(fileContents.getText());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertEquals("TestFile.java", fileContents.getFileName());
        assertEquals(2, fileContents.getCComments().size());
        assertEquals(2, fileContents.getCppComments().size());
    }

    @Test
    public void testFileContentsConstructionFromFileText() {
        String fileName = "TestFile.java";
        String fileContent = "// This is a single line comment\n" +
                            "/** This is a Javadoc comment */\n" +
                            "package com.example.test;\n" +
                            "public class TestFile {\n" +
                            "}\n" +
                            "// Another single line comment\n" +
                            "/** This is another Javadoc comment */";
        FileText fileText = new FileText(fileName, fileContent.getBytes());
        FileContents fileContents = new FileContents(fileText);

        assertNotNull(fileContents.getText());
        assertEquals(fileText, fileContents.getText());
        assertEquals("TestFile.java", fileContents.getFileName());
        assertEquals(2, fileContents.getCComments().size());
        assertEquals(2, fileContents.getCppComments().size());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        String[] lines = {
            "/** This is a Javadoc comment */",
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);
        TextBlock javadocBefore = fileContents.getJavadocBefore(0);

        assertNotNull(javadocBefore);
        assertEquals("This is a Javadoc comment", javadocBefore.getText());
    }

    @Test
    public void testGetJavadocBeforeNegativeIndex() {
        String fileName = "TestFile.java";
        String[] lines = {
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertNull(fileContents.getJavadocBefore(-1));
    }

    @Test
    public void testGetJavadocBeforeNonExistentLine() {
        String fileName = "TestFile.java";
        String[] lines = {
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertNull(fileContents.getJavadocBefore(1));
    }

    @Test
    public void testGetJavadocBeforeBlankLine() {
        String fileName = "TestFile.java";
        String[] lines = {
            "",
            "/** This is a Javadoc comment */",
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertNotNull(fileContents.getJavadocBefore(1));
    }

    @Test
    public void testGetJavadocBeforeLineWithComment() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertNull(fileContents.getJavadocBefore(0));
        assertNotNull(fileContents.getJavadocBefore(1));
    }

    @Test
    public void testGetJavadocBeforeLineWithNoJavadoc() {
        String fileName = "TestFile.java";
        String[] lines = {
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testGetLines() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        List<String> expectedLines = Arrays.asList(lines);
        List<String> actualLines = fileContents.getLines();

        assertEquals(expectedLines, actualLines);
    }

    @Test
    public void testGetLine() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        String expectedLine = "public class TestFile {";
        String actualLine = fileContents.getLine(2);

        assertEquals(expectedLine, actualLine);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetLineIndexOutOfBounds() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.getLine(-1);
    }

    @Test
    public void testIsBlankLine() {
        String fileName = "TestFile.java";
        String[] lines = {
            "/** This is a Javadoc comment */",
            "",
            "public void testMethod() {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testIsCommentLine() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String[] lines = {
            "// This is a single line comment",
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = 1;
        int endColNo = 10;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        startColNo = 5;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        startColNo = 11;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 2;
        startColNo = 0;
        endLineNo = 2;
        endColNo = 10;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        startColNo = 15;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 3;
        startColNo = 0;
        endLineNo = 3;
        endColNo = 10;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        startColNo = 10;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 4;
        startColNo = 0;
        endLineNo = 4;
        endColNo = 10;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        startColNo = 10;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "TestFile.java";
        String[] lines = {
            "/** This is a Javadoc comment */",
            "package com.example.test;",
            "public class TestFile {"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.inPackageInfo());

        fileName = "TestFile.class";
        lines = new String[0];
        fileContents = new FileContents(fileName, lines);

        assertFalse(fileContents.inPackageInfo());

        fileName = "TestFile.java";
        lines = new String[]{"public class TestFile {"};
        fileContents = new FileContents(fileName, lines);

        assertFalse(fileContents.inPackageInfo());

        fileName = "TestFile.java";
        lines = new String[]{"package com.example.test;", "public class TestFile {"};
        fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.inPackageInfo());
    }
}