package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.puppycrawl.tools.checkstyle.api.*;
import java.util.*;
import org.mockito.Mockito;

public class FileContentsTest {

 private FileContents fileContents;
 private CommentListener commentListenerMock;
 private String fileName;

 @Before
 public void setUp() {
 commentListenerMock = mock(CommentListener.class);
 fileName = "testFile.java";
 }

 @Test
 public void testFileContentsWithLines() {
 String[] lines = {"public class Test {}", "// This is a single line comment", "/** This is a javadoc comment */"};
 fileContents = new FileContents(fileName, lines);
 assertEquals(fileName, fileContents.getFileName());
 assertEquals(3, fileContents.getLines().length);
 }

 @Test
 public void testFileContentsWithFileText() {
 String fileContent = "public class Test {\n" +
         "// This is a single line comment\n" +
         "/** This is a javadoc comment */\n" +
         "}";
 FileText fileText = Mockito.mock(FileText.class);
 when(fileText.get(0)).thenReturn("public class Test {");
 when(fileText.get(1)).thenReturn("// This is a single line comment");
 when(fileText.get(2)).thenReturn("/** This is a javadoc comment */");
 when(fileText.getFile()).thenReturn(new File(fileName));
 fileContents = new FileContents(fileText);
 assertEquals(fileName, fileContents.getFileName());
 assertEquals(3, fileContents.getLines().length);
 }

 @Test
 public void testReportSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 5;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment"});
 fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock("// This is a single line comment", startColNo, startLineNo);
 assertEquals(expectedComment, fileContents.cppComments.get(startLineNo));
 }

 @Test
 public void testReportBlockComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 10;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "/** This is a javadoc comment */"});
 fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);
 TextBlock expectedComment = new TextBlock(new String[]{"This is a javadoc comment"}, startColNo, endLineNo);
 assertEquals(expectedComment, fileContents.clangComments.get(startLineNo).get(0));
 }

 @Test
 public void testGetCppComments() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 10;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment", "/** This is a javadoc comment */"});
 fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);
 fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);
 ImmutableMap<Integer, TextBlock> expectedMap = ImmutableMap.of(1, new TextBlock("// This is a single line comment", startColNo, startLineNo), 2, new TextBlock(new String[]{"This is a javadoc comment"}, startColNo, endLineNo));
 assertEquals(expectedMap, fileContents.getCppComments());
 }

 @Test
 public void testGetCComments() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 10;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment", "/** This is a javadoc comment */", "/** Another javadoc comment */"});
 fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);
 fileContents.reportBlockComment("cpp", endLineNo, 0, endLineNo + 1, 10);
 fileContents.reportBlockComment("cpp", endLineNo + 1, 0, endLineNo + 1, 10);
 ImmutableMap<Integer, List<TextBlock>> expectedMap = ImmutableMap.of(1, Arrays.asList(new TextBlock("This is a javadoc comment", startColNo, endLineNo)));
 assertEquals(expectedMap, fileContents.getCComments());
 }

 @Test
 public void testGetJavadocBefore() {
 int lineNoBefore = 1;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "/** This is a javadoc comment before */", "/** This is a javadoc comment */"});
 TextBlock expectedComment = new TextBlock("/** This is a javadoc comment before */", 0, lineNoBefore);
 assertEquals(expectedComment, fileContents.getJavadocBefore(lineNoBefore));
 }

 @Test
 public void testLineIsBlank() {
 int lineNo = 1;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "", "/** This is a javadoc comment */"});
 assertTrue(fileContents.lineIsBlank(lineNo));
 }

 @Test
 public void testLineIsComment() {
 int lineNo = 1;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment", "/** This is a javadoc comment */"});
 assertTrue(fileContents.lineIsComment(lineNo));
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 10;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment", "/** This is a javadoc comment */"});
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 fileContents = new FileContents("package-info.java", new String[0]);
 assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testHasIntersectionWithCComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 10;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment", "/** This is a javadoc comment */"});
 assertFalse(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));
 fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);
 assertTrue(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithCppComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 10;
 fileContents = new FileContents(fileName, new String[]{"public class Test {", "// This is a single line comment", "/** This is a javadoc comment */"});
 fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);
 assertTrue(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));
 }
}