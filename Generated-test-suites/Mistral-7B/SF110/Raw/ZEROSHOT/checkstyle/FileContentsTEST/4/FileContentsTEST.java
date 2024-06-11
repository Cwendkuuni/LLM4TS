package com.example.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "SampleFile.java";
        String[] lines = {"public class SampleClass {", "    // This is a single line comment.", "}", ""};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "SampleFile.java";
        String fileContent = "public class SampleClass {\n" +
                "    // This is a single line comment.\n" +
                "}";
        FileText fileText = new FileText(new File(fileName), fileContent.split("\n"));
        FileContents fileContents = new FileContents(fileText);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileText.toLinesArray().length, fileContents.getLines().length);
    }

    @Test
    public void testReportCppComment() {
        String fileName = "SampleFile.java";
        String[] lines = {"// This is a single line comment.", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        fileContents.reportCppComment(startLineNo, startColNo);
        TextBlock expectedComment = new TextBlock("// This is a single line comment.", startColNo, startLineNo, startColNo + 16);
        TextBlock actualComment = fileContents.cppComments.get(startLineNo);
        assertEquals(expectedComment.getText(), actualComment.getText());
        assertEquals(expectedComment.getStartLine(), actualComment.getStartLine());
        assertEquals(expectedComment.getStartCol(), actualComment.getStartCol());
        assertEquals(expectedComment.getLength(), actualComment.getLength());
    }

    @Test
    public void testGetCppComments() {
        String fileName = "SampleFile.java";
        String[] lines = {"// This is a single line comment.", "// This is another single line comment.", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, TextBlock> expectedMap = new ImmutableMap.Builder<Integer, TextBlock>()
                .put(1, new TextBlock("// This is a single line comment.", 1, 1, 17))
                .put(1, new TextBlock("// This is another single line comment.", 1, 1, 22))
                .build();
        ImmutableMap<Integer, TextBlock> actualMap = fileContents.getCppComments();
        assertEquals(expectedMap.size(), actualMap.size());
        for (Map.Entry<Integer, TextBlock> entry : expectedMap.entrySet()) {
            Integer key = entry.getKey();
            TextBlock value = entry.getValue();
            TextBlock actualValue = actualMap.get(key);
            assertEquals(value.getText(), actualValue.getText());
            assertEquals(value.getStartLine(), actualValue.getStartLine());
            assertEquals(value.getStartCol(), actualValue.getStartCol());
            assertEquals(value.getLength(), actualValue.getLength());
        }
    }

    @Test
    public void testReportCComment() {
        String fileName = "SampleFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment.", " * @author John Doe.", " */", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 3;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        String[] expectedComment = {" * This is a Javadoc comment.", " * @author John Doe."};
        List<TextBlock> actualComment = fileContents.clangComments.get(startLineNo);
        assertEquals(1, actualComment.size());
        TextBlock textBlock = actualComment.get(0);
        String[] text = textBlock.getText().split("\\s+");
        assertEquals(expectedComment.length, text.length);
        for (int i = 0; i < expectedComment.length; ++i) {
            assertEquals(expectedComment[i], text[i]);
        }
    }

    @Test
    public void testGetCComments() {
        String fileName = "SampleFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment.", " * @author John Doe.", " */", "/**", " * This is another Javadoc comment.", " * @author Jane Doe.", " */", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, List<TextBlock>> expectedMap = new ImmutableMap.Builder<Integer, List<TextBlock>>()
                .put(1, Arrays.asList(new TextBlock(" * This is a Javadoc comment.", 1, 1, 37), new TextBlock(" * @author John Doe.", 1, 1, 64)))
                .put(3, Arrays.asList(new TextBlock(" * This is another Javadoc comment.", 1, 3, 38), new TextBlock(" * @author Jane Doe.", 1, 3, 64)))
                .build();
        ImmutableMap<Integer, List<TextBlock>> actualMap = fileContents.getCComments();
        assertEquals(expectedMap.size(), actualMap.size());
        for (Map.Entry<Integer, List<TextBlock>> entry : expectedMap.entrySet()) {
            Integer key = entry.getKey();
            List<TextBlock> value = entry.getValue();
            List<TextBlock> actualValue = actualMap.get(key);
            assertEquals(value.size(), actualValue.size());
            for (int i = 0; i < value.size(); ++i) {
                TextBlock expectedTextBlock = value.get(i);
                TextBlock actualTextBlock = actualValue.get(i);
                assertEquals(expectedTextBlock.getText(), actualTextBlock.getText());
                assertEquals(expectedTextBlock.getStartLine(), actualTextBlock.getStartLine());
                assertEquals(expectedTextBlock.getStartCol(), actualTextBlock.getStartCol());
                assertEquals(expectedTextBlock.getLength(), actualTextBlock.getLength());
            }
        }
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "SampleFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment.", " * @author John Doe.", " */", "public class SampleClass {", "    // This is a single line comment.", "}", ""};
        FileContents fileContents = new FileContents(fileName, lines);
        int lineNoBefore = 3;
        TextBlock expectedTextBlock = new TextBlock(" * This is a Javadoc comment.", 1, 1, 37);
        TextBlock actualTextBlock = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals(expectedTextBlock.getText(), actualTextBlock.getText());
        assertEquals(expectedTextBlock.getStartLine(), actualTextBlock.getStartLine());
        assertEquals(expectedTextBlock.getStartCol(), actualTextBlock.getStartCol());
        assertEquals(expectedTextBlock.getLength(), actualTextBlock.getLength());
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "SampleFile.java";
        String[] lines = {"", "", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        for (int lineNo = 0; lineNo < lines.length; ++lineNo) {
            boolean expectedResult = lineNo == 0;
            boolean actualResult = fileContents.lineIsBlank(lineNo);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void testLineIsComment() {
        String fileName = "SampleFile.java";
        String[] lines = {"// This is a single line comment.", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        for (int lineNo = 0; lineNo < lines.length; ++lineNo) {
            boolean expectedResult = lineNo == 0;
            boolean actualResult = fileContents.lineIsComment(lineNo);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "SampleFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment.", " * @author John Doe.", " */", "public class SampleClass {", "    // This is a single line comment.", "}", "// This is another single line comment."};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 2;
        boolean expectedResult = true;
        boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);

        startLineNo = 1;
        startColNo = 1;
        endLineNo = 1;
        endColNo = 16;
        expectedResult = true;
        actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);

        startLineNo = 1;
        startColNo = 18;
        endLineNo = 3;
        endColNo = 4;
        expectedResult = false;
        actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);

        startLineNo = 1;
        startColNo = 1;
        endLineNo = 4;
        endColNo = 1;
        expectedResult = false;
        actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "SampleFile.java";
        String[] lines = {"package com.example.test;", "public class SampleClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        boolean expectedResult = true;
        boolean actualResult = fileContents.inPackageInfo();
        assertEquals(expectedResult, actualResult);

        fileName = "SampleFile.java";
        lines = new String[]{"public class SampleClass {", "}"};
        FileContents fileContents2 = new FileContents(fileName, lines);
        expectedResult = false;
        actualResult = fileContents2.inPackageInfo();
        assertEquals(expectedResult, actualResult);
    }
}