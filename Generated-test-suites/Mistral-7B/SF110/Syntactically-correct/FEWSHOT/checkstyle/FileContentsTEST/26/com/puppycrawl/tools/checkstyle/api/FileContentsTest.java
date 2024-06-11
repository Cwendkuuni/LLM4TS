package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionWithFile() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
    }

    @Test
    public void testFileContentsConstructionWithFileText() {
        String fileName = "testFile.java";
        FileText fileText = new FileText(Arrays.asList(
            "package com.example;",
            "public class Test {",
            "}"
        ));
        FileContents fileContents = new FileContents(fileName, fileText);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileText.toLinesArray(), fileContents.getLines());
    }

    @Test
    public void testGetLines() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        List<String> linesList = Arrays.asList(lines);
        assertEquals(linesList, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        String expectedLine = "package com.example;";
        assertEquals(expectedLine, fileContents.getLine(0));
    }

    @Test
    public void testGetFileName() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
    }

    @Test
    public void testIsBlankLine() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.lineIsBlank(1));
    }

    @Test
    public void testIsCommentLine() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "// This is a comment", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "// This is a comment in the same line", "public class Test {", "// This is another comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 2;
        int endColNo = 20;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 2;
        startColNo = 5;
        endLineNo = 2;
        endColNo = 20;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"/**", " * This is a javadoc comment", " */", "package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        TextBlock expectedJavadoc = new TextBlock(new String[]{" * This is a javadoc comment"});
        assertEquals(expectedJavadoc, fileContents.getJavadocBefore(3));
    }

    @Test
    public void testGetCppComments() {
        String fileName = "testFile.cpp";
        String[] lines = new String[]{"// This is a C++ single line comment", "// This is a C++ multiline comment", "//*/", "int main() {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(1, new Comment(new String[]{"This is a C++ single line comment"}, 0, 1, 14));
        assertEquals(expectedCppComments, fileContents.getCppComments());
    }

    @Test
    public void testgetCComments() {
        String fileName = "testFile.cpp";
        String[] lines = new String[]{"/* This is a C multiline comment", " * This is a C++ single line comment", " */", "int main() {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(1, Lists.newArrayList(
            new Comment(new String[]{"This is a C multiline comment"}, 0, 1, 41),
            new Comment(new String[]{"This is a C++ single line comment"}, 2, 2, 15)
        ));
        assertEquals(expectedCComments, fileContents.getCComments());
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "testFile.cpp";
        String[] lines = new String[]{"// This is a C++ single line comment", "int main() {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        fileContents.reportSingleLineComment("cpp", 2, 0);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock expectedComment = new Comment(new String[]{"This is a C++ single line comment"}, 2, 0, 15);
        assertSame(expectedComment, cppComments.get(2));
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "testFile.cpp";
        String[] lines = new String[]{"/* This is a C multiline comment", " * This is a C++ single line comment", " */", "int main() {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        fileContents.reportBlockComment("cpp", 1, 0, 3, 41);
        Map<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> expectedComments = Lists.newArrayList(
            new Comment(new String[]{"This is a C multiline comment"}, 1, 0, 42)
        );
        assertSame(expectedComments, cComments.get(1));
    }

    @Test
    public void testReportCppComment() {
        String fileName = "testFile.cpp";
        String[] lines = new String[]{"// This is a C++ single line comment", "int main() {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 0;
        fileContents.reportCppComment(startLineNo, startColNo);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock expectedComment = new Comment(new String[]{"This is a C++ single line comment"}, startLineNo, startColNo, 15);
        assertSame(expectedComment, cppComments.get(startLineNo));
    }

    @Test
    public void testExtractCComment() {
        String fileName = "testFile.cpp";
        String[] lines = new String[]{"/* This is a C multiline comment", " * This is a C++ single line comment", " */", "int main() {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        String[] expectedComment = new String[]{"This is a C multiline comment", " * This is a C++ single line comment", " */"};
        assertArrayEquals(expectedComment, fileContents.extractCComment(2, 0, 3, 41));
    }

    @Test
    public void testGetText() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        FileText expectedText = new FileText(Arrays.asList(lines));
        FileText actualText = fileContents.getText();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testIsInPackageInfo() {
        String fileName = "testFile.java";
        String[] lines = new String[]{"package com.example;", "public class Test {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.inPackageInfo());
    }
}