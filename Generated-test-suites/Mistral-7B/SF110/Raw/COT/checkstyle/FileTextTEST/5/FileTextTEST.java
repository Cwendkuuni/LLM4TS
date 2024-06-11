package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.LineColumn.Column;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = mock(File.class);
        Charset charset = mock(Charset.class);
        FileText fileText = new FileText(file, "UTF-8");

        verify(file).exists();
        verifyNoMoreInteractions(file);
        verify(charset).forName("UTF-8");
        verify(charset).newDecoder();

        String fullText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String expectedFullText = "Lorem ipsum dolor sit amet,\nconsectetur adipiscing elit.";
        when(file.getAbsolutePath()).thenReturn("/path/to/file.txt");
        when(charset.newDecoder()).thenReturn(mock(CharsetDecoder.class));
        when(charset.newDecoder().read(any(char[].class))).thenReturn(fullText.getBytes());

        String readFileResult = FileText.readFile(file, fileText.getCharset().newDecoder());
        assertEquals(expectedFullText, fileText.getFullText());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructorWithNonExistentFile() throws IOException {
        File file = mock(File.class);
        Charset charset = mock(Charset.class);

        when(file.exists()).thenReturn(false);

        try {
            new FileText(file, charset.getName());
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertEquals("/path/to/file.txt (No such file or directory)", e.getMessage());
        }

        verify(file).exists();
        verifyNoMoreInteractions(file);
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = mock(File.class);
        Charset charset = mock(Charset.class);
        FileText fileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(fileText);

        assertEquals(file, copiedFileText.getFile());
        assertEquals(charset, copiedFileText.getCharset());
        assertEquals(fileText.getFullText(), copiedFileText.getFullText());
        assertArrayEquals(fileText.lines, copiedFileText.lines);
        assertArrayEquals(fileText.lineBreaks, copiedFileText.lineBreaks);
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File file = mock(File.class);
        List<String> lines = Arrays.asList("Lorem ipsum dolor sit amet.", "Consectetur adipiscing elit.");
        FileText fileText = new FileText(file, lines);

        verify(file).exists();
        verifyNoMoreInteractions(file);

        assertEquals(file, fileText.getFile());
        assertEquals(null, fileText.getCharset());
        assertEquals("Lorem ipsum dolor sit amet.\nConsectetur adipiscing elit.", fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
        assertNull(fileText.lineBreaks);
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("/path/to/file.txt"), "UTF-8");
        int[] expectedLineBreaks = {0, 12, 23, 23, 34, 45, 45, 56};
        int[] actualLineBreaks = fileText.findLineBreaks();

        assertArrayEquals(expectedLineBreaks, actualLineBreaks);
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("/path/to/file.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(47);

        assertEquals(5, lineColumn.getLineNumber());
        assertEquals(7, lineColumn.getColumnNumber());
    }

    @Test
    public void testGetLine() {
        FileText fileText = new FileText(new File("/path/to/file.txt"), "UTF-8");

        assertEquals("Lorem ipsum dolor sit amet.\nConsectetur adipiscing elit.", fileText.get(0));
        assertEquals("Consectetur adipiscing elit.", fileText.get(1));
    }

    @Test
    public void testSize() {
        FileText fileText = new FileText(new File("/path/to/file.txt"), "UTF-8");

        assertEquals(2, fileText.size());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

}