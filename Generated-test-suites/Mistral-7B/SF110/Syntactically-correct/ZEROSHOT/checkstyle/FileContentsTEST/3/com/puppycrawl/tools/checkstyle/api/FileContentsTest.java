package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.CommentListener;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileUtils;
import com.puppycrawl.tools.checkstyle.api.LineNumberUtil;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.Utilities;
import com.puppycrawl.tools.checkstyle.grammars.JavadocGrammar;
import com.puppycrawl.tools.checkstyle.grammars.PythonGrammar;
import com.puppycrawl.tools.checkstyle.grammars.PythonMultiLineCommentGrammar;
import com.puppycrawl.tools.checkstyle.utils.FileUtilsTest;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class FileContentsTest {

    @Test
    public void testFileContents() throws Exception {
        String fileContent = "/**\n" +
                " * This is a Javadoc comment.\n" +
                " */\n" +
                "public class TestClass {\n" +
                "    // This is a single line C comment.\n" +
                "    private int x; \n" +
                "    private String y;\n" +
                "}\n";

        String fileName = "TestClass.java";

        FileText fileText = new FileText(File.createTempFile("test", ".txt"), fileContent.getBytes());
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileContent, fileContents.getText().getContent().toString());

        // Test getJavadocBefore()
        TextBlock javadocBefore = fileContents.getJavadocBefore(1);
        assertEquals(fileContent.substring(0, fileContent.indexOf("public class TestClass")), javadocBefore.getText());

        // Test getCppComments()
        CommentListener commentListener = mock(CommentListener.class);
        FileUtils fileUtils = mock(FileUtils.class);
        Utilities utilities = mock(Utilities.class);
        JavadocGrammar javadocGrammar = mock(JavadocGrammar.class);
        PythonGrammar pythonGrammar = mock(PythonGrammar.class);
        PythonMultiLineCommentGrammar pythonMultiLineCommentGrammar = mock(PythonMultiLineCommentGrammar.class);
        FileUtilsTest fileUtilsTest = mock(FileUtilsTest.class);

        when(utilities.getCommentGrammar("java")).thenReturn(javadocGrammar);
        when(utilities.getCommentGrammar("python")).thenReturn(pythonGrammar);
        when(utilities.getPythonMultiLineCommentGrammar()).thenReturn(pythonMultiLineCommentGrammar);
        when(fileUtils.getFileText(anyString())).thenReturn(fileText);
        when(commentListener.reportSingleLineComment(eq("CPP"), anyInt(), anyInt())).thenCallRealMethod();
        when(commentListener.reportBlockComment(eq("C"), anyInt(), anyInt(), anyInt(), anyInt())).thenCallRealMethod();

        FileContents fileContentsSpy = spy(new FileContents(fileName, fileContent.split("\\r?\\n")));
        doAnswer(invocation -> {
            Comment comment = invocation.getArgument(1);
            fileContentsSpy.reportCppComment(comment.getStartLineNo(), comment.getStartColNo());
            return null;
        }).when(fileContentsSpy).reportCppComment(anyInt(), anyInt());

        doAnswer(invocation -> {
            Comment comment = invocation.getArgument(1);
            List<TextBlock> blocks = new ArrayList<>();
            blocks.add(comment);
            fileContentsSpy.reportCComment(comment.getStartLineNo(), comment.getStartColNo(), comment.getEndLineNo(), comment.getEndColNo());
            return blocks;
        }).when(fileContentsSpy).reportCComment(anyInt(), anyInt(), anyInt(), anyInt());

        fileContentsSpy.addCommentListener(commentListener);
        fileContentsSpy.parse();

        ImmutableMap<Integer, TextBlock> cppComments = fileContentsSpy.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContentsSpy.getCComments();

        assertEquals(Collections.singletonMap(1, new TextBlock("// This is a single line C comment.", 1, 1)), cppComments);
        assertEquals(Collections.singletonMap(1, Collections.singletonList(new TextBlock("// This is a single line C comment.", 1, 1))), cComments);

        // Test hasIntersectionWithComment()
        assertTrue(fileContentsSpy.hasIntersectionWithComment(1, 0, 1, fileContent.length()));
        assertFalse(fileContentsSpy.hasIntersectionWithComment(1, 5, 2, 10));
    }

    @Test
    public void testFileContentsWithMultilineCComment() throws Exception {
        String fileContent = "/*\n" +
                " * This is a Javadoc comment.\n" +
                " */\n" +
                "/*\n" +
                " * This is a multiline C comment.\n" +
                " */\n" +
                "public class TestClass {\n" +
                "    private int x; \n" +
                "    private String y;\n" +
                "}";

        String fileName = "TestClass.java";

        FileText fileText = new FileText(File.createTempFile("test", ".txt"), fileContent.getBytes());
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileContent, fileContents.getText().getContent().toString());

        // Test getJavadocBefore()
        TextBlock javadocBefore = fileContents.getJavadocBefore(1);
        assertEquals(fileContent.substring(0, fileContent.indexOf("public class TestClass")), javadocBefore.getText());

        // Test getCppComments()
        CommentListener commentListener = mock(CommentListener.class);
        FileUtils fileUtils = mock(FileUtils.class);
        Utilities utilities = mock(Utilities.class);
        JavadocGrammar javadocGrammar = mock(JavadocGrammar.class);
        PythonGrammar pythonGrammar = mock(PythonGrammar.class);
        PythonMultiLineCommentGrammar pythonMultiLineCommentGrammar = mock(PythonMultiLineCommentGrammar.class);
        FileUtilsTest fileUtilsTest = mock(FileUtilsTest.class);

        when(utilities.getCommentGrammar("java")).thenReturn(javadocGrammar);
        when(utilities.getCommentGrammar("python")).thenReturn(pythonGrammar);
        when(utilities.getPythonMultiLineCommentGrammar()).thenReturn(pythonMultiLineCommentGrammar);
        when(fileUtils.getFileText(anyString())).thenReturn(fileText);
        when(commentListener.reportSingleLineComment(eq("CPP"), anyInt(), anyInt())).thenCallRealMethod();
        when(commentListener.reportBlockComment(eq("C"), anyInt(), anyInt(), anyInt(), anyInt())).thenCallRealMethod();

        FileContents fileContentsSpy = spy(new FileContents(fileName, fileContent.split("\\r?\\n")));
        doAnswer(invocation -> {
            Comment comment = invocation.getArgument(1);
            fileContentsSpy.reportCppComment(comment.getStartLineNo(), comment.getStartColNo());
            return null;
        }).when(fileContentsSpy).reportCppComment(anyInt(), anyInt());

        doAnswer(invocation -> {
            Comment comment = invocation.getArgument(1);
            List<TextBlock> blocks = new ArrayList<>();
            blocks.add(comment);
            fileContentsSpy.reportCComment(comment.getStartLineNo(), comment.getStartColNo(), comment.getEndLineNo(), comment.getEndColNo());
            return blocks;
        }).when(fileContentsSpy).reportCComment(anyInt(), anyInt(), anyInt(), anyInt());

        fileContentsSpy.addCommentListener(commentListener);
        fileContentsSpy.parse();

        ImmutableMap<Integer, TextBlock> cppComments = fileContentsSpy.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContentsSpy.getCComments();

        assertEquals(Collections.emptyMap(), cppComments);
        assertEquals(Collections.singletonMap(1, Collections.singletonList(new TextBlock("/* This is a multiline C comment.\n */", 1, 1))), cComments);

        // Test hasIntersectionWithComment()
        assertTrue(fileContentsSpy.hasIntersectionWithComment(1, 0, 1, fileContent.length()));
        assertFalse(fileContentsSpy.hasIntersectionWithComment(1, 5, 2, 10));
    }
}