package com.example.filetext;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() {
        // Test valid construction
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        // Test empty file
        file = new File("emptyFile.txt");
        FileText emptyFileText = new FileText(file, "UTF-8");
        assertEquals(0, emptyFileText.size());
        // Test invalid charset
        file = new File("testFile.txt");
        try {
            new FileText(file, "InvalidCharset");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCopyConstructorAndFromLines() {
        File file = new File("testFile.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText copyFileText = new FileText(originalFileText);
        FileText linesFileText = FileText.fromLines(file, Arrays.asList("Line 1", "Line 2"));
        assertEquals(originalFileText, copyFileText);
        assertEquals(originalFileText, linesFileText);
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReadFile() {
        File file = Mockito.mock(File.class);
        CharsetDecoder decoder = Mockito.mock(CharsetDecoder.class);
        String readFileResult = "Expected readFile result";
        when(file.exists()).thenReturn(true);
        when(decoder.read(any(char[].class), eq(0), anyInt())).thenReturn(readFileResult.length());

        String result = FileText.readFile(file, decoder);
        assertEquals(readFileResult, result);
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        int expectedLineBreakPosition = 5;
        int[] expectedLineBreakPositions = {0, 5, 10, 15};

        assertThat(lineBreaks.length, is(expectedLineBreakPositions.length));
        for (int i = 0; i < lineBreaks.length; i++) {
            assertEquals(expectedLineBreakPositions[i], lineBreaks[i]);
        }
    }
}