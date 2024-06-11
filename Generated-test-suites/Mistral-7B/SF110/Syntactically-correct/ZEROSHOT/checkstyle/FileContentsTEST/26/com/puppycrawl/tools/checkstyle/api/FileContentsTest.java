package com.example.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileContents.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContentsCreationFromFile() {
        String fileName = "FileUnderTest.java";
        String[] lines = {
            "package com.example.test;",
            "",
            "public class FileUnderTest {",
            "    // Single line comment",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            "",
            "}"
        };

        File file = new File(fileName);

        FileContents fileContents = new FileContents(fileName, lines);
        FileText expectedFileText = new FileText(new String[] {
            "package com.example.test;",
            "",
            "public class FileUnderTest {",
            "    // Single line comment",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            ""
        });

        FileText actualFileText = fileContents.getText();
        assertTextEqual(expectedFileText, actualFileText);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        Map<Integer, TextBlock> javadocComments = fileContents.getJavadocBefore(2);

        assertNotNull(cppComments);
        assertNotNull(clangComments);
        assertNotNull(javadocComments);

        List<TextBlock> cComment = clangComments.get(3);
        assertTrue(cComment.size() > 0);
        TextBlock cCommentBlock = cComment.get(0);
        assertTrue(cCommentBlock.getText().contains("Another single line comment"));

        TextBlock javadocComment = javadocComments.get(6);
        assertTrue(javadocComment != null);
        assertTrue(javadocComment.getText().contains("Javadoc comment"));

        FileContents fileContentsEmpty = new FileContents("");
        FileText expectedFileTextEmpty = new FileText(Collections.emptyList());
        assertTextEqual(expectedFileTextEmpty, fileContentsEmpty.getText());

        File fileNull = null;
        FileContents fileContentsNull = new FileContents("fileNull.java", fileNull);
        assertNull(fileContentsNull.getText());
    }

    @Test
    public void testFileContentsCreationFromFileText() {
        String fileName = "FileUnderTest.java";
        String[] lines = {
            "package com.example.test;",
            "",
            "public class FileUnderTest {",
            "    // Single line comment",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            ""
        };

        FileText fileText = new FileText(Arrays.asList(lines));

        FileContents fileContents = new FileContents(fileName, fileText);
        FileText expectedFileText = new FileText(Arrays.asList(lines));

        FileText actualFileText = fileContents.getText();
        assertTextEqual(expectedFileText, actualFileText);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        Map<Integer, TextBlock> javadocComments = fileContents.getJavadocBefore(2);

        assertNotNull(cppComments);
        assertNotNull(clangComments);
        assertNotNull(javadocComments);

        List<TextBlock> cComment = clangComments.get(3);
        assertTrue(cComment.size() > 0);
        TextBlock cCommentBlock = cComment.get(0);
        assertTrue(cCommentBlock.getText().contains("Another single line comment"));

        TextBlock javadocComment = javadocComments.get(6);
        assertTrue(javadocComment != null);
        assertTrue(javadocComment.getText().contains("Javadoc comment"));
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "FileUnderTest.java";
        String[] lines = {
            "package com.example.test;",
            "// Single line comment",
            "public class FileUnderTest {",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            ""
        };

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCppComment(1, 1);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock cComment = cppComments.get(1);
        assertTrue(cComment != null);
        assertTrue(cComment.getText().contains("Single line comment"));
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "FileUnderTest.java";
        String[] lines = {
            "package com.example.test;",
            "",
            "public class FileUnderTest {",
            "    // Single line comment",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        /**",
            "         * Javadoc comment for myMethod",
            "         */",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment for myPrivateMethod",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            ""
        };

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(1, 1, 2, 2);

        ImmutableMap<Integer, List<TextBlock>> clangComments = fileContents.getCComments();
        List<TextBlock> cComment = clangComments.get(1);
        assertTrue(cComment != null);
        TextBlock cCommentBlock = cComment.get(0);
        assertTrue(cCommentBlock.getText().contains("Javadoc comment for myMethod"));

        fileContents.reportCComment(4, 1, 5, 2);

        List<TextBlock> nestedCComment = clangComments.get(4).get(0).getNestedComments();
        TextBlock nestedCCommentBlock = nestedCComment.get(0);
        assertTrue(nestedCCommentBlock.getText().contains("Nested javadoc comment"));
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "FileUnderTest.java";
        String[] lines = {
            "package com.example.test;",
            "/**",
            " * Javadoc comment for the package",
            " */",
            "",
            "public class FileUnderTest {",
            "    // Single line comment",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        /**",
            "         * Javadoc comment for myMethod",
            "         */",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment for myPrivateMethod",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            "",
            "}"
        };

        FileContents fileContents = new FileContents(fileName, lines);

        TextBlock javadocBefore = fileContents.getJavadocBefore(2);
        assertTrue(javadocBefore != null);
        assertTrue(javadocBefore.getText().contains("Javadoc comment for the package"));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "FileUnderTest.java";
        String[] lines = {
            "package com.example.test;",
            "/**",
            " * Javadoc comment for the package",
            " */",
            "",
            "public class FileUnderTest {",
            "    // Single line comment",
            "    private int myField;",
            "",
            "    public void myMethod() {",
            "        /**",
            "         * Javadoc comment for myMethod",
            "         */",
            "        // Another single line comment",
            "    }",
            "",
            "    /**",
            "     * Javadoc comment for myPrivateMethod",
            "     */",
            "    private void myPrivateMethod() {",
            "        /**",
            "         * Nested javadoc comment",
            "         */",
            "    }",
            "",
            "}"
        };

        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 3;
        int startColNo = 1;
        int endLineNo = 4;
        int endColNo = 1;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 3;
        startColNo = 1;
        endLineNo = 6;
        endColNo = 1;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 3;
        startColNo = 1;
        endLineNo = 5;
        endColNo = 1;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 9;
        startColNo = 1;
        endLineNo = 11;
        endColNo = 1;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";
        String[] lines = {
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
            "<manifest xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\">",
            "<description>",
            "</description>",
            "<package name=\"com.example.test\"",
            "        version=\"1.0.0\"",
            "        xmlns:maven-info=\"http://maven.apache.org/POM/4.0.0\"",
            "        maven-info:scm=\"git\"",
            "        maven-info:url=\"https://github.com/example/test.git\">",
            "</package>",
            "</manifest>"
        };

        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.inPackageInfo());

        fileName = "FileUnderTest.java";
        lines = new String[0];

        FileContents fileContentsEmpty = new FileContents(fileName, lines);

        assertFalse(fileContentsEmpty.inPackageInfo());
    }

    private void assertTextEqual(FileText expected, FileText actual) {
        List<String> expectedLines = expected.toLinesList();
        List<String> actualLines = actual.toLinesList();

        for (int i = 0; i < expectedLines.size(); ++i) {
            String expectedLine = expectedLines.get(i);
            String actualLine = actualLines.get(i);
            assertTrue(expectedLine.equals(actualLine));
        }

        assertEquals(expectedLines.size(), actualLines.size());
    }

    private void assertEquals(int expectedSize, List<String> actualList) {
        assertEquals(expectedSize, actualList.size());
    }
}