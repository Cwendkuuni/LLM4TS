package com.example.junit.test.filetext;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

@RunWith(JUnit4.class)
public class FileTextTest {

    @Test
    public void testFileTextConstructor_ValidFileAndCharset() throws Exception {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructor_InvalidFile() throws Exception {
        File file = new File("nonExistentFile.txt");
        new FileText(file, "UTF-8");
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructor_UnsupportedCharset() throws Exception {
        File file = new File("testFile.txt");
        new FileText(file, "X-12345-ABCDEF");
    }

    @Test
    public void testFileTextClone() throws Exception {
        File file = new File("testFile.txt");
        FileText originalFileText = new FileText(file, "UTF-8");
        FileText clonedFileText = new FileText(originalFileText);
        assertEquals(originalFileText, clonedFileText);
    }

    @Test
    public void testFileTextFromLines() throws Exception {
        File file = new File("testFile.txt");
        List<String> lines = Arrays.asList("Test line 1", "Test line 2");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
    }

    @Test
    public void testFileTextGetFile() throws Exception {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
    }

    @Test
    public void testFileTextGetCharset() throws Exception {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText.getCharset());
    }

    @Test
    public void testFileTextGetFullText() throws Exception {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(readFile(file, StandardCharsets.UTF_8), fileText.getFullText());
    }

    @Test
    public void testFileTextToLinesArray() throws Exception {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertArrayEquals(new String[]{"Test line 1", "Test line 2"}, fileText.toLinesArray());
    }

    @Test
    public void testFindLineBreaks_NoLineBreaks() throws Exception {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[]{0}, lineBreaks);
    }

    @Test
    public void testFindLineBreaks_OneLine() throws Exception {
        File file = new File("singleLineFile.txt");
        String content = "This is a single line.";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[]{content.length()}, lineBreaks);
    }

    @Test
    public void testFindLineBreaks_MultipleLines() throws Exception {
        File file = new File("multipleLinesFile.txt");
        String content = "Line 1\nLine 2\nLine 3";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        int[] expectedLineBreaks = {0, content.length() - 1, content.length()};
        assertArrayEquals(expectedLineBreaks, lineBreaks);
    }

    @Test
    public void testLineColumn_ValidPosition() throws Exception {
        File file = new File("testFile.txt");
        String content = "Line 1\nTest line\nLine 3";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        int pos = "Test line".indexOf("test");
        LineColumn lineColumn = fileText.lineColumn(pos);
        int expectedLineNo = 2;
        int expectedColumnNo = 5;
        assertEquals(expectedLineNo, lineColumn.getLineNumber());
        assertEquals(expectedColumnNo, lineColumn.getColumnNumber());
    }

    @Test
    public void testLineColumn_InvalidPosition() throws Exception {
        File file = new File("testFile.txt");
        String content = "Line 1\nTest line\nLine 3";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        int pos = content.length();
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(-1, lineColumn.getLineNumber());
    }

    @Test
    public void testGet_ValidIndex() throws Exception {
        File file = new File("testFile.txt");
        String content = "Line 1\nTest line\nLine 3";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        String expectedResult = "Test line";
        assertEquals(expectedResult, fileText.get(1));
    }

    @Test
    public void testGet_InvalidIndex() throws Exception {
        File file = new File("testFile.txt");
        String content = "Line 1\nTest line\nLine 3";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        String expectedExceptionMessage = "Array index out of range: -1";
        try {
            fileText.get(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    @Test
    public void testSize_ValidSize() throws Exception {
        File file = new File("testFile.txt");
        String content = "Line 1\nTest line\nLine 3";
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        FileText fileText = new FileText(file, "UTF-8");
        int expectedSize = 3;
        assertEquals(expectedSize, fileText.size());
    }

    private static String readFile(File inputFile, Charset charset) throws IOException {
        if (!inputFile.exists()) {
            throw new FileNotFoundException(inputFile.getPath() + " (No such file or directory)");
        }
        StringBuilder buf = new StringBuilder();
        FileInputStream stream = new FileInputStream(inputFile);
        InputStreamReader reader = new InputStreamReader(stream, charset);
        try {
            char[] chars = new char[1024];
            while (true) {
                int len = reader.read(chars);
                if (len < 0) {
                    break;
                }
                buf.append(chars, 0, len);
            }
        } finally {
            try {
                reader.close();
            } finally {
                stream.close();
            }
        }
        return buf.toString();
    }
}